package com.example.CrudWithJoins.response;

import org.springframework.http.HttpStatus;

public class StatusResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public StatusResponse(HttpStatus status, String message) {
        this(status, message, null);
    }

    public StatusResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters and setters
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
