package com.vadimaid.aeon.dto;

public class ErrorResponse {
    private Integer httpStatus;
    private String errorCode;
    private String message;

    public ErrorResponse(Integer httpStatus, String errorCode, String message){
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
