package com.madi.backend.Exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.madi.backend.enums.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<String>> handleExceptions(Exception exception) {
        exception.printStackTrace();
        log.error("Exception: ", exception);

        return ResponseEntity.internalServerError()
                .body(ResponseData.response(ResponseStatus.INTERNAL_SERVER_ERROR, null));
    }

    @ExceptionHandler(UnprocessableException.class)
    public ResponseEntity<ResponseData<String>> handleUnprocessableExceptions(
            UnprocessableException unprocessableException) {
        unprocessableException.printStackTrace();
        log.error("UnprocessableException: ", unprocessableException);

        return ResponseEntity.internalServerError()
                .body(ResponseData.response(unprocessableException.getResponseStatus(), null));
    }
}
