package com.openquartz.easybizlog.starter.support;

import com.openquartz.easybizlog.common.beans.LogRecord;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultLogRecordServiceImpl implements ILogRecordService {

    @Override
    public void record(LogRecord logRecord) {
        log.info("[LogRecordService] record:{}", logRecord);
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        return Collections.emptyList();
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        return Collections.emptyList();
    }
}
