package com.openquartz.easybizlog.storage.mongodb;

import com.openquartz.easybizlog.common.beans.LogRecord;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import com.openquartz.easybizlog.storage.api.model.LogRecordDO;
import com.openquartz.easybizlog.storage.mongodb.mapper.LogRecordMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MongoDbLogRecordService implements ILogRecordService {

    private final LogRecordMapper logRecordMapper;

    @Override
    public void recordLog(LogRecord logRecord) {
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
