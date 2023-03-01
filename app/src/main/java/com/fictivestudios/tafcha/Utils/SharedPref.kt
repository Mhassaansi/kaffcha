package com.fictivestudios.tafcha.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    val mSharedPref: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun <T> saveData(key: String, obj: T) {
        val prefsEditor = mSharedPref.edit()
        if (obj is String) {
            prefsEditor.putString(key, obj.toString())
        } else if (obj is Int) {
            prefsEditor.putInt(key, obj).apply()
        } else if (obj is Boolean) {
            prefsEditor.putBoolean(key, obj)
        } else {
            prefsEditor.putString(key, obj.toString())
        }
        prefsEditor.apply()
    }

    fun <T : Any> getData(key: String): T {
        val obj: T? = null
        if (obj is String) {
            mSharedPref.getString(key, "")
        } else if (obj is Int) {
            mSharedPref.getInt(key, 10000)
        } else if (obj is Boolean) {
            mSharedPref.getBoolean(key, false)
        }
        return obj!!
    }

    fun read(key: String?, defValue: String?): String? {
        return mSharedPref.getString(key, defValue)
    }

    fun read(key: String?, defValue: Int): Int {
        return mSharedPref.getInt(key, defValue)
    }

    fun read(key: String?, defValue: Boolean): Boolean {
        return mSharedPref.getBoolean(key, defValue)
    }

    fun write(key: String?, value: String?) {
        val prefsEditor = mSharedPref.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun write(key: String?, value: Boolean) {
        val prefsEditor = mSharedPref.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    fun write(key: String?, value: Int?) {
        val prefsEditor = mSharedPref.edit()
        prefsEditor.putInt(key, value!!).apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun remove(key: String?) {
        val prefsEditor = mSharedPref.edit()
        prefsEditor.remove(key)
    }
}