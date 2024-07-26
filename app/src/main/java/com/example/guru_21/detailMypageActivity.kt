package com.example.guru_21

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detailMypageActivity : AppCompatActivity() {
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var diary_title: TextView
    lateinit var diary_content: TextView
    lateinit var delete_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_mypage)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        diary_title = findViewById(R.id.diary_detail_title)
        diary_content = findViewById(R.id.diary_detail_content)

        dbHelper = MyDatabaseHelper(this, "travel_diary", null, 1)

        val title = intent.getStringExtra("intent_name")
        if (title != null) {
            sqlitedb = dbHelper.readableDatabase
            val cursor: Cursor =
                sqlitedb.rawQuery("SELECT * FROM travel_diary WHERE title = '$title'", null)

            if (cursor.moveToFirst()) {
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                diary_title.text = title
                diary_content.text = content
            }
            cursor.close()
            sqlitedb.close()
        }
        delete_button = findViewById(R.id.delete_button)

        delete_button.setOnClickListener {

            deleteDiary(title)

        }

    }
    private fun deleteDiary(title: String?) {
        if (title != null) {
            sqlitedb = dbHelper.writableDatabase
            sqlitedb.delete("travel_diary", "title = ?", arrayOf(title))
            sqlitedb.close()

            val intent = Intent(this, mypageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}