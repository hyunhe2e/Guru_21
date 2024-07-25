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
        }
}