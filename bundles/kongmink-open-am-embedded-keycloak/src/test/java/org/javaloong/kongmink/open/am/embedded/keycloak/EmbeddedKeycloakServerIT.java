package org.javaloong.kongmink.open.am.embedded.keycloak;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.jupiter.JettyServerExtension;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(JettyServerExtension.class)
public class EmbeddedKeycloakServerIT {

    @TestServerConfiguration
    private static EmbeddedJettyConfiguration configuration = createJettyConfiguration();

    @Test
    public void testEmbeddedServer(EmbeddedJetty jetty) throws Exception {
        String url = jetty.getUrl() + "auth";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(200);
    }

    private static EmbeddedJettyConfiguration createJettyConfiguration() {
        return EmbeddedJettyConfiguration.builder()
                .withOverrideDescriptor("src/test/resources/web.xml")
                .build();
    }
}
