package com.life.jigsaw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LifeJigsawApplication {

    public static void main(String[] args) {
        // 日志由 Spring Boot 自动管理，无需手动初始化
        SpringApplication.run(LifeJigsawApplication.class, args);
    }
}