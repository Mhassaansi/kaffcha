package com.fictivestudios.tafcha.fragments.registarion_module

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar

import com.fictivestudios.tafcha.models.Login.LoginResultpojo
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_verification.view.*
import kotlinx.android.synthetic.main.fragment_verification.view.btn_back
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response


class VerificationFragment(var v_type:Boolean,var user_id: Int? = 0,var email:String) : BaseFragment() {


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

        mView = inflater.inflate(R.layout.fragment_verification, container, false)

        startCountDown(45000)

        mView.btn_continue.setOnClickListener {
            if(v_type==true) {
                forgetOTP()
            }
            else if(v_type==false){
                verifyOTP()
            }

        }

        mView.btn_back.setOnClickListener {
            getRegisterationActivity?.onBackPressed()
        }




        mView.tv_Link_resend.movementMethod = LinkMovementMethod.getInstance()
        val spans: Spannable = mView.tv_Link_resend.text as Spannable
        val clickSpan: ClickableSpan = object : ClickableSpan() {


            override fun onClick(widget: View) {

                if(v_type == true){

                    forgetPasswordApi()
                }
                else{
                    resendOTP()
                }


            }
        }
        spans.setSpan(clickSpan, 20, spans.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return mView
    }

    private fun startCountDown(startTime: Long) {

        object : CountDownTimer(startTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mView.tv_timer.text = "00:" + millisUntilFinished / 1000
                mView.circularProgressBar.setProgressWithAnimation(100f, 4500)
                mView.circularProgressBar.progressMax = 100f
            }

            override fun onFinish() {

            }
        }.start()
    }

    private fun forgetOTP(){
        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(
                        getRegisterationActivity!!, true, "",
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.forgetPasswordVerify(
                                    JSONObject().apply {
                                        put("otp", mView.otp_view.text.toString())
                                        put("email", email)
                                    }.toString().getJsonRequestBody()
                                )
                            }

                            override fun success(response: Response<CommonResponseDC>) {

                                if (response.body()?.status==1  && response.body()?.message != "record not found..!") {
                                    response.body().let {
                                        getRegisterationActivity?.AddFragment(
                                            ResetPasswordFragment(email, mView.otp_view.text.toString().toInt()),
                                            ResetPasswordFragment(
                                                email,
                                                mView.otp_view.text.toString().toInt()
                                            ).javaClass.simpleName,
                                            false, false
                                        )
                                    }
                                } else {
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
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

    private fun verifyOTP() {
        if(Constants.isNetworkConnected(requireActivity(), true)) {
            lifecycleScope.launch {
                try {
                    RetrofitSetup().callApi(
                        getRegisterationActivity!!, true, "",
                        object : CallHandler<Response<LoginResultpojo>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<LoginResultpojo> {
                                return apiInterFace.verifyOtp(
                                    JSONObject().apply {
                                        put("otp", mView.otp_view.text.toString())
                                        put("user_id", user_id)

                                    }.toString().getJsonRequestBody()
                                )
                            }

                            override fun success(response: Response<LoginResultpojo>) {


                                if(response.body()?.status==1  && response.body()?.message != "record not found..!"){
                                    if(response.body()?.data?.role=="trainer"){
                                        PreferenceUtils.saveString("token", response.body()?.bearer_token.toString())
                                        getRegisterationActivity?.AddFragment(
                                            SetupProfileTrainerFragment(false,false,""),
                                            SetupProfileTrainerFragment(false,false,"").javaClass.simpleName,
                                            false, false
                                        )
                                    }
                                    else{
                                        PreferenceUtils.saveString("token", response.body()?.bearer_token.toString())
                                        getRegisterationActivity?.AddFragment(
                                            SignupUserFragment(false,false,""),
                                            SignupUserFragment(false,false,"").javaClass.simpleName,
                                            false, false
                                        )
                                    }
                                }
                                else {
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
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

    private fun resendOTP() {
        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(
                        getRegisterationActivity!!, true, "",
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.resend_verifyOtp(
                                    JSONObject().apply {
                                       // put("email", loginData.email)
                                        put("user_id", user_id)

                                    }.toString().getJsonRequestBody()
                                )
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if(response.body()?.status==1  && response.body()?.message != "record not found..!"){

                                    Toast.makeText(requireActivity(),response.body()?.message,Toast.LENGTH_LONG).show()
                                }

                                else{
                                    Toast.makeText(requireActivity(),response.body()?.message,Toast.LENGTH_LONG).show()

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

    private fun forgetPasswordApi() {
        if(Constants.isNetworkConnected(requireActivity(), true)) {
            lifecycleScope.launch {
                try {

                    RetrofitSetup().callApi(getRegisterationActivity!!, true, "",
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.forgetPassword(
                                    JSONObject().apply {
                                        put("email", email)
                                    }.toString().getJsonRequestBody()
                                )
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if (response.body()?.status==1  && response.body()?.message != "record not found..!") {
                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()

                                } else {
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



}