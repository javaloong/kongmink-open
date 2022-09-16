package org.javaloong.kongmink.open.core.service.impl;

import org.javaloong.kongmink.open.itest.common.PaxExamTestSupport;
import org.ops4j.pax.exam.Option;

import java.util.Map;

import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

public abstract class AbstractServiceTestSupport extends PaxExamTestSupport {

    @Override
    protected void customizeSettings(Map<String, Boolean> settings) {
        settings.put(USE_JAX_RS_WHITEBOARD, false);
    }

    @Override
    protected Option testBundles() {
        return composite(
                mavenBundle("com.github.ben-manes.caffeine", "caffeine").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-common").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-account-api").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-am-api").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-apim-api").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-data").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-data-jpa").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-core").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-core-service").versionAsInProject(),

                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-itest-common").versionAsInProject()
        );
    }

    @Override
    protected Option junit() {
        return composite(super.junit(), mockito());
    }

    protected Option mockito() {
        return composite(
                mavenBundle("net.bytebuddy", "byte-buddy", "1.11.12"),
                mavenBundle("net.bytebuddy", "byte-buddy-agent", "1.11.12"),
                mavenBundle("org.objenesis", "objenesis", "3.2"),
                mavenBundle("org.mockito", "mockito-core").versionAsInProject()
        );
    }
}
