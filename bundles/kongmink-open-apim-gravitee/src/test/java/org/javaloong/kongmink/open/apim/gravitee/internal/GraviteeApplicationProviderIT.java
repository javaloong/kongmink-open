package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.apim.ApplicationProvider;
import org.javaloong.kongmink.open.apim.model.Application;
import org.javaloong.kongmink.open.apim.model.analytics.HistogramAnalytics;
import org.javaloong.kongmink.open.apim.model.analytics.HitsAnalytics;
import org.javaloong.kongmink.open.apim.model.analytics.StatsAnalytics;
import org.javaloong.kongmink.open.apim.model.analytics.TopHitsAnalytics;
import org.javaloong.kongmink.open.apim.model.analytics.query.AnalyticsQuery;
import org.javaloong.kongmink.open.apim.model.analytics.query.AnalyticsType;
import org.javaloong.kongmink.open.apim.model.application.ApplicationSettings;
import org.javaloong.kongmink.open.apim.model.application.OAuthClientSettings;
import org.javaloong.kongmink.open.apim.model.application.SimpleApplicationSettings;
import org.javaloong.kongmink.open.common.application.ApplicationType;
import org.javaloong.kongmink.open.common.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.ForbiddenException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled
public class GraviteeApplicationProviderIT extends GraviteePortalClientTestSupport {

    private ApplicationProvider applicationProvider;

    @BeforeEach
    public void setUp() {
        applicationProvider = new GraviteeApplicationProvider(createPortalClient());
    }

    @Test
    public void getHistogramAnalytics() {
        String applicationId = "e0e75fa9-3fd7-4584-a75f-a93fd7558400";
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        analyticsQuery.setType(AnalyticsType.DATE_HISTO);
        analyticsQuery.setFrom(new Date().getTime());
        analyticsQuery.setTo(analyticsQuery.getFrom() + 30000);
        analyticsQuery.setInterval(1000);
        analyticsQuery.setAggregations("avg:response-time;avg:api-response-time");
        HistogramAnalytics analytics = applicationProvider.getAnalytics(applicationId,
                analyticsQuery, HistogramAnalytics.class);
        assertThat(analytics).isNotNull();
    }

    @Test
    public void getTopHitsAnalytics() {
        String applicationId = "e0e75fa9-3fd7-4584-a75f-a93fd7558400";
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        analyticsQuery.setType(AnalyticsType.GROUP_BY);
        analyticsQuery.setFrom(new Date().getTime());
        analyticsQuery.setTo(analyticsQuery.getFrom() + 30000);
        analyticsQuery.setInterval(1000);
        analyticsQuery.setField("api");
        analyticsQuery.setRanges("100:199;200:299;300:399;400:499;500:599");
        analyticsQuery.setOrder("order:-response-time");
        TopHitsAnalytics analytics = applicationProvider.getAnalytics(applicationId,
                analyticsQuery, TopHitsAnalytics.class);
        assertThat(analytics).isNotNull();
    }

    @Test
    public void getHitsAnalytics() {
        String applicationId = "e0e75fa9-3fd7-4584-a75f-a93fd7558400";
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        analyticsQuery.setType(AnalyticsType.COUNT);
        analyticsQuery.setFrom(new Date().getTime());
        analyticsQuery.setTo(analyticsQuery.getFrom() + 30000);
        analyticsQuery.setInterval(1000);
        analyticsQuery.setQuery("api:e0e75fa9-3fd7-4584-a75f-a93fd7558400");
        HitsAnalytics analytics = applicationProvider.getAnalytics(applicationId,
                analyticsQuery, HitsAnalytics.class);
        assertThat(analytics).isNotNull();
    }

    @Test
    public void getStatsAnalytics() {
        String applicationId = "e0e75fa9-3fd7-4584-a75f-a93fd7558400";
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        analyticsQuery.setType(AnalyticsType.STATS);
        analyticsQuery.setFrom(new Date().getTime());
        analyticsQuery.setTo(analyticsQuery.getFrom() + 30000);
        analyticsQuery.setInterval(1000);
        analyticsQuery.setField("api");
        StatsAnalytics analytics = applicationProvider.getAnalytics(applicationId,
                analyticsQuery, StatsAnalytics.class);
        assertThat(analytics).isNotNull();
    }

