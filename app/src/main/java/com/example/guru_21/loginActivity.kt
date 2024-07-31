package com.example.guru_21

import android.content.Context
import android.content.Intent
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

        //조회 코드
        btn1.setOnClickListener {
            var state: Boolean = false

            try {
                val name = editname.text.toString()
                val pwd = editpwd.text.toString()

                if (name.isNotBlank() && pwd.isNotBlank()) {
                    // 데이터베이스에서 name과 pwd가 일치하는 레코드가 있는지 확인
                    val cursor = sqlDB.rawQuery(
                        "SELECT * FROM member WHERE NAME = ? AND PWD = ?",
                        arrayOf(name, pwd)
                    )
                    if (cursor.moveToFirst()) {
                        // 로그인 성공
                        state = true
                        Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        // 로그인 실패
                        Toast.makeText(applicationContext, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    cursor.close()
                } else {
                    Toast.makeText(applicationContext, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                sqlDB.close()
                if(state == true) {
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}