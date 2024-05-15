package com.openquartz.easybizlog.storage.jdbc.mapper;

import com.openquartz.easybizlog.storage.jdbc.model.LogRecordDO;
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
