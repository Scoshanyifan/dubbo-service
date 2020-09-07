package com.scosyf.dubbo.web.core.aop;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.scosyf.dubbo.common.constant.CommonConstant;
import com.scosyf.dubbo.common.constant.HttpConstant;
import com.scosyf.dubbo.web.util.request.HttpRequestUtil;
import com.scosyf.dubbo.web.util.request.IpUtil;
import com.scosyf.dubbo.common.util.TokenUtil;
import com.scosyf.dubbo.log.api.api.LogService;
import com.scosyf.dubbo.log.api.constant.LogAnnotation;
import com.scosyf.dubbo.log.api.entity.OperationLog;
import com.scosyf.dubbo.log.api.entity.RequestLog;
import com.scosyf.dubbo.web.redis.RedisManager;
import com.scosyf.dubbo.web.redis.UserInfo;
import com.scosyf.dubbo.web.util.mail.ExceptionMailUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * @program: spring-practice
 * @description: 请求日志工具类
 * @author: kunbu
 * @create: 2019-08-03 15:53
 **/
@Component
@Aspect
public class RequestLogUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogUtil.class);

    @Reference
    private LogService logService;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private ExceptionMailUtil exceptionMailUtil;

    /**
     * * com.kunbu.spring.controller..*.*(..) 表示controller包以及其下子包中的类，类中的所有方法
     * * com.kunbu.spring.controller.*.*(..)) 则仅表示controller包下的类
     * <p>
     * 第一个*表示任意返回类型，修饰符可省略
     * ..表示0或多个，即所有，controller后面的..表示到此路径为止
     * ..*.中的*表示一个单词，即任意类，后面的.是包名的.
     * ..*.*(..)中第二个*表示任意方法，(..)表示方法中任意参数
     **/
    @Pointcut("execution(* com.scosyf.dubbo.web.controller..*.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object requestLog(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        //检查是否调用成功
        boolean success = true;
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            success = false;
            logger.error(">>> RequestLogUtil aop proceed error:", e);
            // 邮件发送异常信息
            exceptionMailUtil.sendExceptionMail(e, "请求调用异常", "-");
        } finally {
            // 调用结果
            logger.info(">>> RequestLogUtil response result:{}", JSONObject.toJSONString(result));
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
///                HttpRequestPrintUtil.printHttpRequest(request, logger);

                Signature signature = joinPoint.getSignature();
                long timeCost = System.currentTimeMillis() - startTime;
                if (timeCost >= CommonConstant.CONTROLLER_CONSUMPTION_MILLIONS) {
                    logger.warn("RequestLogUtil api consume:{} ms, method:{}", timeCost, signature.toShortString());
                }

                RequestLog log = new RequestLog();

                String ip = IpUtil.getIp(request);
                String token = request.getHeader(HttpConstant.HTTP_HEADER_TOKEN);
                String methodName = signature.getName();
                //拿到api信息
                LogAnnotation note = getApiNote(joinPoint);
                if (StringUtils.isNotBlank(token)) {
                    //redis取用户信息
                    UserInfo userInfo = (UserInfo) redisManager.getObject(token);
//                    log.setToken(token);
//                    log.setUserId(userInfo.getUserId().toString());
                    //身份是admin且调用执行成功才保存操作日志
                    if (TokenUtil.checkAdmin(token) && success) {
                        saveOperateLog(ip, startTime, userInfo.getUserId().toString(), userInfo.getUserName(), getParameters(request), note);
                    }
                }

                log.setClassName(signature.getDeclaringTypeName());
                log.setMethodName(methodName);
                //TODO 参数（如果是json传的body，直接获取是拿不到参数的，只能用流）
                log.setParameters(getParameters(request));
//                log.setParameters(JSONObject.toJSONString(request.getParameterMap()));
//                log.setParameters(getParams(joinPoint));
                log.setParameters(getParameters(request));
                log.setUrl(request.getRequestURL().toString());
                log.setUserAgent(request.getHeader(HttpConstant.HTTP_HEADER_USER_AGENT));

                log.setCostTime(timeCost);
                log.setDescription(note != null ? note.value() : null);
                log.setCreateTime(new Date(startTime));
                log.setIp(ip);
                // GET POST
                log.setHttpMethod(request.getMethod().toUpperCase());
                // 200 500
                if (success) {
                    log.setHttpStatus(HttpConstant.HTTP_STATUS_OK);
                } else {
                    log.setHttpStatus(HttpConstant.HTTP_STATUS_ERROR);
                }
                // 请求日志每次都会保存
                logService.saveRequestLog(log);
            } catch (Exception e) {
                logger.error(">>> RequestLogUtil aop handle error:", e);
            }
        }
        return result;
    }

    /**
     * 保存操作日志
     *
     * @param ip
     * @param startTime
     * @param operatorId
     * @param params
     * @param note
     */
    private void saveOperateLog(String ip, long startTime, String operatorId, String operatorName, String params, LogAnnotation note) {
        // 只有标注了ApiNote注解的才保存操作日志（过滤查询等）
        if (note != null) {
            OperationLog log = new OperationLog();
            log.setOperateIp(ip);
            log.setOperateTime(new Date(startTime));
            log.setOperatorId(operatorId);
            log.setOperatorName(operatorName);
            log.setParams(params);
            log.setContent(note.value());
            log.setOperateType(note.type().name());
            logService.saveOperationLog(log);
        }
    }

    /**
     * 接口功能说明
     *
     * @param joinPoint
     * @return
     * @author kunbu
     * @time 2019/8/26 17:41
     **/
    private LogAnnotation getApiNote(ProceedingJoinPoint joinPoint) {
        Method[] ms = joinPoint.getTarget().getClass().getDeclaredMethods();
        for (Method method : ms) {
            if (method.getName().equals(joinPoint.getSignature().getName())) {
                LogAnnotation apiNote = method.getAnnotation(LogAnnotation.class);
                if (apiNote != null) {
                    return apiNote;
                }
            }
        }
        return null;
    }

    /**
     * 获取接口参数
     *
     * It is illegal to call this method if the current request is not in asynchronous mode
     *
     * @param joinPoint
     * @return
     * @author kunbu
     * @time 2019/9/2 14:24
     **/
    @Deprecated
    private String getParams(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            str.append(JSONObject.toJSONString(args[i]) + ";");
        }
        return str.toString();
    }

    /**
     * 获取请求参数
     * String[]表示同一参数名有多个值：xxxx?p=101&p=2333
     *
     * @param request
     * @return
     * @author kunbu
     * @time 2019/9/9 14:18
     **/
    private String getParameters(HttpServletRequest request) throws IOException {
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap != null && !paramMap.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (Map.Entry<String, String[]> en : paramMap.entrySet()) {
                str.append(en.getKey()).append(":").append(en.getValue()[0]).append(";");
            }
            if (str.length() > 0) {
                str.deleteCharAt(str.length() - 1);
            }
            return str.toString();
        } else {
            return HttpRequestUtil.getPostBodyJson(request);
        }
    }



}
