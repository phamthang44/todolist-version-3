package com.greenwich.todo.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler extends Throwable {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(Exception ex, WebRequest request) {
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
        } else if (ex instanceof HttpMessageNotReadableException) {
            message = "Malformed JSON request";
            errorResponse.setError("Malformed JSON request");
        }

        errorResponse.setMessage(message);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}