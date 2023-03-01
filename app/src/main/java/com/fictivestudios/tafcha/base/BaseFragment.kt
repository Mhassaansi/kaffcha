package com.technado.demoapp.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.activities.RegisterationActivity
import com.fictivestudios.tafcha.Utils.BasePreferenceHelper
import com.fictivestudios.tafcha.Utils.Titlebar


abstract class BaseFragment : Fragment() {
    var getActivityContext: MainActivity? = null

    var getRegisterationActivity: RegisterationActivity? = null

    var preferenceHelper: BasePreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPreferenceHelper()
    }

    abstract fun setTitlebar(titlebar: Titlebar)

    fun getRegisterationActivity(): RegisterationActivity? {
        return getRegisterationActivity
    }

    fun getActivityContext(): MainActivity? {
        return getActivityContext
    }
    fun setPreferenceHelper() {
        if (preferenceHelper == null) {
            preferenceHelper = BasePreferenceHelper(getActivityContext)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            val contex = context as MainActivity?
            if (contex != null)
                getActivityContext = context
        }
        else  if (context is RegisterationActivity) {
            val contex = context as RegisterationActivity?
            if (contex != null)
                getRegisterationActivity = context
        }
    }

    override fun onResume() {
        super.onResume()
        if (getActivityContext != null) {
           setTitlebar(getActivityContext!!.getTitlebar())
        }
    }
}