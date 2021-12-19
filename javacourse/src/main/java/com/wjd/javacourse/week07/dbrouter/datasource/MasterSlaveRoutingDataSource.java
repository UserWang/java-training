package com.wjd.javacourse.week07.dbrouter.datasource;

import com.wjd.javacourse.week07.dbrouter.DbContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class MasterSlaveRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        if(DbContextHolder.getDbType() == null){
            return DbContextHolder.DbType.MASTER;
        }
        return DbContextHolder.getDbType();
    }
}