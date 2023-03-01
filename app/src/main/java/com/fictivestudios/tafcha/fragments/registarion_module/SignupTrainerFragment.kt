package com.fictivestudios.tafcha.fragments.registarion_module


import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.Utils.getFirebaseToken
import com.fictivestudios.tafcha.dialogFragments.SuccessDialogueFragment
import com.fictivestudios.tafcha.models.signup.SignupResponseUserIdData
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_signup_trainer.view.*
import kotlinx.android.synthetic.main.fragment_signup_trainer.view.btn_back
import kotlinx.android.synthetic.main.fragment_signup_trainer.view.et_email
import kotlinx.android.synthetic.main.fragment_signup_trainer.view.et_pass
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class SignupTrainerFragment : BaseFragment() {
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


        mView = inflater.inflate(R.layout.fragment_signup_trainer, container, false)


        mView?.btn_signup?.setOnClickListener {

            validateFields()

        }
        mView.btn_back.setOnClickListener {
            getRegisterationActivity?.
            onBackPressed()
        }


        mView.signupsvisibilty.setOnClickListener {

            showPassword1()

        }

        mView.signupsvisibilty1.setOnClickListener {

            showPassword2()
        }

        return mView



    }
    private fun validateFields() {

        if (
            mView.et_email.text.toString().isNullOrBlank())
        {
            mView.et_email.setError(getString(R.string.fields_cant_be_empty))
            //Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT).show()
            return
        }
        if (!Constants.isValidEmail(mView.et_email.text.toString()))
        {
            mView.et_email.setError(getString(R.string.invalid_email_format))
            return
        }

        if( mView.et_pass.text.toString().isNullOrBlank()){
            mView.et_pass.setError(getString(R.string.fields_cant_be_empty))
          //  Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT).show()
            return

        }

        if (mView.et_pass.text.toString().length <8 )
        {
            mView.et_pass.setError(getString(R.string.password_must_be))
            return
        }


        if(  mView.et_confirm_pass.text.toString().isNullOrBlank()){
            mView.et_confirm_pass.setError(getString(R.string.fields_cant_be_empty))
          //  Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT).show()
            return

        }

        if (!mView.et_pass.text.toString().equals(mView.et_confirm_pass.text.toString()))
        {
            mView.et_confirm_pass.setError(getString(R.string.confirm_pass_doesnt_match))

            return
        }


        else
        {
            signup()
        }
    }

private fun signup() = getFirebaseToken {  token ->


    lifecycleScope.launch {
        if(Constants.isNetworkConnected(requireActivity(), true)) {

            try {

                RetrofitSetup().callApi(requireActivity(), true, "",
                    object : CallHandler<Response<SignupResponseUserIdData>> {
                        override suspend fun sendRequest(apiInterFace: ApiInterface): Response<SignupResponseUserIdData> {

                            return apiInterFace.Signup(
                                JSONObject().apply {
                                    put("email", mView.et_email.text.toString())
                                    put("password", mView.et_pass.text.toString())
                                    put("confirm_password", mView.et_confirm_pass.text.toString())
                                    if (PreferenceUtils.getBoolean(Constants.IS_USER)) {
                                        put("role", "user")

                                    } else {
                                        put("role", "trainer")
                                    }
                                    put("device_type", "android")
                                    put("device_token", token)


                                }.toString().getJsonRequestBody()
                            )

                        }

                        override fun success(response: Response<SignupResponseUserIdData>) {

                                if (response.body()!!.status == 0) {
                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                else {
                                    getRegisterationActivity?.AddFragment(
                                        SuccessDialogueFragment(getString(R.string.congrats_your_acc__),
                                            object : View.OnClickListener {
                                                override fun onClick(p0: View?) {
                                                    getRegisterationActivity!!.replaceFragment(
                                                        VerificationFragment(false,
                                                            response.body()!!.data.user_id!!.toInt(),
                                                            ""
                                                        ),
                                                        VerificationFragment(false,
                                                            response.body()!!.data.user_id!!.toInt(),
                                                            ""
                                                        ).javaClass.simpleName,
                                                        false,
                                                        false
                                                    )
                                                }
                                            }
                                        ),
                                        SuccessDialogueFragment.javaClass.simpleName,
                                        true, false)
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