package com.fictivestudios.tafcha.Utils

import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun getFirebaseToken(
    token: (String) -> Unit,
) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            return@addOnCompleteListener
        }
        val token = task.result ?: ""
        Log.e("dsfdsfsdf", "dfdfgdg  ${token}")
        token(token)
    }



    fun showToast(context: Context?, message: String?) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun findGivenDate_next_sevendate(dto:String?=""):String{
        var dt = dto // Start date
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val c: Calendar = Calendar.getInstance()
        c.setTime(sdf.parse(dt))
        c.add(Calendar.DATE, 7) // number of days to add
        dt = sdf.format(c.getTime())
        return  dt
    }

    fun getMediaFilePathFor(
        uri: Uri,
        context: Context
    ): String {
        val returnCursor =
            context.contentResolver.query(uri, null, null, null, null)
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream =
                context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()
            //int bufferSize = 1024;
            val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size %d", "" + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Size %s", file.path)
            Log.e("File Size %d", "" + file.length())
        } catch (e: java.lang.Exception) {
            Log.e("File Size %s", e.message!!)
        }
        return file.path
    }





}