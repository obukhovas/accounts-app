package ru.ao.app.endpoint.config;

import ru.ao.app.business.exception.BusinessException;
import ru.ao.app.endpoint.dto.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AppExceptionMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException e) {
        System.out.println("Server error occurred: " + e.getMessage());
        e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getStatusCodeEnum().getCode());
        errorResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
