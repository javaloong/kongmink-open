package org.javaloong.kongmink.open.liquibase.extender.internal;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ResourceAccessor;
import org.javaloong.kongmink.open.liquibase.extender.LiquibaseExtenderException;
import org.osgi.framework.*;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.BundleTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

import static java.lang.Thread.currentThread;

public class LiquibaseBundleTracker extends BundleTracker<Object> {

    public static final String LIQUIBASE_ENTITY_MANAGER_FACTORY_FILTER_HEADER = "Liquibase-FilterEntityManagerFactory";
    public static final String LIQUIBASE_PATH = "/OSGI-INF/liquibase/";

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseBundleTracker.class);

    private final BundleContext context;

    private final Set<String> bundlesProcessed = new HashSet<>();

    public LiquibaseBundleTracker(BundleContext context) {
        super(context, Bundle.ACTIVE | Bundle.STARTING | Bundle.RESOLVED, null);
        this.context = context;
    }

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        if (LOG.isDebugEnabled())
            LOG.debug("Checking bundle {} for liquibase header", bundle.getSymbolicName());
        try {
            checkAndRunLiquibase(bundle);
        } catch (Exception e) {
            LOG.error("An error occurred while processing bundle {} for liquibase extender.",
                    bundle.getSymbolicName(), e);
        }
        return null;
    }

    private void checkAndRunLiquibase(Bundle bundle) throws LiquibaseExtenderException, InvalidSyntaxException {
        if (bundlesProcessed.contains(bundle.getSymbolicName())) {
            if (LOG.isDebugEnabled())
                LOG.debug("The bundle {} already updated, ignored", bundle.getSymbolicName());
            return;
        }
        String filterEntityManagerFactory = getLiquibaseExtenderFilterEntityManagerFactory(bundle);
        if (filterEntityManagerFactory != null && !bundlesProcessed.contains(bundle.getSymbolicName())) {
            if (LOG.isDebugEnabled())
                LOG.debug("The bundle {} has liquibase header with filter \"{}\"", bundle.getSymbolicName(),
                        filterEntityManagerFactory);
            ServiceReference<EntityManagerFactory> entityManagerFactorySR = getEntityManagerFactory(filterEntityManagerFactory);
            if (entityManagerFactorySR == null) {
                if (LOG.isDebugEnabled())
                    LOG.debug("The entity manager factory is not available, proceed until the entity manager factory is available");
                registerForServiceNotAvailable(bundle, filterEntityManagerFactory);
            } else {
                runLiquibase(bundle, entityManagerFactorySR);
            }
        }
    }

    /**
     * Execute liquibase for bundle, checking dependant bundles to run before
     *
     * @param bundle                 the bundle
     * @param entityManagerFactorySR the reference to entity manager factory service
     * @throws LiquibaseExtenderException liquibase extender exception
     * @throws InvalidSyntaxException     If filter contains an invalid filter string that cannot be parsed.
     */
    private void runLiquibase(Bundle bundle, ServiceReference<EntityManagerFactory> entityManagerFactorySR) throws LiquibaseExtenderException,
            InvalidSyntaxException {
        Collection<Bundle> dependantBundles = getDependantBundles(bundle);
        if (LOG.isDebugEnabled())
            LOG.debug("Check {} dependencies of the bundle {} to run liquibase before this", bundle.getSymbolicName(),
                    dependantBundles.size());
        for (Bundle dependantBundle : dependantBundles) {
            if (dependantBundle != bundle)
                checkAndRunLiquibase(dependantBundle);
        }
        EntityManagerFactory entityManagerFactory = context.getService(entityManagerFactorySR);
        ClassLoader tccl = currentThread().getContextClassLoader();
        currentThread().setContextClassLoader(Liquibase.class.getClassLoader());
        try {
            runLiquibase(bundle, getDataSource(entityManagerFactory));
        } finally {
            currentThread().setContextClassLoader(tccl);
        }
        context.ungetService(entityManagerFactorySR);
    }

    private DataSource getDataSource(EntityManagerFactory entityManagerFactory) throws LiquibaseExtenderException {
        Object value = entityManagerFactory.getProperties().get("javax.persistence.nonJtaDataSource");
        if (value instanceof DataSource) {
            return (DataSource) value;
        }
        value = entityManagerFactory.getProperties().get("javax.persistence.jtaDataSource");
        if (value instanceof DataSource) {
            return (DataSource) value;
        }
        value = entityManagerFactory.getProperties().get("hibernate.connection.datasource");
        if (value instanceof DataSource) {
            return (DataSource) value;
        }
        throw new LiquibaseExtenderException("An error occurred while try to get datasource from entity manager factory");
    }

    private void registerForServiceNotAvailable(final Bundle bundle, final String filterEntityManagerFactory) throws InvalidSyntaxException {
        context.addServiceListener(new ServiceListener() {

            @Override
            public void serviceChanged(ServiceEvent event) {
                if (event.getType() == ServiceEvent.REGISTERED) {

                    try {
                        checkAndRunLiquibase(bundle);
                    } catch (Exception e) {
                        LOG.error(
                                MessageFormat.format("An error occurred while processing bundle {0} for liquibase extender.",
                                        bundle.getSymbolicName()), e);
                    }
                    context.removeServiceListener(this);
                }
            }
        }, filterEntityManagerFactory);
    }

    private synchronized void runLiquibase(Bundle bundle, DataSource dataSource) throws LiquibaseExtenderException {
        LOG.debug("Executing update database.");
        List<String> liquibasePaths = new ArrayList<>();
        for (Enumeration<String> paths = bundle.getEntryPaths(LIQUIBASE_PATH); paths.hasMoreElements(); ) {
            String path = paths.nextElement();
            // TODO this is a restriction which is necessary drop off
            if (path.endsWith(".xml")) {
                liquibasePaths.add(path);
            }
        }
        Connection conn = null;
        try {
            liquibasePaths.sort(String.CASE_INSENSITIVE_ORDER);
            conn = dataSource.getConnection();
            ResourceAccessor resourceAccessor = new OSGiBundleResourceAccessor(bundle);
            Database database = createDatabase(conn);
            for (String path : liquibasePaths) {
                Liquibase liquibase = new Liquibase(path, resourceAccessor, database);
                LOG.debug("Updating database with path: {}", path);
                liquibase.update((String) null);
            }
        } catch (SQLException ex) {
            throw new LiquibaseExtenderException("An error occurred while try to get connection from datasource", ex);
        } catch (LiquibaseException ex) {
            throw new LiquibaseExtenderException("An error occurred while try run liquibase updates", ex);
        } finally {
            if (conn != null) {

                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOG.error("An error occurred while trying to close connection", ex);
                    // do nothing
                }
            }
        }

        bundlesProcessed.add(bundle.getSymbolicName());
    }

    private Collection<Bundle> getDependantBundles(Bundle bundle) {
        BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
        List<BundleWire> bundleWires = bundleWiring.getRequiredWires(null);
        HashSet<Bundle> bundles = new HashSet<>();
        for (BundleWire bundleWire : bundleWires) {
            bundles.add(bundleWire.getProvider().getBundle());
        }
        return bundles;
    }

    /**
     * Get entity manager factory from service filter. Return null if entityManagerFactory not found
     *
     * @param filterEntityManagerFactory the entity manager factory filter
     * @return the entity manager factory
     * @throws LiquibaseExtenderException liquibase extender exception
     */
    private ServiceReference<EntityManagerFactory> getEntityManagerFactory(String filterEntityManagerFactory) throws LiquibaseExtenderException {
        try {
            Collection<ServiceReference<EntityManagerFactory>> entityManagerFactories = context.getServiceReferences(EntityManagerFactory.class, filterEntityManagerFactory);
            if (entityManagerFactories.size() == 0) {
                return null;
            } else if (entityManagerFactories.size() > 1) {
                // TODO Get first data source with this filter, WARNING
                LOG.warn("Found more than one entityManagerFactory for the filter: {}", filterEntityManagerFactory);
            }
            return entityManagerFactories.iterator().next();
        } catch (InvalidSyntaxException e) {
            throw new LiquibaseExtenderException(MessageFormat.format(
                    "Error occurred while trying to get entityManagerFactory with filter {0}: {1}", filterEntityManagerFactory, e.getMessage()), e);

        }
    }

    private String getLiquibaseExtenderFilterEntityManagerFactory(Bundle bundle) {
        Dictionary<String, String> headers = bundle.getHeaders();
        return headers.get(LIQUIBASE_ENTITY_MANAGER_FACTORY_FILTER_HEADER);
    }

    protected Database createDatabase(Connection c) throws DatabaseException {
        return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
    }

}
