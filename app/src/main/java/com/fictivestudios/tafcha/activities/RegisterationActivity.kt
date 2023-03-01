package com.fictivestudios.tafcha.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.fragments.registarion_module.LoginFragment
import com.fictivestudios.tafcha.fragments.registarion_module.PreLoginFragment

import com.fictivestudios.tafcha.fragments.registarion_module.SelectUserFragment
import com.technado.demoapp.base.BaseActivity
import java.util.*

class RegisterationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)


        if(!PreferenceUtils.getString("language").equals("")){
            setLocalewithoutRestart(this,PreferenceUtils.getString("language"))
        }

        setMainFrameLayoutID()
        replaceFragment(PreLoginFragment(),PreLoginFragment().javaClass.simpleName,false,false)
    }

    override fun setMainFrameLayoutID() {
      mainFrameLayoutID = R.id.register_container
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun setLocalewithoutRestart(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

    }
}