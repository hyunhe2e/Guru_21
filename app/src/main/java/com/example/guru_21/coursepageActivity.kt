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
    private lateinit var postTitleEditText: EditText
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coursepage)

        val inputText = intent.getStringExtra("inputText")

        if (inputText != null) {
            postTitleEditText.setText(inputText)
        }

        if (inputText != null) {
            postTitleEditText.setText(inputText)
        }

        // 입력된 텍스트를 사용하여 새로운 게시물을 추가
        addPost(inputText ?: "", "")

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

        dbManager = MyDatabaseHelper(this, "review", null, 1)
        sqlitedb = dbManager.readableDatabase
        mainLayout = findViewById(R.id.scroll_layout)
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

        val dbManager = MyDatabaseHelper(this, "tripDB.db", null, 1)
        val cursor = dbManager.getUserCourses(userId)

        cursor.close()
    }


    private fun loadPosts() {
        val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM review;", null)

        if (cursor.moveToFirst()) {
            do {

                val place = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))

                val formatContent = "$place: $content"

                addPost(place, formatContent)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    private fun addPost(title: String, content: String) {
        val inflater = LayoutInflater.from(this)
        val postView = inflater.inflate(R.layout.activity_coursepagepost, mainLayout, false)

        val postTitle = postView.findViewById<TextView>(R.id.post_title)
        val postContent = postView.findViewById<TextView>(R.id.post_content)

        postTitle.text = title
        postContent.text = content

        postView.setOnClickListener {
            val intent = Intent(this, postdetailActivity::class.java)
            intent.putExtra("postTitle", title)
            startActivity(intent)
        }

        mainLayout.addView(postView)
    }

}