package com.madi.backend.enums;

public enum ResponseStatus {
    SUCCESS(200),
    INTERNAL_SERVER_ERROR(500),
    ;

    public final int code;

    ResponseStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
