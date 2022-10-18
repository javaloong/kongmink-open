package org.javaloong.kongmink.open.core.auth.policy.evaluation;

import java.util.*;

public class EvaluationContext {

    private final List<Object> messageParameters = new ArrayList<>();

    private final Map<String, Object> attributes = new HashMap<>();

    public EvaluationContext() {
    }

    public EvaluationContext(Map<String, Object> attributes) {
        this.attributes.putAll(attributes);
    }

    public EvaluationContext addMessageParameter(Object value) {
        messageParameters.add(value);
        return this;
    }

    public Object[] getMessageParameters() {
        return messageParameters.toArray();
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
