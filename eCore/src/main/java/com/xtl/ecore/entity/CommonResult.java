package com.xtl.ecore.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xtl.ecore.exception.BusinessException;

import java.io.Serializable;

/**
 * 返回结果实体类 各个项目维护各自的业务错误码
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 2893718547600255931L;
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T result;

    public CommonResult() {
    }

    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.result = data;
    }

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    /**
     * 针对返回code进行处理,如果远程调用出现BusinessException则抛出相同Exception
     */
    public T safeResult() {
        if (code == 0)
            return result;
        throw new BusinessException(code, msg);
    }
}