    @Test
    public void findById() {
        String id = "e0e75fa9-3fd7-4584-a75f-a93fd7558400";
        Optional<Application> result = applicationProvider.findById(id);
        assertThat(result).hasValueSatisfying(app -> {
            assertThat(app)
                    .returns("Default application", Application::getName);
        });
    }

    @Test
    public void createSimpleApplication() {
        Application application = new Application();
        application.setName("test");
        application.setDescription("test description");
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
    }

    @Test
    public void createOAuthApplication() {
        Application application = new Application();
        application.setName("test oauth");
        application.setDescription("test oauth description");
        application.setApplicationType(ApplicationType.WEB);
        ApplicationSettings settings = new ApplicationSettings();
        OAuthClientSettings oauthSettings = new OAuthClientSettings();
        oauthSettings.setApplicationType(ApplicationType.WEB.toString());
        oauthSettings.setGrantTypes(Collections.singletonList("authorization_code"));
        oauthSettings.setRedirectUris(Collections.singletonList("http://localhost/redirect"));
        settings.setOauth(oauthSettings);
        application.setSettings(settings);
        Application newApplication = applicationProvider.create(application);
        assertThat(newApplication)
                .returns(ApplicationType.WEB, Application::getApplicationType)
                .returns(Collections.singletonList("authorization_code"),
                        app -> app.getSettings().getOauth().getGrantTypes())
                .returns(Collections.singletonList("http://localhost/redirect"),
                        app -> app.getSettings().getOauth().getRedirectUris())
                .extracting(Application::getId).isNotNull();
    }

    @Test
    public void updateSimpleApplication() {
        Application application = new Application();
        application.setId("c5e00d43-ba8a-4422-a00d-43ba8a64221b");
        application.setName("test2");
        ApplicationSettings settings = new ApplicationSettings();
        SimpleApplicationSettings simpleSettings = new SimpleApplicationSettings();
        simpleSettings.setType("WEB");
        settings.setApp(simpleSettings);
        application.setSettings(settings);
        applicationProvider.update(application);
        Optional<Application> result = applicationProvider.findById(application.getId());
        assertThat(result).hasValueSatisfying(value -> {
            assertThat(value)
                    .returns("test2", Application::getName)
                    .returns("WEB", app -> app.getSettings().getApp().getType());
        });
    }

    @Test
    public void updateOAuthApplication() {
        Application application = new Application();
        application.setId("7ed439cd-3f6a-4161-9439-cd3f6a1161b8");
        application.setName("test oauth2");
        application.setDescription("test oauth description");
        application.setApplicationType(ApplicationType.WEB);
        ApplicationSettings settings = new ApplicationSettings();
        OAuthClientSettings oauthSettings = new OAuthClientSettings();
        oauthSettings.setApplicationType(ApplicationType.WEB.toString());
        oauthSettings.setGrantTypes(Arrays.asList("authorization_code", "implicit"));
        oauthSettings.setRedirectUris(Collections.singletonList("http://localhost/redirect"));
        settings.setOauth(oauthSettings);
        application.setSettings(settings);
        applicationProvider.update(application);
        Optional<Application> result = applicationProvider.findById(application.getId());
        assertThat(result).hasValueSatisfying(value -> {
            assertThat(value)
                    .returns("test oauth2", Application::getName)
                    .extracting(app -> app.getSettings().getOauth())
                    .returns(oauthSettings.getGrantTypes(), OAuthClientSettings::getGrantTypes);
        });
    }

    @Test
    public void delete() {
        String id = "c5e00d43-ba8a-4422-a00d-43ba8a64221b";
        applicationProvider.delete(id);
        assertThatThrownBy(() -> applicationProvider.findById(id))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void findAll() {
        Page<Application> result = applicationProvider.findAll(1, 10);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getData()).isNotEmpty().hasSize(1)
                .extracting(Application::getName).contains("Default Application");
    }
}
