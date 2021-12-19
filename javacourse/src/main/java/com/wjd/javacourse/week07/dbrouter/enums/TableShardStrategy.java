package com.wjd.javacourse.week07.dbrouter.enums;


public enum TableShardStrategy {

    UIDSN(TableShardDisposeType.MutiParamMutiTail.getCode(),"uid", "sn", new String[]{"other_biz_extend","other_biz_orders"});

    // 分表计算方式
    private int strategy;

    // 分表code
    private String shardCode;

    // 分表code2 sn分表规则 substring(2,4)
    private String sencondShardCode;

    // 分表表名
    private String[] shardTableList;

    TableShardStrategy(int strategy,String[] shardTableList) {
        this.strategy = strategy;
        this.shardTableList = shardTableList;
    }

    TableShardStrategy(int strategy, String shardCode, String[] shardTableList) {
        this.strategy = strategy;
        this.shardCode = shardCode;
        this.shardTableList = shardTableList;
    }

    TableShardStrategy(int strategy, String shardCode, String sencondShardCode, String[] shardTableList) {
        this.strategy = strategy;
        this.shardCode = shardCode;
        this.sencondShardCode = sencondShardCode;
        this.shardTableList = shardTableList;
    }

    public String getShardCode() {
        return shardCode;
    }

    public void setShardCode(String shardCode) {
        this.shardCode = shardCode;
    }

    public String[] getShardTableList() {
        return shardTableList;
    }

    public void setShardTableList(String[] shardTableList) {
        this.shardTableList = shardTableList;
    }

    public String getSencondShardCode() {
        return sencondShardCode;
    }

    public void setSencondShardCode(String sencondShardCode) {
        this.sencondShardCode = sencondShardCode;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }
}