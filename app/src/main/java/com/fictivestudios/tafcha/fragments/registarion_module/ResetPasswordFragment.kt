package com.fictivestudios.tafcha.fragments.registarion_module

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.dialogFragments.SuccessDialogueFragment
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment

import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import kotlinx.android.synthetic.main.fragment_reset_password.view.et_confirm_pass
import kotlinx.android.synthetic.main.fragment_reset_password.view.et_pass
import kotlinx.android.synthetic.main.fragment_reset_password.view.signupsvisibilty
import kotlinx.android.synthetic.main.fragment_reset_password.view.signupsvisibilty1
import kotlinx.android.synthetic.main.fragment_signup_trainer.view.*

import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response



class ResetPasswordFragment(var email:String,var otp:Int) : BaseFragment() {


    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.reset_password))

        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )

        titlebar.setBtnRight(R.drawable.notify_icon,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            NotificationsFragment(),
                            NotificationsFragment().javaClass.simpleName,
                            true, true
                        )
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mView = inflater.inflate(R.layout.fragment_reset_password, container, false)

        mView.btn_continue.setOnClickListener {

            validatePassword()

        }



        mView.signupsvisibilty.setOnClickListener {

            showPassword1()

        }

        mView.signupsvisibilty1.setOnClickListener {

            showPassword2()
        }

        return mView

    }

    private fun validatePassword() {

        if (mView.et_pass.text.toString().length <8 )
        {
            mView.et_pass.setError(getString(R.string.password_must_be))
            return
        }
        if (!mView.et_pass.text.toString().equals(mView.et_confirm_pass.text.toString()) )
        {
            mView.et_confirm_pass.setError(getString(R.string.confirm_pass_doesnt_match))

            return
        }
        else{
            changePasswordApi()

        }
    }


    private fun changePasswordApi(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(getRegisterationActivity!!, true, "",
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.resetPassword(
                                    JSONObject().apply {
                                        put("otp", otp)
                                        put("email", email)
                                        put("new_password", mView.et_confirm_pass.text.toString())
                                    }.toString().getJsonRequestBody()
                                )
                            }

                            override fun success(response: Response<CommonResponseDC>) {

                                if (response.body()?.status==1  && response.body()?.message != "record not found..!") {
                                    getRegisterationActivity?.AddFragment(
                                        SuccessDialogueFragment(getString(R.string.password_reset_successfull),
                                            object : View.OnClickListener {
                                                override fun onClick(p0: View?) {
                                                    getRegisterationActivity
                                                        ?.replaceFragment(
                                                            LoginFragment(),
                                                            LoginFragment().javaClass.simpleName,
                                                            false, true
                                                        )
                                                }
                                            }

                                        ),
                                        SuccessDialogueFragment.javaClass.simpleName,
                                        true, false)

                                } else if (response.body()!!.status == 0) {
                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()

                                }


                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }

                        })


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }


    fun showPassword1(){


        if(mView.et_pass.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
            mView.signupsvisibilty.setImageResource(R.drawable.password_show)
            mView.et_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
        else{
            mView.signupsvisibilty.setImageResource(R.drawable.passwod_hide)

            //Hide Password
            mView.et_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    fun showPassword2(){


        if(mView.et_confirm_pass.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
            mView.signupsvisibilty1.setImageResource(R.drawable.password_show)
            mView.et_confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

        }
        else{
            mView.signupsvisibilty1.setImageResource(R.drawable.passwod_hide)

            //Hide Password
            mView.et_confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance())

        }
    }


}