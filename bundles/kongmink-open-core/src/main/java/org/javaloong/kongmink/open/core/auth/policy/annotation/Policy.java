package org.javaloong.kongmink.open.core.auth.policy.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Policy {

    String value();

    String message() default "{error.notAuthorized}";
}
