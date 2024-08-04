//후기 작성 코드로 변경
package com.example.guru_21

import android.content.Context
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
    // 데이터베이스 헬퍼와 데이터베이스 인스턴스
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    // UI 요소 변수 선언
    lateinit var diary_title: EditText
    lateinit var diary_content: EditText
    lateinit var save_diary_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mypage)

        // 로그인 상태 확인
        if (isLoggedIn(this)) {
            setupViews()
        } else {
            // 로그인되지 않은 경우
            Toast.makeText(this, "로그인 해주세요", Toast.LENGTH_SHORT).show()
            // 필요한 경우 로그인 화면으로 리다이렉트
            val intent = Intent(this, loginActivity::class.java)
            startActivity(intent)
            finish()  // 현재 액티비티 종료
        }
    }

    // UI 요소 초기화 및 이벤트 설정
    private fun setupViews(){
        fetchUserData(SessionManager.getUserId(this))   // 사용자 데이터 가져오기
        diary_title= findViewById(R.id.diary_title)
        diary_content= findViewById(R.id.diary_content)
        save_diary_button = findViewById(R.id.save_diary_button)

        // 데이터베이스 초기화
        dbHelper= MyDatabaseHelper(this, "tripDB",null,1)

        // Intent로 전달된 placename을 받아서 diary_title에 설정
        val placename = intent.getStringExtra("placename")
        if (placename != null) {
            diary_title.setText(placename)
        }

        // "Save Diary" 버튼 클릭 리스너
        save_diary_button.setOnClickListener {
            // 입력된 제목과 내용 가져오기
            var title = diary_title.text.toString()
            var content = diary_content.text.toString()
            val userId = SessionManager.getUserId(this)

            // 데이터베이스에 리뷰 저장
            sqlitedb = dbHelper.writableDatabase
            sqlitedb.execSQL("INSERT INTO review(title, content) VALUES ('$title', '$content')")
            sqlitedb.close()

            // 리뷰 저장 후, myPlaceInfoActivity로 이동
            val intent = Intent(this, myPlaceInfoActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    // 로그인 상태 확인
    private fun isLoggedIn(context: Context): Boolean {
        return SessionManager.getUserId(context) != null
    }

    // 사용자 데이터 가져오기
    private fun fetchUserData(userId: String?) {
        if (userId.isNullOrBlank()) {
            Toast.makeText(this, "유효하지 않은 사용자 ID", Toast.LENGTH_SHORT).show()
            return
        }

        val dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
        val cursor = dbManager.getUserCourses(userId)

        cursor.close()
    }
}