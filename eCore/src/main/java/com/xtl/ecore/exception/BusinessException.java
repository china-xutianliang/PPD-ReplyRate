package com.xtl.ecore.exception;


import com.xtl.ecore.enums.ResponseCode;
import com.xtl.ecore.utils.StringUtils;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private Object data;

    public BusinessException(String message) {
        super(message);
        this.code = ResponseCode.ERROR.getCode();
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResponseCode e) {
        super(e.getMessage());
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public BusinessException(ResponseCode e, Object data) {
        super(e.getMessage());
        this.code = e.getCode();
        this.message = e.getMessage();
        this.data = data;
    }

    public BusinessException(ResponseCode e, String msg) {
        super(e.getMessage());
        this.code = e.getCode();
        if (StringUtils.isNotBlank(msg))
            this.message = msg;
        else
            this.message = e.getMessage();
    }

    public BusinessException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
