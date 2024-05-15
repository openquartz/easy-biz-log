package com.openquartz.easybizlog.storage.jdbc.model;

import com.openquartz.easybizlog.common.beans.CodeVariableType;
import com.openquartz.easybizlog.common.beans.LogRecord;
import com.openquartz.easybizlog.common.serde.JSONUtil;
import com.openquartz.easybizlog.common.serde.json.TypeReference;
import com.openquartz.easybizlog.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

@Data
public class LogRecordDO {

    /**
     * id
     */
    private Long id;
    /**
     * 租户
     */
    private String tenant;

    /**
     * 保存的操作日志的类型，比如：订单类型、商品类型
     */
    @NotBlank(message = "type required")
    @Length(max = 200, message = "type max length is 200")
    private String type;
    /**
     * 日志的子类型，比如订单的C端日志，和订单的B端日志，type都是订单类型，但是子类型不一样
     */
    private String subType;

    /**
     * 日志绑定的业务标识
     */
    @NotBlank(message = "bizNo required")
    @Length(max = 200, message = "bizNo max length is 200")
    private String bizNo;
    /**
     * 操作人
     */
    @NotBlank(message = "operator required")
    @Length(max = 63, message = "operator max length 63")
    private String operator;

    /**
     * 日志内容
     */
    @NotBlank(message = "opAction required")
    @Length(max = 511, message = "operator max length 511")
    private String action;
    /**
     * 记录是否是操作失败的日志
     */
    private boolean fail;
    /**
     * 日志的创建时间
     */
    private Date createTime;
    /**
     * 日志的额外信息
     */
    private String extra;

    private String codeVariable;

    public static LogRecordDO from(LogRecord logRecord) {
        LogRecordDO logRecordPO = new LogRecordDO();
        BeanUtils.copyProperties(logRecord, logRecordPO);
        logRecordPO.setCodeVariable(JSONUtil.toJson(logRecord.getCodeVariable()));
        return logRecordPO;
    }

    public static List<LogRecord> from(List<LogRecordDO> logRecordPOS) {
        List<LogRecord> ret = new ArrayList<>();
        for (LogRecordDO logRecordPO : logRecordPOS) {
            ret.add(toLogRecord(logRecordPO));
        }
        return ret;
    }

    private static LogRecord toLogRecord(LogRecordDO logRecordPO) {
        LogRecord logRecord = new LogRecord();
        BeanUtils.copyProperties(logRecordPO, logRecord);
        if (StringUtils.isNotBlank(logRecordPO.getCodeVariable())) {
            Map<CodeVariableType, Object> toBean = JSONUtil.parseObject(logRecordPO.getCodeVariable(),
                new TypeReference<Map<CodeVariableType, Object>>() {
                });
            logRecord.setCodeVariable(toBean);
        }
        return logRecord;
    }
}
