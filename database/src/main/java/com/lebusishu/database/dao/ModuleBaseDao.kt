package com.lebusishu.database.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

/**
 * Description : 提供数据库基础方法
 * Create by wxh on 11/6/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
open class ModuleBaseDao() : BaseDao() {
    private val TAG = ModuleBaseDao::class.java.simpleName

    /**
     * 插入数据
     */
    fun insert(db: SQLiteDatabase?, table: String?, values: ContentValues?): Long {
        if (db == null || table.isNullOrEmpty() || values == null) {
            return -1
        }
        try {
            return db.insert(table, null, values);
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
        return -1
    }

    /**
     * 删除数据
     */
    fun delete(
        db: SQLiteDatabase?,
        table: String?,
        where: String?,
        vararg whereArgs: String?
    ): Int {
        if (db == null || table.isNullOrEmpty() || where.isNullOrEmpty()) {
            return -1
        }
        try {
            return db.delete(table, where, whereArgs)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
        return -1
    }

    /**
     * 修改数据
     */
    fun update(
        db: SQLiteDatabase?,
        table: String?, values: ContentValues?, where: String?,
        vararg whereArgs: String?
    ): Int {
        if (db == null || table.isNullOrEmpty() || where.isNullOrEmpty() || values == null) {
            return -1
        }
        try {
            return db.update(table, values, where, whereArgs)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
        return -1
    }

    /**
     * 查询数据
     */
    fun query(
        db: SQLiteDatabase?,
        table: String?,
        selection: String?,
        selectionArgs: Array<String>,
        groupBy: String?,
        having: String?,
        orderBy: String?,
        limit: String?,
        vararg columns: String?
    ): Cursor? {
        if (db == null || table.isNullOrEmpty()) {
            return null
        }
        var cursor: Cursor? = null
        try {
            cursor =
                db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
        return cursor
    }

    /**
     * 执行查询SQL
     */
    fun rawQuery(
        db: SQLiteDatabase?,
        selectSql: String?, vararg selectArgs: String?
    ): Cursor? {
        if (db == null || selectSql.isNullOrEmpty()) {
            return null
        }
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectSql, selectArgs, null)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
        return cursor
    }

    /**
     * 执行SQL
     */
    fun execSql(
        db: SQLiteDatabase?,
        execSql: String?, bindArgs: Array<Any>?
    ) {
        if (db == null || execSql.isNullOrEmpty()) {
            return
        }
        try {
            db.execSQL(execSql, bindArgs)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
    }
}