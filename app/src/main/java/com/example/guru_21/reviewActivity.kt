package com.example.guru_21

import android.app.AlertDialog
import android.content.Context
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

class reviewActivity : AppCompatActivity() {
    // 데이터베이스 헬퍼 및 리뷰 리스트와 어댑터
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var reviewList: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

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
    // UI 설정 및 데이터 로드
    private fun setupViews(){
        fetchUserData(SessionManager.getUserId(this))

        // 데이터베이스 헬퍼 및 리스트 초기화
        dbHelper = MyDatabaseHelper(this, "tripDB", null, 1)
        reviewList = ArrayList()

        // ListView 및 어댑터 설정
        val listView: ListView = findViewById(R.id.review_list_view)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reviewList)
        listView.adapter = adapter

        loadReview()    // 리뷰 데이터 로드

        // ListView 아이템 클릭 시 상세 페이지로 이동
        listView.setOnItemClickListener { _, _, position, _ ->
            val title = reviewList[position]
            val intent = Intent(this, detailMypageActivity::class.java)
            intent.putExtra("intent_name", title)
            startActivity(intent)
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

        val dbManager = MyDatabaseHelper(this, "tripDB.db", null, 1)
        val cursor = dbManager.getUserCourses(userId)

        cursor.close()
    }

    // 리뷰 데이터 로드
    fun loadReview() {
        // 읽기 전용 데이터베이스 인스턴스 얻기
        val db = dbHelper.readableDatabase

        // "review" 테이블에서 "title" 컬럼 조회
        val cursor: Cursor = db.query(
            "review",
            arrayOf("title"),
            null, null, null, null, null
        )

        reviewList.clear()  // 리뷰 리스트 초기화
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                if(title != null){
                    reviewList.add(title)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        // 어댑터에 데이터 변경 알리기
        adapter.notifyDataSetChanged()
    }

    // 액티비티가 다시 포그라운드로 나올 때 리뷰 데이터를 새로 로드
    override fun onResume() {
        super.onResume()
        loadReview()
    }
}
