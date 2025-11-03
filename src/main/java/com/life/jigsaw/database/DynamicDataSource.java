package com.life.jigsaw.database;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 *
 */
@Log4j2
public class DynamicDataSource extends AbstractRoutingDataSource
{
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources)
    {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey()
    {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

    // 动态添加数据源
    public void addDataSource(String key, DataSource dataSource) {
        // 更新 targetDataSources
        Map<Object, Object> targetDataSources = new HashMap<>(getResolvedDataSources());
        targetDataSources.put(key, dataSource);

        // 更新配置
        setTargetDataSources(targetDataSources);
        afterPropertiesSet(); // 重新初始化 resolvedDataSources
    }

    // 动态删除数据源
    public void removeDataSource(String key) {
        // 更新 targetDataSources
        Map<Object, Object> targetDataSources = new HashMap<>(getResolvedDataSources());
        targetDataSources.remove(key);

        // 更新配置
        setTargetDataSources(targetDataSources);
        afterPropertiesSet(); // 重新初始化 resolvedDataSources
    }

    // 判断数据源是否存在
    public boolean containsDataSource(String key) {
        return getResolvedDataSources().containsKey(key);
    }

    // 获取数据源
    public DataSource getDataSource(String key) {
        return getResolvedDataSources().get(key);
    }
}