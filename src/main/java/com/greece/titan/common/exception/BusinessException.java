package com.greece.titan.common.exception;

import com.greece.titan.common.response.ErrorCode;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;
    private String errCd;

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String errCd) {
        this.errCd = errCd;
    }


    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrCd() {
        return errCd;
    }

}

