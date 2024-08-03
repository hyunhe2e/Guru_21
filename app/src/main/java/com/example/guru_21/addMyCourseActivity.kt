//내 코스에 장소 추가하기(입력)

package com.example.guru_21

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guru_21.databinding.ActivityAddMycourseBinding

class addMyCourseActivity : AppCompatActivity() {

    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var btnAddMyplace: Button
    lateinit var btnfind: Button
    lateinit var edtPlaceName: EditText
    lateinit var edtPlaceAddress: EditText
    lateinit var edtPlaceCall: EditText
    lateinit var edtPlaceCost: EditText
    lateinit var edtPlaceComment: EditText
    private lateinit var binding: ActivityAddMycourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMycourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 상태 확인
        if (isLoggedIn(this)) {
            handleGoogleInfo()
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

    private fun setupViews() {
        btnAddMyplace = findViewById(R.id.btnAddMyplace)
        btnfind = findViewById(R.id.btnfind)
        edtPlaceName = findViewById(R.id.edtPlaceName)
        edtPlaceAddress = findViewById(R.id.edtPlaceAddress)
        edtPlaceCall = findViewById(R.id.edtPlaceCall)
        edtPlaceCost = findViewById(R.id.edtPlaceCost)
        edtPlaceComment = findViewById(R.id.edtPlaceComment)

        // 구글 정보 가져오기
        val receiveName = intent.getStringExtra("SendName")
        val receiveAddr = intent.getStringExtra("SendAddr")
        val receiveNum = intent.getStringExtra("SendNum")
        edtPlaceName.setText(receiveName)
        edtPlaceAddress.setText(receiveAddr)
        edtPlaceCall.setText(receiveNum)

        dbManager = MyDatabaseHelper(this, "tripDB", null, 1)

        btnAddMyplace.setOnClickListener {
            addPlaceToDatabase()
        }

        btnfind.setOnClickListener {
            val intent = Intent(this, courseActivity::class.java)
            startActivity(intent)
        }

        // 사진 버튼 이벤트
        binding.Btnpicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }
    }

    private fun addPlaceToDatabase() {
        val strPlaceName = edtPlaceName.text.toString()
        val strPlaceAddress = edtPlaceAddress.text.toString()
        val strPlaceCall = edtPlaceCall.text.toString()
        val strPlaceCost = edtPlaceCost.text.toString()
        val strPlaceComment = edtPlaceComment.text.toString()

        sqlitedb = dbManager.writableDatabase
        sqlitedb.execSQL("INSERT INTO mycourse(placename, placeaddress, placecall, placecost, placecomment) VALUES (?, ?, ?, ?, ?)",
            arrayOf(strPlaceName, strPlaceAddress, strPlaceCall, strPlaceCost, strPlaceComment))
        sqlitedb.close()

        val intent = Intent(this, makeCourseActivity::class.java)
        intent.putExtra("intent_name", strPlaceName)
        startActivity(intent)
    }

    private fun isLoggedIn(context: Context): Boolean {
        return SessionManager.getUserId(context) != null
    }


    private fun handleGoogleInfo() {
        fetchUserData(SessionManager.getUserId(this))
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

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // 결과 OK
        if (it.resultCode == RESULT_OK && it.data != null) {
            val uri = it.data!!.data

            Glide.with(this)
                .load(uri)
                .into(binding.Imagepicture)
        }
    }
}
