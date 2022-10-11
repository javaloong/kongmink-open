package org.javaloong.kongmink.open.core.auth.policy.evaluation;

import java.util.List;

public interface Policy<T> {

    String getName();

    List<T> getRules();
}
