package com.openquartz.logserver.aop;

import com.openquartz.easybizlog.common.context.LogRecordContext;
import com.openquartz.easybizlog.core.service.impl.DiffParseFunction;
import com.openquartz.logserver.pojo.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author svnee
 */
@Aspect
@Component
public class UserBeanAspect {

    @Pointcut(value = "execution(* com.openquartz.logserver.service.impl.UserServiceImpl.testGlobalVariableDiff(..))")
    private void myPointCut() {
    }

    @Before(value = "myPointCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setSex("男");
        user.setAge(18);
        User.Address address = new User.Address();
        address.setProvinceName("湖北省");
        address.setCityName("武汉市");
        user.setAddress(address);
        LogRecordContext.putGlobalVariable(DiffParseFunction.OLD_OBJECT, user);
    }
}
