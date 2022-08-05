package org.javaloong.kongmink.open.rest.core.internal.provider;

import org.javaloong.kongmink.open.rest.AbstractExceptionMapper;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component(service = ExceptionMapper.class, immediate = true)
@JaxrsExtension
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@Provider
public class ValidationExceptionMapper extends AbstractExceptionMapper<ValidationException> {

    @Override
    protected List<Object> createErrors(ValidationException exception) {
        List<Object> errors = new ArrayList<>();
        if (exception instanceof ConstraintViolationException) {
            errors = ((ConstraintViolationException) exception).getConstraintViolations().stream()
                    .map(this::toValidationError)
                    .collect(Collectors.toList());
        }
        return errors;
    }

    @Override
    protected Response.StatusType getStatusType(ValidationException exception) {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    protected String getMessage(Response.StatusType statusType, ValidationException exception) {
        StringBuilder sb = new StringBuilder("Validation failed.");
        if (exception instanceof ConstraintViolationException) {
            sb.append(" Error count: ");
            sb.append(((ConstraintViolationException) exception).getConstraintViolations().size());
        }
        return sb.toString();
    }

    private ValidationError toValidationError(ConstraintViolation<?> constraintViolation) {
        ValidationError error = new ValidationError();
        String field = getFieldFromPath(constraintViolation.getPropertyPath());
        if (field != null && !field.equals("")) {
            error.field = field;
            error.value = constraintViolation.getInvalidValue();
        }
        error.message = constraintViolation.getMessage();
        return error;
    }

    private String getFieldFromPath(Path fieldPath) {
        Iterator<Path.Node> nodes = fieldPath.iterator();
        String fieldName = null;
        while (nodes.hasNext()) {
            fieldName = nodes.next().toString();
        }
        return fieldName;
    }

    static class ValidationError {

        public String field;
        public Object value;
        public String message;
    }
}
