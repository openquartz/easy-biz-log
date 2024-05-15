package com.openquartz.logserver.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openquartz.logserver.repository.po.LogRecordPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogRecordMapper extends BaseMapper<LogRecordPO> {
}
