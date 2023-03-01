package com.fictivestudios.tafcha.Utils

import android.app.Activity
import android.content.Context

open class PreferenceHelper {
    public fun putBooleanPreference(
        context: Context, prefsName: String,
        key: String, value: Boolean
    ) {
        val preferences = context.getSharedPreferences(
            prefsName,
            Activity.MODE_PRIVATE
        )
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    public fun getBooleanPreference(
        context: Context, prefsName: String,
        key: String
    ): Boolean {
        val preferences = context.getSharedPreferences(
            prefsName,
            Activity.MODE_PRIVATE
        )
        return preferences.getBoolean(key, false)
    }

    fun putStringPreference(
        context: Context, prefsName: String,
        key: String, value: String
    ) {
        val preferences = context.getSharedPreferences(
            prefsName,
            Activity.MODE_PRIVATE
        )
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    protected fun getStringPreference(
        context: Context,
        prefsName: String,
        key: String
    ): String {
        val preferences = context.getSharedPreferences(
            prefsName,
            Activity.MODE_PRIVATE
        )
        return preferences.getString(key, "")!!
    }
}