package com.life.jigsaw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动服务后 输入配置信息
 */
@Component
@Slf4j
public class PrintApplicationConfig {
    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.datasource.driverClassName:none}")
    private String dbDriver;

    @Value("${spring.datasource.druid.master.url:none}")
    private String dbUrl;

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public CommandLineRunner commandLineRunner() {
        return args -> {
            // 在这里打印日志
            log.info("--------------- 【{}】 start success!  -------------",applicationName);
            log.info("服务端口：{}  contextPath:{}",serverPort,contextPath);
            log.info("swagger3-ui: http://127.0.0.1:{}{}/swagger-ui/index.html", serverPort,contextPath);
            // log.info("dbDriver: {}",dbDriver);
            log.info("Database INFO: {}", dbUrl.split("\\?")[0]);
        };
    }


}
