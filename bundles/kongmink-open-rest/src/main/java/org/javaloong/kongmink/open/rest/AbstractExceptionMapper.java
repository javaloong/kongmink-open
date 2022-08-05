package org.javaloong.kongmink.open.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.List;

public abstract class AbstractExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {

    @Override
    public Response toResponse(E exception) {
        Response.StatusType statusType = getStatusType(exception);
        Error error = createError(statusType, exception);
        return Response.status(error.status)
                .entity(error)
                .build();
    }

    protected String getMessage(Response.StatusType statusType, E exception) {
        if (statusType.getStatusCode() >= 500) {
            return statusType.getReasonPhrase();
        }
        return exception.getLocalizedMessage();
    }

    protected Error createError(Response.StatusType statusType, E exception) {
        return new Error(
                statusType.getStatusCode(),
                statusType.toEnum().name(),
                getMessage(statusType, exception),
                createErrors(exception));
    }

    protected abstract List<Object> createErrors(E exception);

    protected abstract Response.StatusType getStatusType(E exception);

    static class Error {

        public int status;
        public String code;
        public String message;
        public List<Object> errors;

        public Error(int status, String code, String message, List<Object> errors) {
            this.status = status;
            this.code = code;
            this.message = message;
            this.errors = errors;
        }
    }
}
