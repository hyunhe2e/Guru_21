package com.example.guru_21

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE travel_diary(title text, content text)")
        db.execSQL("CREATE TABLE  mycourse(placename text, placecost INTEGER, placecomment text)")

        // 정현 course 사용
        db.execSQL("CREATE TABLE User (" + "user_id varchar(20) PRIMARY KEY, " + "pwd varchar(20));");

        db.execSQL("CREATE TABLE Post (" + "id INTEGER PRIMARY KEY, " + "user_id INTEGER, " +
                "title TEXT, " + "status TEXT CHECK(status IN ('1', '2')), " + "FOREIGN KEY (user_id) REFERENCES User(id));");

        db.execSQL("CREATE TABLE Course (" + "course_id INTEGER PRIMARY KEY, " + "post_id INTEGER, " +
                "user_id INTEGER, " + "place_name VARCHAR(30), " + "place_addr VARCHAR(255), " + "place_num VARCHAR(20), " +
                "place_cost INTEGER, " + "memo TEXT, " + "FOREIGN KEY (post_id) REFERENCES Post(id), " +
                "FOREIGN KEY (user_id) REFERENCES User(id));");

        //보현 로그인 관련
        db.execSQL("CREATE TABLE Member (" +
                "NAME CHAR(20) PRIMARY KEY, " +
                "PWD VARCHAR(15), " +
                "EMAIL VARCHAR(30));")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

}