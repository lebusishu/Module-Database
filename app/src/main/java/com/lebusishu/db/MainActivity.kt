package com.lebusishu.db

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lebusishu.database.utils.ModuleReflectTool
import com.lebusishu.sqlite.SqliteActivity

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTest()
        initFtsTest()
    }

    private fun initTest() {
        val dbs = ModuleReflectTool.getDatabases()
        if (dbs.isNullOrEmpty()) {
            return
        }
        for (db in dbs) {
            val count = AppDao.mInstance.addDBVersion(db as String)
            Log.i(TAG, "add target $db db count:$count")
            var version = AppDao.mInstance.getDBVersion(db)
            Log.i(TAG, "get target $db db version:$version")
            version++
            AppDao.mInstance.replaceDBVersion("module", version, db)
            Log.i(TAG, "update target $db db version:$version")
        }
    }

    private fun initFtsTest() {
        val btn = findViewById<Button>(R.id.fts_test)
        btn.setOnClickListener {
            val intent = Intent(this, SqliteActivity::class.java)
            startActivity(intent)
        }
    }
}