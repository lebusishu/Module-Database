package com.lebusishu.db

import android.content.ContentValues
import com.lebusishu.database.dao.ModuleBaseDao

/**
 * Description : app测试数据库dao
 * Create by wxh on 11/9/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
class AppDao : ModuleBaseDao() {
    companion object {
        val mInstance: AppDao by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppDao()
        }
    }

    fun addDBVersion(dbName: String): Int {
        return if (getDBVersionCount(dbName) > 0) {
            replaceDBVersion("module", 1,dbName)
        } else {
            val values = ContentValues()
            values.put("NAME", "module")
            values.put("version", 1)
            val count = insert(getDatabase(dbName), "DB_VERSION1", values)
            count.toInt()
        }
    }

    private fun getDBVersionCount(dbName: String): Int {
        val sql = "SELECT COUNT(*) FROM DB_VERSION1 WHERE NAME = ?"
        val cursor = rawQuery(getDatabase(dbName), sql, "module")
        if (cursor == null || cursor.count <= 0) {
            return -1
        }
        var count = 0
        while (cursor.moveToNext()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    /**
     * 更新数据库版本
     */
    fun replaceDBVersion(name: String, version: Int, dbName: String): Int {
        val sql = "UPDATE DB_VERSION1 SET VERSION=$version WHERE NAME = ?"
        val cursor = rawQuery(getDatabase(dbName), sql, name) ?: return -1
        val count = cursor.count
        cursor.close()
        return count
    }

    fun getDBVersion(dbName: String): Int {
        val sql = "SELECT VERSION FROM DB_VERSION1 WHERE NAME = ?"
        val cursor = rawQuery(getDatabase(dbName), sql, "module")
        if (cursor == null || cursor.count <= 0) {
            return -1
        }
        var version = 0
        while (cursor.moveToNext()) {
            version = cursor.getInt(0)
        }

        cursor.close()
        return version
    }
}