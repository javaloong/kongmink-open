package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.common.model.ApiKey;
import org.javaloong.kongmink.open.common.model.Application;
import org.javaloong.kongmink.open.common.model.ApplicationLog;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.analytics.HistogramAnalytics;
import org.javaloong.kongmink.open.common.model.analytics.HitsAnalytics;
import org.javaloong.kongmink.open.common.model.analytics.StatsAnalytics;
import org.javaloong.kongmink.open.common.model.analytics.TopHitsAnalytics;
import org.javaloong.kongmink.open.common.model.analytics.query.AnalyticsQuery;
import org.javaloong.kongmink.open.common.model.analytics.query.AnalyticsType;
import org.javaloong.kongmink.open.common.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.common.model.application.ApplicationType;
import org.javaloong.kongmink.open.common.model.application.OAuthClientSettings;
import org.javaloong.kongmink.open.common.model.application.SimpleApplicationSettings;
import org.javaloong.kongmink.open.common.model.log.query.LogQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.ws.rs.ForbiddenException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("gravitee")
public class GraviteeApplicationProviderIT extends GraviteePortalClientTestSupport {

    private ApplicationProvider applicationProvider;

    @BeforeEach
    public void setUp() {
        applicationProvider = new GraviteeApplicationProvider(createPortalClient());
    }

    @Test
    public void testSimpleApplicationCRUD() {
        // create application
        Application application = new Application();
        application.setName("test simple");
        application.setDescription("test simple application");
        application.setApplicationType(ApplicationType.SIMPLE);
        ApplicationSettings settings = new ApplicationSettings();
        SimpleApplicationSettings simpleSettings = new SimpleApplicationSettings();
        simpleSettings.setClientId("test-resource-server");
        simpleSettings.setType("SERVICE");
        settings.setApp(simpleSettings);
        application.setSettings(settings);
        Application newApplication = applicationProvider.create(application);
        assertThat(newApplication)
                .returns("test-resource-server", app -> app.getSettings().getApp().getClientId())
                .extracting(Application::getId).isNotNull();
        // update application
        newApplication.setDescription("test simple application2");
        simpleSettings.setType("WEB");
        settings.setApp(simpleSettings);
        newApplication.setSettings(settings);
        applicationProvider.update(newApplication);
        Optional<Application> result = applicationProvider.findById(newApplication.getId());
        assertThat(result).hasValueSatisfying(value -> {
            assertThat(value)
                    .returns("test simple application2", Application::getDescription)
                    .returns("WEB", app -> app.getSettings().getApp().getType());
        });
        // delete application
        applicationProvider.delete(newApplication.getId());
        assertThatThrownBy(() -> applicationProvider.findById(newApplication.getId()))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void testOAuthApplicationCRUD() {
        // create application
        Application application = new Application();
        application.setName("test oauth");
        application.setDescription("test oauth application");
        application.setApplicationType(ApplicationType.BROWSER);
        ApplicationSettings settings = new ApplicationSettings();
        OAuthClientSettings oauthSettings = new OAuthClientSettings();
        oauthSettings.setApplicationType(ApplicationType.BROWSER.toString());
        oauthSettings.setGrantTypes(Collections.singletonList("authorization_code"));
        oauthSettings.setRedirectUris(Collections.singletonList("http://localhost/redirect"));
        settings.setOauth(oauthSettings);
        application.setSettings(settings);
        Application newApplication = applicationProvider.create(application);
        assertThat(newApplication)
                .returns(ApplicationType.BROWSER, Application::getApplicationType)
                .returns(Collections.singletonList("authorization_code"),
                        app -> app.getSettings().getOauth().getGrantTypes())
                .returns(Collections.singletonList("http://localhost/redirect"),
                        app -> app.getSettings().getOauth().getRedirectUris())
                .extracting(Application::getId).isNotNull();
        // update application
        newApplication.setDescription("test oauth application2");
        oauthSettings.setGrantTypes(Arrays.asList("authorization_code", "implicit"));
        oauthSettings.setRedirectUris(Collections.singletonList("http://localhost/redirect"));
        settings.setOauth(oauthSettings);
        newApplication.setSettings(settings);
        applicationProvider.update(newApplication);
        Optional<Application> result = applicationProvider.findById(newApplication.getId());
        assertThat(result).hasValueSatisfying(value -> {
            assertThat(value)
                    .returns("test oauth application2", Application::getDescription)
                    .extracting(app -> app.getSettings().getOauth())
                    .returns(oauthSettings.getGrantTypes(), OAuthClientSettings::getGrantTypes);
        });
        // delete application
        applicationProvider.delete(newApplication.getId());
        assertThatThrownBy(() -> applicationProvider.findById(newApplication.getId()))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void findAll() {
        Page<Application> result = applicationProvider.findAll(1, 10);
        assertThat(result.getTotalCount()).isGreaterThan(1);
        assertThat(result.getData()).isNotEmpty()
                .extracting(Application::getName).contains("Default application");
    }

    @Test
    public void getLog() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
        String logId = "57ac6be3-efdd-4bbe-ac6b-e3efddfbbeb9";
        Long timestamp = 1671089430523L;
        ApplicationLog applicationLog = applicationProvider.getLog(applicationId, logId, timestamp);
        assertThat(applicationLog).isNotNull()
                .extracting(ApplicationLog::getApi).isEqualTo("ebbb4e21-9bbc-400d-bb4e-219bbc400d97");
    }

    @Test
    public void getLogs() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
        LogQuery logQuery = new LogQuery();
        logQuery.setFrom(1671033600000L);
        logQuery.setTo(1671119999999L);
        logQuery.setQuery("api:ebbb4e21-9bbc-400d-bb4e-219bbc400d97");
        logQuery.setOrder("DESC");
        Page<ApplicationLog> result = applicationProvider.getLogs(applicationId, logQuery, 1, 10);
        assertThat(result.getData()).isNotEmpty()
                .extracting(ApplicationLog::getApi).contains("ebbb4e21-9bbc-400d-bb4e-219bbc400d97");
    }

