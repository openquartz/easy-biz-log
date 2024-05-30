package com.openquartz.logserver.function;

import com.openquartz.easybizlog.core.service.IParseFunction;
import com.openquartz.logserver.pojo.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author svnee
 */
@Slf4j
@Component
public class OrderBeforeParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "ORDER_BEFORE";
    }

    @Override
    public String apply(Object value) {
        log.info("@@@@@@@@");
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        log.info("###########,{}", value);
        Order order = new Order();
        order.setProductName("xxxx");
        return order.getProductName().concat("(").concat(value.toString()).concat(")");
    }
}
