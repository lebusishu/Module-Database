package com.lebusishu.db

import com.lebusishu.TypeConfig
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
    @ModuleDBVariable(value = "1", type = TypeConfig.TYPE_DB_VERSION)
    private val dbVersion = 0

    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " NAME2 TEXT ," +
                " NAME3 TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = TypeConfig.TYPE_DB_CREATE_TABLE
    )
    private val createDbTable = ""
    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION1 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = TypeConfig.TYPE_DB_CREATE_TABLE
    )
    private val createDbTable1 = ""

    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION2 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = TypeConfig.TYPE_DB_CREATE_TABLE
    )
    private val createDbTable2 = ""

    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION3 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = TypeConfig.TYPE_DB_CREATE_TABLE
    )
    private val createDbTable3 = ""

    @ModuleDBVariable(value = "DB_VERSION,DB_VERSION2", type = TypeConfig.TYPE_DB_TABLE_UPDATE)
    private val updateTables = ""

    @ModuleDBVariable(value = "DB_VERSION3", type = TypeConfig.TYPE_DB_TABLE_DELETE)
    private val deleteTables = ""
}