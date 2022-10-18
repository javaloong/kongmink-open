package org.javaloong.kongmink.open.core.auth.policy.internal.evaluation;

import org.javaloong.kongmink.open.core.auth.policy.evaluation.EvaluationContext;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.Policy;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluationException;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluator;
import org.jeasy.rules.api.*;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component(service = PolicyEvaluator.class)
public class EasyRulesPolicyEvaluator implements PolicyEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(EasyRulesPolicyEvaluator.class);

    private final Map<String, Policy<?>> policies;

    private final RulesEngine rulesEngine;

    public EasyRulesPolicyEvaluator() {
        this(new ConcurrentHashMap<>());
    }

    public EasyRulesPolicyEvaluator(Map<String, Policy<?>> policies) {
        this.rulesEngine = createRulesEngine();
        this.policies = policies;
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    void addPolicy(Policy<?> policy) {
        logger.info("Add policy: " + policy.getName());
        policies.put(policy.getName(), policy);
    }

    void removePolicy(Policy<?> policy) {
        logger.info("Remove policy: " + policy.getName());
        policies.remove(policy.getName());
    }

    @Override
    public boolean evaluate(String name, EvaluationContext evaluationContext) {
        Objects.requireNonNull(name, "Name must not be null");
        if (!policies.containsKey(name)) {
            throw new PolicyEvaluationException("Unable to find policy: " + name);
        }
        Policy<?> policy = policies.get(name);
        return evaluate(policy, evaluationContext);
    }

    @Override
    public boolean evaluate(Policy<?> policy, EvaluationContext evaluationContext) {
        Objects.requireNonNull(policy, "Policy must not be null");
        Objects.requireNonNull(evaluationContext, "EvaluationContext must not be null");
        Rules rules = new Rules();
        policy.getRules().forEach(rules::register);
        AttributeBasedFacts facts = new AttributeBasedFacts(evaluationContext);
        rulesEngine.fire(rules, facts.getFacts());
        return facts.hasPermission();
    }

    private RulesEngine createRulesEngine() {
        DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.registerRuleListener(new PolicyRuleListener());
        return rulesEngine;
    }

    static class PolicyRuleListener implements RuleListener {

        @Override
        public void onEvaluationError(Rule rule, Facts facts, Exception exception) {
            throw new PolicyEvaluationException(exception);
        }
    }
}
