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
@ModuleDBConfig(dbName = "demo2", dbVersion = 2)
class DBConfig2 {
    @ModuleDBVariable(value = "./", type = TypeConfig.TYPE_DB_PATH)
    private val dbPath = ""

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

    @ModuleDBVariable(
        value = "1:DB_VERSION,DB_VERSION1;2:DB_VERSION2",
        type = TypeConfig.TYPE_DB_TABLE_UPDATE
    )
    private val updateTables = ""

    @ModuleDBVariable(
        value = "1:DB_VERSION1,DB_VERSION2",
        type = TypeConfig.TYPE_DB_TABLE_DELETE
    )
    private val deleteTables = ""
}