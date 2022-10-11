package org.javaloong.kongmink.open.core.auth.policy.evaluation;

public class PolicyEvaluationException extends RuntimeException {

    public PolicyEvaluationException(String message) {
        super(message);
    }

    public PolicyEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PolicyEvaluationException(Throwable cause) {
        super(cause);
    }
}
