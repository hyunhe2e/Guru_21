//내 코스에 장소 추가하기(입력)

package com.example.guru_21

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity
import com.squareup.okhttp.Address


class addMyCourseActivity : AppCompatActivity() {
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var btnAddMyplace: Button
    lateinit var btnfind: Button
    lateinit var edtPlaceName: EditText
    lateinit var edtPlaceAddress: EditText
    lateinit var edtPlaceCall: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mycourse)

        btnAddMyplace = findViewById(R.id.btnAddMyplace)
        btnfind = findViewById(R.id.btnfind)
        edtPlaceName =findViewById(R.id.edtPlaceName)
        edtPlaceAddress = findViewById(R.id.edtPlaceAddress)
        edtPlaceCall = findViewById(R.id.edtPlaceCall)


        dbManager = MyDatabaseHelper(this, "myCourseDB", null, 1)

        btnAddMyplace.setOnClickListener {
            var str_placename: String = edtPlaceName.text.toString()
            var str_placeaddress: String = edtPlaceAddress.text.toString()
            var str_placecall: String = edtPlaceCall.text.toString()


            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO mycourse VALUES ('"+str_placename+"', '"+str_placeaddress+"', '"+str_placecall+"');")
            sqlitedb.close()

            val intent = Intent(this, makeCourseActivity::class.java)
            intent.putExtra("intent_name", str_placename);
            startActivity(intent)
        }

        btnfind.setOnClickListener{
            var intent = Intent(this, courseActivity::class.java)
            startActivity(intent)
        }

    }


}