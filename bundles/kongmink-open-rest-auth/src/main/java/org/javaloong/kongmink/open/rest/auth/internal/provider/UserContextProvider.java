package org.javaloong.kongmink.open.rest.auth.internal.provider;

import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.rest.auth.internal.mapper.UserMapper;
import org.javaloong.kongmink.open.service.UserService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Component(service = ContextProvider.class)
@JaxrsExtension
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@Provider
public class UserContextProvider implements ContextProvider<User> {

    @Reference
    UserService userService;

    @Override
    public User createContext(Message message) {
        SecurityContext context = message.get(SecurityContext.class);
        Pac4jPrincipal principal = (Pac4jPrincipal) context.getUserPrincipal();
        if (principal == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        User user = UserMapper.mapToUser(principal.getProfile());
        return userService.loadByUser(user);
    }
}
