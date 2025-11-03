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
    
    /**
     * 邮箱授权码（从外部文件加载）
     */
    private String password;
    
    /**
     * 获取邮箱授权码
     * @return 邮箱授权码
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置邮箱授权码
     * @param password 邮箱授权码
     */
    public void setPassword(String password) {
        this.password = password;
    }
}