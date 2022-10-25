package org.javaloong.kongmink.open.service;

import org.javaloong.kongmink.open.common.model.User;

public interface UserDetailsService {

    User loadByUser(User user);
}
