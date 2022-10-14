package org.javaloong.kongmink.open.rest.auth.policy.internal;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.core.auth.policy.CommonFields;
import org.javaloong.kongmink.open.core.auth.policy.annotation.Fact;
import org.javaloong.kongmink.open.core.auth.policy.annotation.Policy;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluator;
import org.javaloong.kongmink.open.core.i18n.TranslationProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import javax.ws.rs.ForbiddenException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PolicyAuthorizationInInterceptor extends AbstractPolicyAuthorizationInterceptor {

    private final PolicyEvaluator policyEvaluator;
    private final TranslationProvider i18nProvider;

    private Locale locale;

    public PolicyAuthorizationInInterceptor(PolicyEvaluator policyEvaluator, TranslationProvider i18nProvider) {
        super(Phase.PRE_INVOKE);
        this.policyEvaluator = policyEvaluator;
        this.i18nProvider = i18nProvider;
    }

    @Override
    protected void handlePolicy(Message message, Object resourceInstance, Method method, List<Object> arguments) {
        Policy methodPolicySpec = method.getAnnotation(Policy.class);
        if (methodPolicySpec == null) return;

        boolean hasPermission = policyEvaluator.evaluate(methodPolicySpec.value(),
                createAttributes(method, arguments));
        if (!hasPermission) {
            throw new ForbiddenException(getLocalizedMessage(resourceInstance.getClass(),
                    methodPolicySpec.message(), locale, arguments.toArray()));
        }
    }

    private Map<String, Object> createAttributes(Method method, List<Object> arguments) {
        Map<String, Object> attributes = new HashMap<>(arguments.size());
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Object argument = arguments.get(i);
            Fact factAnn = parameters[i].getAnnotation(Fact.class);
            if (factAnn != null) {
                attributes.put(factAnn.value(), argument);
            } else {
                if (argument instanceof User) {
                    attributes.put(CommonFields.SUBJECT, argument);
                }
            }
        }
        return attributes;
    }

    private String getLocalizedMessage(Class<?> resourceClass, String message, Locale locale, Object... arguments) {
        if (message.indexOf('{') < 0) {
            return message;
        }
        String key = removeCurlyBraces(message);
        Bundle bundle = FrameworkUtil.getBundle(resourceClass);
        String localizedMessage = i18nProvider.getText(bundle, key, null, locale, arguments);
        if (localizedMessage == null) {
            bundle = FrameworkUtil.getBundle(PolicyEvaluator.class);
            localizedMessage = i18nProvider.getText(bundle, key, key, locale, arguments);
        }
        return localizedMessage;
    }

    private String removeCurlyBraces(String parameter) {
        return parameter.substring(1, parameter.length() - 1);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
