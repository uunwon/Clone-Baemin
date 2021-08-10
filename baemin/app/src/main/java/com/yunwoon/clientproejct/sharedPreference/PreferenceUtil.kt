package com.yunwoon.clientproejct.sharedPreference

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun putString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getInt(key:String, default:Int ): Int {
        return prefs.getInt(key, default)
    }

    fun putInt(key:String, str: Int) {
        prefs.edit().putInt(key, str).apply()
    }

    fun remove(key:String) {
        editor.remove(key).apply()
    }

    fun clear() {
        editor.clear().apply()
    }
}