package com.fictivestudios.tafcha.Utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


class BasePreferenceHelper(activityContext: Context?) : PreferenceHelper() {
    var context: Context? = activityContext
    var activity: Activity? = null
    val AUTHENTICATE_USER_TOKEN = "user_token"
    val FIREBASE_TOKEN = "firebase_token"
    val TOKEN = "token"
    val AUTHENTICATE_PURCHASE_STATUS = "purchase_status"
    val LOGIN_STATUS = "login_status"
    val USER = "user_model"
    val FILENAME = "preferences"
    var prefrnce = PreferenceHelper()

    fun BasePreferenceHelper(c: Context) {
        this.context = c
    }

    fun getSharedPreferences(): SharedPreferences {
        return activity!!.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE)
    }

    fun setLoginStatus(status: String?) {
        status?.let { putStringPreference(context!!, FILENAME, LOGIN_STATUS, it) }
    }

    fun getLoginStatus(): String {
        return getStringPreference(context!!, FILENAME, LOGIN_STATUS)
    }
/*
    fun putUser(user: UserModel) {
        putStringPreference(
            context!!, FILENAME, USER, GsonBuilder()
                .create().toJson(user)
        )
    }

    fun getUser(): UserModel? {
        try {
            return GsonBuilder().create().fromJson(
                getStringPreference(context!!, FILENAME, USER), UserModel::class.java
            )
        } catch (e: Exception) {
            return null
        }
    }*/
}