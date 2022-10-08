package org.javaloong.kongmink.open.rest.auth.policy.internal;

import org.javaloong.kongmink.open.core.policy.evaluation.PolicyEvaluator;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

import javax.ws.rs.Priorities;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

@Component(service = Feature.class)
@JaxrsExtension
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
public class PolicyFeature implements Feature {

    @Reference
    PolicyEvaluator policyEvaluator;

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new PolicyAuthorizationFilter(policyEvaluator), Priorities.AUTHORIZATION + 1);

        return true;
    }
}
