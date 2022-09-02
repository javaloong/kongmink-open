package org.javaloong.kongmink.open.itest.common;

import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import javax.inject.Inject;
import java.io.File;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.ops4j.pax.exam.Constants.START_LEVEL_SYSTEM_BUNDLES;
import static org.ops4j.pax.exam.Constants.START_LEVEL_TEST_BUNDLE;
import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.cm.ConfigurationAdminOptions.configurationFolder;

@RunWith(ExtPaxExam.class)
@ExamReactorStrategy(PerClass.class)
public abstract class PaxExamTestSupport {

    protected static final String USE_CONFIG_ADMIN = "USE_CONFIG_ADMIN";
    protected static final String USE_EVENT_ADMIN = "USE_EVENT_ADMIN";
    protected static final String USE_COORDINATOR = "USE_COORDINATOR";
    protected static final String USE_SCR = "USE_SCR";
    protected static final String USE_JDBC = "USE_JDBC";
    protected static final String USE_JPA = "USE_JPA";
    protected static final String USE_JPA_PROVIDER = "USE_JPA_PROVIDER";
    protected static final String USE_TX_CONTROL = "USE_TX_CONTROL";
    protected static final String USE_JAX_RS_WHITEBOARD = "USE_JAX_RS_WHITEBOARD";

    protected static final String TX_CONTROL_FILTER = "tx.control.filter";

    @Inject
    BundleContext bundleContext;

    Map<String, Boolean> configurationSettings = defaultSettings();

    public static int getAvailablePort(int min, int max) {
        for (int i = min; i <= max; i++) {
            try (ServerSocket socket = new ServerSocket(i)) {
                return socket.getLocalPort();
            } catch (Exception e) {
                System.err.println("Port " + i + " not available, trying next one");
                // try next port
            }
        }
        throw new IllegalStateException("Can't find available network ports");
    }

    public static <T> T getService(BundleContext bundleContext, Class<T> type) {
        ServiceReference<T> serviceReference = bundleContext.getServiceReference(type);
        return bundleContext.getService(serviceReference);
    }

    public <T> T getService(Class<T> type) {
        return getService(bundleContext, type);
    }

    @Configuration
    public Option[] config() {
        return new Option[]{
                baseOptions(),
                useOption(this::jdbc, USE_JDBC),
                useOption(this::jpa, USE_JPA),
                useOption(this::jpaProvider, USE_JPA_PROVIDER),
                useOption(this::txControl, USE_TX_CONTROL),
                useOption(this::jaxRSWhiteboard, USE_JAX_RS_WHITEBOARD),
                testBundles(),
                // Felix config admin
                useOption(this::configAdmin, USE_CONFIG_ADMIN),
                // Felix event admin
                useOption(this::eventAdmin, USE_EVENT_ADMIN),
                // Felix coordinator
                useOption(this::coordinator, USE_COORDINATOR),
                // Felix scr
                useOption(this::scr, USE_SCR)
        };
    }

    private Option useOption(Supplier<Option> supplier, String useKey) {
        return configurationSettings.get(useKey) ? supplier.get() : null;
    }

    private Map<String, Boolean> defaultSettings() {
        Map<String, Boolean> settings = new HashMap<>();
        settings.put(USE_JDBC, true);
        settings.put(USE_JPA, true);
        settings.put(USE_JPA_PROVIDER, true);
        settings.put(USE_TX_CONTROL, true);
        settings.put(USE_JAX_RS_WHITEBOARD, true);
        settings.put(USE_CONFIG_ADMIN, true);
        settings.put(USE_EVENT_ADMIN, false);
        settings.put(USE_COORDINATOR, true);
        settings.put(USE_SCR, true);
        customizeSettings(settings);
        return settings;
    }

    protected void customizeSettings(Map<String, Boolean> settings) {
        // Override configuration settings
    }

    protected Option configAdmin() {
        return mavenBundle("org.apache.felix", "org.apache.felix.configadmin")
                .versionAsInProject().startLevel(START_LEVEL_SYSTEM_BUNDLES);
    }

    protected Option eventAdmin() {
        return mavenBundle("org.apache.felix", "org.apache.felix.eventadmin")
                .versionAsInProject().startLevel(START_LEVEL_SYSTEM_BUNDLES);
    }

    protected Option coordinator() {
        return mavenBundle("org.apache.felix", "org.apache.felix.coordinator")
                .versionAsInProject().startLevel(START_LEVEL_SYSTEM_BUNDLES);
    }

