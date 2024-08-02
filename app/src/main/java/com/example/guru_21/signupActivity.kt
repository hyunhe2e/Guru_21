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

        dbManager = MyDatabaseHelper(this, "tripDB.db", null, 1)

        //입력 코드
        btn1.setOnClickListener {
            var state: Boolean = false

            try {
                sqlDB = dbManager.writableDatabase
                val name = edtname.text.toString()
                val pwd = editpwd.text.toString()
                val pwd2 = editpwdcheck.text.toString()
                val email = editemail.text.toString()

                if (name.isNotBlank() && pwd.isNotBlank() && pwd2.isNotBlank() && email.isNotEmpty()) {
                    if(pwd == pwd2){
                        sqlDB.execSQL("INSERT INTO Member (userID, PWD, EMAIL) VALUES ('$name', '$pwd', '$email');")
                        Toast.makeText(applicationContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        state = true
                    }
                } else {
                    Toast.makeText(applicationContext, "모든 입력정보 칸에 빠짐없이 입력해주세요.", Toast.LENGTH_SHORT)
                        .show()
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