//내 코스에 장소 추가하기(입력)

package com.example.guru_21

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity


class addMyCourseActivity : AppCompatActivity() {
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var btnAddMyplace: Button
    lateinit var edtPlaceName: EditText
    lateinit var edtPlaceCost: EditText
    lateinit var edtPlaceComment: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mycourse)

        btnAddMyplace = findViewById(R.id.btnAddMyplace)
        edtPlaceName =findViewById(R.id.edtPlaceName)
        edtPlaceCost = findViewById(R.id.edtPlaceCost)
        edtPlaceComment = findViewById(R.id.edtPlaceComment)


        dbManager = MyDatabaseHelper(this, "myCourseDB", null, 1)

        btnAddMyplace.setOnClickListener {
            var str_placename: String = edtPlaceName.text.toString()
            var str_placecost: String = edtPlaceCost.text.toString()
            var str_placecomment: String = edtPlaceComment.text.toString()


            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO mycourse VALUES ('"+str_placename+"', '"+str_placecost+"', '"+str_placecomment+"');")
            sqlitedb.close()

            val intent = Intent(this, makeCourseActivity::class.java)
            intent.putExtra("intent_name", str_placename);
            startActivity(intent)
        }
    }


}