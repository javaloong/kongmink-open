package org.javaloong.kongmink.open.core.policy.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Fact {

    String value();
}
