package com.lebusishu.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.lebusishu.database.utils.ModuleReflectTool


/**
 * Description : helper管理
 * Create by wxh on 11/5/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
class ModuleDatabaseHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    private var dbName: String? = null

    constructor(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int, dbName: String
    ) : this(context, name, factory, version) {
        this.dbName = dbName
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //创建需要的表
        createAllTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i("ModuleDatabaseHelper", "db old version:$oldVersion,new version:$newVersion")
    }

    /**
     * 创建数据库所需要的表
     */
    private fun createAllTables(db: SQLiteDatabase?) {
        val tables = ModuleReflectTool.getCreateTableSqls(dbName!!)
        if (tables.isNullOrEmpty()) {
            return
        }
        try {
            db!!.beginTransaction()
            for (table in tables) {
                db.execSQL(table as String)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("ModuleDatabaseHelper", e.message, e)
        } finally {
            db!!.endTransaction()
        }
    }
}