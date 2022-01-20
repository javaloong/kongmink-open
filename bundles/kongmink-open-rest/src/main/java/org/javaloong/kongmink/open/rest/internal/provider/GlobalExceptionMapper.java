package org.javaloong.kongmink.open.rest.internal.provider;

import org.javaloong.kongmink.open.rest.AbstractExceptionMapper;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.List;

/**
 * Trap exceptions.
 *
 * @author Xu Cheng
 */
@Component(service = ExceptionMapper.class, immediate = true)
@JaxrsExtension
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@Provider
public class GlobalExceptionMapper extends AbstractExceptionMapper<Exception> {

    @Override
    protected List<Object> createErrors(Exception exception) {
        return Collections.emptyList();
    }

    protected Response.StatusType getStatusType(Exception ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse().getStatusInfo();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}
