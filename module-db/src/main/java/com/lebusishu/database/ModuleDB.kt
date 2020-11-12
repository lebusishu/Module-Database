package com.lebusishu.database

import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log
import com.lebusishu.database.config.DBConfigs
import com.lebusishu.database.utils.ModuleReflectTool

/**
 * Description : 数据库管理类
 * Create by wxh on 11/5/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
class ModuleDB {
    private var helper: ModuleDatabaseHelper? = null
    private var db: SQLiteDatabase? = null

    //打开的当前数据库
    private var currentDb: String? = null

    /**
     * 打开数据库
     */
    private fun open(dbName: String) {
        val name = dbName + DBConfigs.DATABASE_NAME_SUFFIX
        val context = ModuleReflectTool.getApplication()
        val dbVersion = ModuleReflectTool.getDatabaseVersion(dbName)
        Log.i("ModuleDB", "open dbName:$dbName version:$dbVersion name:$name")
        helper = ModuleDatabaseHelper(context, name, null, dbVersion, dbName)
        db = helper!!.writableDatabase
    }

    /**
     * 获取数据库
     */
    fun getDatabase(dbName: String): SQLiteDatabase {
        if (db == null || !db!!.isOpen) {
            create(dbName)
        }
        return db!!
    }

    companion object {
        @Volatile
        private var mInstance: ModuleDB? = null

        /**
         * 创建用户数据库
         */
        fun create(dbName: String): ModuleDB? {
            if (mInstance == null) {
                mInstance = ModuleDB()
                mInstance!!.open(dbName)
                mInstance!!.currentDb = dbName
            } else if (mInstance!!.db != null && !TextUtils.equals(dbName, mInstance!!.currentDb)) {
                close()
                mInstance = ModuleDB()
                mInstance!!.open(dbName)
                mInstance!!.currentDb = dbName
            }
            return mInstance
        }

        /**
         * 关闭数据库
         */
        private fun close() {
            if (mInstance == null) {
                return
            }
            if (mInstance!!.db != null) {
                mInstance!!.db!!.close()
            }
            if (mInstance!!.helper != null) {
                mInstance!!.helper!!.close()
                mInstance!!.helper = null
            }
            mInstance = null
        }

        /**
         * 获取数据库名
         */
        fun getDBName(): String {
            return mInstance!!.helper!!.databaseName
        }
    }
}