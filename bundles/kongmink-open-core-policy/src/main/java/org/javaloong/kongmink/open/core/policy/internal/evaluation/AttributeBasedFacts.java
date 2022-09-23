package org.javaloong.kongmink.open.core.policy.internal.evaluation;

import org.javaloong.kongmink.open.core.policy.CommonFields;
import org.jeasy.rules.api.Facts;

import java.util.Map;

public class AttributeBasedFacts {

    private final Facts facts = new Facts();

    public AttributeBasedFacts(Map<String, Object> attributes) {
        if (attributes != null) {
            attributes.forEach(facts::put);
        }

        facts.put(CommonFields.ALLOW, false);
    }

    public Facts getFacts() {
        return facts;
    }

    public boolean hasPermission() {
        return facts.get(CommonFields.ALLOW);
    }
}
