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
 * 处理sql中的关键字
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
        if (driverClassName.contains("mysql")) {  //mysql 中 Key 是关键字，需要加反单引号，我们不少表中使用了Key 当字段名
            //正则含义： 仅当Key前后不是字母和数字和下划线时匹配Key，并将Key 替换成`Key`
            originalSql = originalSql.replaceAll("(?<![a-zA-Z0-9_])Key(?![a-zA-Z0-9_])","`Key`")
                    .replaceAll("(?<![a-zA-Z0-9_])Character(?![a-zA-Z0-9_])","`Character`");
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
