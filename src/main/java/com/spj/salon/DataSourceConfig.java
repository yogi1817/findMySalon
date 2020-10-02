package com.spj.salon;

import com.spj.salon.config.EnvironmentConfig;
import com.spj.salon.config.ServiceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Yogesh Sharma
 */
@Configuration
@Slf4j
public class DataSourceConfig {

    @Autowired
    private EnvironmentConfig envConfig;

    @Autowired
    private ServiceConfig serviceConfig;

    @Bean
    public DataSource getDataDource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.username(envConfig.getDatasourceUsername());
        dataSourceBuilder.password(envConfig.getDatasourcePassword());
        dataSourceBuilder.url(serviceConfig.getDatabaseUrl());
        dataSourceBuilder.driverClassName(serviceConfig.getDriverClassName());

        log.debug("Connection stablished");
        return dataSourceBuilder.build();
    }
}
