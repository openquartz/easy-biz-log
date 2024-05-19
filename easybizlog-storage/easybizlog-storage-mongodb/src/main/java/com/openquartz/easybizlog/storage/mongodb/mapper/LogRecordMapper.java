package com.openquartz.easybizlog.storage.mongodb.mapper;

import com.openquartz.easybizlog.storage.api.model.LogRecordDO;
import java.util.List;

/**
 * log record mapper
 * @author svnee
 */
public interface LogRecordMapper {

    void save(LogRecordDO logRecordDO);

    List<LogRecordDO> queryLog(String bizNo, String type);

    List<LogRecordDO> queryLog(String bizNo, String type, String subType);

}
