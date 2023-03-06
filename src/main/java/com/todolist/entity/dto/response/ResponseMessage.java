package com.todolist.entity.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ResponseMessage {

    private HttpStatus httpStatus;
    private HttpStatusCode httpStatusCode;
    private String message;

    public ResponseMessage() {
    }

    public ResponseMessage(HttpStatus httpStatus, HttpStatusCode httpStatusCode, String message) {
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
