package com.openquartz.logserver.service.impl;

import com.openquartz.easybizlog.core.annotation.LogRecord;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import com.openquartz.logserver.service.SkuService;
import com.openquartz.logserver.infrastructure.constants.LogRecordType;
import com.openquartz.logserver.pojo.ObjectSku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author svnee
 **/
@Slf4j
@Service
public class SkuServiceImpl implements SkuService {
    @Resource
    private ILogRecordService logRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(
            fail = "创建SKU失败，失败原因：「{{#_errorMsg}}」",
            subType = LogRecordType.SKU,
            extra = "{{#sku.toString()}}",
            success = "新增SKU名称为{{#sku.skuName}}",
            type = LogRecordType.SKU, bizNo = "{{#sku.code}}")
    public Long createObjectSkuNoJoinTransaction(ObjectSku sku) {
        com.openquartz.easybizlog.common.beans.LogRecord logRecord = getLogRecord(sku);
        logRecordService.recordLog(logRecord);
        return null;
    }

    private com.openquartz.easybizlog.common.beans.LogRecord getLogRecord(ObjectSku sku) {
        com.openquartz.easybizlog.common.beans.LogRecord logRecord = new com.openquartz.easybizlog.common.beans.LogRecord();
        logRecord.setTenant("test");
        logRecord.setType(LogRecordType.SKU);
        logRecord.setSubType(LogRecordType.SKU);
        logRecord.setBizNo(sku.getCode());
        logRecord.setOperator("operator");
        logRecord.setAction("新增SKU名称为" + sku.getSkuName());
        logRecord.setCreateTime(new Date());
        logRecord.setExtra(sku.getRemark());
        return logRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(
            fail = "创建SKU失败，失败原因：「{{#_errorMsg}}」",
            subType = LogRecordType.SKU,
            extra = "{{#sku.toString()}}",
            success = "新增SKU名称为{{#sku.skuName}}",
            type = LogRecordType.SKU, bizNo = "{{#sku.code}}")
    public Long createObjectBusinessError(ObjectSku sku) {
        com.openquartz.easybizlog.common.beans.LogRecord logRecord = getLogRecord(sku);
        logRecordService.recordLog(logRecord);
        int i = 1 / 0;
        return null;
    }

    @Override
    @LogRecord(
            fail = "创建SKU失败，失败原因：「{{#_errorMsg}}」",
            subType = LogRecordType.SKU,
            extra = "{{#sku.toString()}}",
            success = "新增SKU名称为{{#sku.skuName}}",
            type = LogRecordType.SKU, bizNo = "{{#sku.code}}")
    public Long createObjectBusinessError2(ObjectSku sku) {
        com.openquartz.easybizlog.common.beans.LogRecord logRecord = getLogRecord(sku);
        logRecordService.recordLog(logRecord);
        int i = 1 / 0;
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(
            fail = "创建SKU失败，失败原因：「{{#_errorMsg}}」",
            subType = LogRecordType.SKU,
            extra = "{{#sku.getRemark()}}",
            success = "新增SKU名称为{{#sku.skuName}}",
            type = LogRecordType.SKU, bizNo = "{{#sku.code}}")
    public Long createObjectSkuNoJoinTransactionRollBack(ObjectSku sku) {
        com.openquartz.easybizlog.common.beans.LogRecord logRecord = getLogRecord(sku);
        logRecord.setExtra("不回滚");
        logRecordService.recordLog(logRecord);
        return null;
    }
}
