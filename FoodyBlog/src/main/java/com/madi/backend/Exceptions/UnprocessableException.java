package com.madi.backend.Exceptions;

import com.madi.backend.enums.ResponseStatus;

import lombok.Getter;

@Getter
public class UnprocessableException extends RuntimeException {
    private final ResponseStatus responseStatus;

    public UnprocessableException(ResponseStatus responseStatus) {
        super(responseStatus.name());
        this.responseStatus = responseStatus;
    }
}
