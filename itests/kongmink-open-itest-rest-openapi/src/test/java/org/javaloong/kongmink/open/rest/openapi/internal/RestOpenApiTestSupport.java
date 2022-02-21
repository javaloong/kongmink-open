package org.javaloong.kongmink.open.rest.openapi.internal;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.javaloong.kongmink.open.itest.common.PaxExamTestSupport;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.ops4j.pax.exam.Option;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Hashtable;
import java.util.Map;

import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

public abstract class RestOpenApiTestSupport extends PaxExamTestSupport {

    @Inject
    ClientBuilder clientBuilder;

    public WebTarget webTarget() {
        return webTarget("http://localhost:8080" + RESTConstants.BASE_PATH);
    }

    public WebTarget webTarget(String uri) {
        return clientBuilder.build()
                .register(new JacksonJsonProvider())
                .target(uri);
    }

    @Override
    protected void customizeSettings(Map<String, Boolean> settings) {
        settings.put(USE_COORDINATOR, false);
        settings.put(USE_JDBC, false);
        settings.put(USE_JPA, false);
        settings.put(USE_JPA_PROVIDER, false);
        settings.put(USE_TX_CONTROL, false);
    }

    @Override
    protected Option testBundles() {
        return composite(openapi(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest-openapi").versionAsInProject(),

                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-itest-common").versionAsInProject()
        );
    }

    protected Option openapi() {
        return composite(
                mavenBundle("jakarta.validation", "jakarta.validation-api").versionAsInProject(),
                mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject(),
                mavenBundle("io.github.classgraph", "classgraph").versionAsInProject(),
                mavenBundle("io.swagger.core.v3", "swagger-annotations").versionAsInProject(),
                mavenBundle("io.swagger.core.v3", "swagger-models").versionAsInProject(),
                mavenBundle("io.swagger.core.v3", "swagger-core").versionAsInProject(),
                mavenBundle("io.swagger.core.v3", "swagger-integration").versionAsInProject(),
                mavenBundle("io.swagger.core.v3", "swagger-jaxrs2").versionAsInProject(),
                mavenBundle("org.apache.aries.jax.rs", "org.apache.aries.jax.rs.openapi.resource", "2.0.1")
        );
    }

    public static ServiceRegistration<?> registerJAXRSResource(BundleContext context, Object resource) {
        Hashtable<String, String> props = new Hashtable<>();
        props.put(JaxrsWhiteboardConstants.JAX_RS_RESOURCE, "true");
        props.put(JaxrsWhiteboardConstants.JAX_RS_APPLICATION_SELECT,
                "(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")");
        return context.registerService(Object.class, resource, props);
    }
}
