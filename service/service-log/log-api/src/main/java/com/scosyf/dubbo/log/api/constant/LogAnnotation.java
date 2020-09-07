package com.scosyf.dubbo.log.api.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: KunBu
 * @time: 2019/8/3 15:31
 * @description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAnnotation {

    /**
     * 操作说明
     **/
    String value() default "";

    /**
     * 操作类型
     **/
    LogOperationTypeEnum type();
}
