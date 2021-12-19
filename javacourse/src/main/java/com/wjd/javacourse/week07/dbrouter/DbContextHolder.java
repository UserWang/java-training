package com.wjd.javacourse.week07.dbrouter;

public class DbContextHolder {

    public enum DbType {
        MASTER, SLAVE
    }

    public enum BUSINESS_TYPE {
        PPD, OPEN
    }

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();
    private static final ThreadLocal<BUSINESS_TYPE> busiContextHolder = new ThreadLocal<>();

    public static void setDbType(DbType dbType) {
        if (dbType == null){
            throw new NullPointerException();
        }
        contextHolder.set(dbType);
    }

    public static DbType getDbType() {
        return contextHolder.get() == null ? DbType.MASTER : contextHolder.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDbType() {
        contextHolder.remove();
    }

    /**
     * 清除业务类型
     */
    public static void clearBizType() {
        busiContextHolder.remove();
    }

    /**
     * 全部清除
     */
    public static void clearAll(){
        clearDbType();
        clearBizType();
    }

    public static void setBusiType(BUSINESS_TYPE type) {
        if (type == null){
            throw new NullPointerException();
        }
        busiContextHolder.set(type);
    }

    public static BUSINESS_TYPE getBusiType() {
        return busiContextHolder.get();
    }

}

