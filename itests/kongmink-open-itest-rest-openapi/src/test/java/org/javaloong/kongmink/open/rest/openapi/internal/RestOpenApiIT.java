package org.javaloong.kongmink.open.rest.openapi.internal;

import org.javaloong.kongmink.open.rest.RESTConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class RestOpenApiIT extends RestOpenApiTestSupport {

    private static final List<ServiceRegistration<?>> registrations = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        registrations.add(registerJAXRSResource(FrameworkUtil.getBundle(RestOpenApiIT.class).getBundleContext()
                , new TestOpenApiResource()));
    }

    @AfterClass
    public static void unregisterServiceRegistrations() {
        registrations.forEach(ServiceRegistration::unregister);
        registrations.clear();
    }

    @Test
    public void testOpenApiEndpoint() {
        String response = webTarget().path("openapi.json").request().get(String.class);
        assertTrue(response.matches(".*\"title\":\\s*\"My Service\".*"));
        assertTrue(response.matches(".*\"description\":\\s*\"Service REST API\".*"));
        assertTrue(response.matches(".*\"email\":\\s*\"me@me.com\".*"));
        assertTrue(response.contains("\"" + RESTConstants.BASE_PATH + "/operation\":"));
    }
}
