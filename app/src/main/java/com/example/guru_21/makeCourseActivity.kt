package com.example.guru_21

import android.content.Context
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class makeCourseActivity : AppCompatActivity() {

    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var layout: LinearLayout

    lateinit var btnGoaddMyCourse: Button
    lateinit var btnUpload: Button

    //후기 목록 버튼
    lateinit var watch_review_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_course)

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
            dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
            sqlitedb = dbManager.readableDatabase

            layout = findViewById(R.id.myCourse)
            btnGoaddMyCourse=findViewById<Button>(R.id.btnGoaddMycourse)
            btnUpload=findViewById<Button>(R.id.btnUpload)

            watch_review_button = findViewById<Button>(R.id.watch_review_button)

            //추가하기 버튼
            btnGoaddMyCourse.setOnClickListener{
                var intent = Intent(this, addMyCourseActivity::class.java)
                startActivity(intent)
            }

            // 업로드 버튼
            btnUpload.setOnClickListener{
                var intent = Intent(this, coursepageActivity::class.java)
                startActivity(intent)
            }

            // watch_review_button 클릭 시 후기 목록창으로 이동
            watch_review_button.setOnClickListener {
                val intent = Intent(this, reviewActivity::class.java)
                startActivity(intent)
            }

            var cursor: Cursor

            cursor = sqlitedb.rawQuery("SELECT placename, placeaddress, placecall, placecost, placecomment FROM mycourse;", null)

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
                tvplaceAddress.text = str_placeaddress
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
                    var intent = Intent(this, myPlaceInfoActivity::class.java)
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

