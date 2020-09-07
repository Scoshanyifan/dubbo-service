package com.scosyf.dubbo.log.provider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.scosyf.dubbo.common.entity.PageResult;
import com.scosyf.dubbo.common.entity.ServiceResult;
import com.scosyf.dubbo.common.util.mongo.MongoUtil;
import com.scosyf.dubbo.log.api.api.LogService;
import com.scosyf.dubbo.log.api.dto.LogOperationQueryDto;
import com.scosyf.dubbo.log.api.dto.LogRequestLogQueryDto;
import com.scosyf.dubbo.log.api.entity.OperationLog;
import com.scosyf.dubbo.log.api.entity.RequestLog;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ServiceResult saveRequestLog(RequestLog requestLog) {
        RequestLog save = mongoTemplate.save(requestLog);
        return ServiceResult.success(save);
    }

    @Override
    public ServiceResult pageRequestLog(LogRequestLogQueryDto queryDto) {
        PageResult pageResult = PageResult.init(queryDto.getPageNum(), queryDto.getPageSize());

        Query query = new Query();

        Criteria ip = MongoUtil.strRegex(RequestLog.IP, queryDto.getIpRegex());
        MongoUtil.appendCriteria(query, ip);

        Criteria url = MongoUtil.strRegex(RequestLog.URL, queryDto.getUrlRegex());
        MongoUtil.appendCriteria(query, url);

        Criteria className = MongoUtil.strRegex(RequestLog.CLASS_NAME, queryDto.getClassNameRegex());
        MongoUtil.appendCriteria(query, className);

        Criteria methodName = MongoUtil.strRegex(RequestLog.METHOD_NAME, queryDto.getMethodNameRegex());
        MongoUtil.appendCriteria(query, methodName);

        Criteria httpMethod = MongoUtil.strIs(RequestLog.HTTP_METHOD, queryDto.getHttpMethod());
        MongoUtil.appendCriteria(query, httpMethod);

        Criteria httpStatus = MongoUtil.strIs(RequestLog.HTTP_STATUS, queryDto.getHttpStatus());
        MongoUtil.appendCriteria(query, httpStatus);

        Criteria costTime = MongoUtil.longCompare(RequestLog.COST_TIME, queryDto.getCostTimeMin(), queryDto.getCostTimeMax());
        MongoUtil.appendCriteria(query, costTime);

        // 时区问题(spring boot帮我们做了转换，入参和结果，数据库存的是0区时刻)
        Criteria createTime = MongoUtil.dateCompare(RequestLog.CREATE_TIME, queryDto.getStartTime(), queryDto.getEndTime(), false);
        MongoUtil.appendCriteria(query, createTime);

        // 先计算总数
        long total = mongoTemplate.count(query, RequestLog.class);
        // 再排序分页
        query.with(Sort.by(Sort.Direction.DESC, RequestLog.CREATE_TIME));
        query.skip((queryDto.getPageNum() - 1) * queryDto.getPageSize()).limit(queryDto.getPageSize());
        logger.info(">>> pageRequestLog query:{}", query);
        List<RequestLog> logList = mongoTemplate.find(query, RequestLog.class);
        if (CollectionUtils.isNotEmpty(logList)) {
            pageResult.setList(logList);
            pageResult.setTotal(total);
            pageResult.setPages(total / queryDto.getPageSize() + 1);
        }
        return ServiceResult.success(pageResult);
    }

    @Override
    public ServiceResult saveOperationLog(OperationLog operationLog) {
        OperationLog save = mongoTemplate.save(operationLog);
        return ServiceResult.success(save);
    }

    @Override
    public ServiceResult pageOperationLog(LogOperationQueryDto queryDto) {
        PageResult pageResult = PageResult.init(queryDto.getPageNum(), queryDto.getPageSize());

        Query query = new Query();

        Criteria operateType = MongoUtil.strIs(OperationLog.OPERATE_TYPE, queryDto.getOperateType());
        MongoUtil.appendCriteria(query, operateType);

        Criteria content = MongoUtil.strRegex(OperationLog.CONTENT, queryDto.getContent());
        MongoUtil.appendCriteria(query, content);

        Criteria operatorName = MongoUtil.strRegex(OperationLog.OPERATOR_NAME, queryDto.getOperatorName());
        MongoUtil.appendCriteria(query, operatorName);

        Criteria operatorId = MongoUtil.strIs(OperationLog.OPERATE_IP, queryDto.getOperatorId());
        MongoUtil.appendCriteria(query, operatorId);

        Criteria operateTime = MongoUtil.dateCompare(OperationLog.OPERATE_TIME, queryDto.getOperateTimeStart(), queryDto.getOperateTimeEnd(), false);
        MongoUtil.appendCriteria(query, operateTime);

        long total = mongoTemplate.count(query, OperationLog.class);
        query.skip((queryDto.getPageNum() - 1) * queryDto.getPageSize()).limit(queryDto.getPageSize());
        logger.info(">>> pageOperationLog query:{}", query);
        List<OperationLog> operateLogs = mongoTemplate.find(query, OperationLog.class);
        if (CollectionUtils.isNotEmpty(operateLogs)) {
            pageResult.setList(operateLogs);
            pageResult.setTotal(total);
            pageResult.setPages(total / queryDto.getPageSize());
        }
        return ServiceResult.success(pageResult);
    }
}
