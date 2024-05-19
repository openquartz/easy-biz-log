package com.openquartz.easybizlog.storage.mongodb.mapper;

import com.mongodb.client.MongoClient;
import com.openquartz.easybizlog.storage.api.model.LogRecordDO;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogRecordMapperImpl implements LogRecordMapper {

    private final MongoClient mongoClient;

    public LogRecordMapperImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void save(LogRecordDO entity) {

    }

    @Override
    public List<LogRecordDO> queryLog(String bizNo, String type) {
        return Collections.emptyList();
    }

    @Override
    public List<LogRecordDO> queryLog(String bizNo, String type, String subType) {
        return Collections.emptyList();
    }
}
