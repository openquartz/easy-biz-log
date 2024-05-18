package com.openquartz.logserver;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.openquartz.easybizlog.starter.annotation.EnableLogRecord;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {DataSourceTransactionManagerAutoConfiguration.class})
@MapperScan(basePackages = "com.openquartz.logserver.repository.mapper", annotationClass = Mapper.class)
@EnableLogRecord(tenant = "OpenQuartz", joinTransaction = true)
@EnableTransactionManagement(order = 0)
@EnableSpringUtil
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
