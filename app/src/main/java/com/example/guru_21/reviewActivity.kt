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
        private fun setupViews(){
            fetchUserData(SessionManager.getUserId(this))

            dbHelper = MyDatabaseHelper(this, "review", null, 1)
            reviewList = ArrayList()

            val listView: ListView = findViewById(R.id.review_list_view)
            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reviewList)
            listView.adapter = adapter

            loadReview()

            listView.setOnItemClickListener { _, _, position, _ ->
                val title = reviewList[position]
                val intent = Intent(this, detailMypageActivity::class.java)
                intent.putExtra("intent_name", title)
                startActivity(intent)
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

    fun loadReview() {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "review",
            arrayOf("title"),
            null, null, null, null, null
        )

        reviewList.clear()
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                reviewList.add(title)
            } while (cursor.moveToNext())
        }
        cursor.close()
        adapter.notifyDataSetChanged()
    }
    fun showAddReviewDialog() {

        val dialogView = layoutInflater.inflate(R.layout.activity_add_mypage, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.diary_title)
        val contentEditText: EditText = dialogView.findViewById(R.id.diary_content)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Diary")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = titleEditText.text.toString()
                val content = contentEditText.text.toString()

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        loadReview()
    }
}