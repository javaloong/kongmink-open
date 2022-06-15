package org.javaloong.kongmink.open.account.keycloak.internal.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotImplementedException extends WebApplicationException {

    public NotImplementedException() {
        this("The operation you've called is not implemented yet");
    }

    public NotImplementedException(String message) {
        super(Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity(message)
                .type(MediaType.TEXT_PLAIN).build());
    }
}
