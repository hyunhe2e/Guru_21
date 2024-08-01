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

class reviewActivity : AppCompatActivity() {
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var reviewList: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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