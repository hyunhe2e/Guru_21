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

class mypageActivity : AppCompatActivity() {
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var diaryList: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        dbHelper = MyDatabaseHelper(this, "travel_diary", null, 1)
        diaryList = ArrayList()

        val listView: ListView = findViewById(R.id.diary_list_view)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, diaryList)
        listView.adapter = adapter


        loadDiaries()

        listView.setOnItemClickListener { _, _, position, _ ->
            val title = diaryList[position]
            val intent = Intent(this, detailMypageActivity::class.java)
            intent.putExtra("intent_name", title)
            startActivity(intent)
        }
    }


    fun loadDiaries() {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "travel_diary",
            arrayOf("title"),
            null, null, null, null, null
        )

        diaryList.clear()
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                diaryList.add(title)
            } while (cursor.moveToNext())
        }
        cursor.close()
        adapter.notifyDataSetChanged()
    }


}

