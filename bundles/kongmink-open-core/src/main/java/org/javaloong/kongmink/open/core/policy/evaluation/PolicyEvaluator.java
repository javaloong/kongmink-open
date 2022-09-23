package org.javaloong.kongmink.open.core.policy.evaluation;

import java.util.Map;

public interface PolicyEvaluator {

    boolean evaluate(String name, Map<String, Object> attributes);

    boolean evaluate(Policy<?> policy, Map<String, Object> attributes);
}
