package com.openquartz.easybizlog.storage.jdbc;

import com.openquartz.easybizlog.common.beans.LogRecord;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import com.openquartz.easybizlog.storage.jdbc.mapper.LogRecordMapper;
import com.openquartz.easybizlog.storage.jdbc.model.LogRecordDO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JdbcLogRecordService implements ILogRecordService {

    private final LogRecordMapper logRecordMapper;

    @Override
    public void record(LogRecord logRecord) {
        logRecordMapper.save(LogRecordDO.from(logRecord));
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        List<LogRecordDO> logRecordPOS = logRecordMapper.queryLog(bizNo, type);
        return LogRecordDO.from(logRecordPOS);
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        List<LogRecordDO> logRecordPOS = logRecordMapper.queryLog(bizNo, type, subType);
        return LogRecordDO.from(logRecordPOS);
    }
}
