package com.wjd.javacourse.week07.dbrouter.enums;

public enum TableShardDisposeType {

    SingleParamSingleTail(1, "生成分表名需要单个字段(例如uid)，生成单个尾数,例如：tableName_1"),
    MutiParamSingleTail(2, "生成分表名需要2个字段(例如uid+sn)，生成单个尾数,例如：tableName_1"),
    SingleParamMutiTail(3, "生成分表名需要单个字段(例如uid)，生成2个尾数,例如：tableName_01"),
    MutiParamMutiTail(4, "生成分表名需要2个字段(例如uid+sn)，生成2个尾数,例如：tableName_01"),
    Date(5, "日期结尾,例如：tableName_202101");

    private Integer code;

    private String name;

    TableShardDisposeType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}