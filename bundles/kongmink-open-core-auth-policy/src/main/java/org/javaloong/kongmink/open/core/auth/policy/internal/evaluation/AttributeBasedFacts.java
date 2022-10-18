package org.javaloong.kongmink.open.core.auth.policy.internal.evaluation;

import org.javaloong.kongmink.open.core.auth.policy.CommonFields;
import org.javaloong.kongmink.open.core.auth.policy.evaluation.EvaluationContext;
import org.jeasy.rules.api.Facts;

public class AttributeBasedFacts {

    private final EvaluationContext evaluationContext;
    private final Facts facts = new Facts();

    public AttributeBasedFacts(EvaluationContext evaluationContext) {
        this.evaluationContext = evaluationContext;
    }

    public Facts getFacts() {
        // Facts to be taken into consideration by RuleCondition.
        if (evaluationContext.getAttributes() != null) {
            evaluationContext.getAttributes().forEach(facts::put);
        }
        facts.put(CommonFields.EVALUATION_CONTEXT, evaluationContext);

        // Facts to be taken into consideration by Actions. The values of these facts will change as Action(s) get executed.
        facts.put(CommonFields.ALLOW, false);
        return facts;
    }

    public boolean hasPermission() {
        return facts.get(CommonFields.ALLOW);
    }
}
