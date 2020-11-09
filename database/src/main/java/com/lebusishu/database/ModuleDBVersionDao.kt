package com.lebusishu.database

import android.database.sqlite.SQLiteDatabase
import com.lebusishu.database.dao.ModuleBaseDao

/**
 * Description : 用于增量更新记录版本号
 * Create by wxh on 11/5/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
class ModuleDBVersionDao : ModuleBaseDao() {
    companion object {
        private val TAG = ModuleDBVersionDao::class.java.simpleName
        val mInstance: ModuleDBVersionDao by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ModuleDBVersionDao()
        }
    }


    /**
     * 获取数据库版本号
     */
    fun getDBVersion(db: SQLiteDatabase): Int {
        val sql = "SELECT VERSION FROM DB_VERSION WHERE NAME = 'module'"
        val cursor = rawQuery(db, sql, null) ?: return 1
        val version=cursor.getInt(0)
        cursor.close()
        return version
    }

    /**
     * 更新数据库版本
     */
    fun replaceDBVersion(db: SQLiteDatabase, name: String, version: Int) {
        val sql = "UPDATE DB_VERSION SET VERSION=$version WHERE NAME = '$name'"
        rawQuery(db, sql, null)
    }
}