package ru.ao.app.business.exception;

public class BusinessException extends RuntimeException {

    private final StatusCodeEnum statusCodeEnum;

    public BusinessException(StatusCodeEnum statusCodeEnum) {
        super(statusCodeEnum.getMessage());
        this.statusCodeEnum = statusCodeEnum;
    }

    public BusinessException(StatusCodeEnum statusCodeEnum, String message) {
        super(message);
        this.statusCodeEnum = statusCodeEnum;
    }

    public BusinessException(StatusCodeEnum statusCodeEnum, Throwable cause) {
        super(statusCodeEnum.getMessage(), cause);
        this.statusCodeEnum = statusCodeEnum;
    }

    public StatusCodeEnum getStatusCodeEnum() {
        return statusCodeEnum;
    }
}
