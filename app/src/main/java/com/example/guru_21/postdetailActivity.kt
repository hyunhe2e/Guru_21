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

    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postdetail2)

        dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        val postTitle = intent.getStringExtra("postTitle")

        val titleTextView = findViewById<TextView>(R.id.post_detail_title)
        val contentTextView = findViewById<TextView>(R.id.post_detail_content)

        titleTextView.text = postTitle

        if (postTitle != null) {
            loadPlaceDetails(postTitle, contentTextView)
        }

    }

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