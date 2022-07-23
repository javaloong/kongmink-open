package org.javaloong.kongmink.open.rest.auth.jwt.internal;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.tinybundles.core.TinyBundle;

import static org.ops4j.pax.exam.CoreOptions.streamBundle;
import static org.ops4j.pax.tinybundles.core.TinyBundles.withBnd;

/**
 * This must be in its own bundle and static to avoid that TinyBundles has to be deployed in OSGi
 */
public class BndDSOptions {

    private BndDSOptions() {
    }

    /**
     * Create a bundle with DS support and automatically generated exports and imports
     */
    public static Option dsBundle(String symbolicName, TinyBundle bundleDef) {
        return streamBundle(bundleDef
                .symbolicName(symbolicName)
                .build(withBnd()));
    }

    /**
     * Create a fragment bundle and automatically generated exports and imports
     */
    public static Option fragmentBundle(String symbolicName, TinyBundle bundleDef) {
        return streamBundle(bundleDef
                .symbolicName(symbolicName)
                .build(withBnd())).noStart();
    }
}
