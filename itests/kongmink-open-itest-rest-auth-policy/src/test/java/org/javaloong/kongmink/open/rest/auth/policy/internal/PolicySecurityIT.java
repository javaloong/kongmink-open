package org.javaloong.kongmink.open.rest.auth.policy.internal;

import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.common.model.Client;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.EvaluationContext;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.ops4j.pax.exam.Option;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;

public class PolicySecurityIT extends SecurityTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(PolicySecurityIT.class).getBundleContext();
        context.registerService(PolicyEvaluator.class, Mockito.mock(PolicyEvaluator.class), null);
    }

    @Inject
    PolicyEvaluator policyEvaluator;

    @Test
    public void testPolicyNoAuthPermission() {
        when(policyEvaluator.evaluate(any(String.class), any(EvaluationContext.class))).thenReturn(false);

        given().contentType(ContentType.JSON).body(new Client())
                .post("/clients").then().assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void testPolicyAuthPermission() {
        when(policyEvaluator.evaluate(any(String.class), any(EvaluationContext.class))).thenReturn(true);

        Client client = new Client();
        client.setName("test");
        given().contentType(ContentType.JSON).body(client)
                .post("/clients").then().assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Override
    protected Option testBundles() {
        return composite(super.testBundles(),
                BndDSOptions.dsBundle("test.resources",
                        bundle().add(TestClientResource.class))
        );
    }
}
