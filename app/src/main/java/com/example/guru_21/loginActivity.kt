package com.example.guru_21

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        dbManager = MyDatabaseHelper(this, "MyDatabase.db", null, 1)

        //입력 코드
        btn1.setOnClickListener {
            try {
                sqlDB = dbManager.writableDatabase
                val name = editname.text.toString()
                val pwd = editpwd.text.toString()

                if (name.isNotBlank() && pwd.isNotBlank()) {
                    sqlDB.execSQL("INSERT INTO Member (tryName, trytext) VALUES ('$name', '$pwd');")
                    Toast.makeText(applicationContext, "로그인 시도", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                sqlDB.close()
            }
        }
    }
}