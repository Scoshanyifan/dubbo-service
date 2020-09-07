package com.scosyf.dubbo.log.api.dto;

import com.scosyf.dubbo.common.dto.BaseQueryDto;

import java.io.Serializable;
import java.util.Date;

public class LogOperationQueryDto extends BaseQueryDto implements Serializable {

    private String operateType;

    private String content;

    private String operatorId;

    private String operatorName;

    private Date operateTimeStart;

    private Date operateTimeEnd;

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Date getOperateTimeStart() {
        return operateTimeStart;
    }

    public void setOperateTimeStart(Date operateTimeStart) {
        this.operateTimeStart = operateTimeStart;
    }

    public Date getOperateTimeEnd() {
        return operateTimeEnd;
    }

    public void setOperateTimeEnd(Date operateTimeEnd) {
        this.operateTimeEnd = operateTimeEnd;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @Override
    public String toString() {
        return "OperateLogQueryParam{" +
                "operateType='" + operateType + '\'' +
                ", content='" + content + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", operateTimeStart='" + operateTimeStart + '\'' +
                ", operateTimeEnd='" + operateTimeEnd + '\'' +
                "} " + super.toString();
    }

}
