package org.javaloong.kongmink.open.rest.auth.internal.provider;

import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;
import org.javaloong.kongmink.open.common.model.user.User;
import org.javaloong.kongmink.open.common.model.user.UserProfile;
import org.javaloong.kongmink.open.common.user.UserConstants;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;
import org.pac4j.oidc.profile.OidcProfile;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Component(service = ContextProvider.class)
@JaxrsExtension
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@Provider
public class UserContextProvider implements ContextProvider<User> {

    @Override
    public User createContext(Message message) {
        SecurityContext context = message.get(SecurityContext.class);
        Pac4jPrincipal principal = (Pac4jPrincipal) context.getUserPrincipal();
        if (principal == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return createUser((OidcProfile) principal.getProfile());
    }

    private User createUser(OidcProfile oidcProfile) {
        User user = new User();
        user.setId(oidcProfile.getId());
        user.setUsername(oidcProfile.getUsername());
        user.setEmail(oidcProfile.getEmail());
        user.setEmailVerified(oidcProfile.getEmailVerified());
        user.setEnabled(true);
        user.setProfile(createUserProfile(oidcProfile.getId(), oidcProfile));
        return user;
    }

    private UserProfile createUserProfile(String userId, OidcProfile oidcProfile) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setCompanyName(oidcProfile.getAttribute(UserConstants.COMPANY_NAME_KEY, String.class));
        userProfile.setCompanyProvince(oidcProfile.getAttribute(UserConstants.COMPANY_PROVINCE_KEY, String.class));
        userProfile.setCompanyCity(oidcProfile.getAttribute(UserConstants.COMPANY_CITY_KEY, String.class));
        userProfile.setCompanyAddress(oidcProfile.getAttribute(UserConstants.COMPANY_ADDRESS_KEY, String.class));
        userProfile.setContactName(oidcProfile.getAttribute(UserConstants.CONTACT_NAME_KEY, String.class));
        userProfile.setContactPhone(oidcProfile.getAttribute(UserConstants.CONTACT_PHONE_KEY, String.class));
        return userProfile;
    }
}
