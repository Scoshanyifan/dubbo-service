package com.scosyf.dubbo.web.controller.log;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.scosyf.dubbo.log.api.constant.LogAnnotation;
import com.scosyf.dubbo.common.entity.ApiResult;
import com.scosyf.dubbo.common.entity.ServiceResult;
import com.scosyf.dubbo.log.api.dto.LogOperationQueryDto;
import com.scosyf.dubbo.web.util.request.HttpRequestUtil;
import com.scosyf.dubbo.log.api.api.LogService;
import com.scosyf.dubbo.log.api.constant.LogOperationTypeEnum;
import com.scosyf.dubbo.log.api.dto.LogRequestLogQueryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/project/api/web/log")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Reference
    private LogService logService;

    @LogAnnotation(value = "请求日志分页", type = LogOperationTypeEnum.QUERY)
    @GetMapping("/request/page")
    public ApiResult requestLogPage(HttpServletRequest request,
                                    @RequestBody LogRequestLogQueryDto dto) {

        HttpRequestUtil.printHttpRequest(request, logger);

        try {
            logger.info(">>> params json:{}", HttpRequestUtil.getPostBodyJson(request));
            logger.info(">>> params:{}", HttpRequestUtil.getPostDataPretty(request));
        } catch (IOException e) {
            logger.error(">>> getPostBodyStr error", e);
        }

        logger.info(">>> requestLogPage dto:{}", dto);

        ServiceResult result = logService.pageRequestLog(dto);
        return ApiResult.result(result);
    }

    /**
     * 1. 使用params提交（参数在url中）
     * 2. 使用body的form-data提交（参数在body中），会报错Required request body is missing
     *
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */
    @LogAnnotation(value = "操作日志分页", type = LogOperationTypeEnum.QUERY)
    @GetMapping("/operate/page")
    public ApiResult operateLogPage(HttpServletRequest request,
                                    @RequestParam Integer pageNum, @RequestParam Integer pageSize) {

        HttpRequestUtil.printHttpRequest(request, logger);

        logger.info(">>> operateLogPage pageNum:{}, pageSize:{}", pageNum, pageSize);

        LogOperationQueryDto queryDto = new LogOperationQueryDto();
        queryDto.setPageNum(pageNum);
        queryDto.setPageSize(pageSize);
        ServiceResult result = logService.pageOperationLog(queryDto);
        return ApiResult.result(result);
    }

}
