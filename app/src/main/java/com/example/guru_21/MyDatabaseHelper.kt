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

        //addMyCourse, makeCourse, mypageInfo
        db.execSQL("CREATE TABLE  mycourse(userID text PRIMARY KEY, courseID text, placename text, placeaddress text, placecall text, placecost INTEGER, placecomment text, review text)")

        // course_page
        db.execSQL("CREATE TABLE all_course(postID int PRIMARY KEY, userID text, title text, content text, " +
                "FOREIGN KEY(userID) REFERENCES Member(NAME))")


        //보현 로그인 관련
        db.execSQL("CREATE TABLE Member (" +
                "NAME CHAR(20) PRIMARY KEY, " +
                "PWD VARCHAR(15), " +
                "EMAIL VARCHAR(30));")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

}