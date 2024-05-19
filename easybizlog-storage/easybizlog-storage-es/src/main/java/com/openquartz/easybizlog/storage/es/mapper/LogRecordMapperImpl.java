package com.openquartz.easybizlog.storage.es.mapper;

import com.openquartz.easybizlog.common.serde.JSONUtil;
import com.openquartz.easybizlog.storage.api.model.LogRecordDO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

@Slf4j
public class LogRecordMapperImpl implements LogRecordMapper {

    private final RestHighLevelClient client;
    private final String index;

    public LogRecordMapperImpl(RestHighLevelClient client, String index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void save(LogRecordDO logRecordDO) {

        try {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest
                .id(String.valueOf(logRecordDO.getId()))
                .source(logRecordDO.serialize(logRecordDO), XContentType.JSON);
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("[LogRecordMapper#save]save error", ex);
        }
    }

    @Override
    public List<LogRecordDO> queryLog(String bizNo, String type) {

        try {

            QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("bizNo", bizNo))
                .must(QueryBuilders.termQuery("type", type));

            return queryLog(queryBuilder);
        } catch (Exception e) {
            log.error("[LogRecordServiceImpl#queryLog] queryLog is failed,exception occurred.", e);
        }
        return null;
    }

    private List<LogRecordDO> queryLog(QueryBuilder queryBuilder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(new SearchSourceBuilder().query(queryBuilder));

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        List<LogRecordDO> resultList = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            LogRecordDO logRecordDO = JSONUtil.parseObject(searchHit.getSourceAsString(), LogRecordDO.class);
            if (Objects.isNull(logRecordDO)) {
                continue;
            }
            resultList.add(logRecordDO);
        }
        return resultList;
    }

    @Override
    public List<LogRecordDO> queryLog(String bizNo, String type, String subType) {

        try {

            QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("bizNo", bizNo))
                .must(QueryBuilders.termQuery("type", type))
                .must(QueryBuilders.termQuery("subType", subType));

            return queryLog(queryBuilder);
        } catch (Exception ex) {
            log.error("[LogRecordServiceImpl#queryLog] queryLog is failed,exception occurred.", ex);
        }
        return null;
    }
}
