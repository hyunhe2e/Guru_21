//내 코스의 장소 정보 보기

package com.example.guru_21

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.okhttp.Address

class myPlaceInfoActiviry:AppCompatActivity() {
    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var tvmyplaceName: TextView
    lateinit var tvmyplaceAddress: TextView
    lateinit var tvmyplaceCall: TextView

    lateinit var str_placename: String
    lateinit var str_placeaddress: String
    lateinit var str_placecall: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myplace_info)


        tvmyplaceName = findViewById(R.id.edtplacename)
        tvmyplaceAddress = findViewById(R.id.edtplaceaddress)
        tvmyplaceCall = findViewById(R.id.edtplacecall)


        val intent = intent
        str_placename = intent.getStringExtra("intent_name").toString()

        dbManager = MyDatabaseHelper(this, "myCourseDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM mycourse WHERE placename = '"+str_placename+"';", null)

        if(cursor.moveToNext()) {
            str_placeaddress = cursor.getString(cursor.getColumnIndex("placeaddress")).toString()
            str_placecall = cursor.getString(cursor.getColumnIndex("placecall")).toString()
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()

        tvmyplaceName.text=str_placename
        tvmyplaceAddress.text=str_placeaddress
        tvmyplaceCall.text=str_placecall+"\n"
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
                dbManager = MyDatabaseHelper(this, "myCourseDB", null, 1)
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