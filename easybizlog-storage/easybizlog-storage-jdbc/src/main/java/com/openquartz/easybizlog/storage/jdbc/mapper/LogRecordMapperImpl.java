package com.openquartz.easybizlog.storage.jdbc.mapper;

import com.openquartz.easybizlog.storage.jdbc.CustomerJdbcTemplate;
import com.openquartz.easybizlog.storage.jdbc.model.LogRecordDO;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.NonNull;

@Slf4j
public class LogRecordMapperImpl implements LogRecordMapper {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerJdbcTemplate customerJdbcTemplate;

    /**
     * GeneratedKey
     */
    public static final String GENERATED_KEY = "GENERATED_KEY";

    private static final String INSERT_SQL = "insert into t_biz_log(biz_no,type,action,operator,fail,extra,sub_type,code_variable,tenant,create_time) values(?,?,?,?,?,?,?,?,?,?)";

    private static final String SELECT_BIZ_NO_TYPE_SQL = "select id,biz_no,type,action,operator,fail,extra,sub_type,code_variable,tenant,create_time from t_biz_log where biz_no = ? and type = ?";

    private static final String SELECT_BIZ_NO_TYPE_SUB_TYPE_SQL = "select id,biz_no,type,action,operator,fail,extra,sub_type,code_variable,tenant,create_time from t_biz_log where biz_no = ? and type = ? and sub_type = ?";

    public LogRecordMapperImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerJdbcTemplate = new CustomerJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void save(LogRecordDO entity) {

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        customerJdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, entity.getBizNo());
                ps.setString(2, entity.getType());
                ps.setString(3, entity.getAction());
                ps.setString(4, entity.getOperator());
                ps.setInt(5, entity.isFail() ? 1 : 0);
                ps.setString(6, entity.getExtra());
                ps.setString(7, entity.getSubType());
                ps.setString(8, entity.getCodeVariable());
                ps.setString(9, entity.getTenant());
                ps.setDate(10, new Date(entity.getCreateTime().getTime()));
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        }, generatedKeyHolder);

        List<Map<String, Object>> objectMapList = generatedKeyHolder.getKeyList();

        Map<String, Object> map = objectMapList.get(0);
        Long id = ((Number) map.get(GENERATED_KEY)).longValue();
        entity.setId(id);
    }

    @Override
    public List<LogRecordDO> queryLog(String bizNo, String type) {

        return jdbcTemplate.query(SELECT_BIZ_NO_TYPE_SQL,
            (rs, rowNum) -> buildLogRecordDO(rs), bizNo, type);
    }

    @Override
    public List<LogRecordDO> queryLog(String bizNo, String type, String subType) {
        return jdbcTemplate.query(SELECT_BIZ_NO_TYPE_SUB_TYPE_SQL,
            (rs, rowNum) -> buildLogRecordDO(rs), bizNo, type, subType);
    }

    private static LogRecordDO buildLogRecordDO(ResultSet rs) throws SQLException {
        LogRecordDO logRecordDO = new LogRecordDO();
        logRecordDO.setBizNo(rs.getString("biz_no"));
        logRecordDO.setType(rs.getString("type"));
        logRecordDO.setAction(rs.getString("action"));
        logRecordDO.setOperator(rs.getString("operator"));
        logRecordDO.setFail(rs.getInt("fail") == 1);
        logRecordDO.setExtra(rs.getString("extra"));
        logRecordDO.setSubType(rs.getString("sub_type"));
        logRecordDO.setCodeVariable(rs.getString("code_variable"));
        logRecordDO.setTenant(rs.getString("tenant"));
        logRecordDO.setCreateTime(rs.getDate("create_time"));
        logRecordDO.setId(rs.getLong("id"));
        return logRecordDO;
    }
}