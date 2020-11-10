package com.lebusishu.db

import com.lebusishu.annotations.ModuleDBConfig
import com.lebusishu.annotations.ModuleDBVariable

/**
 * Description : 数据库信息
 * Create by wxh on 11/9/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
@ModuleDBConfig(dbName = "demo1")
class DBConfig {
    @ModuleDBVariable(value = "1", type = 2)
    private val dbVersion = 1

    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION1 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = 1
    )
    private val createDbTable1 = ""
    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION2 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = 1
    )
    private val createDbTable2 = ""
    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION3 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = 1
    )
    private val createDbTable3 = ""
}