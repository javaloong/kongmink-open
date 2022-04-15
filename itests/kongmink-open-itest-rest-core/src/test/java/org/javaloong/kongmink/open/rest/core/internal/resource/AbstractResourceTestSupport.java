package org.javaloong.kongmink.open.rest.core.internal.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.itest.common.PaxExamTestSupport;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.ops4j.pax.exam.Option;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static org.ops4j.pax.exam.CoreOptions.*;

public abstract class AbstractResourceTestSupport extends PaxExamTestSupport {

    private static final List<ServiceRegistration<?>> registrations = new ArrayList<>();

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

    @AfterClass
    public static void unregisterServiceRegistrations() {
        registrations.forEach(ServiceRegistration::unregister);
        registrations.clear();
    }

    public static void registerUserContextProvider(BundleContext context) {
        Hashtable<String, String> props = new Hashtable<>();
        props.put(JaxrsWhiteboardConstants.JAX_RS_EXTENSION, "true");
        props.put(JaxrsWhiteboardConstants.JAX_RS_APPLICATION_SELECT,
                "(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")");
        registrations.add(context.registerService(ContextProvider.class, new MockUserContextProvider(), props));
    }

    @Override
    protected void customizeSettings(Map<String, Boolean> settings) {
        settings.put(USE_JDBC, false);
        settings.put(USE_JPA_PROVIDER, false);
    }

    @Override
    protected Option testBundles() {
        return composite(
                mavenBundle("org.glassfish", "jakarta.el", "3.0.3"),
                mavenBundle("com.fasterxml", "classmate", "1.5.1"),
                mavenBundle("org.apache.geronimo.specs", "geronimo-validation_2.0_spec", "1.1"),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-bean-validator").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-common").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-service").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-rest-core").versionAsInProject(),

                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-itest-common").versionAsInProject()
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

    static class MockUserContextProvider implements ContextProvider<User> {

        @Override
        public User createContext(Message message) {
            User user = new User();
            user.setId("1");
            user.setUsername("user1");
            return user;
        }
    }
}
