package com.example.guru_21

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "user_session"  // SharedPreferences 파일 이름
    private const val KEY_USER_ID = "USER_ID"    // 사용자 ID 키
    private const val KEY_AUTH_TOKEN = "AUTH_TOKEN"  // 인증 토큰 키

    // 사용자 ID를 SharedPreferences에서 가져오는 함수
    fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_USER_ID, null)
    }

    // 인증 토큰을 SharedPreferences에서 가져오는 함수
    fun getAuthToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_AUTH_TOKEN, null)
    }

    // 인증 토큰을 SharedPreferences에 저장하는 함수
    fun setAuthToken(context: Context, authToken: String) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(KEY_AUTH_TOKEN, authToken)
        editor.apply()  // 변경 사항 저장
    }

    // 모든 세션 데이터를 SharedPreferences에서 삭제하는 함수
    fun clearSession(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()  // 변경 사항 저장
    }
}

