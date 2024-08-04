package com.example.guru_21

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class loginActivity : AppCompatActivity() {

    // UI 요소 선언
    lateinit var editname: EditText
    lateinit var editpwd: EditText
    lateinit var btn1: Button

    // 데이터베이스 매니저와 SQL 데이터베이스 객체 선언
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // UI 요소 초기화
        editname = findViewById(R.id.editname)
        editpwd = findViewById(R.id.editpwd)
        btn1 = findViewById(R.id.btn1)

        // 데이터베이스 매니저와 SQL 데이터베이스 초기화
        dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
        sqlDB = dbManager.writableDatabase

        // 로그인 버튼 클릭 시 실행되는 코드
        btn1.setOnClickListener {
            var state: Boolean = false
            val name = editname.text.toString() // 사용자 입력 이름
            val pwd = editpwd.text.toString()   // 사용자 입력 비밀번호

            try {
                if (name.isNotBlank() && pwd.isNotBlank()) {
                    // 데이터베이스에서 이름과 비밀번호가 일치하는 사용자가 있는지 확인
                    val cursor = sqlDB.rawQuery(
                        "SELECT * FROM member WHERE NAME = ? AND PWD = ?",
                        arrayOf(name, pwd)
                    )
                    if (cursor.moveToFirst()) {
                        // 로그인 성공 시
                        state = true
                        Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        // 로그인 실패 시
                        Toast.makeText(applicationContext, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    cursor.close() // 커서 닫기
                } else {
                    Toast.makeText(applicationContext, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // 예외 발생 시
                Toast.makeText(applicationContext, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                if(state == true) {
                    // JWT 생성 및 사용자 세션 저장
                    val authToken = JwtUtils.generateToken(name)
                    loginUser(name, authToken)
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    sqlDB.close() // 데이터베이스 닫기
                }
            }
        }
    }

    // 로그인된 사용자 정보를 SharedPreferences에 저장
    fun loginUser(userId: String, authToken: String) {
        // SharedPreferences 인스턴스 가져오기
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)

        // SharedPreferences.Editor를 사용하여 데이터 저장
        val editor = sharedPref.edit()
        editor.putString("USER_ID", userId)        // 사용자 ID 저장
        editor.putString("AUTH_TOKEN", authToken)  // 인증 토큰 저장
        editor.apply()  // 변경 사항 적용

        // 로그인 성공 후 메인 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}
