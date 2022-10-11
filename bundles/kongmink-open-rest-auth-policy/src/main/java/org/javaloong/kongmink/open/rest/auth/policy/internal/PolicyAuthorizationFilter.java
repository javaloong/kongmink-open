package org.javaloong.kongmink.open.rest.auth.policy.internal;

import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.logging.NoOpFaultListener;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluator;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class PolicyAuthorizationFilter extends PolicyAuthorizationInInterceptor
        implements ContainerRequestFilter {

    public PolicyAuthorizationFilter(PolicyEvaluator policyEvaluator) {
        super(policyEvaluator);
    }

    @Override
    protected void handlePolicy(Message message, Object resourceInstance, Method method, List<Object> arguments) {
        try {
            super.handlePolicy(message, resourceInstance, method, arguments);
        } catch (Exception ex) {
            message.put(FaultListener.class.getName(), new NoOpFaultListener());
            throw ex;
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        InterceptorChain chain = PhaseInterceptorChain.getCurrentMessage().getInterceptorChain();
        chain.add(this);
    }
}
