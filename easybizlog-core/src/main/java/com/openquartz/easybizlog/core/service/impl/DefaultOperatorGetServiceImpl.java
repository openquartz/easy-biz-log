package com.openquartz.easybizlog.core.service.impl;

import com.openquartz.easybizlog.common.beans.Operator;
import com.openquartz.easybizlog.core.service.IOperatorGetService;

/**
 * @author svnee
 */
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    @Override
    public Operator getUser() {
        Operator operator = new Operator();
        operator.setOperatorId("sys");
        return operator;
    }
}
