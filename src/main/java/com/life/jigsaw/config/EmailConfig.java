package com.life.jigsaw.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import lombok.Data;

/**
 * 邮箱配置类
 * 从email.properties加载敏感配置信息
 */
@Configuration
@PropertySource("classpath:email.properties")
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class EmailConfig {
    private String password;

}