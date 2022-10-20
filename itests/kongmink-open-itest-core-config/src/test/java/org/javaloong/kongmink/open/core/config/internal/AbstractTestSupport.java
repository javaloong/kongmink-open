package org.javaloong.kongmink.open.core.config.internal;

import org.javaloong.kongmink.open.itest.common.PaxExamTestSupport;
import org.ops4j.pax.exam.Option;

import java.util.Map;

import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

public abstract class AbstractTestSupport extends PaxExamTestSupport {

    @Override
    protected void customizeSettings(Map<String, Boolean> settings) {
        settings.put(USE_JDBC, false);
        settings.put(USE_JPA, false);
        settings.put(USE_JPA_PROVIDER, false);
        settings.put(USE_TX_CONTROL, false);
        settings.put(USE_JAX_RS_WHITEBOARD, false);
        settings.put(USE_CONFIGURATOR, true);
    }

    @Override
    protected Option testBundles() {
        return composite(jackson(), xaTxControlService(),
                mavenBundle("org.hibernate.javax.persistence", "hibernate-jpa-2.1-api").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-common").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-data").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-core").versionAsInProject(),
                mavenBundle("org.javaloong.kongmink.open", "kongmink-open-core-config").versionAsInProject(),

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
