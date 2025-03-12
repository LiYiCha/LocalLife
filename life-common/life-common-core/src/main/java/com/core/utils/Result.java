package com.core.utils;

import java.io.Serializable;

public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    private Result(Object data) {
        this.data = data;
    }

    public Result() {
    }

    public static Result success() {
        Result tResult = new Result();
        tResult.setCode(ResultEnum.SUCCESS.code);
        tResult.setMsg(ResultEnum.SUCCESS.message);
        return tResult;
    }

    public static Result success(Object data) {
        Result tResult = new Result (data);
        tResult.setCode(ResultEnum.SUCCESS.code);
        tResult.setMsg(ResultEnum.SUCCESS.message);
        return tResult;
    }

    public static Result error() {
        Result tResult = new Result();
        tResult.setCode(ResultEnum.error.code);
        tResult.setMsg(ResultEnum.error.message);
        return tResult;
    }

    public static Result error(int code, String msg) {
        Result tResult = new Result();
        tResult.setCode(code);
        tResult.setMsg(msg);
        return tResult;
    }

    public static Result error(ResultEnum resultCodeEnum) {
        Result tResult = new Result();
        tResult.setCode(resultCodeEnum.code);
        tResult.setMsg(resultCodeEnum.message);
        return tResult;
    }
    public static Result error(String msg) {
        Result tResult = new Result();
        tResult.setCode(ResultEnum.error.code);
        tResult.setMsg(msg);
        return tResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
