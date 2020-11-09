package com.lebusishu.database.annotations

/**
 * Description : 数据库需要的SQL语句，数据库版本等
 * Create by wxh on 11/9/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ModuleDBVariable(val value: String, val type: Int) {
}