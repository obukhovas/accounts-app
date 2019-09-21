package ru.ao.app.business.exception;

public enum StatusCodeEnum {

    ACCESS_SERVICE_ERROR(0, "Error occurs while access data"),
    GET_CONNECTION_ERROR(1, "Could not open Database connection"),
    INITIALIZE_TEST_DATA_ERROR(2, "Could not initialize test data"),

    ACCOUNT_NOT_FOUNT_ERROR(3, "Account not found"),
    SOURCE_ACCOUNT_NOT_FOUNT_ERROR(4, "Source Account not found"),
    TARGET_ACCOUNT_NOT_FOUNT_ERROR(5, "Target Account not found");

    private int code;
    private String message;

    StatusCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
