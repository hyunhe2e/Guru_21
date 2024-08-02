package com.example.guru_21

import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class mypageActivity : AppCompatActivity() {
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var managecourse: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)


        managecourse = findViewById<Button>(R.id.manage_course)

        managecourse.setOnClickListener{
            var intent = Intent(this, makeCourseActivity::class.java)
            startActivity(intent)
        }
    }


}

