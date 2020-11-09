package com.lebusishu.database.dao

import android.database.sqlite.SQLiteDatabase
import com.lebusishu.database.ModuleDB

/**
 * Description : 数据库操作基类
 * Create by wxh on 11/6/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
abstract class BaseDao {
    private var moduleDB: ModuleDB? = null
    private fun connectionDB(dbName: String) {
        moduleDB = ModuleDB.create(dbName)
    }

    fun getDatabase(dbName: String): SQLiteDatabase {
        connectionDB(dbName)
        return moduleDB!!.getDatabase(dbName)
    }
}