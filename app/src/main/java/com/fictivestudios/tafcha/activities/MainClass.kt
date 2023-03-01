package com.fictivestudios.tafcha.activities

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.facebook.FacebookSdk
import com.jakewharton.threetenabp.AndroidThreeTen

class MainClass : Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(getApplicationContext());
       // AndroidThreeTen.init(this)
    }

}