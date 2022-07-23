package org.javaloong.kongmink.open.rest.auth.jwt.internal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.javaloong.kongmink.open.itest.common.PaxExamTestSupport;
import org.junit.BeforeClass;
import org.ops4j.pax.exam.Option;

import java.util.Map;

import static org.ops4j.pax.exam.CoreOptions.*;

public abstract class SecurityTestSupport extends PaxExamTestSupport {

    @BeforeClass
    public static void setupRestAssured() {
        RestAssured.baseURI = "http://localhost:8080/api";
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-apim-api").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-service").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest-auth").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest-auth-jwt").versionAsInProject(),

                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-itest-common").versionAsInProject()
        );
    }

    protected Option shiro() {
        return composite(pac4j(),
                systemPackage("javax.mail.internet;version=1.0"),
                mavenBundle("commons-beanutils", "commons-beanutils").versionAsInProject(),
                mavenBundle("commons-collections", "commons-collections").versionAsInProject(),
                wrappedBundle(mavenBundle("org.owasp.encoder", "encoder").versionAsInProject()),
                mavenBundle("org.apache.commons", "commons-lang3").versionAsInProject(),
                mavenBundle("org.apache.commons", "commons-text", "1.8"),
                mavenBundle("org.apache.commons", "commons-configuration2", "2.7"),
                mavenBundle("org.apache.shiro", "shiro-core").versionAsInProject(),
                mavenBundle("org.apache.shiro", "shiro-web").versionAsInProject(),
                mavenBundle("org.apache.shiro", "shiro-jaxrs").versionAsInProject()
        );
    }

    protected Option pac4j() {
        return composite(
                wrappedBundle(mavenBundle("com.github.stephenc.jcip", "jcip-annotations").versionAsInProject()),
                mavenBundle("net.minidev", "accessors-smart").versionAsInProject(),
                mavenBundle("net.minidev", "json-smart").versionAsInProject(),
                mavenBundle("com.nimbusds", "nimbus-jose-jwt").versionAsInProject(),
                mavenBundle("org.pac4j", "pac4j-core").versionAsInProject(),
                mavenBundle("org.pac4j", "pac4j-jwt").versionAsInProject(),
                mavenBundle("org.pac4j", "pac4j-http").versionAsInProject(),
                wrappedBundle(mavenBundle("io.buji", "buji-pac4j").versionAsInProject())
        );
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

    protected String getAccessToken() {
        return "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEZklVSW5wMUlscTdCMHhfMC0tdjgxNjdHcm5pR2Y0eVVfdExKX0RtRnhVIn0.eyJleHAiOjE5NzQxMDExNzAsImlhdCI6MTY1ODQ4MTk3MCwianRpIjoiNjUyOGJkODQtMzY1Zi00NmRhLTgzYWEtOTc1M2MyYzMyYTU4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDkwL2F1dGgvcmVhbG1zL2tvbmdtaW5rLW9wZW4iLCJhdWQiOlsiZGV2ZWxvcGVyLWFjY291bnQiLCJhY2NvdW50Il0sInN1YiI6ImRhNzFhOTdlLTVjOTktNDg2OC1hMGEyLTY5MGZmMmE0NDU2MyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImRldmVsb3Blci1wb3J0YWwiLCJzZXNzaW9uX3N0YXRlIjoiMjY3YmZhMTktMzUyMi00MWUwLTg2NjEtZjJmY2FhMjAyNDNhIiwiYWNyIjoiMSIsInJlc291cmNlX2FjY2VzcyI6eyJkZXZlbG9wZXItYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYXBwbGljYXRpb25zIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiIyNjdiZmExOS0zNTIyLTQxZTAtODY2MS1mMmZjYWEyMDI0M2EiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlcjEiLCJlbWFpbCI6InVzZXIxQGV4YW1wbGUuY29tIn0.HV6fu49mXUFdVVL9R1Z3i1Uss2c9gzq81r2I3dE7td6HI3wTNa1zUZl2vsm5sS-H6z7wLbuZtSaRdTLIOdOIEy-DcYuqcLBgY6SjRoLxf64mRmqzOsHb-hP2HnZ2_hE8jpdmCKYnmmvnMaOM5WBqrdRmI_yxbvLXK5byF5azuDbD6DdOEZ6OSLDyOfnR7lZI457fdkK5C-ckhAcaDcj6OAVKf_RB-BH0M-53_AIfoQuO0SwqTtm7R6lM-mcB1bSApeBCRDg_YR--4mIzyvXCgQvXzgUyWW1-uVA1W0FYzJSpLPe29Bp0_6HIqgF16AZ3LAb50hqdkkmX786WwSqBog";
    }
}
