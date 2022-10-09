package org.javaloong.kongmink.open.liquibase.extender.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    private LiquibaseBundleTracker tracker;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        LOG.debug("Starting liquibase extender activator");
        registerBundleTracker(bundleContext);
    }

    private void registerBundleTracker(BundleContext bundleContext) {
        tracker = new LiquibaseBundleTracker(bundleContext);
        LOG.debug("Open Liquibase Bundle Tracker");
        tracker.open();
    }

    @Override
    public void stop(BundleContext arg0) throws Exception {
        tracker.close();
    }
}
