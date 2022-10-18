package org.javaloong.kongmink.open.core.auth.policy.internal.evaluation;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.core.auth.policy.AccessControlPolicies;
import org.javaloong.kongmink.open.core.auth.policy.CommonFields;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.EvaluationContext;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.Policy;
import org.javaloong.kongmink.open.core.config.ConfigConstants;
import org.javaloong.kongmink.open.core.config.ConfigManager;
import org.javaloong.kongmink.open.core.config.ConfigProperties;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.RuleBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;

@Component(service = Policy.class)
public class UserApplicationCreationPolicy implements Policy<Rule> {

    @Reference
    ConfigManager configManager;
    @Reference
    ApplicationProvider applicationProvider;

    @Override
    public String getName() {
        return AccessControlPolicies.USER_APPLICATION_CREATION;
    }

    @Override
    public List<Rule> getRules() {
        return Collections.singletonList(userCreateApplicationRule());
    }

    private Rule userCreateApplicationRule() {
        return new RuleBuilder()
                .name("user create application rule")
                .when(facts -> {
                    User user = facts.get(CommonFields.SUBJECT);
                    long userApplicationsCount = getUserApplicationsCount();
                    int userApplicationsLimit = getUserApplicationsLimit(user.getId());
                    getEvaluationContext(facts)
                            .addMessageParameter(userApplicationsLimit)
                            .addMessageParameter(userApplicationsCount);
                    return userApplicationsCount < userApplicationsLimit;
                })
                .then(facts -> facts.put(CommonFields.ALLOW, true))
                .build();
    }

    private EvaluationContext getEvaluationContext(Facts facts) {
        return facts.get(CommonFields.EVALUATION_CONTEXT);
    }

    private int getUserApplicationsLimit(String userId) {
        ConfigProperties configProperties = configManager.getConfig(userId);
        return configProperties.get(ConfigConstants.USER_APPLICATIONS_LIMIT, Integer.class);
    }

    private long getUserApplicationsCount() {
        Page<Application> result = applicationProvider.findAll(1, 1);
        return result.getTotalCount();
    }
}