    @Test
    public void getHistogramAnalytics() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        analyticsQuery.setType(AnalyticsType.DATE_HISTO);
        analyticsQuery.setFrom(1650384000000L);
        analyticsQuery.setTo(1650470399999L);
        analyticsQuery.setInterval(600000);
        analyticsQuery.setAggregations("field:status");
        HistogramAnalytics analytics = applicationProvider.getAnalytics(applicationId,
                analyticsQuery, HistogramAnalytics.class);
        assertThat(analytics).isNotNull();
    }

    @Test
    public void getTopHitsAnalytics() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        analyticsQuery.setType(AnalyticsType.GROUP_BY);
        analyticsQuery.setFrom(1650384000000L);
        analyticsQuery.setTo(1650470399999L);
        analyticsQuery.setInterval(600000);
        analyticsQuery.setField("status");
        analyticsQuery.setRanges("100:199;200:299;300:399;400:499;500:599");
        TopHitsAnalytics analytics = applicationProvider.getAnalytics(applicationId,
                analyticsQuery, TopHitsAnalytics.class);
        assertThat(analytics).isNotNull();
    }

    @Test
    public void getHitsAnalytics() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        analyticsQuery.setType(AnalyticsType.COUNT);
        analyticsQuery.setFrom(1650384000000L);
        analyticsQuery.setTo(1650470399999L);
        analyticsQuery.setInterval(600000);
//        analyticsQuery.setQuery("api:e0e75fa9-3fd7-4584-a75f-a93fd7558400");
        HitsAnalytics analytics = applicationProvider.getAnalytics(applicationId,
                analyticsQuery, HitsAnalytics.class);
        assertThat(analytics).isNotNull();
    }

    @Test
    public void getStatsAnalytics() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        analyticsQuery.setType(AnalyticsType.STATS);
        analyticsQuery.setFrom(1650384000000L);
        analyticsQuery.setTo(1650470399999L);
        analyticsQuery.setInterval(600000);
        analyticsQuery.setField("api");
        StatsAnalytics analytics = applicationProvider.getAnalytics(applicationId,
                analyticsQuery, StatsAnalytics.class);
        assertThat(analytics).isNotNull();
    }

    @Test
    public void renewSharedKey() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
        ApiKey apiKey = applicationProvider.renewSharedKey(applicationId);
        assertThat(apiKey).isNotNull();
    }

    @Test
    public void revokeKey() {
        String applicationId = "bfe2c4c2-dfc7-43c3-a2c4-c2dfc703c365";
        String apiKey = "c9d063f4-bfc1-4ea7-9063-f4bfc19ea750";
        applicationProvider.revokeKey(applicationId, apiKey);
    }
}
