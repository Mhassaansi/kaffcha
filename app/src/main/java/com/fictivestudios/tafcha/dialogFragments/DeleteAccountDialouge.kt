package com.fictivestudios.tafcha.dialogFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.*
import com.fictivestudios.tafcha.fragments.profile.PrivacyPolicyFragment
import com.fictivestudios.tafcha.fragments.profile.TermsConditionsFragment
import com.fictivestudios.tafcha.fragments.registarion_module.LoginFragment
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_dialogue_agreement.view.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_success_dialogue.view.*


class DeleteAccountDialouge(listener: View.OnClickListener?,listener1: View.OnClickListener?) : BaseFragment() {


    private lateinit var mView: View
    var listener = listener
    var listener1 = listener1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.del_acnt_fragment, container, false)



        mView.btn_accept.setOnClickListener(listener)
        mView.btn_reject.setOnClickListener(listener1)
//        mView.btn_reject.setOnClickListener {
//            getRegisterationActivity?.onBackPressed()
//        }



        return mView
    }

}