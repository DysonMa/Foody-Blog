package com.madi.backend.Exceptions;

import java.time.LocalDateTime;

import com.madi.backend.enums.ResponseStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData<T> {
    private Integer code;
    private String message;
    private LocalDateTime dateTime;
    private T data;

    public static <T> ResponseData<T> response(ResponseStatus responseStatus, T response) {
        return ResponseData.<T>builder()
                .code(responseStatus.getCode())
                .message(responseStatus.name())
                .dateTime(LocalDateTime.now())
                .data(response)
                .build();
    }

}
