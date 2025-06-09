package com.greenwich.todo.exception;

import java.util.Date;

public class ErrorResponse {
    private Date timestamp;  //thời gian xảy ra lỗi
    private int status;  //mã lỗi HTTP
    private String path;
    private String error;
    private String message;  //thông điệp lỗi

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}