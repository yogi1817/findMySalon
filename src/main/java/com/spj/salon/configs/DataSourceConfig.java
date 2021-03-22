package com.spj.salon.configs;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yogesh Sharma
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataSourceConfig {

    private final EnvironmentConfig envConfig;
    private final ServiceConfig serviceConfig;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.username(envConfig.getDatasourceUsername());
        dataSourceBuilder.password(envConfig.getDatasourcePassword());
        dataSourceBuilder.url(serviceConfig.getDatabaseUrl());
        dataSourceBuilder.driverClassName(serviceConfig.getDriverClassName());

        log.debug("Connection established");
        return dataSourceBuilder.build();
    }
}
