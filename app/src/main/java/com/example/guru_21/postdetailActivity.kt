package com.example.guru_21

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class postdetailActivity : AppCompatActivity() {
    // 데이터베이스 헬퍼 및 데이터베이스 인스턴스
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postdetail2)

        // 데이터베이스 헬퍼 및 데이터베이스 인스턴스 초기화
        dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        // 인텐트에서 게시물 제목 가져오기
        val postTitle = intent.getStringExtra("postTitle")

        // UI 요소 초기화
        val titleTextView = findViewById<TextView>(R.id.post_detail_title)
        val contentTextView = findViewById<TextView>(R.id.post_detail_content)

        // 게시물 제목을 UI에 설정
        titleTextView.text = postTitle

        // 게시물 제목이 null이 아닌 경우 상세 정보 로드
        if (postTitle != null) {
            loadPlaceDetails(postTitle, contentTextView)
        }

    }

    // 게시물 제목을 기반으로 장소 세부 정보를 로드하여 TextView에 설정
    private fun loadPlaceDetails(title: String, content: TextView) {
        val cursor = sqlitedb.rawQuery("""
            SELECT  mycourse.userID,  mycourse.placename,  mycourse.placeaddress,  mycourse.placecall,  mycourse.placecost
            FROM mycourse
            INNER JOIN review ON review.placename = review.title
            WHERE review.title = ?
        """, arrayOf(title))

        if (cursor.moveToFirst()) {
            val placeName = cursor.getString(cursor.getColumnIndexOrThrow("placename"))
            val placeAddress = cursor.getString(cursor.getColumnIndexOrThrow("placeaddress"))
            val placeCall = cursor.getString(cursor.getColumnIndexOrThrow("placecall"))
            val placeCost = cursor.getString(cursor.getColumnIndexOrThrow("placecost"))

            // 세부 정보를 포맷하여 TextView에 설정
            val details = """
                Name: $placeName
                Address: $placeAddress
                Call: $placeCall
                Cost: $placeCost
            """.trimIndent()

            content.text = details
        }
        cursor.close()
    }

}