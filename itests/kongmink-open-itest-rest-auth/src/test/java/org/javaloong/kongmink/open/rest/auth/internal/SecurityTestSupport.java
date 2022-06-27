package org.javaloong.kongmink.open.rest.auth.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.javaloong.kongmink.open.itest.common.PaxExamTestSupport;
import org.javaloong.kongmink.open.rest.auth.oidc.DummyOidcAuthenticator;
import org.junit.BeforeClass;
import org.ops4j.pax.exam.Option;
import org.osgi.framework.Constants;

import java.util.Map;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;

public abstract class SecurityTestSupport extends PaxExamTestSupport {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://localhost:8080/api";
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    return objectMapper;
                }));
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
        return composite(shiro(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-common").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest-auth").versionAsInProject(),

                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-itest-common").versionAsInProject()
        );
    }

    protected Option shiro() {
        return composite(
                systemPackage("javax.mail.internet;version=1.0"),
                mavenBundle("commons-beanutils", "commons-beanutils").versionAsInProject(),
                mavenBundle("commons-collections", "commons-collections").versionAsInProject(),
                wrappedBundle(mavenBundle("org.owasp.encoder", "encoder").versionAsInProject()),
                mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject(),
                mavenBundle("org.apache.commons", "commons-text", "1.8"),
                mavenBundle("org.apache.commons", "commons-configuration2", "2.7"),
                mavenBundle("org.apache.shiro", "shiro-core").versionAsInProject(),
                mavenBundle("org.apache.shiro", "shiro-web").versionAsInProject(),
                mavenBundle("org.apache.shiro", "shiro-jaxrs").versionAsInProject(),
                pac4jOidc(),
                mavenBundle("org.pac4j", "pac4j-core").versionAsInProject(),
                wrappedBundle(mavenBundle("io.buji", "buji-pac4j").versionAsInProject())
        );
    }

    protected Option pac4jOidc() {
        return composite(
                dummyOidcBundle(),
                wrappedBundle(mavenBundle("com.github.stephenc.jcip", "jcip-annotations").versionAsInProject()),
                mavenBundle("net.minidev", "accessors-smart").versionAsInProject(),
                mavenBundle("net.minidev", "json-smart").versionAsInProject(),
                mavenBundle("com.nimbusds", "content-type").versionAsInProject(),
                mavenBundle("com.nimbusds", "lang-tag").versionAsInProject(),
                mavenBundle("com.nimbusds", "oauth2-oidc-sdk").versionAsInProject(),
                mavenBundle("com.nimbusds", "nimbus-jose-jwt").versionAsInProject(),
                mavenBundle("org.pac4j", "pac4j-http").versionAsInProject(),
                mavenBundle("org.pac4j", "pac4j-oidc").versionAsInProject()
        );
    }

    protected Option dummyOidcBundle() {
        return BndDSOptions.fragmentBundle(DummyOidcAuthenticator.class.getSimpleName(),
                bundle().add(DummyOidcAuthenticator.class)
                .set(Constants.REQUIRE_CAPABILITY, String.format( // Fix osgi.ee=unknown
                        "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=%s))\"", Runtime.version().feature()))
                .set(Constants.FRAGMENT_HOST, "org.javaloong.kongmink.open.kongmink-open-rest-auth"));
    }

    @Override
    protected Option junit() {
        return composite(super.junit(), mockito(), restAssured());
    }

    protected Option mockito() {
        return composite(
                mavenBundle("net.bytebuddy", "byte-buddy").versionAsInProject(),
                mavenBundle("net.bytebuddy", "byte-buddy-agent").versionAsInProject(),
                mavenBundle("org.objenesis", "objenesis").versionAsInProject(),
                mavenBundle("org.mockito", "mockito-core").versionAsInProject()
        );
    }

    protected Option restAssured() {
        return composite(
                mavenBundle("org.hamcrest", "hamcrest").versionAsInProject(),
                mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject(),
                mavenBundle("org.codehaus.groovy", "groovy-json").versionAsInProject().noStart(),
                mavenBundle("org.codehaus.groovy", "groovy-xml").versionAsInProject().noStart(),
                mavenBundle("org.codehaus.groovy", "groovy").versionAsInProject(),
                wrappedBundle(mavenBundle("org.ccil.cowan.tagsoup", "tagsoup").versionAsInProject()),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpclient").versionAsInProject()),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpmime").versionAsInProject()),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpcore").versionAsInProject()),
                mavenBundle("io.rest-assured", "json-path").versionAsInProject(),
                mavenBundle("io.rest-assured", "xml-path").versionAsInProject(),
                mavenBundle("io.rest-assured", "rest-assured").versionAsInProject(),
                mavenBundle("io.rest-assured", "rest-assured-common").versionAsInProject()
        );
    }
}
