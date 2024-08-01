package com.example.guru_21

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class addMypageActivity : AppCompatActivity() {
    //후기 작성 코드로 변경
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var diary_title: EditText
    lateinit var diary_content: EditText
    lateinit var save_diary_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_mypage)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        diary_title= findViewById(R.id.diary_title)
        diary_content= findViewById(R.id.diary_content)
        save_diary_button = findViewById(R.id.save_diary_button)

        dbHelper= MyDatabaseHelper(this, "review",null,1)

        // Intent로 전달된 placename을 받아서 diary_title에 설정
        val placename = intent.getStringExtra("placename")
        if (placename != null) {
            diary_title.setText(placename)
        }

        save_diary_button.setOnClickListener {
            var title = diary_title.text.toString()
            var content = diary_content.text.toString()

            sqlitedb=dbHelper.writableDatabase
            sqlitedb.execSQL("INSERT INTO review VALUES ('$title', '$content')")
            sqlitedb.close()

            val intent = Intent(this, myPlaceInfoActivity::class.java)

            startActivity(intent)
            finish()


        }
    }
}