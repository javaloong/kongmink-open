package org.javaloong.kongmink.open.rest.auth.internal.provider;

import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;
import org.javaloong.kongmink.open.common.model.user.User;
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
        Pac4jPrincipal principal = (Pac4jPrincipal)context.getUserPrincipal();
        if(principal == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return createUser((OidcProfile)principal.getProfile());
    }

    private User createUser(OidcProfile profile) {
        User user = new User();
        user.setId(profile.getId());
        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        user.setEmailVerified(profile.getEmailVerified());
        return user;
    }
}
