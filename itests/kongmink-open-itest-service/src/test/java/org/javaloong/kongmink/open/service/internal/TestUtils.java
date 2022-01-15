package org.javaloong.kongmink.open.service.internal;

import org.javaloong.kongmink.open.common.model.client.Client;
import org.javaloong.kongmink.open.common.model.user.User;

import java.time.LocalDateTime;

public abstract class TestUtils {

    public static User createUser(String id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setCreatedTimestamp(LocalDateTime.now());
        return user;
    }

    public static Client createClient(String id, String name) {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        return client;
    }
}
