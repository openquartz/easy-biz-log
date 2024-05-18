CREATE TABLE `ebl_biz_log`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `tenant`        varchar(63)   NOT NULL DEFAULT '' COMMENT '租户标识',
    `type`          varchar(63)   NOT NULL DEFAULT '' COMMENT '保存的操作日志的类型，比如：订单类型、商品类型',
    `sub_type`      varchar(63)   NOT NULL DEFAULT '' COMMENT '日志的子类型，比如订单的C端日志，和订单的B端日志，type都是订单类型，但是子类型不一样',
    `biz_no`        varchar(63)   NOT NULL DEFAULT '' COMMENT '日志绑定的业务标识',
    `operator`      varchar(63)   NOT NULL DEFAULT '' COMMENT '操作人',
    `action`        varchar(1023) NOT NULL DEFAULT '' COMMENT '日志内容',
    `fail`          tinyint unsigned NOT NULL DEFAULT '0' COMMENT '记录是否是操作失败的日志',
    `create_time`   datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `extra`         varchar(2000) NOT NULL DEFAULT '' COMMENT '扩展信息',
    `code_variable` varchar(2000) NOT NULL DEFAULT '' COMMENT '代码变量信息',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '业务操作日志';