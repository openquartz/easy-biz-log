package com.openquartz.easybizlog.core.service;

import com.openquartz.easybizlog.common.beans.Operator;

/**
 * @author svnee
 */
public interface IOperatorGetService {

    /**
     * 可以在里面外部的获取当前登陆的用户，比如UserContext.getCurrentUser()
     *
     * @return 转换成Operator返回
     */
    Operator getUser();
}
