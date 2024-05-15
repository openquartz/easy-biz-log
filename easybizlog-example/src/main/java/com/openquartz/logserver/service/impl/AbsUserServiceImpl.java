package com.openquartz.logserver.service.impl;

import com.openquartz.easybizlog.core.annotation.LogRecord;
import com.openquartz.logserver.infrastructure.constants.LogRecordType;
import com.openquartz.logserver.pojo.User;

/**
 * @author svnee
 **/
public abstract class AbsUserServiceImpl {
    public boolean testAbstract(User user, User newUser) {
        return false;
    }

    public boolean testInterface(User user, User newUser) {
        return false;
    }

    @LogRecord(success = "更新了用户信息{_DIFF{#user, #newUser}}",
            type = LogRecordType.USER, bizNo = "{{#newUser.id}}",
            extra = "{{#newUser.toString()}}")
    @LogRecord(success = "更新了用户信息{_DIFF{#user, #newUser}}",
            type = LogRecordType.ORDER, bizNo = "{{#newUser.id}}",
            extra = "{{#newUser.toString()}}")
    public boolean testAbstracts(User user, User newUser) {
        return false;
    }

    @LogRecord(success = "更新了用户信息{_DIFF{#user, #newUser}}",
            type = LogRecordType.ORDER, bizNo = "{{#newUser.id}}",
            extra = "{{#newUser.toString()}}")
    public boolean testInterfaceAndAbstract2(User user, User newUser) {
        return false;
    }
}