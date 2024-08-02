//내 코스의 장소 정보 보기

package com.example.guru_21

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class myPlaceInfoActivity:AppCompatActivity() {
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var tvmyplaceName: TextView
    lateinit var tvmyplaceAddress: TextView
    lateinit var tvmyplaceCall: TextView
    lateinit var tvmyplaceCost: TextView
    lateinit var tvmyplaceComment: TextView

    lateinit var str_placename: String
    lateinit var str_placeaddress: String
    lateinit var str_placecall: String
    var placecost: Int = 0
    lateinit var str_placecomment: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myplace_info)

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

        tvmyplaceName = findViewById(R.id.edtplacename)
        tvmyplaceAddress = findViewById(R.id.edtplaceaddress)
        tvmyplaceCall = findViewById(R.id.edtplacecall)
        tvmyplaceCost = findViewById(R.id.edtplacecost)
        tvmyplaceComment = findViewById(R.id.edtplacecomment)


        var intent = intent
        str_placename = intent.getStringExtra("intent_name").toString()

        dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT placename, placeaddress, placecall, placecost, placecomment FROM mycourse WHERE placename = '"+str_placename+"';", null)

        if(cursor.moveToNext()) {
            str_placeaddress = cursor.getString(cursor.getColumnIndex("placeaddress")).toString()
            str_placecall = cursor.getString(cursor.getColumnIndex("placecall")).toString()
            placecost = cursor.getInt(cursor.getColumnIndex("placecost"))
            str_placecomment = cursor.getString(cursor.getColumnIndex("placecomment")).toString()
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        tvmyplaceName.text=str_placename
        tvmyplaceAddress.text=str_placeaddress
        tvmyplaceCall.text=str_placecall
        tvmyplaceCost.text=""+placecost
        tvmyplaceComment.text=str_placecomment+"\n"

        // 장소 이름 클릭 시 후기 작성 창으로 이동
        tvmyplaceName.setOnClickListener {
            val intent = Intent(this, addMypageActivity::class.java)
            intent.putExtra("placename", str_placename)
            startActivity(intent)
        }

    }

    private fun isLoggedIn(context: Context): Boolean {
        return SessionManager.getUserId(context) != null
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_myplace_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.gomakecourse -> {
                val intent = Intent (this, makeCourseActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.deletemyplace -> {
                dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
                sqlitedb = dbManager.readableDatabase
                sqlitedb.execSQL("DELETE FROM mycourse WHERE placename = '"+str_placename+"';")
                sqlitedb.close()
                dbManager.close()

                val intent = Intent(this, makeCourseActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}