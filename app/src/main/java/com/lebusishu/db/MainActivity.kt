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
            val count = AppDao.mInstance.addDBVersion(db as String)
            Log.i(TAG, "add target $db db count:$count")
            var version = AppDao.mInstance.getDBVersion(db)
            Log.i(TAG, "get target $db db version:$version")
            version++
            AppDao.mInstance.replaceDBVersion("module", version,db)
            Log.i(TAG, "update target $db db version:$version")
        }
    }
}