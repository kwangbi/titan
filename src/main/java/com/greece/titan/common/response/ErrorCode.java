package com.greece.titan.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "02", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "02", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "02", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "02", "Server Error"),
    INVALID_TYPE_VALUE(400, "02", " Invalid Type Value"),
    PAGE_NOT_FOUND(404, "02", " api not found"),
    HANDLE_ACCESS_DENIED(403, "02", "Access is Denied");


    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}

