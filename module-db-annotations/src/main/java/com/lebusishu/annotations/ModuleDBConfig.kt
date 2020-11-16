package com.lebusishu.annotations

/**
 * Description : 数据库配置类
 * Create by wxh on 11/9/20
 * Phone ：15233620521
 * Email：wangxiaohui1118@gmail.com
 * Person in charge : lebusishu
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class ModuleDBConfig(val dbName: String="module",val dbVersion:Int=1) {
}