package org.javaloong.kongmink.open.core.auth.policy.internal.evaluation;

import org.javaloong.kongmink.open.core.auth.policy.CommonFields;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.EvaluationContext;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.Policy;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluationException;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.PolicyEvaluator;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.RuleBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EasyRulesPolicyEvaluatorTest {

    private PolicyEvaluator policyEvaluator;

    @BeforeEach
    public void setUp() {
        Map<String, Policy<?>> policyMap = Collections.singletonMap("TEST", new TestPolicy());
        policyEvaluator = new EasyRulesPolicyEvaluator(policyMap);
    }

    @Test
    public void testEvaluateWhenPolicyDoesNotExist() {
        assertThatThrownBy(() -> policyEvaluator.evaluate("TEST2", new EvaluationContext()))
                .isInstanceOf(PolicyEvaluationException.class)
                .hasMessage("Unable to find policy: TEST2");
    }

    @Test
    public void testEvaluate() {
        assertThat(policyEvaluator.evaluate("TEST", new EvaluationContext())).isTrue();
    }

    static class TestPolicy implements Policy<Rule> {

        @Override
        public String getName() {
            return "test";
        }

        @Override
        public List<Rule> getRules() {
            return Collections.singletonList(testRule());
        }

        private Rule testRule() {
            return new RuleBuilder()
                    .name("test rule")
                    .when(facts -> true)
                    .then(facts -> facts.put(CommonFields.ALLOW, true))
                    .build();
        }
    }
}
