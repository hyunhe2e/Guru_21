package com.example.guru_21

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var signupButton: Button
    lateinit var mypageButton: Button
    lateinit var watchCourseButton: Button
    lateinit var makeCourseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById<Button>(R.id.loginButton)
        signupButton = findViewById<Button>(R.id.signupButton)
        mypageButton = findViewById<Button>(R.id.mypageButton)
        watchCourseButton = findViewById<Button>(R.id.watchCourseButton)
        makeCourseButton = findViewById<Button>(R.id.makeCourseButton)

        //로그인 창으로 이동하는 코드
        loginButton.setOnClickListener{
            var intent = Intent(this, loginActivity::class.java)
            startActivity(intent)
        }
        //회원가입 창으로 이동하는 코드
        signupButton.setOnClickListener{
            var intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
        }
        //마이페이지 창으로 이동하는 코드
        mypageButton.setOnClickListener{
            var intent = Intent(this, mypageActivity::class.java)
            startActivity(intent)
        }
        //코스 살펴보기 창으로 이동하는 코드
        watchCourseButton.setOnClickListener{
            var intent = Intent(this, watchCourseActivity::class.java)
            startActivity(intent)
        }
        //코스 만들기 창으로 이동하는 코드
        makeCourseButton.setOnClickListener{
            var intent = Intent(this, makeCourseActivity::class.java)
            startActivity(intent)
        }


        }
}