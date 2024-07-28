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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        watchCourseButton = findViewById<Button>(R.id.watchCourseButton)
        makeCourseButton = findViewById<Button>(R.id.makeCourseButton)

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