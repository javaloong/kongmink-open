package org.javaloong.kongmink.open.rest.auth.policy.internal;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.javaloong.kongmink.open.core.auth.policy.annotation.Fact;
import org.javaloong.kongmink.open.core.auth.policy.annotation.Policy;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluator;

import javax.ws.rs.ForbiddenException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyAuthorizationInInterceptor extends AbstractPolicyAuthorizationInterceptor {

    private final PolicyEvaluator policyEvaluator;

    public PolicyAuthorizationInInterceptor(PolicyEvaluator policyEvaluator) {
        super(Phase.PRE_INVOKE);
        this.policyEvaluator = policyEvaluator;
    }

    @Override
    protected void handlePolicy(Message message, Object resourceInstance, Method method, List<Object> arguments) {
        Policy methodPolicySpec = method.getAnnotation(Policy.class);
        if (methodPolicySpec == null) return;

        boolean hasPermission = policyEvaluator.evaluate(methodPolicySpec.value(),
                createAttributes(method, arguments));
        if (!hasPermission) {
            throw new ForbiddenException(methodPolicySpec.message());
        }
    }

    private Map<String, Object> createAttributes(Method method, List<Object> arguments) {
        Map<String, Object> attributes = new HashMap<>(arguments.size());
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Fact factAnn = parameters[i].getAnnotation(Fact.class);
            if (factAnn != null) {
                attributes.put(factAnn.value(), arguments.get(i));
            }
        }
        return attributes;
    }
}
