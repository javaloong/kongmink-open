package org.javaloong.kongmink.open.apim;

import org.javaloong.kongmink.open.apim.model.ApiUser;
import org.javaloong.kongmink.open.common.user.User;

public interface ApiUserProvider {

    ApiUser connectUser(User user);
}
