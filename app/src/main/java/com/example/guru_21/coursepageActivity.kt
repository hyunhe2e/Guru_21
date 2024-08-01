package com.example.guru_21

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.guru_21.databinding.ActivityCoursepageBinding
import java.sql.Types.NULL

class coursepageActivity : AppCompatActivity() {

    lateinit var course1: TextView
    lateinit var course2: TextView
    lateinit var course3: TextView

    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    private lateinit var binding: ActivityCoursepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coursepage)

        course1 = findViewById(R.id.course1)
        course2 = findViewById(R.id.course2)
        course3 = findViewById(R.id.course3)

        dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM mycourse;", null)



        }
    }
