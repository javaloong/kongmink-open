package org.javaloong.kongmink.open.rest.auth.internal;

import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.javaloong.kongmink.open.common.auth.SecurityContext;
import org.javaloong.kongmink.open.common.auth.SecurityContextProvider;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.rest.auth.internal.mapper.UserMapper;
import org.osgi.service.component.annotations.Component;
import org.pac4j.core.profile.CommonProfile;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Component(service = SecurityContextProvider.class)
public class ShiroSecurityContextProvider implements SecurityContextProvider {

    public static final String ACCESS_TOKEN = "access_token";

    @Override
    public SecurityContext getContext() {
        final PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        if (principals != null) {
            final Pac4jPrincipal principal = principals.oneByType(Pac4jPrincipal.class);
            if (principal != null) {
                CommonProfile profile = principal.getProfile();
                User user = UserMapper.mapToUser(profile);
                String accessToken = (String) profile.getAttribute(ACCESS_TOKEN);
                return createSecurityContext(user, accessToken);
            }
        }
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    private SecurityContext createSecurityContext(User user, String token) {
        SecurityContext securityContext = new SecurityContext();
        securityContext.setUser(user);
        securityContext.setToken(token);
        return securityContext;
    }
}
