package com.lebusishu.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils

/**
 * <p>Description : 数据库升级，数据迁移
 * 1,如果有字段删除
 * 2，字段数量不改变
 * 3，有字段增加。
 * 注意：尽量做到别改变列名，改名后会当做新增字段使用 当有字段改变（增加）需要增加到表原有字段后面
 * </p>
 * Create by wxh on 11/5/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
class ModuleDBMigrationHelper {
    companion object {
        val mInstance: ModuleDBMigrationHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ModuleDBMigrationHelper()
        }
        private const val tableTempName = "_TEMP"
        private var tableTempNameIndex = 0
        fun getTableTempName(): String {
            return tableTempName + tableTempNameIndex
        }
    }

    /**
     * 迁移所有的数据
     *
     * @param db         数据库
     */
    fun migrateAllTable(db: SQLiteDatabase) {
        tableTempNameIndex++
        buildTempTable(db)
        rebuildSpecifyTables(db)
        restoreTable(db, false)
    }

    /**
     * 迁移指定的数据
     *
     * @param db         数据库
     */
    fun migrateSpecifyTable(db: SQLiteDatabase, vararg tables: String?) {
        var inPreTransaction = false
        if (db.inTransaction()) {
            db.setTransactionSuccessful()
            db.endTransaction()
            inPreTransaction = true
        }
        tableTempNameIndex++
        buildTempTable(db, *tables)
        rebuildSpecifyTables(db, *tables)
        restoreTable(db, true, *tables)
        if (inPreTransaction && !db.inTransaction()) {
            db.beginTransaction()
        }
    }

    /**
     * 获取所有表
     *
     * @param db 数据库
     * @return List<String>
     */
    private fun getAllTables(db: SQLiteDatabase): ArrayList<String> {
        val list = ArrayList<String>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                "select name from sqlite_master where type='table' order by name",
                null
            )
            if (cursor == null || cursor.count <= 0) {
                return list
            }
            val index = cursor.getColumnIndex("name")
            while (cursor.moveToNext()) {
                val tableName = cursor.getString(index)
                if ("sqlite_sequence".equals(tableName, true)
                    || "android_metadata".equals(tableName, true)
                ) {
                    continue
                }
                if (getTableTempName().equals(tableName, true)) {
                    db.execSQL("drop table $tableName")
                    continue
                }
                list.add(tableName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }

    /**
     * 获取表字段的结构
     *
     * @param db        数据库
     * @param table 表名
     * @return List<Migrate>
     */
    private fun getMigrates(db: SQLiteDatabase, table: String?): List<Migrate> {
        val migrates = ArrayList<Migrate>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("PRAGMA table_info($table)", null)
            if (cursor == null || cursor.count <= 0) {
                return migrates
            }
            while (cursor.moveToNext()) {
                val migrate = Migrate()
                migrate.cid = cursor.getString(cursor.getColumnIndex("cid"))
                migrate.name = cursor.getString(cursor.getColumnIndex("name"))
                migrate.type = cursor.getString(cursor.getColumnIndex("type"))
                migrate.dflt_value = cursor.getString(cursor.getColumnIndex("dflt_value"))
                migrate.notnull = cursor.getString(cursor.getColumnIndex("notnull"))
                migrate.pk = cursor.getString(cursor.getColumnIndex("pk"))
                migrates.add(migrate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return migrates
    }

    /**
     * 获取表的列
     *
     * @param db        数据库
     * @param table 表名
     * @return List<String>
     */
    private fun getColumns(db: SQLiteDatabase, table: String?): List<String> {
        val columns = ArrayList<String>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $table limit 1", null)
            if (cursor == null || cursor.count <= 0) {
                return columns
            }
            return cursor.columnNames.asList()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return columns
    }

    /**
     * 判断某张表是否存在
     *
     * @param table 表名
     * @return boolean
     */
    private fun tableIsExist(db: SQLiteDatabase, table: String?): Boolean {
        if (table.isNullOrEmpty()) {
            return false
        }
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                "SELECT COUNT(*) FROM sqlite_mater WHERE type='table' AND name='$table'",
                null
            )
            if (cursor == null || cursor.count <= 0) {
                return false
            }
            return cursor.getInt(0) > 0
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return false
    }

    /**
     * 清除全部表数据
     *
     * @param db 数据库
     */
    private fun cleanTables(db: SQLiteDatabase, list: List<String?>) {

        for (table in list) {
            db.execSQL("DROP TABLE $table")
        }
    }

    /**
     * 创建全部表
     *
     * @param db 数据库
     */
    private fun buildTables(db: SQLiteDatabase, list: List<String?>) {

    }

    /**
     * 重新创建指定表
     *
     * @param db 数据库
     */
    private fun rebuildSpecifyTables(db: SQLiteDatabase, vararg tables: String?) {
        val list: List<String?>

        if (tables.isNullOrEmpty()) {
            list = getAllTables(db)
        } else {
            list = tables.toList()
        }
        for (table in list) {
            cleanTables(db, list)
            buildTables(db, list)
        }
    }

    /**
     * 删除指定的表
     *
     * @param db         数据库
     * @param tables 指定的数据
     */
    private fun dropSpecifyTable(db: SQLiteDatabase, vararg tables: String?) {
        if (tables.isEmpty()) {
            return
        }
        for (table in tables) {
            db.execSQL("DROP TABLE $table")
        }
    }

    /**
     * 生成缓存表
     *
     * @param db         数据库
     * @param tables     缓存指定表
     */
    private fun buildTempTable(db: SQLiteDatabase, vararg tables: String?) {
        //如果为空，则迁移所有表
        if (tables.isEmpty()) {
            val list = getAllTables(db)
            if (list.isEmpty()) {
                return
            }
            for (s in list) {
                generateTempTable(db, s)
            }
            //缓存指定的表
        } else {
            for (table in tables) {
                generateTempTable(db, table)
            }
        }
    }

    /**
     * 将原有数据恢复到数据库
     *
     * @param db         数据库
     * @param tables 还原指定数据
     */
    private fun restoreTable(db: SQLiteDatabase, isSpecify: Boolean, vararg tables: String?) {
        var list: ArrayList<String>? = null
        if (!isSpecify) {
            list = getAllTables(db)
            if (list.isNullOrEmpty()) {
                return
            }
        }
        val insert = StringBuilder()
        val delete = StringBuilder()
        val properties = ArrayList<String?>()
        if (tables.isNotEmpty()) {
            for (table in tables) {
                val tableNameTemp = table + getTableTempName()
                if (tableIsExist(db, tableNameTemp)) {
                    continue
                }
                properties.clear()
                if (!isSpecify) {
                    list!!.remove(table)
                }
                /*
                 * 1,如果有字段删除 2，字段数量不改变 3，有字段增加（尽量做到别改变列名）
                 * 当有字段改变（增加）需要增加到properties后面
                 */
                val tempColumns = getColumns(db, tableNameTemp)
                if (tempColumns.isNullOrEmpty()) {
                    continue
                }
                val migrates = getMigrates(db, table)
                for (migrate in migrates) {
                    if (!"_id".equals(migrate.name, true)) {
                        if (tempColumns.contains(migrate.name)) {
                            properties.add(migrate.name)
                        }
                    }
                }
                try {
                    db.beginTransaction()
                    insert.append("INSERT INTO ").append(table).append(" (")
                    insert.append(TextUtils.join(",", properties))
                    insert.append(") SELECT")
                    insert.append(TextUtils.join(",", properties))
                    insert.append(" FROM ").append(tableNameTemp).append(";")
                    db.execSQL(insert.toString())
                    insert.delete(0, insert.length)

                    delete.append("DROP TABLE ").append(tableNameTemp)
                    db.execSQL(delete.toString())
                    delete.delete(0, delete.length)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    delete.delete(0, delete.length)
                    delete.append("DROP TABLE ").append(tableNameTemp)
                    db.execSQL(delete.toString())
                    delete.delete(0, delete.length)
                    e.printStackTrace()
                } finally {
                    if (db.inTransaction()) {
                        db.endTransaction()
                    }
                }
            }
            //所有数据库的表---这里检测是否有剩余无用的缓存表（特别是修改表名后造成的无用表缓存）
            if (!isSpecify && list != null) {
                for (table in list) {
                    val tempTable = table + getTableTempName()
                    if (tableIsExist(db, table)) {
                        db.execSQL("DROP TABLE $tempTable")
                    }
                }
            }
        }
    }

    /**
     * 生成缓存数据的表
     *
     * @param db        db
     * @param table 表名
     */
    private fun generateTempTable(db: SQLiteDatabase, table: String?) {
        if (!tableIsExist(db, table)) {
            return
        }
        var tableName = table
        var tableTempName: String? = null
        if (tableName!!.endsWith(getTableTempName(), true)) {
            db.execSQL("DELETE FROM $table")
            tableTempName = table
            tableName = tableName.substring(0, tableName.length - 5)
        } else {
            tableTempName = table + getTableTempName()
        }
        val create = StringBuilder()
        val insert = StringBuilder()
        var divider = ""
        val properties = ArrayList<String?>()
        try {
            properties.clear()
            create.append("CREATE TABLE IF NOT EXISTS ").append(tableTempName).append(" (")
            val migrates = getMigrates(db, tableName)
            for (migrate in migrates) {
                // 如果包含关键字，不生成缓存表，并删除原表
                if ("FROM".equals(migrate.name, true) || "TO".equals(migrate.name, true)) {
                    db.execSQL("DELETE FROM $tableName")
                }
                properties.add(migrate.name)
                create.append(divider).append(migrate.name).append(" ").append(migrate.type)
                val tag = "1" == migrate.pk
                if ("INTEGER".equals(migrate.type) && !tag) {
                    create.append(" DEFAULT 0")
                }
                if (tag) {
                    create.append(" PRIMARY KEY")
                }
                divider = ","
            }
            create.append(");")
            db.execSQL(create.toString())
            create.delete(0, create.length)
            if (properties.contains("_id")) {
                properties.remove("_id")
            }
            if (properties.contains("_ID")) {
                properties.remove("_ID")
            }
            db.beginTransaction()
            insert.append("INSERT INTO ").append(tableTempName).append(" (")
            insert.append(TextUtils.join(",", properties))
            insert.append(") SELECT")
            insert.append(TextUtils.join(",", properties))
            insert.append(" FROM ").append(tableName).append(";")
            db.execSQL(insert.toString())
            insert.delete(0, insert.length)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db.inTransaction()) {
                db.endTransaction()
            }
        }
    }
}