package com.life.jigsaw.config;

import org.springframework.stereotype.Component;

/**
 * API配置类
 * 统一管理API路径和版本信息
 */
@Component
public class ApiConfig {
    
    // API基础路径
    public static final String API_BASE_PATH = "/api/puzzle";
    
    // API版本号
    public static final String API_VERSION = "/v2";
    
    // API完整基础路径
    public static final String API_FULL_BASE_PATH = API_BASE_PATH + API_VERSION;
    
    // 公开接口路径（相对于上下文路径）
    public static final String PUBLIC_PATH_LOGIN = "/user/login";
    public static final String PUBLIC_PATH_ADD_USER = "/user/add";
    public static final String PUBLIC_PATH_SEND_EMAIL_CODE = "/user/sendEmailCode";
    
    // Swagger路径
    public static final String SWAGGER_PATH = "/swagger-ui/**";
    public static final String SWAGGER_API_DOCS_PATH = "/v3/api-docs/**";
}