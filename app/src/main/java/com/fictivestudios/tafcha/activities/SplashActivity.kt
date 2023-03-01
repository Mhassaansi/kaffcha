package com.fictivestudios.tafcha.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fictivestudios.tafcha.R
import android.content.Intent
import android.util.Log
import com.fictivestudios.tafcha.Notification.FCMEnums
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.Constants.Companion.IS_LOGIN
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.fragments.live_other.HomeFragment
import java.util.*


class SplashActivity : AppCompatActivity() {
    lateinit var extras:Bundle

//    override fun onNewIntent(intent: Intent?) {
//        if(intent?.extras!=null) {
//            extras = intent.extras!!
//        }
//        super.onNewIntent(intent)
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ///lateinit var extras:Bundle
         var nKey:String?=null
        val notificationIntent: Intent
        PreferenceUtils.init(this)

 if(PreferenceUtils.getString("language") != null){

     if(PreferenceUtils.getString("language")=="ar"){
         setLocale(this,"ar")
     }
     else if(PreferenceUtils.getString("language")=="fr"){
         setLocale(this,"fr")
     }
     else if(PreferenceUtils.getString("language")=="ru"){
         setLocale(this,"ru")
     }
     else if(PreferenceUtils.getString("language")=="de"){
         setLocale(this,"de")
     }
     else if(PreferenceUtils.getString("language")=="zh"){
         setLocale(this,"zh")
     }
     else {
         setLocale(this,"en")

     }

 }





        if(PreferenceUtils.getBoolean(IS_LOGIN)){

            if(intent.extras!=null) {
                extras = intent.extras!!
                if (extras.getString("title") != null) {
                    nKey = extras.getString("title")!!
                    PreferenceUtils.saveString("Noti",nKey)
                    //Log.e("Text777777777777777", "" + nKey)
                    when(nKey){

                        FCMEnums.CHALLENGE_Recieved.value -> {

                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type",  nKey)
                            startActivity(notificationIntent)
                            finish()

                        }
                        FCMEnums.CHALLENGE_Accept.value -> {

                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }
                        FCMEnums.CHALLENGE_WON.value -> {

                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }
                        FCMEnums.CHALLENGE_LOST.value -> {

                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }
                        FCMEnums.EVENT_CREATE.value -> {
                            // setMainFrameLayoutID()
                            // AddFragment(HomeFragment(), HomeFragment().javaClass.simpleName,false,false)
                        }
                        FCMEnums.EVENT_STRAT_TIME.value ->  {
//                        setMainFrameLayoutID()
//                        AddFragment(HomeFragment(), HomeFragment().javaClass.simpleName,false,false)
                        }
                        FCMEnums.LIVE_STREAM_STRATED.value ->  {
                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }
                        FCMEnums.REMINDER.value ->  {
                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }
                        FCMEnums.FRIEND_REQUEST_Recieved.value -> {

                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }
                        FCMEnums.FRIEND_REQUEST_ACCEPT.value -> {

                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }
                        FCMEnums.ADD_TRAINER.value ->{

                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }

                        FCMEnums.VOTE_RECIVED.value ->{

                            notificationIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            notificationIntent.putExtra("type", nKey)
                            startActivity(notificationIntent)
                            finish()
                        }

                        else -> null
                    }

                }
                else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
                }
            }
            else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }


        }
        else{
            startActivity(Intent(this@SplashActivity, RegisterationActivity::class.java))
            finish()
        }


    }

    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
//        val refresh = Intent(requireActivity(), MainActivity::class.java)
//        requireActivity().finish()
//        startActivity(refresh)
    }
}