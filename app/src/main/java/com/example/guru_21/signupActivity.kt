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
import com.example.guru_21.loginActivity.MyDatabaseHelper

class signupActivity : AppCompatActivity() {

    lateinit var edtname: EditText
    lateinit var editemail: EditText
    lateinit var editpwd: EditText
    lateinit var editpwdcheck: EditText
    lateinit var btn1: Button

    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edtname = findViewById(R.id.edtname)
        editemail = findViewById(R.id.editemail)
        editpwd = findViewById(R.id.editpwd)
        editpwdcheck = findViewById(R.id.editpwdcheck)
        btn1 = findViewById(R.id.btn1)

        dbManager = MyDatabaseHelper(this)

        //입력 코드
        btn1.setOnClickListener {
            try {
                sqlDB = dbManager.writableDatabase
                val name = edtname.text.toString()
                val pwd = editpwd.text.toString()

                if (name.isNotBlank() && pwd.isNotBlank()) {
                    sqlDB.execSQL("INSERT INTO member (tryName, trytext) VALUES ('$name', '$pwd');")
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
            //초기화 코드
            //sqlDB = dbManager.writableDatabase
            //dbManager.onUpgrade(sqlDB, 1, 2)
            //sqlDB.close()
        }
    }
    //db 코드
    class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "member", null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {        //테이블을 생성
            db!!.execSQL(
                "CREATE TABLE member (" +
                        "Name CHAR(20) PRIMARY KEY, " +
                        "PWD TEXT, " +
                        "EMAIL TEXT);"
            )
        }

        override fun onUpgrade(
            db: SQLiteDatabase?,
            oldVersion: Int,
            newVersion: Int
        ) {     //테이블을 삭제한 후 다시 생성
            db!!.execSQL("DROP TABLE IF EXISTS member")
            onCreate(db)
        }

    }
}