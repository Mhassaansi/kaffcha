package com.fictivestudios.tafcha.fragments.registarion_module

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.*
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.models.Login.LoginResultpojo
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.btn_back
import kotlinx.android.synthetic.main.fragment_login.view.et_email
import kotlinx.android.synthetic.main.fragment_login.view.et_pass
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception


class LoginFragment : BaseFragment() {


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
        mView = inflater.inflate(R.layout.fragment_login, container, false)








            mView.tv_Link_signup.setOnClickListener {

                getRegisterationActivity()?.
                replaceFragment(SelectUserFragment(false,false),
                    SelectUserFragment(false,false).javaClass.simpleName,true,true)
            }

//                if (PreferenceUtils.getBoolean(IS_USER))
//                {
//                    getRegisterationActivity()?.
//                    replaceFragment(SignupTrainerFragment(),SignupTrainerFragment().javaClass.simpleName,true,true)
//
//                }
//                else
//                {
//                    getRegisterationActivity()?.
//                    replaceFragment(SignupTrainerFragment(),SignupTrainerFragment().javaClass.simpleName,true,true)
//
//                }

           // }
//        }
//        spans.setSpan( clickSpan, 23, spans.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)





        mView.btn_login?.setOnClickListener {

            validateFields()

        }
        mView.btn_back.setOnClickListener {
            getRegisterationActivity?.
            onBackPressed()
        }
        mView.tv_forget_pass.setOnClickListener {
           // PreferenceUtils.saveBoolean(F_PASS,true)
            getRegisterationActivity?.
            replaceFragment(ForgotPasswordFragment(),ForgotPasswordFragment().javaClass.simpleName,true,true)

        }
        mView.loginpassvisibilty.setOnClickListener {
            showPassword1()
        }

        return mView

    }

    private fun validateFields() {

        if (mView.et_email.text.toString().isNullOrBlank()&&
            mView.et_pass.text.toString().isNullOrBlank()
             )
        {
            Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT).show()
            return
        }

        if (!Constants.isValidEmail(mView.et_email.text.toString()))
        {
            mView.et_email.setError(getString(R.string.invalid_email_format))
            return
        }
        if (mView.et_pass.text.toString().length <8 )
        {
            mView.et_pass.setError(getString(R.string.password_must_be))
            return
        }
        else
        {
           loginApi()
        }
    }

    private fun loginApi() = getFirebaseToken { token ->

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(getRegisterationActivity!!, true, "",
                        object : CallHandler<Response<LoginResultpojo>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<LoginResultpojo> {

                                return apiInterFace.login(
                                    JSONObject().apply {
                                        put("email", mView.et_email.text.toString())
                                        put("password", mView.et_pass.text.toString())
                                        put("device_type", "android")
                                        put("device_token", token)
                                    }.toString().getJsonRequestBody()
                                )
                            }

                            override fun success(response: Response<LoginResultpojo>) {

                                response.body().let {
                                    if (it!!.status == 0 && it.message == "Invalid Creidentials...!") {
                                        Toast.makeText(
                                            requireActivity(),
                                            it.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else if (it.status == 0 && it.message == "The selected email is invalid.") {
                                        Toast.makeText(
                                            requireActivity(),
                                            it.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    else if (it.status == 0 && it.message=="verify email first") {
                                          Log.d("loginrespo",it.message.toString())
                                        PreferenceData.storeLoginData(requireActivity(), it.data)
                                       // PreferenceData.storeProfileData(requireActivity(),it.data)
                                        getRegisterationActivity!!.replaceFragment(
                                            VerificationFragment(false,it.data.id, ""),
                                            VerificationFragment(false,it.data.id, "").javaClass.simpleName,
                                            false,
                                            true
                                        )
                                    }
                                    else {
                                        if (it.data.role == "trainer") {
                                            PreferenceUtils.saveBoolean(Constants.IS_USER, false)
                                            PreferenceUtils.saveBoolean(Constants.IS_LOGIN,true)
                                            PreferenceData.storeLoginData(
                                                requireActivity(),
                                                it.data
                                            )
                                            //store barer token
                                            PreferenceUtils.saveString(
                                                "token",
                                                it.bearer_token.toString()
                                            )
                                            startActivity(Intent(context, MainActivity::class.java))
                                            requireActivity().finish()

                                        } else {
                                            PreferenceUtils.saveBoolean(Constants.IS_USER, true)
                                            PreferenceData.storeLoginData(
                                                requireActivity(),
                                                it.data
                                            )
                                            //Save Login state
                                            PreferenceUtils.saveBoolean(Constants.IS_LOGIN,true)
                                            //store barer token
                                            PreferenceUtils.saveString(
                                                "token",
                                                it.bearer_token.toString()
                                            )
                                            startActivity(Intent(context, MainActivity::class.java))
                                            requireActivity().finish()

                                        }

                                    }

                                }

                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }


                        }
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else{
                Toast.makeText(requireActivity(), R.string.internetconnection, Toast.LENGTH_LONG).show()
            }
        }
    }


    fun showPassword1(){


        if(mView.et_pass.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
            mView.loginpassvisibilty.setImageResource(R.drawable.password_show)
            mView.et_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
        else{
            mView.loginpassvisibilty.setImageResource(R.drawable.passwod_hide)

            //Hide Password
            mView.et_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }



}