package com.lebusishu.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lebusishu.db.R
import com.lebusishu.sqlite.StringUtil
import org.sqlite.database.sqlite.SQLiteDatabase
import java.io.File

class SqliteActivity : AppCompatActivity() {
    var file: File? = null;
    var db: SQLiteDatabase? = null
    lateinit var result: TextView
    lateinit var key: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)
        file = application.getDatabasePath("dbTest.db")
        file?.parentFile?.mkdir()
        initDb()
        initView()
    }

    private fun initDb() {
        try {
            db = SQLiteDatabase.openOrCreateDatabase(file, null)
            val noftssql = "CREATE TABLE IF NOT EXISTS message(msg TEXT);"
            val ftssql =
                "CREATE VIRTUAL TABLE IF NOT EXISTS ftsmessage USING fts5(msg,tokenize='wcicu zh_CN');"
            db!!.execSQL(noftssql)
            db!!.execSQL(ftssql)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        val insert_data = findViewById<Button>(R.id.insert_data)
        val normal_search = findViewById<Button>(R.id.normal_search)
        val fts_search = findViewById<Button>(R.id.fts_search)
        result = findViewById(R.id.search_result)
        key = findViewById(R.id.search_key)
        insert_data.setOnClickListener { insertData() }
        normal_search.setOnClickListener { normalSearch() }
        fts_search.setOnClickListener { ftsSearch() }
    }

    private fun insertData() {
        val values = ContentValues()
        db!!.beginTransaction()
        for (i in 0..10000) {
            values.clear()
            values.put("msg", StringUtil.randomString())
            db!!.insert("message", null, values)
            db!!.insert("ftsmessage", null, values)
            db!!.setTransactionSuccessful()
            db!!.endTransaction()
        }
        result.text = "10000 data insert success!"
    }

    private fun normalSearch() {
        var cursor: Cursor? = null;
        try {
            val keyWord = key.text
            cursor = db!!.rawQuery("SELECT * FROM message WHERE msg LIKE '%$keyWord%'", null)
            val builder = StringBuilder("normal count:")
            val start = System.currentTimeMillis()
            val count = cursor.count
            val time = System.currentTimeMillis() - start
            builder.append(count).append("\n")
            builder.append("time:").append(time).append("\n")
            result.text = builder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun ftsSearch() {
        var cursor: Cursor? = null;
        try {
            val keyWord = key.text.toString()
            cursor =
                db!!.rawQuery("SELECT * FROM ftsmessage WHERE ftsmessage MATCH ?", arrayOf(keyWord))
            val builder = StringBuilder("fts count:")
            val start = System.currentTimeMillis()
            val count = cursor.count
            val time = System.currentTimeMillis() - start
            builder.append(count).append("\n")
            builder.append("time:").append(time).append("\n")
            result.text = result.text.toString() + builder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    companion object {
        init {
            System.loadLibrary("sqliteFTS")
        }
    }
}