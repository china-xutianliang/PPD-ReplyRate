package com.xtl.ecore.exception;

import com.xtl.ecore.entity.CommonResult;
import com.xtl.ecore.enums.ResponseCode;
import com.xtl.ecore.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;

@Component
@Slf4j
public class GlobalExceptionHandler {

    @PostConstruct
    public void init() {
        // 注册全局异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this::handleUncaughtException);
    }

    private void handleUncaughtException(Thread t, Throwable e) {
        if (e instanceof BusinessException) {
            handleBusinessException((BusinessException) e);
        } else if (e instanceof SQLException) {
            handleSQLException((SQLException) e);
        } else if (e instanceof EntityNotFoundException) {
            handleEntityNotFoundException((EntityNotFoundException) e);
        } else {
            handleGeneralException(e);
        }
    }

    private CommonResult<?> handleBusinessException(BusinessException e) {
        log.warn("BusinessException：" + e.getCode() + "," + e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getData());
    }

    private CommonResult<?> handleSQLException(SQLException e) {
        log.error("SQLException: ", e);
        return ResultUtils.error(ResponseCode.DB_ERROR.getCode(), "数据库访问异常");
    }

    private CommonResult<?> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("EntityNotFoundException: ", e);
        return ResultUtils.error(ResponseCode.NOT_FOUND.getCode(), "资源未找到");
    }

    private CommonResult<?> handleGeneralException(Throwable e) {
        log.error("Exception:", e);
        return ResultUtils.error(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
    }
}