package org.javaloong.kongmink.open.core.auth.policy.internal.evaluation;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.core.auth.policy.AccessControlPolicies;
import org.javaloong.kongmink.open.core.auth.policy.CommonFields;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.EvaluationContext;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluator;
import org.javaloong.kongmink.open.core.config.ConfigConstants;
import org.javaloong.kongmink.open.core.config.ConfigFactory;
import org.javaloong.kongmink.open.core.config.ConfigProperties;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserApplicationCreationPolicyIT extends AbstractTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UserApplicationCreationPolicyIT.class).getBundleContext();
        context.registerService(ConfigFactory.class, Mockito.mock(ConfigFactory.class), null);
        context.registerService(ApplicationProvider.class, Mockito.mock(ApplicationProvider.class), null);
    }

    @Inject
    ConfigFactory configFactory;
    @Inject
    ApplicationProvider applicationProvider;
    @Inject
    PolicyEvaluator policyEvaluator;

    @Test
    public void testEvaluate() {
        when(configFactory.getConfig(anyString())).thenReturn(createConfig());
        when(applicationProvider.findAll(anyInt(), anyInt())).thenReturn(createPage(0));
        EvaluationContext evaluationContext = new EvaluationContext();
        evaluationContext.setAttribute(CommonFields.SUBJECT, createUser());
        boolean result = policyEvaluator.evaluate(AccessControlPolicies.USER_APPLICATION_CREATION, evaluationContext);
        assertThat(result).isTrue();
        when(applicationProvider.findAll(anyInt(), anyInt())).thenReturn(createPage(1));
        result = policyEvaluator.evaluate(AccessControlPolicies.USER_APPLICATION_CREATION, evaluationContext);
        assertThat(result).isFalse();
    }

    private User createUser() {
        User user = new User();
        user.setId("1");
        return user;
    }

    private ConfigProperties createConfig() {
        Map<String, Object> configMap = Collections.singletonMap(ConfigConstants.USER_APPLICATIONS_LIMIT, 1);
        return new ConfigProperties(configMap);
    }

    private Page<Application> createPage(int totalCount) {
        return new Page<>(null, totalCount);
    }
}
