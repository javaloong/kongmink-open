package org.javaloong.kongmink.open.rest.auth.policy.internal;

import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluator;
import org.javaloong.kongmink.open.core.i18n.TranslationProvider;
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
public class PolicyAuthorizationFeature implements Feature {

    @Reference
    PolicyEvaluator policyEvaluator;
    @Reference
    TranslationProvider i18nProvider;

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new PolicyAuthorizationFilter(policyEvaluator, i18nProvider),
                Priorities.AUTHORIZATION + 1);

        return true;
    }
}
