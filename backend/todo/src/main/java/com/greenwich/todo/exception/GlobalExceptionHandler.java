package com.greenwich.todo.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception ex, WebRequest request) {
        System.out.println("====================> handleException");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase()); //ghi thẳng chuỗi thì là nghiệp dư ko nên
        //ghi như sau thì chuyên nghiệp

        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException){
            int start = message.lastIndexOf("[");

            int end = message.lastIndexOf("]");
            message = message.substring(start + 1, end - 1);
            errorResponse.setError("Payload Invalid");
        }  else if (ex instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" ") + 1).trim();
            errorResponse.setError("PathVariable Invalid");
        }

        errorResponse.setMessage(message);
        return errorResponse;

    }

}