package com.wjd.javacourse.week07.dbrouter.annotation;

import com.wjd.javacourse.week07.dbrouter.enums.TableShardStrategy;

import java.lang.annotation.*;

@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TableSeg {

    /**
     * 分表方式，取模，如%4：表示取4余数，
     * 如果不设置，直接根据shardNum值分表
     * @return
     */
    int shardNum();

    /**
     * 根据什么字段分表
     * @return
     */
    TableShardStrategy shardBy();
}

