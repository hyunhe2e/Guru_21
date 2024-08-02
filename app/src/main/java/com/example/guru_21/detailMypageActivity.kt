package com.example.guru_21

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detailMypageActivity : AppCompatActivity() {
    //후기창 화면 코드로 변경
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var diary_title: TextView
    lateinit var diary_content: TextView
    lateinit var delete_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mypage)

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
        diary_title = findViewById(R.id.diary_detail_title)
        diary_content = findViewById(R.id.diary_detail_content)

        dbHelper = MyDatabaseHelper(this, "review", null, 1)

        val title = intent.getStringExtra("intent_name")
        if (title != null) {
            sqlitedb = dbHelper.readableDatabase
            val cursor: Cursor =
                sqlitedb.rawQuery("SELECT * FROM review WHERE title = '$title'", null)

            if (cursor.moveToFirst()) {
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                diary_title.text = title
                diary_content.text = content
            }
            cursor.close()
            sqlitedb.close()
        }
        delete_button = findViewById(R.id.delete_button)

        delete_button.setOnClickListener {

            deleteDiary(title)

        }
    }

    private fun isLoggedIn(context: Context): Boolean {
        val userId = SessionManager.getUserId(context)
        return userId != null
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

    private fun deleteDiary(title: String?) {
        if (title != null) {
            sqlitedb = dbHelper.writableDatabase
            sqlitedb.delete("review", "title = ?", arrayOf(title))
            sqlitedb.close()

            val intent = Intent(this, myPlaceInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}