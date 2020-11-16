# Module-Database
一个轻量级数据库框架，根据注解配置生成数据库和表，以及数据库升级时数据迁移
## module-db-annotations
**自定义注解**
> ModuleDBConfig 定义数据库名称和版本，当数据库版本变化是，会触发数据库的升级
> ModuleDBVariable 数据库的其他数据，如创建表的SQL，版本升级时，触发的表升级和删除，具体如下
```kotlin

        /*
        创建表
         */
        const val TYPE_DB_CREATE_TABLE: Int = 1

        /*
        打开其他数据库路径
         */
        const val TYPE_DB_PATH: Int = 2

        /*
        发生变化的表
         */
        const val TYPE_DB_TABLE_UPDATE: Int = 3

        /*
        删除的表
         */
        const val TYPE_DB_TABLE_DELETE: Int = 4
```

## module-db-compiler
**自定义编译器**
> 编译阶段识别注解并生成kotlin模板文件，以便动态生成数据库和管理数据库升级，模板格式如下
```kotlin
    init {
      this.tablesMapping = HashMap<String,Any>()
      this.versionsMapping = HashMap<String,Int>()
      this.pathMapping = HashMap<String,String>()
      this.updateTableMapping = HashMap<String,String>()
      this.deleteTableMapping = HashMap<String,String>()
      val tables0 = ArrayList<String>()
      tables0.add("CREATE TABLE IF NOT EXISTS DB_VERSION (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , NAME2 TEXT , NAME3 TEXT , VERSION INTEGER DEFAULT 1);")
      tables0.add("CREATE TABLE IF NOT EXISTS DB_VERSION1 (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , VERSION INTEGER DEFAULT 1);")
      tables0.add("CREATE TABLE IF NOT EXISTS DB_VERSION2 (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , VERSION INTEGER DEFAULT 1);")
      tables0.add("CREATE TABLE IF NOT EXISTS DB_VERSION3 (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , VERSION INTEGER DEFAULT 1);")
      tablesMapping.put("demo1",tables0)
      versionsMapping.put("demo1",1)
      pathMapping.put("demo1","./")
      updateTableMapping.put("demo1","DB_VERSION,DB_VERSION2")
      deleteTableMapping.put("demo1","DB_VERSION3")
      val tables1 = ArrayList<String>()
      tables1.add("CREATE TABLE IF NOT EXISTS DB_VERSION (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , NAME2 TEXT , NAME3 TEXT , VERSION INTEGER DEFAULT 1);")
      tables1.add("CREATE TABLE IF NOT EXISTS DB_VERSION1 (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , VERSION INTEGER DEFAULT 1);")
      tables1.add("CREATE TABLE IF NOT EXISTS DB_VERSION2 (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , VERSION INTEGER DEFAULT 1);")
      tables1.add("CREATE TABLE IF NOT EXISTS DB_VERSION3 (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , VERSION INTEGER DEFAULT 1);")
      tablesMapping.put("demo2",tables1)
      versionsMapping.put("demo2",1)
      pathMapping.put("demo2","./")
      updateTableMapping.put("demo2","DB_VERSION")
      deleteTableMapping.put("demo2","DB_VERSION1,DB_VERSION2")
    }
```
## module-db
**识别模板，创建数据库，数据库基本操作和数据库升级迁移的封装，避免了对数据库操作过多的非业务操作。注，如直接打开指定路径数据，暂不支持数据库升级**

## 快速入门
**第一步:配置数据库**
```kotlin
@ModuleDBConfig(dbName = "demo1")
class DBConfig {
    @ModuleDBVariable(value = "1", type = TypeConfig.TYPE_DB_VERSION)
    private val dbVersion = 0

    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " NAME2 TEXT ," +
                " NAME3 TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = TypeConfig.TYPE_DB_CREATE_TABLE
    )
    private val createDbTable = ""
    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION1 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = TypeConfig.TYPE_DB_CREATE_TABLE
    )
    private val createDbTable1 = ""

    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION2 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = TypeConfig.TYPE_DB_CREATE_TABLE
    )
    private val createDbTable2 = ""

    @ModuleDBVariable(
        value = "CREATE TABLE IF NOT EXISTS DB_VERSION3 (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " NAME TEXT ," +
                " VERSION INTEGER DEFAULT 1);", type = TypeConfig.TYPE_DB_CREATE_TABLE
    )
    private val createDbTable3 = ""

    @ModuleDBVariable(value = "DB_VERSION,DB_VERSION2", type = TypeConfig.TYPE_DB_TABLE_UPDATE)
    private val updateTables = ""

    @ModuleDBVariable(value = "DB_VERSION3", type = TypeConfig.TYPE_DB_TABLE_DELETE)
    private val deleteTables = ""
}
```
**第二步:实现DAO**
```kotlin
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
            val count = insert(getDatabase(dbName), "DB_VERSION", values)
            count.toInt()
        }
    }
    fun getDBVersion(dbName: String): Int {
        val sql = "SELECT VERSION FROM DB_VERSION WHERE NAME = ?"
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
```
**第三步:model调用**
就是这么方便
## 混淆
```kotlin
//配置混淆
```

License
---
    Copyright 2017 TangXiaoLv

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

