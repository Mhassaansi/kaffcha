package com.fictivestudios.tafcha.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.text.format.DateFormat
import android.widget.Toast
import com.fictivestudios.tafcha.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Constants {
    companion object {

        var exdesp:String = " "

        const val IS_USER = "is_user"
        const val IS_LOGIN = "forget_password"

        var height:Int=0
        var weight:Int=0

        fun isValidEmail(str: String): Boolean{
            return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
        }


        fun dateWithTime():String{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              var  current =  LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatted = current.format(formatter)
                println("Current Date and Time is: $formatted")
                return formatted
            } else {
               // val s = DateFormat.format("yyyy-MM-dd HH:mm:ss")
                var current = ""
                val d = Date()
                val s = DateFormat.format("yyyy-MM-dd HH:mm:ss", d.time)
                val formatted = current.format(s)
                return formatted
            }


        }

        fun isNetworkConnected(context: Context, showToast: Boolean):Boolean{
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm.activeNetworkInfo == null) {
                if(showToast)
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT)
                return false
            }
           return true
        }

    }
}