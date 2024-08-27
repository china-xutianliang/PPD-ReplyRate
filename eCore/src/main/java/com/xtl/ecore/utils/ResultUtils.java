package com.xtl.ecore.utils;

import com.xtl.ecore.entity.CommonResult;
import com.xtl.ecore.enums.ResponseCode;
import com.xtl.ecore.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("all")
public class ResultUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResultUtils.class);

    public static CommonResult<?> success() {
        return success(null);
    }

    public static CommonResult<?> success(int code, String msg) {
        return success(code, msg, null);
    }

    public static <T> CommonResult<T> success(T data) {
        return success(ResponseCode.OK.getCode(), "success", data);
    }

    public static <T> CommonResult<T> success(int code, String msg, T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setResult(data);
        return result;
    }

    public static <T> CommonResult<T> error(int code, String msg, T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setResult(data);
        return result;
    }

    public static <T> CommonResult<T> error(int code, String msg) {
        return error(code, msg, null);
    }

    public static <T> CommonResult<T> error(String msg) {
        return error(ResponseCode.ERROR.getCode(), msg, null);
    }

    public static <T> CommonResult<T> error(Throwable throwable) {
        if (throwable instanceof BusinessException) {
            return handleBusinessException(throwable);
        } else if (throwable instanceof InvocationTargetException) {
            Throwable targetException = ((InvocationTargetException) throwable).getTargetException();
            return error(targetException);
        } else if (throwable instanceof NoSuchMethodException) {
            return error("operation no find");
        } else {
            logger.error("action execute error", throwable);
            return error(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
    }

    private static <T> CommonResult<T> handleBusinessException(Throwable throwable) {
        BusinessException businessException = (BusinessException) throwable;
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        if (stackTrace != null && stackTrace.length > 1) {
            logger.warn(String.format("业务处理异常: %s, %s, %s", stackTrace[0], stackTrace[1], throwable.getMessage()));
        } else {
            logger.warn("业务处理异常", businessException);
        }
        return error(businessException.getCode(), businessException.getMessage());
    }

}