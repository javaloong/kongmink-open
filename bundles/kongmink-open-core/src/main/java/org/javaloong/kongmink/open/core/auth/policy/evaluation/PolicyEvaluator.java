package org.javaloong.kongmink.open.core.auth.policy.evaluation;

public interface PolicyEvaluator {

    boolean evaluate(String name, EvaluationContext evaluationContext);

    boolean evaluate(Policy<?> policy, EvaluationContext evaluationContext);
}
