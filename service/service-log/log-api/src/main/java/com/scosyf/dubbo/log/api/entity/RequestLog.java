package com.scosyf.dubbo.log.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document("request_log")
public class RequestLog implements Serializable {

    public static final String CLASS_NAME = "className";
    public static final String METHOD_NAME = "methodName";
    public static final String PARAMETERS = "parameters";
    public static final String DESCRIPTION = "description";
    public static final String HTTP_METHOD = "httpMethod";
    public static final String HTTP_STATUS = "httpStatus";
    public static final String IP = "ip";
    public static final String URL = "url";
    public static final String USER_AGENT = "userAgent";
    public static final String COST_TIME = "costTime";
    public static final String CREATE_TIME = "createTime";

    @Id
    private String id;

    private String className;
    private String methodName;
    private String parameters;
    private String description;

    private String httpMethod;
    private String httpStatus;
    private String ip;
    private String url;
    private String userAgent;

    private Long costTime;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RequestLog{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters='" + parameters + '\'' +
                ", description='" + description + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", httpStatus='" + httpStatus + '\'' +
                ", ip='" + ip + '\'' +
                ", url='" + url + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", costTime=" + costTime +
                ", createTime=" + createTime +
                '}';
    }
}
