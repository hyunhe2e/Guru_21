package com.example.guru_21

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class coursepageActivity : AppCompatActivity() {

    private lateinit var mainLayout: LinearLayout
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coursepage)

        dbManager = MyDatabaseHelper(this, "review", null, 1)
        sqlitedb = dbManager.readableDatabase

        mainLayout = findViewById(R.id.scroll_layout)

        loadPosts()

    }

    private fun loadPosts() {
        val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM review;", null)

        if (cursor.moveToFirst()) {
            do {

                val place = cursor.getString(cursor.getColumnIndex("title")) // Assuming this is the place
                val content = cursor.getString(cursor.getColumnIndex("content"))

                val formatContent = "$place: $content"

                addPost("", formatContent)
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

        mainLayout.addView(postView)
    }

}