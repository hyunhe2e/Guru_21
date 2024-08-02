package com.example.guru_21

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var watchCourseButton: Button
    lateinit var makeCourseButton: Button
    lateinit var logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        watchCourseButton = findViewById<Button>(R.id.watchCourseButton)
        makeCourseButton = findViewById<Button>(R.id.makeCourseButton)
        logout = findViewById<Button>(R.id.logout)

        //코스 살펴보기 창으로 이동하는 코드
        watchCourseButton.setOnClickListener{
            var intent = Intent(this, coursepageActivity::class.java)
            startActivity(intent)
        }
        //코스 만들기 창으로 이동하는 코드
        makeCourseButton.setOnClickListener{
            var intent = Intent(this, makeCourseActivity::class.java)
            startActivity(intent)
        }
        //로그아웃 코드
        logout.setOnClickListener {
            // 사용자 세션 또는 저장된 데이터 삭제
            val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            // 로그인 화면으로 리다이렉트
            val intent = Intent(this, loginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        }
    //로그인창으로 이동하는 코드
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.login -> {
                val intent = Intent(this,loginActivity::class.java )
                startActivity(intent)
                return true
            }
            R.id.signup -> {
                val intent = Intent(this,signupActivity::class.java )
                startActivity(intent)
                return true
            }
            R.id.mypage -> {
                val intent = Intent(this,mypageActivity::class.java )
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}