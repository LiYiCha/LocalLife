package com.core.utils;

import java.io.Serializable;

/**
 *  统一返回结果类
 * @author ：一茶
 */
public class Result implements Serializable {
    /**
     * 定义返回码
     */
    private int code;
    /**
     * 定义返回信息
     */
    private String msg;
    /**
     * 定义返回数据
     */
    private Object data;

   /**
     * 有参构造函数
     * @param data 返回数据
     */
    private Result(Object data) {
        this.data = data;
    }

    /**
     * 无参构造函数
     */
    public Result() {
    }

    /**
     * 返回成功结果
     */
    public static Result success() {
        Result tResult = new Result();
        tResult.setCode(ResultEnum.SUCCESS.code);
        tResult.setMsg(ResultEnum.SUCCESS.message);
        return tResult;
    }

    /**
     * 返回成功结果，并传入返回数据
     * @param data 返回数据
     */
    public static Result success(Object data) {
        Result tResult = new Result (data);
        tResult.setCode(ResultEnum.SUCCESS.code);
        tResult.setMsg(ResultEnum.SUCCESS.message);
        return tResult;
    }

    /**
     * 返回错误结果
     */
    public static Result error() {
        Result tResult = new Result();
        tResult.setCode(ResultEnum.error.code);
        tResult.setMsg(ResultEnum.error.message);
        return tResult;
    }

    /**
     * 返回错误结果，并传入返回码和返回信息
     * @param code 返回码
     * @param msg 返回信息
     */
    public static Result error(int code, String msg) {
        Result tResult = new Result();
        tResult.setCode(code);
        tResult.setMsg(msg);
        return tResult;
    }

    /**
     * 返回错误结果，并传入返回码和返回信息
     * @param resultCodeEnum 返回码和返回信息
     */
    public static Result error(ResultEnum resultCodeEnum) {
        Result tResult = new Result();
        tResult.setCode(resultCodeEnum.code);
        tResult.setMsg(resultCodeEnum.message);
        return tResult;
    }
   /**
    * 返回错误结果，并传入返回信息
    * @param msg 返回信息
    */
    public static Result error(String msg) {
        Result tResult = new Result();
        tResult.setCode(ResultEnum.error.code);
        tResult.setMsg(msg);
        return tResult;
    }

    // 获取返回码
    public int getCode() {
        return code;
    }

    // 设置返回码
    public void setCode(int code) {
        this.code = code;
    }

    // 获取返回信息
    public String getMsg() {
        return msg;
    }

    // 设置返回信息
    public void setMsg(String msg) {
        this.msg = msg;
    }
    // 获取返回数据

    public Object getData() {
        return data;
    }
    // 设置返回数据

    public void setData(Object data) {
        this.data = data;
    }
}
