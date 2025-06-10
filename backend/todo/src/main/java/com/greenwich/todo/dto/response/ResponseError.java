package com.greenwich.todo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseError extends ResponseData {

    public ResponseError(int status, String message) {
        super(status, message);
    }

    public ResponseError validateSqlError(String message) {
        if (message.contains("cannot be null")) {
            message = "Missing required field(s). Please check your request.";
        } else if (message.contains("not found")) {
            message = "Resource not found. Please check the ID or resource you are trying to access.";
        } else if (message.contains("already exists")) {
            message = "Resource already exists. Please check your request.";
        }
        return new ResponseError(500, message);
    }
}
