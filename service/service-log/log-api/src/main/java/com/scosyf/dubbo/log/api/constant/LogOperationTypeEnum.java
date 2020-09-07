package com.scosyf.dubbo.log.api.constant;

public enum LogOperationTypeEnum {

    ADD("新增"),
    DELETE("删除"),
    MODIFY("修改"),
    QUERY("查询"),
    OTHER("其他"),

    ;

    private String desc;

    LogOperationTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
