package com.lebusishu.db

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lebusishu.database.utils.ModuleReflectTool

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTest()
    }

    private fun initTest() {
        val dbs=ModuleReflectTool.getDatabases()
        if (dbs.isNullOrEmpty()){
            return
        }
        for (db in dbs){
            Log.i(TAG, "db name is :$db")
            val list=ModuleReflectTool.getCreateTableSqls(db as String)
            Log.i(TAG, "get target $db table sql:\n"+list?.joinToString ("\n"))
            val v= ModuleReflectTool.getDatabaseVersion(db)
            Log.i(TAG, "get target $db db version:$v")
            val count = AppDao.mInstance.addDBVersion(db)
            Log.i(TAG, "add target $db db count:$count")
            var version = AppDao.mInstance.getDBVersion(db)
            Log.i(TAG, "get target $db db version:$version")
            version++
            AppDao.mInstance.replaceDBVersion("module", version,db)
            Log.i(TAG, "update target $db db version:$version")

        }
    }
}