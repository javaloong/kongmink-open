package org.javaloong.kongmink.open.service.core.internal;

import org.javaloong.kongmink.open.apim.ApiProvider;
import org.javaloong.kongmink.open.apim.model.Api;
import org.javaloong.kongmink.open.apim.model.ApiMetrics;
import org.javaloong.kongmink.open.apim.model.ApiPage;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.service.ApiService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApiServiceImplIT extends AbstractServiceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(ApiServiceImplIT.class).getBundleContext();
        context.registerService(ApiProvider.class, Mockito.mock(ApiProvider.class), null);
    }

    @Inject
    ApiProvider apiProvider;
    @Inject
    ApiService apiService;

    @Test
    public void testCRUD() {
        when(apiProvider.getCategories()).thenReturn(
                Collections.singletonList(TestUtils.createCategory("1", "Official Apis")));
        apiService.getCategories();
        verify(apiProvider).getCategories();
        when(apiProvider.getMetrics(anyString())).thenReturn(new ApiMetrics());
        apiService.getMetrics("1");
        verify(apiProvider).getMetrics("1");
        ApiPage apiPage = TestUtils.createApiPage("1", "page1", "content1", ApiPage.Type.MARKDOWN);
        when(apiProvider.getPage(anyString(), anyString(), anyString())).thenReturn(apiPage);
        apiService.getPage("1", "1", "en");
        verify(apiProvider).getPage("1", "1", "en");
        when(apiProvider.getPages(anyString(), anyString(), nullable(String.class), anyInt(), anyInt())).thenReturn(new Page<>());
        apiService.getPages("1", "en", null, 1, 10);
        verify(apiProvider).getPages("1", "en", null, 1, 10);
        when(apiProvider.getPlans(anyString(), anyInt(), anyInt())).thenReturn(new Page<>());
        apiService.getPlans("1", 1, 10);
        verify(apiProvider).getPlans("1", 1, 10);
        Api api = TestUtils.createApi("1", "api1", "1.0.0", "Official Apis");
        when(apiProvider.findById(anyString())).thenReturn(Optional.of(api));
        apiService.findById("1");
        verify(apiProvider).findById("1");
        when(apiProvider.findAll(nullable(String.class), anyInt(), anyInt())).thenReturn(new Page<>());
        apiService.findAll(null, 1, 10);
        verify(apiProvider).findAll(null, 1, 10);
        when(apiProvider.search(nullable(String.class), anyInt(), anyInt())).thenReturn(new Page<>());
        apiService.search(null, 1, 10);
        verify(apiProvider).search(null, 1, 10);
    }
}
