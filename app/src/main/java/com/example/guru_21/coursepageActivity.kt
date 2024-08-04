package com.example.guru_21

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
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
    // UI 요소와 데이터베이스 관련 변수 선언
    private lateinit var mainLayout: LinearLayout
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coursepage)

        // UI 요소 초기화
        mainLayout = findViewById(R.id.scroll_layout)

        // Intent로 전달된 텍스트를 가져와서 EditText에 설정
        //val inputText = intent.getStringExtra("inputText")
        //addPost(inputText ?: "", "")

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

    // UI 초기화 및 게시물 로딩
    private fun setupViews(){
        try {
            fetchUserData(SessionManager.getUserId(this))

            dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
            sqlitedb = dbManager.readableDatabase

            loadPosts()  // 게시물 로딩
        } catch (e: Exception) {
            Log.e("셋업에러췤", "Error during setup", e)
        }
    }

    // 로그인 상태 확인
    private fun isLoggedIn(context: Context): Boolean {
        return SessionManager.getUserId(context) != null
    }

    // 사용자 데이터 가져오기
    private fun fetchUserData(userId: String?) {
        if (userId.isNullOrBlank()) {
            Toast.makeText(this, "유효하지 않은 사용자 ID", Toast.LENGTH_SHORT).show()
            return
        }

        //val dbManager = MyDatabaseHelper(this, "tripDB.db", null, 1)
        //val cursor = dbManager.getUserCourses(userId)

        //cursor.close()
    }

    // 데이터베이스에서 모든 게시물을 로드
    private fun loadPosts() {
        val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM review;", null)

        if (cursor.moveToFirst()) {
            do {
                val place = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))

                val formatContent = "$place: $content"      // 내용 포맷팅

                // 게시물 추가
                addPost(place, formatContent)
            } while (cursor.moveToNext())    // 다음 게시물로 이동
        }
        cursor.close()
    }

    // 게시물을 레이아웃에 추가
    private fun addPost(title: String, content: String) {
        val inflater = LayoutInflater.from(this)
        val postView = inflater.inflate(R.layout.activity_coursepagepost, mainLayout, false)

        // 게시물의 제목과 내용을 설정
        val postTitle = postView.findViewById<TextView>(R.id.post_title)
        val postContent = postView.findViewById<TextView>(R.id.post_content)

        postTitle.text = title
        postContent.text = content

        // 게시물 클릭 시 상세보기 화면으로 이동
        //postView.setOnClickListener {
        //    val intent = Intent(this, postdetailActivity::class.java)
        //   intent.putExtra("postTitle", title)
        //    startActivity(intent)
        //}

        // 레이아웃에 게시물 추가
        mainLayout.addView(postView)
    }

}