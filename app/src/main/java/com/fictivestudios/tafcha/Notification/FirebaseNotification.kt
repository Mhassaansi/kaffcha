package com.fictivestudios.tafcha.Notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context

import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat


import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.activities.SplashActivity
import com.fictivestudios.tafcha.models.notification.NotificationItem
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.security.AccessController.getContext


class FirebaseNotification : FirebaseMessagingService() {

    private val notificationData by lazy { NotificationItem() }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e("dfgdfgdfgdf", "fgfdgdfgdf data ${remoteMessage.data}")
        Log.e("dfgdfgdfgdf", "fgfdgdfgdf notification ${remoteMessage.notification}")

//        remoteMessage.notification.let {
//            notificationData.title = it?.title ?: getString(R.string.app_name)
//            notificationData.description = it?.body ?: getString(R.string.app_name)
//        }

        if (remoteMessage.data.isNotEmpty()) {
            remoteMessage.data.let { data ->

                notificationData.title = data["title"] ?: getString(R.string.app_name)


                notificationData.description = data["body"] ?: getString(R.string.app_name)

                notificationData.notification_type = data["type"] ?: getString(R.string.app_name)

            }
        }

        sendNotification()

    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification() {
        val notificationIntent: Intent
        if (isAppRunning()) {
            notificationIntent = Intent(this, MainActivity::class.java)
        } else {
            notificationIntent = Intent(this, SplashActivity::class.java)
        }

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        notificationIntent.putExtra("type", notificationData.notification_type)
        val contentIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, packageName)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(notificationData.title ?: "")
            .setContentText(notificationData.description ?: "")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            // .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(contentIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                packageName,
                notificationData.title,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = notificationData.description
                enableVibration(true)
                setShowBadge(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setAllowBubbles(true)
                }
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun navigate(bundle: Bundle) {


    }
    fun isAppRunning() : Boolean {

        val services = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
        return services.firstOrNull{it.processName.equals("com.fictivestudios.tafcha",true)} != null
    }


//    private fun getUserPendingIntent(destinationId: Int, bundle: Bundle? = null): PendingIntent =
//        NavDeepLinkBuilder(this)
//            .setComponentName(UserMain::class.java)
//            .setGraph(R.navigation.user_flow)
//            .setDestination(destinationId)
//            .setArguments(bundle)
//            .createPendingIntent()

    /**
     * Pending Intent Handle
     * */
//    private fun getCleanerPendingIntent(destinationId: Int, bundle: Bundle? = null): PendingIntent =
//        NavDeepLinkBuilder(this)
//            .setComponentName(CleanerMain::class.java)
//            .setGraph(R.navigation.cleaner_flow)
//            .setDestination(destinationId)
//            .setArguments(bundle)
//            .createPendingIntent()


}