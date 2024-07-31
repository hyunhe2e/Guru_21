package com.example.guru_21

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class loginActivity : AppCompatActivity() {

    lateinit var editname: EditText
    lateinit var editpwd: EditText
    lateinit var btn1: Button

    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editname = findViewById(R.id.editname)
        editpwd = findViewById(R.id.editpwd)
        btn1 = findViewById(R.id.btn1)

        dbManager = MyDatabaseHelper(this)
        sqlDB = dbManager.readableDatabase

        btn1.setOnClickListener {
            val name = editname.text.toString().trim()
            val pwd = editpwd.text.toString().trim()

            if (name.isNotBlank() && pwd.isNotBlank()) {
                val cursor: Cursor = sqlDB.rawQuery(
                    "SELECT * FROM member WHERE gname = ? AND gpwd = ?",
                    arrayOf(name, pwd)
                )

                if (cursor.count > 0) {
                    Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                cursor.close()
            } else {
                Toast.makeText(applicationContext, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
