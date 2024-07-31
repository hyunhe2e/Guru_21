package com.example.guru_21

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class makeCourseActivity : AppCompatActivity() {

    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var layout: LinearLayout

    lateinit var btnGoaddMyCourse: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_course)


        dbManager = MyDatabaseHelper(this, "myCourseDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        layout = findViewById(R.id.myCourse)
        btnGoaddMyCourse=findViewById<Button>(R.id.btnGoaddMycourse)

        //추가하기 버튼
        btnGoaddMyCourse.setOnClickListener{
            var intent = Intent(this, addMyCourseActivity::class.java)
            startActivity(intent)
        }

        var cursor: Cursor

        cursor = sqlitedb.rawQuery("SELECT * FROM mycourse;", null)

        var num: Int = 0

        while(cursor.moveToNext()) {
            var str_placename = cursor.getString(cursor.getColumnIndex("placename")).toString()
            var str_placeaddress = cursor.getString(cursor.getColumnIndex("placeaddress")).toString()
            var str_placecall = cursor.getString(cursor.getColumnIndex("placecall")).toString()
            var str_placecost = cursor.getString(cursor.getColumnIndex("placecost")).toString()
            var str_placecomment = cursor.getString(cursor.getColumnIndex("placecomment")).toString()

            var layout_item: LinearLayout = LinearLayout(this)
            layout_item.orientation = LinearLayout.VERTICAL
            layout_item.setPadding(20, 10, 20, 10)
            layout_item.id = num
            layout_item.setTag(str_placename)

            var tvplaceName: TextView = TextView(this)
            tvplaceName.text =str_placename
            tvplaceName.textSize = 30F
            tvplaceName.setBackgroundColor(Color.LTGRAY)
            layout_item.addView(tvplaceName)


            var tvplaceAddress: TextView =TextView(this)
            tvplaceAddress.text = str_placeaddress.toString()
            layout_item.addView(tvplaceAddress)

            var tvplaceCall: TextView=TextView(this)
            tvplaceCall.text=str_placecall
            layout_item.addView(tvplaceCall)

            var tvplaceCost: TextView=TextView(this)
            tvplaceCost.text=str_placecost
            layout_item.addView(tvplaceCost)

            var tvplaceComment: TextView=TextView(this)
            tvplaceComment.text=str_placecomment
            layout_item.addView(tvplaceComment)

            layout_item.setOnClickListener {
                val intent = Intent(this, myPlaceInfoActiviry::class.java)
                intent.putExtra("intent_name", str_placename)
                startActivity(intent)
            }

            layout.addView(layout_item)
            num++
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()
    }

    //메뉴
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_make_course, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.home -> {
                val intent = Intent(this,MainActivity::class.java )
                startActivity(intent)
                return true
            }
            R.id.mypage -> {
                val intent = Intent(this,mypageActivity::class.java )
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

