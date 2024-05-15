package com.openquartz.easybizlog.common.beans;

import lombok.Builder;
import lombok.Data;

/**
 * @author svnee
 */
@Data
@Builder
public class LogRecordOps {
    private String successLogTemplate;
    private String failLogTemplate;
    private String operatorId;
    private String type;
    private String bizNo;
    private String subType;
    private String extra;
    private String condition;
    private String isSuccess;
}
