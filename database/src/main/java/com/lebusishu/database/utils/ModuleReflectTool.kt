package com.lebusishu.database.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import java.lang.reflect.Method

/**
 * Description : reflect tool method
 * Create by wxh on 2020/10/16
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
class ModuleReflectTool {
    companion object {
        private lateinit var application: Context

        /**
         * Get Application by reflect
         *
         * @return Application
         */
        @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
        @JvmName("getApplication1")
        fun getApplication(): Context? {
            return try {
                val clazz = Class.forName("android.app.ActivityThread")
                val method: Method = clazz.getDeclaredMethod("currentActivityThread")
                method.isAccessible = true
                val obj = method.invoke(null)
                val field = clazz.getDeclaredField("mInitialApplication")
                field.isAccessible = true
                application = field.get(obj) as Application
                application
            } catch (e: Exception) {
                null
            }
        }

        /**
         * 获取创建表的SQL
         */
        fun getDatabases(): List<Any>? {
            return try {
                val clazz = Class.forName("com.lebusishu.db.kt_auto_sqls")
                val tablesField =
                    clazz.getDeclaredField("tablesMapping").apply { isAccessible = true }
                val obj = clazz.newInstance()
                val result = tablesField.get(obj) as HashMap<*, *>
                if (result.isNullOrEmpty()) null else result.keys.toList()
            } catch (e: Exception) {
                null
            }
        }

        /**
         * 获取创建表的SQL
         */
        fun getCreateTableSqls(dbName: String): ArrayList<*>? {
            return try {
                val clazz = Class.forName("com.lebusishu.db.kt_auto_sqls")
                val tablesField =
                    clazz.getDeclaredField("tablesMapping").apply { isAccessible = true }
                val obj = clazz.newInstance()
                val result = tablesField.get(obj) as HashMap<*, *>
                if (result.isNullOrEmpty()) null else result[dbName] as ArrayList<*>
            } catch (e: Exception) {
                null
            }
        }

        /**
         * 获取数据库版本
         */
        fun getDatabaseVersion(dbName: String): Int {
            return try {
                val clazz = Class.forName("com.lebusishu.db.kt_auto_sqls")
                val tablesField =
                    clazz.getDeclaredField("versionsMapping").apply { isAccessible = true }
                val obj = clazz.newInstance()
                val result = tablesField.get(obj) as HashMap<*, *>
                if (result.isNullOrEmpty()) 1 else result[dbName] as Int

            } catch (e: Exception) {
                1
            }
        }
    }
}