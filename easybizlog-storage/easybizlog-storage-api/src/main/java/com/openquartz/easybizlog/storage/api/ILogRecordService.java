package com.openquartz.easybizlog.storage.api;

import com.openquartz.easybizlog.common.beans.LogRecord;
import java.util.List;

/**
 * 日志记录服务
 * @author svnee
 */
public interface ILogRecordService {

    /**
     * 保存log
     *
     * @param logRecord 日志实体
     */
    void recordLog(LogRecord logRecord);

    /**
     * 返回最多100条记录
     *
     * @param type  操作日志类型
     * @param bizNo 操作日志的业务标识，比如：订单号
     * @return 操作日志列表
     */
    List<LogRecord> queryLog(String bizNo, String type);

    /**
     * 返回最多100条记录
     *
     * @param type    操作日志类型
     * @param subType 操作日志子类型
     * @param bizNo   操作日志的业务标识，比如：订单号
     * @return 操作日志列表
     */
    List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType);

    /**
     * clean method
     */
    default void clean() {
    }
}
