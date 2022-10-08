package org.javaloong.kongmink.open.core.policy.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Policy {

    String value();

    String message() default "Operation Not Allowed";
}
