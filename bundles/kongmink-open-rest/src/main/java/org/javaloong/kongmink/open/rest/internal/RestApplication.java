package org.javaloong.kongmink.open.rest.internal;

import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationBase;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Map;

/**
 * The JAX-RS application for the rest JAX-RS resources.
 *
 * @author Xu Cheng
 */
@Component(service = Application.class)
@JaxrsName(RESTConstants.JAX_RS_NAME)
@JaxrsApplicationBase(RESTConstants.BASE_PATH)
@ApplicationPath(RESTConstants.BASE_PATH)
public class RestApplication extends Application {

    @Override
    public Map<String, Object> getProperties() {
        // When this option is enabled, the default CXF WebApplicationExceptionMapper
        // is relegated to the lowest priority, so that it becomes possible to
        // handle a WebApplicationException exception with a custom exception mapper
        return Collections.singletonMap("default.wae.mapper.least.specific", true);
    }
}
