package com.scosyf.dubbo.log.api.api;

import com.scosyf.dubbo.common.entity.ServiceResult;
import com.scosyf.dubbo.log.api.dto.LogOperationQueryDto;
import com.scosyf.dubbo.log.api.dto.LogRequestLogQueryDto;
import com.scosyf.dubbo.log.api.entity.OperationLog;
import com.scosyf.dubbo.log.api.entity.RequestLog;

public interface LogService {

    ServiceResult saveRequestLog(RequestLog requestLog);

    ServiceResult pageRequestLog(LogRequestLogQueryDto queryDto);

    ServiceResult saveOperationLog(OperationLog operationLog);

    ServiceResult pageOperationLog(LogOperationQueryDto queryDto);

}
