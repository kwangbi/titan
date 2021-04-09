package com.greece.titan.common.response;

public interface Defaults {
    String cert = "N";

    interface Response {
        String code = Codes.Response.SUCCESS.getCode();
        String message = Codes.Response.SUCCESS.getMessage();
    }
}
