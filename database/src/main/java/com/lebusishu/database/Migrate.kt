package com.lebusishu.database

/**
 * Description : 数据库迁移
 * Created by wxh on 2016/6/17 16:21.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 */
class Migrate {
    var cid: String? = null

    // 列名
    var name: String? = null

    // 类型
    var type: String? = null

    // 是否为空0可以为空，1不可以为空
    var notnull: String? = null

    // 默认值
    var dflt_value: String? = null

    // 是否主键0不是主键1是主键
    var pk: String? = null
}