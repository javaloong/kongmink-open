package org.javaloong.kongmink.open.apim;

import org.javaloong.kongmink.open.common.model.ApiUser;
import org.javaloong.kongmink.open.common.model.User;

public interface ApiUserProvider {

    ApiUser connectUser(User user);
}
