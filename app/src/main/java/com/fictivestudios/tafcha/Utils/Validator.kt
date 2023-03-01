package com.fictivestudios.tafcha.Utils


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Matcher
import java.util.regex.Pattern


/** Valid Email Check Method */
fun String.isNotValidEmail() = !Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z]{2,5}" +
            ")+"
).matcher(this.trim())
    .matches()


/** Valid Password Check Method */
fun String.isNotValidPassword(): Boolean {
    val pattern: Pattern
    val passwordPattern =
        "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@*#$%^&+=!])(?=\\S+$).{4,}$"
    pattern = Pattern.compile(passwordPattern)
    val matcher: Matcher = pattern.matcher(this.trim())
    return !matcher.matches()
}


/** Valid Mobile Check Method */
fun String.isNotMobileValid() = !Pattern.compile("([0-9]{7,15})")
    .matcher(this.trim())
    .matches()


fun Context.validationMessage(message: String) =
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

//fun Context.nextsevendays():String
//{
//
//
//}