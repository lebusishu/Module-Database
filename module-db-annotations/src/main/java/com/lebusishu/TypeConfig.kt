package com.lebusishu

/**
 * Description : 注解类型
 * Create by wxh on 11/10/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
interface TypeConfig {
    companion object {
        /*
        创建表
         */
        const val TYPE_DB_CREATE_TABLE: Int = 1

        /*
        数据库版本
         */
        const val TYPE_DB_VERSION: Int = 2

        /*
        发生变化的表
         */
        const val TYPE_DB_TABLE_UPDATE: Int = 3

        /*
        删除的表
         */
        const val TYPE_DB_TABLE_DELETE: Int = 4

        /*
        数据库路径
         */
        const val TYPE_DB_PATH: Int = 5
    }
}