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
    //후기 작성 코드로 변경
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

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
    private fun setupViews(){
        fetchUserData(SessionManager.getUserId(this))
        diary_title= findViewById(R.id.diary_title)
        diary_content= findViewById(R.id.diary_content)
        save_diary_button = findViewById(R.id.save_diary_button)

        dbHelper= MyDatabaseHelper(this, "tripDB",null,1)

        // Intent로 전달된 placename을 받아서 diary_title에 설정
        val placename = intent.getStringExtra("placename")
        if (placename != null) {
            diary_title.setText(placename)
        }

        save_diary_button.setOnClickListener {
            var title = diary_title.text.toString()
            var content = diary_content.text.toString()
            val userId = SessionManager.getUserId(this)
            sqlitedb = dbHelper.writableDatabase
            sqlitedb.execSQL("INSERT INTO review VALUES ('$userId','$title', '$content')")
            sqlitedb.close()

            val intent = Intent(this, myPlaceInfoActivity::class.java)

            startActivity(intent)
            finish()
        }

    }

    private fun isLoggedIn(context: Context): Boolean {
        return SessionManager.getUserId(context) != null
    }


    private fun fetchUserData(userId: String?) {
        if (userId.isNullOrBlank()) {
            Toast.makeText(this, "유효하지 않은 사용자 ID", Toast.LENGTH_SHORT).show()
            return
        }

        val dbManager = MyDatabaseHelper(this, "tripDB.db", null, 1)
        val cursor = dbManager.getUserCourses(userId)

        cursor.close()
    }
}