package ru.ao.app.endpoint.config;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(prepareMessage(exception))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Map<String, String> prepareMessage(ConstraintViolationException exception) {
        Map<String, String> response = new HashMap<>();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            response.put(cv.getPropertyPath().toString(), cv.getMessage());
        }
        return response;
    }
}