    protected Option scr() {
        return composite(
                mavenBundle("org.osgi", "org.osgi.util.function", "1.1.0")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.osgi", "org.osgi.util.promise", "1.1.1")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.apache.felix", "org.apache.felix.scr")
                        .versionAsInProject().startLevel(START_LEVEL_SYSTEM_BUNDLES)
        );
    }

    protected Option baseOptions() {
        return composite(
                // Settings for the OSGi 4.3 Weaving
                // By default, we will not weave any classes. Change this setting to include classes
                // that you application needs to have woven.
                systemProperty("org.apache.aries.proxy.weaving.enabled").value("none"),
                // This gives a fast fail when any bundle is unresolved
                systemProperty("pax.exam.osgi.unresolved.fail").value("true"),
                frameworkStartLevel(START_LEVEL_TEST_BUNDLE),
                workingDirectory("target/pax-exam"),
                logback(),
                junit(),
                configurationLocation(new File("src/test/resources/config"))
        );
    }

    protected Option configurationLocation(File folder) {
        return folder.exists() ? configurationFolder(folder) : null;
    }

    protected Option logback() {
        return composite(
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO"),
                systemProperty("org.ops4j.pax.logging.property.file").value("src/test/resources/logback.xml"),
                mavenBundle("org.ops4j.pax.logging", "pax-logging-api", "2.0.10")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.ops4j.pax.logging", "pax-logging-logback", "2.0.10")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES)
        );
    }

    protected Option junit() {
        return composite(
                junitBundles(),
                mavenBundle("org.assertj", "assertj-core").versionAsInProject(),
                mavenBundle("org.awaitility", "awaitility").versionAsInProject()
        );
    }

    protected abstract Option testBundles();

    protected Option jaxRSWhiteboard() {
        return composite(
                ariesJaxRSWhiteboard(),
                ariesJaxRSWhiteboardJackson()
        );
    }

    protected Option ariesJaxRSWhiteboardJackson() {
        return composite(
                jackson(),
                mavenBundle("org.apache.aries.jax.rs", "org.apache.aries.jax.rs.jackson", "2.0.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option jackson() {
        return composite(
                mavenBundle("com.fasterxml.jackson.core", "jackson-core", "2.13.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml.jackson.core", "jackson-annotations", "2.13.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml.jackson.core", "jackson-databind", "2.13.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.13.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", "2.13.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml.jackson.jaxrs", "jackson-jaxrs-base", "2.13.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml.jackson.jaxrs", "jackson-jaxrs-json-provider", "2.13.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml.jackson.module", "jackson-module-jaxb-annotations", "2.13.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.yaml", "snakeyaml", "1.30")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option ariesJaxRSWhiteboard() {
        return composite(
                cxf(),
                jaxb(),
                spiflyBundles(),
                mavenBundle("org.osgi", "org.osgi.service.jaxrs", "1.0.0")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.apache.aries.component-dsl", "org.apache.aries.component-dsl.component-dsl", "1.2.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.aries.jax.rs", "org.apache.aries.jax.rs.whiteboard", "2.0.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option cxf() {
        return composite(
                httpService(),
                systemPackage("javax.xml.ws;version=1.0"),
                mavenBundle("javax.annotation", "javax.annotation-api", "1.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.aries.spec", "org.apache.aries.javax.jax.rs-api", "1.0.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml.woodstox", "woodstox-core", "6.2.8")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.ws.xmlschema", "xmlschema-core", "2.2.5")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.codehaus.woodstox", "stax2-api", "4.2.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.cxf", "cxf-core", "3.4.8")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.cxf", "cxf-rt-frontend-jaxrs", "3.4.8")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.cxf", "cxf-rt-rs-client", "3.4.8")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.cxf", "cxf-rt-rs-sse", "3.4.8")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.cxf", "cxf-rt-rs-extension-providers", "3.4.8")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.cxf", "cxf-rt-security", "3.4.8")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.cxf", "cxf-rt-transports-http", "3.4.8")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option jaxb() {
        return composite(
                mavenBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.activation-api-1.2.1", "1.2.1_3")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("jakarta.xml.bind", "jakarta.xml.bind-api", "2.3.3")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("com.sun.xml.bind", "jaxb-osgi", "2.3.3")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES)
        );
    }

    protected Option httpService() {
        return felixHttpService();
    }

    protected Option felixHttpService() {
        return composite(
                mavenBundle("org.apache.felix", "org.apache.felix.http.servlet-api", "1.1.2")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.apache.felix", "org.apache.felix.http.jetty", "4.1.12")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES)
        );
    }

    protected Option paxWebHttpService() {
        return composite(
                mavenBundle("javax.servlet", "javax.servlet-api", "3.1.0"),
                mavenBundle("javax.annotation", "javax.annotation-api", "1.3"),
                mavenBundle("org.apache.xbean", "xbean-bundleutils", "4.19"),
                mavenBundle("org.apache.xbean", "xbean-reflect", "4.19"),
                mavenBundle("org.apache.xbean", "xbean-finder", "4.19"),
                mavenBundle("org.eclipse.jetty", "jetty-continuation", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jetty", "jetty-http", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jetty", "jetty-io", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jetty", "jetty-security", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jetty", "jetty-server", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jetty", "jetty-servlet", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jetty", "jetty-xml", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jetty", "jetty-util", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jetty", "jetty-util-ajax", "9.4.43.v20210629"),
                mavenBundle("org.eclipse.jdt.core.compiler", "ecj", "4.5.1"),
                mavenBundle("org.ops4j.pax.web", "pax-web-api", "7.3.19"),
                mavenBundle("org.ops4j.pax.web", "pax-web-spi", "7.3.19"),
                mavenBundle("org.ops4j.pax.web", "pax-web-jetty", "7.3.19"),
                mavenBundle("org.ops4j.pax.web", "pax-web-runtime", "7.3.19"),
                mavenBundle("org.ops4j.pax.web", "pax-web-jsp", "7.3.19"),
                mavenBundle("org.ops4j.pax.web", "pax-web-extender-whiteboard", "7.3.19")
        );
    }

    protected Option jpaProvider() {
        return hibernate();
    }

    protected Option hibernate() {
        return composite(
                transaction(),
                systemPackage("javax.xml.namespace;version=1.0"),
                systemPackage("javax.xml.stream;version=1.2"),
                systemPackage("javax.xml.stream.events;version=1.2"),
                systemPackage("javax.xml.stream.util;version=1.2"),
                mavenBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.activation-api-1.2.1", "1.2.1_3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.servicemix.specs", "org.apache.servicemix.specs.jaxb-api-2.3", "2.3_3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.antlr", "2.7.7_5")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.dom4j", "1.6.1_5")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.fasterxml", "classmate", "1.5.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.javassist", "javassist", "3.27.0-GA")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("net.bytebuddy", "byte-buddy", "1.11.12")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.jboss.logging", "jboss-logging", "3.4.1.Final")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.jboss", "jandex", "2.2.3.Final")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.hibernate.common", "hibernate-commons-annotations", "5.0.1.Final")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.hibernate", "hibernate-core", "5.2.18.Final")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.hibernate", "hibernate-osgi", "5.2.18.Final")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option txControl() {
        return composite(
                xaTxControlService(),
                xaJpaResourceProvider()
        );
    }

    protected Option localTxControlService() {
        return composite(
                systemProperty(TX_CONTROL_FILTER).value("(osgi.local.enabled=true)"),
                mavenBundle("org.apache.aries.tx-control", "tx-control-service-local", "1.0.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option xaTxControlService() {
        return composite(
                systemProperty(TX_CONTROL_FILTER).value("(osgi.xa.enabled=true)"),
                mavenBundle("org.apache.aries.tx-control", "tx-control-service-xa", "1.0.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option localJpaResourceProvider() {
        return composite(
                mavenBundle("org.apache.aries.tx-control", "tx-control-provider-jpa-local", "1.0.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option xaJpaResourceProvider() {
        return composite(
                mavenBundle("org.apache.aries.tx-control", "tx-control-provider-jpa-xa", "1.0.1")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option jpa() {
        return composite(
                mavenBundle("org.apache.aries.jpa", "org.apache.aries.jpa.container", "2.7.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.aries.jpa.javax.persistence", "javax.persistence_2.1", "2.7.3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option spiflyBundles() {
        return composite(
                mavenBundle("org.ow2.asm", "asm", "9.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.ow2.asm", "asm-util", "9.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.ow2.asm", "asm-tree", "9.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.ow2.asm", "asm-analysis", "9.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.ow2.asm", "asm-commons", "9.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.aries.spifly", "org.apache.aries.spifly.dynamic.bundle", "1.3.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option transaction() {
        return composite(
                // api
                mavenBundle("javax.interceptor", "javax.interceptor-api", "1.2.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javax-inject", "1_3")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("javax.el", "javax.el-api", "3.0.0")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("javax.enterprise", "cdi-api", "1.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("javax.transaction", "javax.transaction-api", "1.2")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option jdbc() {
        return h2();
    }

    protected Option hsqldb() {
        return composite(
                mavenBundle("org.ops4j.pax.jdbc", "pax-jdbc", "1.5.0")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.ops4j.pax.jdbc", "pax-jdbc-hsqldb", "1.5.0")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("org.hsqldb", "hsqldb").versionAsInProject()
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }

    protected Option h2() {
        return composite(
                mavenBundle("org.ops4j.pax.jdbc", "pax-jdbc", "1.5.0")
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1),
                mavenBundle("com.h2database", "h2").versionAsInProject()
                        .startLevel(START_LEVEL_TEST_BUNDLE - 1)
        );
    }
}
