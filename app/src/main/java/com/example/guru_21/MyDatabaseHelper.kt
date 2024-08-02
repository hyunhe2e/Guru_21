package com.example.guru_21

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase) {
        //mypage
        db.execSQL("CREATE TABLE travel_diary(userID text PRIMARY KEY, title text, content text, " + "FOREIGN KEY(userID) REFERENCES Member(NAME))")
        //review
        db.execSQL("CREATE TABLE review(userID text PRIMARY KEY, title text, content text, " + "FOREIGN KEY(userID) REFERENCES Member(NAME))")

        //addMyCourse, makeCourse, mypageInfo
        db.execSQL("CREATE TABLE  mycourse(userID text PRIMARY KEY, courseID text, placename text, placeaddress text, placecall text, placecost INTEGER, placecomment text, review text, "
                + "FOREIGN KEY(userID) REFERENCES Member(NAME))")

        // course_page
        db.execSQL("CREATE TABLE all_course(userID text PRIMARY KEY, title text, content text, " +
                "FOREIGN KEY(userID) REFERENCES Member(NAME))")


        //회원관리
        db.execSQL("CREATE TABLE Member (" +
                "NAME text PRIMARY KEY, " +
                "PWD VARCHAR(15), " +
                "EMAIL VARCHAR(30));")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
    // ID에 따른 코스 조회
    fun getUserCourses(userId: String): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM mycourse WHERE userID = ?", arrayOf(userId))
    }

    // ID에 따른 전체 코스 조회
    fun getAllCourses(userId: String): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM all_course WHERE userID = ?", arrayOf(userId))
    }
}
