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




class DialogueAgreement : BaseFragment() {


    private lateinit var mView: View
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

        mView = inflater.inflate(R.layout.fragment_dialogue_agreement, container, false)



        mView.btn_accept.setOnClickListener {

            if (!mView.tv_terms.isChecked || !mView.tv_privacy.isChecked)
            {
                Toast.makeText(context, getString(R.string.please_accept_terms), Toast.LENGTH_SHORT).show()
            }
            else
            {
                getRegisterationActivity?.
                replaceFragment(LoginFragment(),
                    LoginFragment().javaClass.simpleName,
                    true,true)
            }


        }

        mView.btn_reject.setOnClickListener {
            getRegisterationActivity?.onBackPressed()
        }


        mView.text_term.setOnClickListener {
            getRegisterationActivity?.
                    replaceFragment(
                        TermsConditionsFragment(),
                    TermsConditionsFragment().javaClass.simpleName,
                    true,true)
        }

        mView.text_privacy.setOnClickListener {
            getRegisterationActivity?.
            replaceFragment(
                PrivacyPolicyFragment(),
                PrivacyPolicyFragment().javaClass.simpleName,
                true,true)
        }

        return mView
    }

}