package com.scosyf.dubbo.web.controller;

import com.scosyf.dubbo.common.code.ApiCodeEnum;
import com.scosyf.dubbo.common.entity.ApiResult;
import com.scosyf.dubbo.web.util.mail.MailSendUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project/open")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private MailSendUtil mailSendUtil;

    @PostMapping("/mail/send")
    public ApiResult sendMail(String content, String to) {
        if (StringUtils.isNotBlank(to)) {
            try {
                mailSendUtil.sendSimpleMail(new String[]{to}, "测试邮件发送", content);
            } catch (Exception e) {
                logger.error(">>> sendMail error", e);
                return ApiResult.fail(ApiCodeEnum.MAIL_SEND_ERROR);
            }
        } else {

        }
        return ApiResult.success(content);
    }

}
