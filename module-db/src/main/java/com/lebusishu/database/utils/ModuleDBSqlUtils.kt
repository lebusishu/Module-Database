package com.lebusishu.database.utils

/**
 * Description : DB SQL 操作工具类
 * Create by wxh on 11/5/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
class ModuleDBSqlUtils {
    companion object {
        /**
         * 将字符串数组转为字符串
         *
         * @param arrays 数据源
         * @return String
         */
        fun buildStringWithStrArray(vararg arrays: String?): String? {
            if (arrays.isEmpty()) {
                return null;
            }
            val builder = StringBuilder("'")
            for (arr in arrays) {
                builder.append(arr).append("','")
            }
            return builder.delete(builder.length - 2, builder.length).toString()
        }

        /**
         * 将int数组转为字符串
         *
         * @param arrays 数据源
         * @return String
         */
        fun buildStringWithIntArray(vararg arrays: Int?): String? {
            if (arrays.isEmpty()) {
                return null
            }
            val builder = StringBuilder()
            for (arr in arrays) {
                builder.append(arr).append(",")
            }
            return builder.delete(builder.length - 1, builder.length).toString()
        }

        /**
         * 将基本类型列表转为字符串
         *
         * @param arrays 数据源
         * @return String
         */
        fun buildStringWithList(list: List<Any>?): String? {
            if (list == null || list.isEmpty()) {
                return null
            }
            val builder = StringBuilder(",")
            list.forEachIndexed { index, s ->
                builder.append(s).append(if (index == list.indices.last) "'" else "','")
            }
            return builder.toString()
        }

        /**
         * 构造插入SQLiteStatement
         *
         * @param table 表名
         * @param columns 需要插入数据的列
         * @return StringBuilder
         */
        fun buildInsertSql(table: String, vararg columns: String): StringBuilder? {
            if (table.isEmpty() || columns.isEmpty()) {
                return null
            }
            val builder = StringBuilder("INSERT INTO ")
            builder.append(table).append(" (")
            columns.forEachIndexed { index, column ->
                builder.append(column).append(if (index == columns.indices.last) "" else ",")
            }
            builder.append(") VALUES (")
            buildPlaceholders(builder, columns.size)
            builder.append(")")
            return builder
        }

        /**
         * 构造删除SQLiteStatement
         *
         * @param table 表名
         * @param columns 需要数据的列
         * @return StringBuilder
         */
        fun buildDeleteSql(table: String, vararg columns: String): StringBuilder? {
            if (table.isEmpty()) {
                return null
            }
            val builder = StringBuilder("DELETE FROM ")
            builder.append(table)
            if (columns.isNotEmpty()) {
                builder.append(" WHERE ")
            }
            return builder
        }

        /**
         * 构建升级SQLiteStatement
         *
         * @param table 表名
         * @param updateColumns 需要升级数据列名
         * @return StringBuilder
         */
        fun buildUpdateSql(
            table: String,
            whereColumns: Array<String>,
            vararg updateColumns: String
        ): StringBuilder? {
            if (table.isEmpty() || updateColumns.isEmpty()) {
                return null
            }
            val builder = StringBuilder("UPDATE ")
            builder.append(table).append(" SET ")
            updateColumns.forEachIndexed { index, column ->
                builder.append(column).append("=?")
                    .append(if (index == updateColumns.indices.last) "" else ",")
            }
            if (whereColumns.isNotEmpty()) {
                builder.append(" WHERE ")
                buildColumnsEqValue(builder, table, *whereColumns)
            }
            return builder
        }

        /**
         * 构造查询sql
         *
         * @param table 表名
         * @param where 条件
         * @param columns 要查询的列
         * @return String
         */
        fun buildSelectSql(table: String, where: String, vararg columns: String): StringBuilder? {
            if (table.isEmpty()) {
                return null
            }
            val builder = StringBuilder("SELECT ")
            if (columns.isEmpty()) {
                builder.append("*")
            } else {
                columns.forEachIndexed { index, column ->
                    builder.append(column).append(if (index == columns.indices.last) "" else ",")
                }
            }
            builder.append(" FROM ").append(table).append(if (where.isEmpty()) "" else " $where")
            return builder
        }

        /**
         * 构造SQLiteStatement
         */
        private fun buildPlaceholders(builder: StringBuilder, count: Int) {
            for (i in 0..count) {
                if (i < count - 1) {
                    builder.append("?,")
                } else {
                    builder.append("?")
                }
            }
        }

        /**
         * 构建等于条件
         *
         * @param tableAlias 表别名
         * @param columns 列
         * @return StringBuilder
         */
        private fun buildColumnsEqValue(
            builder: StringBuilder,
            tableAlias: String,
            vararg columns: String
        ): StringBuilder {
            if (tableAlias.isEmpty() || columns.isEmpty()) {
                return builder
            }
            columns.forEachIndexed { index, column ->
                buildColumn(builder, tableAlias, column).append("=?")
                    .append(if (index == columns.indices.last) "" else " AND ")
            }
            return builder
        }

        /**
         * 增加列
         *
         * @param tableAlias 表别名
         * @param column 列名
         * @return StringBuilder
         */
        private fun buildColumn(
            builder: StringBuilder,
            tableAlias: String,
            column: String
        ): StringBuilder {
            builder.append(tableAlias).append(".").append(column)
            return builder
        }
    }
}
