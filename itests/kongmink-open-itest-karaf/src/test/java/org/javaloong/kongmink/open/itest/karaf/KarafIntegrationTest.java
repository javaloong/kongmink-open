package org.javaloong.kongmink.open.itest.karaf;

import org.apache.karaf.itests.KarafTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import static org.ops4j.pax.exam.CoreOptions.maven;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class KarafIntegrationTest extends KarafTestSupport {

    @Configuration
    public Option[] config() {
        return OptionUtils.combine(super.config(),
                KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.logging.cfg", getConfigFile("/etc/org.ops4j.pax.logging.cfg")),
                KarafDistributionOption.features(
                        maven().groupId("org.javaloong.kongmink.open").artifactId("kongmink-open-karaf-features")
                                .type("xml").classifier("features").versionAsInProject())
        );
    }

    @Test
    public void testKarafFeatures() throws Exception {
        installAndAssertFeature("kongmink-open-resource-server-h2");
    }
}
