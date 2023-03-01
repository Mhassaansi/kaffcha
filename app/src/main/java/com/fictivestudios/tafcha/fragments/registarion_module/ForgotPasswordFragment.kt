package com.fictivestudios.tafcha.fragments.registarion_module

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceData
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response


class ForgotPasswordFragment : BaseFragment() {
    private val loginData by lazy { PreferenceData.retrieveLoginData(requireActivity()) }

    private lateinit var mView: View

    override fun setTitlebar(titlebar: Titlebar) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mView = inflater.inflate(R.layout.fragment_forgot_password, container, false)



        mView.btn_get_code.setOnClickListener {
if(mView.et_email.text.isNullOrBlank()){
    mView.et_email.error = getString(R.string.fields_cant_be_empty)
}
           else if (!Constants.isValidEmail(mView.et_email.text.toString())) {
                mView.et_email.error = getString(R.string.invalid_email_format)
            } else {
                forgetPasswordApi()
            }

        }
        mView.btn_back.setOnClickListener {
            getRegisterationActivity?.onBackPressed()
        }
        return mView


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
                                    put("email", mView.et_email.text.toString())
                                }.toString().getJsonRequestBody()
                            )
                        }

                        override fun success(response: Response<CommonResponseDC>) {
                            if (response.body()?.status==1  && response.body()?.message != "record not found..!") {
                                PreferenceUtils.saveBoolean("forgetpassword", true)
                                getRegisterationActivity?.replaceFragment(
                                    VerificationFragment(true,0, mView.et_email.text.toString()),
                                    VerificationFragment(
                                        true,
                                        0,
                                        mView.et_email.text.toString()
                                    ).javaClass.simpleName, true, true
                                )
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