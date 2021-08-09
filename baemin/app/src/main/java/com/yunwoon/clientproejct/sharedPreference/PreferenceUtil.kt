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

    fun clear() {
        editor.clear().apply()
    }
}