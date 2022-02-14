package org.javaloong.kongmink.open.am.keycloak.internal;

import org.javaloong.kongmink.open.itest.common.PaxExamTestSupport;
import org.javaloong.kongmink.open.itest.common.annotation.AfterOsgi;
import org.javaloong.kongmink.open.itest.common.annotation.BeforeOsgi;
import org.ops4j.pax.exam.Option;

import java.util.Map;

import static org.ops4j.pax.exam.Constants.START_LEVEL_SYSTEM_BUNDLES;
import static org.ops4j.pax.exam.CoreOptions.*;

public class KeycloakMockTestSupport extends PaxExamTestSupport {

    @BeforeOsgi
    public static void startServers() {
        MockServer.start();
    }

    @AfterOsgi
    public static void stopServers() {
        MockServer.stop();
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
        return composite(
                mavenBundle("org.apache.cxf", "cxf-rt-rs-json-basic").versionAsInProject(),
                mavenBundle("org.apache.cxf", "cxf-rt-rs-security-jose").versionAsInProject(),
                mavenBundle("org.apache.cxf", "cxf-rt-rs-security-jose-jaxrs").versionAsInProject(),
                mavenBundle("org.apache.cxf", "cxf-rt-rs-security-oauth2").versionAsInProject(),
                mavenBundle("org.keycloak", "keycloak-common").versionAsInProject(),
                mavenBundle("org.keycloak", "keycloak-core").versionAsInProject(),
                mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject(),
                mavenBundle("org.modelmapper", "modelmapper").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-common").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-am-core").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-am-keycloak").versionAsInProject(),

                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-itest-common").versionAsInProject()
        );
    }

    @Override
    protected Option httpService() {
        return composite(
                mavenBundle("org.apache.felix", "org.apache.felix.http.servlet-api", "1.1.2")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.apache.felix", "org.apache.felix.http.jetty", "4.1.12")
                        .startLevel(START_LEVEL_SYSTEM_BUNDLES).noStart()
        );
    }
}
