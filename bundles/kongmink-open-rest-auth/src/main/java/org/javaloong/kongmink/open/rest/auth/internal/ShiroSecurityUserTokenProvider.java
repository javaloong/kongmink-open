package org.javaloong.kongmink.open.rest.auth.internal;

import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.core.auth.UserToken;
import org.javaloong.kongmink.open.core.auth.UserTokenProvider;
import org.javaloong.kongmink.open.rest.auth.internal.mapper.UserMapper;
import org.osgi.service.component.annotations.Component;
import org.pac4j.core.profile.CommonProfile;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Component(service = UserTokenProvider.class)
public class ShiroSecurityUserTokenProvider implements UserTokenProvider {

    public static final String ACCESS_TOKEN = "access_token";

    @Override
    public UserToken getUserToken() {
        final PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        if (principals != null) {
            final Pac4jPrincipal principal = principals.oneByType(Pac4jPrincipal.class);
            if (principal != null) {
                CommonProfile profile = principal.getProfile();
                User user = UserMapper.mapToUser(profile);
                String accessToken = (String) profile.getAttribute(ACCESS_TOKEN);
                return createUserToken(user, accessToken);
            }
        }
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    private UserToken createUserToken(User user, String token) {
        UserToken userToken = new UserToken();
        userToken.setUser(user);
        userToken.setToken(token);
        return userToken;
    }
}
