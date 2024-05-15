package com.openquartz.logserver.service.impl;

import com.openquartz.easybizlog.common.context.LogRecordContext;
import com.openquartz.easybizlog.core.annotation.LogRecord;
import com.openquartz.logserver.service.UserQueryService;
import com.openquartz.logserver.infrastructure.constants.LogRecordType;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author svnee
 */
@Service
@Slf4j
public class UserQueryServiceImpl implements UserQueryService {

    @Override
    @LogRecord(success = "获取用户列表,内层方法调用人{{#user}}", type = LogRecordType.ORDER, bizNo = "MT0000011")
    public List<User> getUserList(List<String> userIds) {
        LogRecordContext.putVariable("user", "sys");
        return null;
    }
}
