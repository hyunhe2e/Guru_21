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

    // UI 요소를 위한 변수 선언
    lateinit var edtname: EditText
    lateinit var editemail: EditText
    lateinit var editpwd: EditText
    lateinit var editpwdcheck: EditText
    lateinit var btn1: Button

    // 데이터베이스 관련 변수 선언
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)  // UI 레이아웃 설정

        // UI 요소 초기화
        edtname = findViewById(R.id.edtname)
        editemail = findViewById(R.id.editemail)
        editpwd = findViewById(R.id.editpwd)
        editpwdcheck = findViewById(R.id.editpwdcheck)
        btn1 = findViewById(R.id.btn1)

        // 데이터베이스 헬퍼 클래스 초기화
        dbManager = MyDatabaseHelper(this, "tripDB.db", null, 1)

        // 버튼 클릭 리스너 설정
        btn1.setOnClickListener {
            var state: Boolean = false

            try {
                // 데이터베이스를 쓰기 모드로 열기
                sqlDB = dbManager.writableDatabase

                // 입력된 데이터 가져오기
                val name = edtname.text.toString()
                val pwd = editpwd.text.toString()
                val pwd2 = editpwdcheck.text.toString()
                val email = editemail.text.toString()

                // 입력된 데이터 검증
                if (name.isNotBlank() && pwd.isNotBlank() && pwd2.isNotBlank() && email.isNotEmpty()) {
                    // 비밀번호 확인
                    if(pwd == pwd2){
                        // 데이터베이스에 회원 정보 저장
                        sqlDB.execSQL("INSERT INTO Member (NAME, PWD, EMAIL) VALUES ('$name', '$pwd', '$email');")
                        Toast.makeText(applicationContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        state = true
                    } else {
                        Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "모든 입력정보 칸에 빠짐없이 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // 예외 발생 시 오류 메시지 표시
                Toast.makeText(applicationContext, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                // 데이터베이스 닫기
                sqlDB.close()
                // 회원가입 성공 시 메인 액티비티로 이동
                if(state) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
