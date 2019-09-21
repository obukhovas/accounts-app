package ru.ao.app.endpoint.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.app.business.exception.BusinessException;
import ru.ao.app.endpoint.dto.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AppExceptionMapper implements ExceptionMapper<BusinessException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionMapper.class);

    @Override
    public Response toResponse(BusinessException e) {
        LOGGER.debug("Server error occurred", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getStatusCodeEnum().getCode());
        errorResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
