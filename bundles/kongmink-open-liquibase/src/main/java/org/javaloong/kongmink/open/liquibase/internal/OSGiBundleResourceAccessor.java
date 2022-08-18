package org.javaloong.kongmink.open.liquibase.internal;

import liquibase.resource.OSGiResourceAccessor;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import java.io.IOException;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class OSGiBundleResourceAccessor extends OSGiResourceAccessor {

    private final Bundle bundle;

    public OSGiBundleResourceAccessor(Bundle bundle) {
        super(bundle);
        this.bundle = bundle;
    }

    @Override
    public SortedSet<String> list(String relativeTo, String path, boolean recursive, boolean includeFiles, boolean includeDirectories) throws IOException {
        init();

        String finalPath = getFinalPath(relativeTo, path);

        return listFromBundleWiring(finalPath);
    }

    protected SortedSet<String> listFromBundleWiring(String path) {
        final SortedSet<String> returnSet = new TreeSet<>();

        BundleWiring wiring = bundle.adapt(BundleWiring.class);
        Collection<String> resources = wiring.listResources(path, "*", BundleWiring.LISTRESOURCES_LOCAL);
        if (resources == null) {
            return returnSet;
        }

        returnSet.addAll(resources);
        return returnSet;
    }
}
