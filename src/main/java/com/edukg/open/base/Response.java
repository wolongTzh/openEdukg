package com.edukg.open.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -4505655308965878999L;

    //请求成功返回码为：0000
    private static final String successCode = "0";
    //返回数据
    private T data;
    //返回码
    private String code;
    //返回描述
    private String msg;

    public Response() {
        this.code = successCode;
        this.msg = "请求成功";
    }

    public static <T> Response<T> success(T object) {
        Response<T> result = new Response<T>();
        result.setCode("0");
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static <T> Response<T> success(T object, String msg) {
        Response<T> result = new Response<T>();
        result.setCode("0");
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    public static <T> Response<T> error(String code, String msg) {
        Response<T> result = new Response<T>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Response<T> fail(Integer code, String msg) {
        Response<T> result = new Response<T>();
        result.setCode(code.toString());
        result.setMsg(msg);
        return result;
    }

}