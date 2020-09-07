package com.scosyf.dubbo.web.core.exception;

import com.scosyf.dubbo.common.entity.ApiResult;
import com.scosyf.dubbo.web.util.mail.ExceptionMailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ExceptionMailUtil exceptionMailUtil;

    @ExceptionHandler(Exception.class)
    public ApiResult handleRuntimeException(Exception e) {
        logger.error("ERROR :", e);
        exceptionMailUtil.sendExceptionMail(e, "全局异常", e.getMessage());
        return ApiResult.exception(e);
    }

}
