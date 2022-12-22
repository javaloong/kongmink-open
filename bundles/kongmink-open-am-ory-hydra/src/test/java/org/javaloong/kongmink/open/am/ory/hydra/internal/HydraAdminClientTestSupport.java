package org.javaloong.kongmink.open.am.ory.hydra.internal;

import org.osgi.util.converter.Converters;

import java.util.HashMap;
import java.util.Map;

public abstract class HydraAdminClientTestSupport {

    public static final String SERVER_URL = "http://hydra:4445";

    public HydraAdminClient createClient() {
        return new HydraAdminClient(createConfig());
    }

    private HydraAdminClient.Configuration createConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put("serverUrl", SERVER_URL);
        return Converters.standardConverter().convert(props).to(HydraAdminClient.Configuration.class);
    }
}
