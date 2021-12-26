package com.wjd.javacourse.week08.util;/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */
public class ShardingDatabasesAndTableConfiguration {
    //创建两个数据源
    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put("mydb_01", NewJDBCUtils.createDataSource("url01"));
        dataSourceMap.put("mydb_00", NewJDBCUtils.createDataSource("url00"));
        return dataSourceMap;
    }

    private static ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration configuration = new ShardingRuleConfiguration();
        ShardingTableRuleConfiguration tableRuleConfiguration = getTableRuleConfiguration();
        configuration.getTables().add(tableRuleConfiguration);
        /**
         * 设置数据库的分片规则
         * inline表示行表达式分片算法
         */
        configuration.setDefaultDatabaseShardingStrategy(
                new StandardShardingStrategyConfiguration("uid", "inline"));
        /**
         * 设置表的分片规则
         */
        configuration.setDefaultTableShardingStrategy(
                new StandardShardingStrategyConfiguration("uid", "uid_inline"));
        Properties props = new Properties();
        props.setProperty("algorithm-expression", "mydb_0${uid%2}"); //表示根据uid取模得到目标库
        /**
         * 定义具体的分片规则算法，用于提供分库分表的算法规则
         */
        configuration.getShardingAlgorithms().put("inline", new ShardingSphereAlgorithmConfiguration("INLINE", props));
        Properties properties = new Properties();
        properties.setProperty("algorithm-expression", "user_info_0${uid%10}");
        configuration.getShardingAlgorithms().put("uid_inline", new ShardingSphereAlgorithmConfiguration("INLINE", properties));
//        configuration.getKeyGenerators().put("snowflake", new ShardingSphereAlgorithmConfiguration("SNOWFLAKE", getProperties()));
        return configuration;
    }

    //用户表的分片规则
    private static ShardingTableRuleConfiguration getTableRuleConfiguration() {
        ShardingTableRuleConfiguration tableRule = new ShardingTableRuleConfiguration("user_info", "mydb_0${0..1}.user_info_0${0..9}");
//        tableRule.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("uid", "snowflake"));
        return tableRule;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("worker-id", "123");
        return properties;
    }

    public static DataSource getDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = createDataSourceMap();
        System.out.println("----dataSourceMap---");
        System.out.println(dataSourceMap);
        ShardingRuleConfiguration shardingRuleConfiguration = createShardingRuleConfiguration();
        System.out.println("----shardingRuleConfiguration---");
        System.out.println(shardingRuleConfiguration);
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(shardingRuleConfiguration), new Properties());
    }

}
