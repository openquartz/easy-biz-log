package com.openquartz.easybizlog.storage.mongodb.mapper;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.openquartz.easybizlog.common.utils.BeanUtils;
import com.openquartz.easybizlog.storage.api.model.LogRecordDO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
public class LogRecordMapperImpl implements LogRecordMapper {

    private final MongoClient mongoClient;
    private final String database;
    private final String collection;

    public LogRecordMapperImpl(MongoClient mongoClient, String database, String collection) {
        this.mongoClient = mongoClient;
        this.database = database;
        this.collection = collection;
    }

    @Override
    public void save(LogRecordDO entity) {

        if (Objects.isNull(entity)) {
            return;
        }

        Document doc = new Document(BeanUtils.objectToMap(entity));
        mongoClient
            .getDatabase(database)
            .getCollection(collection)
            .insertOne(doc);
    }

    @Override
    public List<LogRecordDO> queryLog(String bizNo, String type) {

        FindIterable<Document> documents = mongoClient
            .getDatabase(database)
            .getCollection(collection)
            .find(Filters.and(Filters.eq("bizNo", bizNo), Filters.eq("type", type)));

        List<LogRecordDO> resultList = new ArrayList<>();
        for (Document document : documents) {
            resultList.add(BeanUtils.mapToObject(document, LogRecordDO.class));
        }

        return resultList;
    }

    @Override
    public List<LogRecordDO> queryLog(String bizNo, String type, String subType) {
        FindIterable<Document> documents = mongoClient
            .getDatabase(database)
            .getCollection(collection)
            .find(Filters.and(Filters.eq("bizNo", bizNo), Filters.eq("type", type), Filters.eq("subType", subType)));

        List<LogRecordDO> resultList = new ArrayList<>();
        for (Document document : documents) {
            resultList.add(BeanUtils.mapToObject(document, LogRecordDO.class));
        }

        return resultList;
    }
}
