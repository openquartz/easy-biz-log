package com.openquartz.easybizlog.core.service.impl;

import com.openquartz.easybizlog.core.service.ILogRecordPerformanceMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * @author svnee
 */
@Slf4j
public class DefaultLogRecordPerformanceMonitor implements ILogRecordPerformanceMonitor {

    @Override
    public void print(StopWatch stopWatch) {
        log.debug("LogRecord performance={}", stopWatch.prettyPrint());
    }
}
