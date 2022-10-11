package org.javaloong.kongmink.open.apim.gravitee.internal;

import org.javaloong.kongmink.open.core.auth.UserToken;
import org.osgi.util.converter.Converters;

import java.util.HashMap;
import java.util.Map;

public abstract class GraviteePortalClientTestSupport {

    public static final String SERVER_URL = "http://PORTAL.API.SERVER:8083/portal";

    public GraviteePortalClient createPortalClient() {
        GraviteePortalClient client = new GraviteePortalClient(createPortalClientConfig());
        client.userTokenProvider = this::createUserToken;
        return client;
    }

    public UserToken createUserToken() {
        UserToken userToken = new UserToken();
        userToken.setToken(getToken());
        return userToken;
    }

    protected String getToken() {
        return "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJnZVREbkp5WEJoeHVRTWVkYmxMVnRBbWR2Y3l0akZtRUVwOGRhQ1JGMkJnIn0.eyJleHAiOjE2NDczNDg5MzAsImlhdCI6MTY0NzMxMjkzMSwiYXV0aF90aW1lIjoxNjQ3MzEyOTMwLCJqdGkiOiJlYjVkYjA2YS1jMmE4LTQzZWItOTk5NS0yMThiMGQ0NTcyNmUiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4xMi4xMDA6OTA5MC9hdXRoL3JlYWxtcy9rb25nbWluay1vcGVuIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjMwZjNlZmNjLWMzZmYtNDNmOC05YjlhLWVjM2Q4ZDlmNmJjOSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImdyYXZpdGVlIiwibm9uY2UiOiJhSEJ0TUZOS1NWWkNhalYtTUhSaFNuSkpVMFE0WWpGQlRYbFRRVmxNTUZwc1pUWnZkR2hUUWtrMVdEZHAiLCJzZXNzaW9uX3N0YXRlIjoiMDQwNDAwY2UtYTJiOC00NDdiLThiMGItMWMwZGE4OTExNGE2IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtb3Blbi1hZG1pbiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsInNpZCI6IjA0MDQwMGNlLWEyYjgtNDQ3Yi04YjBiLTFjMGRhODkxMTRhNiIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyMSIsImVtYWlsIjoidXNlcjFAZXhhbXBsZS5jb20ifQ.i8tknz5hy3hn8RWRB-lux_6jHc5qz01EiiqrIiWMZsDlGPMGbWIFgjr-GYiRisMlAcQ_BoqO-TquA1kYWYMTjrQehWJIIcNilJn0_8I8nO_ARsaKQFh_y9C1cDoylczymZPdbmt1WNRPt5K_DLNlL0lX7e6n3yiiifSEwrzICyVeMMgTw9dTM6i7NpOkXiTOyNjMZAWT7eb_FTvq_JdDBbaSuTArP7UxRBW_ZsFbUP-GtZmITXa5hmpOEO74qijy34nL-9ALRROYSyt9Ofwm1GLWQOGWEqICCGfyp6zpZk0Q6sDZTw7Td_kyetwoKuFBgEkEc42OaBTNDxqr8L64Fw";
    }

    private GraviteePortalClientConfiguration createPortalClientConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put("serverUrl", SERVER_URL);
        return Converters.standardConverter().convert(props).to(GraviteePortalClientConfiguration.class);
    }
}
