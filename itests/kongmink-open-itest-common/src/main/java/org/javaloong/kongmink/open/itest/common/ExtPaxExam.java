package org.javaloong.kongmink.open.itest.common;

import org.javaloong.kongmink.open.itest.common.annotation.AfterOsgi;
import org.javaloong.kongmink.open.itest.common.annotation.BeforeOsgi;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.ops4j.pax.exam.junit.PaxExam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

/**
 * Shows how to extend the PaxExam runner to implement hooks that are executed outside of OSGi in the parent
 * junit process
 */
public class ExtPaxExam extends PaxExam {

    private final Optional<Method> before;
    private final Optional<Method> after;

    public ExtPaxExam(Class<?> klass) throws InitializationError {
        super(klass);
        this.before = getStaticMethodWith(klass, BeforeOsgi.class);
        this.after = getStaticMethodWith(klass, AfterOsgi.class);
    }

    @Override
    public void run(RunNotifier notifier) {
        this.before.ifPresent(this::invoke);
        super.run(notifier);
        this.after.ifPresent(this::invoke);
    }

    private Optional<Method> getStaticMethodWith(Class<?> klass, Class<? extends Annotation> annotation) {
        Optional<Method> foundMethod = Arrays.stream(klass.getMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .findFirst();
        if (foundMethod.isPresent()) {
            Method m = foundMethod.get();
            if (!Modifier.isStatic(m.getModifiers())) {
                throw new IllegalStateException("Method " + m.getName() + " must be static to be used as " + annotation.getName());
            }
        }
        return foundMethod;
    }

    private void invoke(Method method) {
        try {
            method.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException("Error calling method " + method, e);
        }
    }
}
