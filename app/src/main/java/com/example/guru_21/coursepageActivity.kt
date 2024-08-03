package com.example.guru_21

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class coursepageActivity : AppCompatActivity() {

    private lateinit var mainLayout: LinearLayout
    private lateinit var postTitleEditText: TextView
    private lateinit var dbManager: MyDatabaseHelper
    private lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coursepage)

        mainLayout = findViewById(R.id.scroll_layout)

        val inflater = LayoutInflater.from(this)
        val postView = inflater.inflate(R.layout.activity_coursepagepost, mainLayout, false)

        postTitleEditText = postView.findViewById<TextView>(R.id.post_title)

        val inputText = intent.getStringExtra("inputText")
        if (inputText != null) {
            postTitleEditText.text = inputText
        }

        // 로그인 상태 확인
        if (isLoggedIn(this)) {
            setupViews()
            // 새 게시물을 추가
            addPost(inputText ?: "", "")
        } else {
            // 로그인되지 않은 경우
            Toast.makeText(this, "로그인 해주세요", Toast.LENGTH_SHORT).show()
            // 필요한 경우 로그인 화면으로 리다이렉트
            val intent = Intent(this, loginActivity::class.java)
            startActivity(intent)
            finish()  // 현재 액티비티 종료
        }
    }

    private fun setupViews() {
        dbManager = MyDatabaseHelper(this, "review", null, 1)
        sqlitedb = dbManager.readableDatabase

        val userId = SessionManager.getUserId(this)
        fetchUserData(userId)
        loadPosts()
    }

    private fun isLoggedIn(context: Context): Boolean {
        return SessionManager.getUserId(context) != null
    }

    private fun fetchUserData(userId: String?) {
        if (userId.isNullOrBlank()) {
            Toast.makeText(this, "유효하지 않은 사용자 ID", Toast.LENGTH_SHORT).show()
            return
        }

        if (::sqlitedb.isInitialized && sqlitedb.isOpen) {
            val cursor = sqlitedb.rawQuery("SELECT * FROM user_courses WHERE user_id = ?", arrayOf(userId))
            cursor.close()
        } else {
            Toast.makeText(this, "데이터베이스가 열려 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPosts() {
        if (::sqlitedb.isInitialized && sqlitedb.isOpen) {
            val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM review WHERE stat = 1 ;", null)
            if (cursor.moveToFirst()) {
                do {
                    val place = cursor.getString(cursor.getColumnIndex("title"))
                    val content = cursor.getString(cursor.getColumnIndex("content"))
                    val formatContent = "$place: $content"
                    addPost(place, formatContent)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } else {
            Toast.makeText(this, "데이터베이스가 열려 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addPost(title: String, content: String) {
        val inflater = LayoutInflater.from(this)
        val postView = inflater.inflate(R.layout.activity_coursepagepost, mainLayout, false)

        val postContent = postView.findViewById<TextView>(R.id.post_content)
        postContent.text = content

        postView.setOnClickListener {
            val intent = Intent(this, postdetailActivity::class.java)
            intent.putExtra("postTitle", title)
            startActivity(intent)
        }

        mainLayout.addView(postView)
    }

    override fun onDestroy() {
        super.onDestroy()

        // 데이터베이스가 열린 상태에서만 닫습니다.
        if (::sqlitedb.isInitialized && sqlitedb.isOpen) {
            sqlitedb.close()
        }
        if (::dbManager.isInitialized) {
            dbManager.close()
        }
    }
}
