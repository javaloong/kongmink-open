package org.javaloong.kongmink.open.am.keycloak.internal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;

public class JAXRSClientUtils {

    public static String getCreatedId(Response response) throws WebApplicationException {
        URI location = response.getLocation();
        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            Response.StatusType statusInfo = response.getStatusInfo();
            throw new WebApplicationException("Create method returned status " +
                    statusInfo.getReasonPhrase() + " (Code: " + statusInfo.getStatusCode() + "); " +
                    "expected status: Created (201)", response);
        }
        if (location == null) {
            return null;
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
