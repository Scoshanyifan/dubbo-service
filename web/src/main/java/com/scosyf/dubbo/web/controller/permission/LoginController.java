package com.scosyf.dubbo.web.controller.permission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.scosyf.dubbo.common.entity.ApiResult;
import com.scosyf.dubbo.common.entity.ServiceResult;
import com.scosyf.dubbo.log.api.api.LogService;
import com.scosyf.dubbo.log.api.constant.LogAnnotation;
import com.scosyf.dubbo.log.api.constant.LogOperationTypeEnum;
import com.scosyf.dubbo.log.api.dto.LogOperationQueryDto;
import com.scosyf.dubbo.log.api.dto.LogRequestLogQueryDto;
import com.scosyf.dubbo.web.util.request.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/project/api/web/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Reference
    private LogService logService;

    @LogAnnotation(value = "请求日志分页", type = LogOperationTypeEnum.QUERY)
    @PostMapping("/login")
    public ApiResult requestLogPage(HttpServletRequest request,
                                    @RequestParam String name) {


        return ApiResult.success();
    }

}
