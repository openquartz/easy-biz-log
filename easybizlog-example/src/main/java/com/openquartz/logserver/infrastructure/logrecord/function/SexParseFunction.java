package com.openquartz.logserver.infrastructure.logrecord.function;

import com.openquartz.easybizlog.core.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author svnee
 **/
@Slf4j
@Component
public class SexParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return false;
    }

    @Override
    public String functionName() {
        return "SEX";
    }

    @Override
    public String apply(Object value) {
        return StringUtils.isEmpty(value) ? "" : value + "333";
    }
}
