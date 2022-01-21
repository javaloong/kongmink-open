package org.javaloong.kongmink.open.rest.core.internal.validation;

import org.hibernate.validator.spi.scripting.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.validation.ValidationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link ScriptEvaluatorFactory} suitable for OSGi environments. It is created
 * based on the {@code BundleContext} which is used to iterate through {@code Bundle}s and find all {@link ScriptEngineFactory}
 * candidates.
 *
 * @author Marko Bekhta
 */
public class OsgiScriptEvaluatorFactory extends AbstractCachingScriptEvaluatorFactory {

    private final List<ScriptEngineManager> scriptEngineManagers;

    public OsgiScriptEvaluatorFactory(BundleContext context) {
        this.scriptEngineManagers = Collections.unmodifiableList( findManagers( context ) );
    }

    @Override
    protected ScriptEvaluator createNewScriptEvaluator(String languageName) throws ScriptEvaluatorNotFoundException {
        return scriptEngineManagers.stream()
                .map( manager -> manager.getEngineByName( languageName ) )
                .filter( Objects::nonNull )
                .map(ScriptEngineScriptEvaluator::new)
                .findFirst()
                .orElseThrow( () -> new ValidationException( String.format( "Unable to find script evaluator for '%s'.", languageName ) ) );
    }

    private List<ScriptEngineManager> findManagers(BundleContext context) {
        return findFactoryCandidates( context ).stream()
                .map( className -> {
                    try {
                        return new ScriptEngineManager( Class.forName( className ).getClassLoader() );
                    }
                    catch (ClassNotFoundException e) {
                        throw new ValidationException( "Unable to instantiate '" + className + "' based engine factory manager.", e );
                    }
                } ).collect( Collectors.toList() );
    }

    /**
     * Iterates through all bundles to get the available {@link ScriptEngineFactory} classes
     *
     * @return the names of the available ScriptEngineFactory classes
     *
     */
    private List<String> findFactoryCandidates(BundleContext context) {
        return Arrays.stream( context.getBundles() )
                .filter( Objects::nonNull )
                .filter( bundle -> !"system.bundle".equals( bundle.getSymbolicName() ) )
                .flatMap( this::toStreamOfResourcesURL )
                .filter( Objects::nonNull )
                .flatMap( url -> toListOfFactoryCandidates( url ).stream() )
                .collect( Collectors.toList() );
    }

    private Stream<URL> toStreamOfResourcesURL(Bundle bundle) {
        Enumeration<URL> entries = bundle.findEntries(
                "META-INF/services",
                "javax.script.ScriptEngineFactory",
                false
        );
        return entries != null ? Collections.list( entries ).stream() : Stream.empty();
    }

    private List<String> toListOfFactoryCandidates(URL url) {
        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream(), StandardCharsets.UTF_8) ) ) {
            return reader.lines()
                    .map( String::trim )
                    .filter( line -> !line.isEmpty() )
                    .filter( line -> !line.startsWith( "#" ) )
                    .collect( Collectors.toList() );
        }
        catch (IOException e) {
            throw new ValidationException( "Unable to read the ScriptEngineFactory resource file", e );
        }
    }
}
