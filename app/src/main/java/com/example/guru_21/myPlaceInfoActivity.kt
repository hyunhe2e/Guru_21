//내 코스의 장소 정보 보기

package com.example.guru_21

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class myPlaceInfoActivity:AppCompatActivity() {
    // 데이터베이스 헬퍼 및 데이터베이스 인스턴스
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    // UI 요소
    lateinit var tvmyplaceName: TextView
    lateinit var tvmyplaceAddress: TextView
    lateinit var tvmyplaceCall: TextView
    lateinit var tvmyplaceCost: TextView
    lateinit var tvmyplaceComment: TextView

    private var str_placename: String = ""
    private var str_placeaddress: String = ""
    private var str_placecall: String = ""
    private var placecost: Int = 0
    private var str_placecomment: String = ""

    lateinit var imageView: ImageView
    var byteArray: ByteArray? = null


    // ByteArray를 Bitmap으로 변환하는 메서드
    fun byteArrayToBitmap(byteArray: ByteArray?):Bitmap?{
        return try{
            if(byteArray!=null && byteArray.isNotEmpty()){
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            } else{
                null
            }
        }catch (e: Exception){
            null
        }
    }



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

        // UI 요소 초기화
        tvmyplaceName = findViewById(R.id.edtplacename)
        tvmyplaceAddress = findViewById(R.id.edtplaceaddress)
        tvmyplaceCall = findViewById(R.id.edtplacecall)
        tvmyplaceCost = findViewById(R.id.edtplacecost)
        tvmyplaceComment = findViewById(R.id.edtplacecomment)
        imageView = findViewById(R.id.imageView)

        // 인텐트에서 장소 이름을 가져옴
        var intent = intent
        str_placename = intent.getStringExtra("intent_name") ?: ""

        dbManager = MyDatabaseHelper(this, "tripDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        try {
            var cursor: Cursor
            cursor = sqlitedb.rawQuery(
                "SELECT placename, placeaddress, placecall, placecost, placecomment, placeimage FROM mycourse WHERE placename = '" + str_placename + "';",
                null
            )

            // 커서에서 장소 정보 읽기
            if (cursor.moveToNext()) {
                str_placeaddress =
                    cursor.getString(cursor.getColumnIndex("placeaddress")).toString()
                str_placecall = cursor.getString(cursor.getColumnIndex("placecall")).toString()
                placecost = cursor.getInt(cursor.getColumnIndex("placecost"))
                str_placecomment =
                    cursor.getString(cursor.getColumnIndex("placecomment")).toString()
                byteArray = cursor.getBlob(cursor.getColumnIndex("placeimage"))
            }

            // 이미지가 있는 경우 Bitmap으로 변환하여 ImageView에 설정
            if (byteArray != null) {
                var bitmap = byteArrayToBitmap(byteArray)
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    imageView.setImageDrawable(null)
                }
            } else {
                imageView.setImageDrawable(null)
            }

            cursor.close()
        } catch (e: Exception){

        } finally {
            tvmyplaceName.text=str_placename
            tvmyplaceAddress.text=str_placeaddress
            tvmyplaceCall.text=str_placecall
            tvmyplaceCost.text=""+placecost
            tvmyplaceComment.text=str_placecomment+"\n"
            sqlitedb.close()
            dbManager.close()
        }



        // 장소 이름 클릭 시 후기 작성 창으로 이동
        tvmyplaceName.setOnClickListener {
            val intent = Intent(this, addMypageActivity::class.java)
            intent.putExtra("placename", str_placename)
            startActivity(intent)
        }

    }

    // 로그인 상태 확인
    private fun isLoggedIn(context: Context): Boolean {
        return SessionManager.getUserId(context) != null
    }

    // 사용자 데이터 가져오기
    private fun fetchUserData(userId: String?) {
        if (userId.isNullOrBlank()) {
            Toast.makeText(this, "유효하지 않은 사용자 ID", Toast.LENGTH_SHORT).show()
            return
        }

        val dbManager = MyDatabaseHelper(this, "tripDB.db", null, 1)
        val cursor = dbManager.getUserCourses(userId)

        cursor.close()
    }

    // 메뉴 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_myplace_info, menu)
        return true
    }

    // 메뉴 항목 선택 처리
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

                // 삭제 후 makeCourseActivity로 이동
                val intent = Intent(this, makeCourseActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}