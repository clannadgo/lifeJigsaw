package com.life.jigsaw.database;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

/**
 * 处理SQL中的关键字，适配多数据库类型
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Component
@Slf4j
public class MybatisKeyWordInterceptor implements Interceptor {
    @Value("${spring.datasource.druid.master.driver-class-name:}")
    private String driverClassName;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = org.apache.ibatis.reflection.SystemMetaObject.forObject(statementHandler);

        // 获取原始SQL
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();

        // 根据数据库类型对SQL进行处理
        if (driverClassName.contains("mysql") || driverClassName.contains("mariadb")) {
            // MySQL/MariaDB 关键字处理：使用反单引号
            originalSql = originalSql.replaceAll("(?<![a-zA-Z0-9_])Key(?![a-zA-Z0-9_])","`Key`")
                    .replaceAll("(?<![a-zA-Z0-9_])Character(?![a-zA-Z0-9_])","`Character`");
        } else if (driverClassName.contains("sqlite")) {
            // SQLite 关键字处理：使用双引号
            originalSql = originalSql.replaceAll("(?<![a-zA-Z0-9_])Key(?![a-zA-Z0-9_])","\"Key\"")
                    .replaceAll("(?<![a-zA-Z0-9_])Character(?![a-zA-Z0-9_])","\"Character\"");
        }
        
        // 设置修改后的SQL
        metaObject.setValue("delegate.boundSql.sql", originalSql);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
