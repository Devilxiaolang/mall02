package com.company.Commons;

public enum  ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERRMOE"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"),
    NEED_LOGIN(20,"NEED_LOGIN");

    ResponseCode() {
    }

    private int code;
    private String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
