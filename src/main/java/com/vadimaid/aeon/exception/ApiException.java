package com.vadimaid.aeon.exception;

import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;

public class ApiException extends ServletException {

    private HttpStatus status;

    private String errorCode;

    public ApiException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
