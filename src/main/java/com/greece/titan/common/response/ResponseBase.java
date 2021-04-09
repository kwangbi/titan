package com.greece.titan.common.response;

import java.util.Objects;

public class ResponseBase<T> {
    protected String code = Defaults.Response.code;
    protected String message = Defaults.Response.message;
    protected String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    protected T result;


    protected ResponseBase() {
        this.result = null;
    }

    protected ResponseBase(final T value) {
        this.result = Objects.requireNonNull(value);
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(final T result) {
        this.result = result;
    }

    public static <T> ResponseBase<T> success() {
        return new ResponseBase<>();
    }

    public static <T> ResponseBase<T> error() {
        ResponseBase<T> res = new ResponseBase<>();
        res.setCode(Codes.Response.ERROR.getCode());
        res.setMessage(Codes.Response.ERROR.getMessage());
        return res;
    }

    public static <T> ResponseBase<T> create(final String code, final String message) {
        ResponseBase<T> res = new ResponseBase<>();
        res.setCode(code);
        res.setMessage(message);
        return res;
    }

    public static <T> ResponseBase<T> of(final T result) {
        return new ResponseBase<>(result);
    }

    // business exception 공통포맷
    public static <T> ResponseBase<T> business(final String code, final String message, final String timestamp) {
        ResponseBase<T> res = new ResponseBase<>();
        res.setTimestamp(timestamp);
        res.setCode(code);
        res.setMessage(message);
        return res;
    }

    @Override
    public String toString() {
        return "ResponseBase{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

}
