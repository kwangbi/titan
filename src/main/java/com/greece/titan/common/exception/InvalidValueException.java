package com.greece.titan.common.exception;

import com.greece.titan.common.response.ErrorCode;

public class InvalidValueException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
