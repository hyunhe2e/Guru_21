package com.example.guru_21

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // UI 요소 선언
    lateinit var watchCourseButton: Button
    lateinit var makeCourseButton: Button
    lateinit var logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI 요소 초기화
        watchCourseButton = findViewById<Button>(R.id.watchCourseButton)
        makeCourseButton = findViewById<Button>(R.id.makeCourseButton)
        logout = findViewById<Button>(R.id.logout)

        // "코스 살펴보기" 버튼 클릭 시 coursepageActivity로 이동
        watchCourseButton.setOnClickListener {
            var intent = Intent(this, coursepageActivity::class.java)
            startActivity(intent)
        }

        // "코스 만들기" 버튼 클릭 시 makeCourseActivity로 이동
        makeCourseButton.setOnClickListener {
            var intent = Intent(this, makeCourseActivity::class.java)
            startActivity(intent)
        }

        // "로그아웃" 버튼 클릭 시 사용자 세션 데이터 삭제
        logout.setOnClickListener {
            val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear() // 저장된 모든 데이터 삭제
            editor.apply() // 변경 사항 적용
        }
    }

    // 메뉴 옵션 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // 메뉴 리소스 파일을 인플레이트
        return true
    }

    // 메뉴 항목 선택 시 호출되는 메서드
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.login -> {
                // 로그인 액티비티로 이동
                val intent = Intent(this, loginActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.signup -> {
                // 회원가입 액티비티로 이동
                val intent = Intent(this, signupActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.mypage -> {
                // 마이페이지 액티비티로 이동
                val intent = Intent(this, mypageActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
