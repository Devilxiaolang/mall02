package com.company.Commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    public ServerResponse() {
    }

    public ServerResponse(int status) {
        this.status = status;
    }

    public ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createSuccessResponse() {
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createSuccessResponse(T data) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createSuccessResponse(String msg, T data) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createSuccessMsgResponse(String msg) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createErrorResponse() {
        return new ServerResponse(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createErrorResponse(T data) {
        return new ServerResponse(ResponseCode.ERROR.getCode(), data);
    }

    public static <T> ServerResponse<T> createErrorResponse(String msg, T data) {
        return new ServerResponse(ResponseCode.ERROR.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createErrorMsgResponse(String msg) {
        return new ServerResponse(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> ServerResponse<T> createErrorCodeMsgResponse(int status, String msg) {
        return new ServerResponse(status, msg);
    }

}
