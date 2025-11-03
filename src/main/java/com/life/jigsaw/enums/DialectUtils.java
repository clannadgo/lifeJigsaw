package com.life.jigsaw.enums;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceWrapper;
import com.life.jigsaw.exceptions.ServiceException;

import javax.sql.DataSource;

public class DialectUtils {
    public final static String SQLITE_JDBC = "org.sqlite.JDBC";
    public final static String MYSQL_JDBC = "com.mysql.cj.jdbc.Driver";


    public final static String SQLITE_DIALECT = "org.hibernate.community.dialect.SQLiteDialect";
    public final static String MYSQL_DIALECT = "org.hibernate.dialect.MySQLDialect";



    public static String getHibernateDialect(DataSource dataSource) {
        String driverName = ((DruidDataSourceWrapper) dataSource).getDriverClassName();
        if (SQLITE_JDBC.equals(driverName)) {
            return SQLITE_DIALECT;
        } else if (MYSQL_JDBC.equals(driverName)) {
            return MYSQL_DIALECT;
        }
        throw new ServiceException("不支持的数据库类型: " + driverName);
    }

}
