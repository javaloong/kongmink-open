package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.itest.common.PaxExamTestSupport;
import org.javaloong.kongmink.open.itest.common.annotation.AfterOsgi;
import org.javaloong.kongmink.open.itest.common.annotation.BeforeOsgi;
import org.ops4j.pax.exam.Option;

import java.util.Map;

import static org.ops4j.pax.exam.Constants.START_LEVEL_SYSTEM_BUNDLES;
import static org.ops4j.pax.exam.CoreOptions.*;

public abstract class GraviteeMockTestSupport extends PaxExamTestSupport {

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
                oauth2(),
                mavenBundle("org.modelmapper", "modelmapper").versionAsInProject(),
                wrappedBundle(mavenBundle("org.modelmapper", "modelmapper-module-jsr310").versionAsInProject()),
                mavenBundle("com.github.ben-manes.caffeine", "caffeine").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-common").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-apim-core").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-apim-gravitee").versionAsInProject(),

                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-itest-common").versionAsInProject()
        );
    }

    protected Option oauth2() {
        return composite(
                wrappedBundle(mavenBundle("com.github.stephenc.jcip", "jcip-annotations").versionAsInProject()),
                mavenBundle("net.minidev", "accessors-smart").versionAsInProject(),
                mavenBundle("net.minidev", "json-smart").versionAsInProject(),
                mavenBundle("com.nimbusds", "content-type").versionAsInProject(),
                mavenBundle("com.nimbusds", "lang-tag").versionAsInProject(),
                mavenBundle("com.nimbusds", "oauth2-oidc-sdk").versionAsInProject(),
                mavenBundle("com.nimbusds", "nimbus-jose-jwt").versionAsInProject()
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
