package com.scosyf.dubbo.common.code;

/**
 * @author: KunBu
 * @time: 2020/4/17 9:03
 * @description:
 */
public enum ApiCodeEnum implements ResultCode {
    //

    SYS_ERROR(100, "系统异常"),
    API_ERROR(101, "接口异常"),

    MAIL_SEND_ERROR(110, "邮件发送异常");

    ;

    private Integer code;
    private String msg;

    ApiCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
