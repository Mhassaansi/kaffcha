package com.fictivestudios.tafcha.fragments.profile

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
import com.fictivestudios.tafcha.dialogFragments.SuccessDialogueFragment
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_update_password.view.*
import kotlinx.android.synthetic.main.fragment_update_password.view.et_pass
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception



class UpdatePasswordFragment : BaseFragment() {


    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.update_password))

        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       mView = inflater.inflate(R.layout.fragment_update_password, container, false)

        mView.btn_continue.setOnClickListener {
            validatePassword()
        }


        mView.changepassvisibilty.setOnClickListener {
            showPassword1()
        }

        mView.passpassvisibilty.setOnClickListener {
            showPassword2()

        }

        mView.confirmpassvisibilty.setOnClickListener {
            showPassword3()
        }


       return mView
    }

    private fun validatePassword() {

        if (mView.et_old_pass.text.toString().isNullOrBlank())
        {
            mView.et_old_pass.setError(getString(R.string.fields_cant_be_empty))
            return
        }

        if (mView.et_old_pass.text.toString().length < 8)
        {
            mView.et_old_pass.setError(getString(R.string.password_must_be))
           // return
        }

//!mView.et_pass.text.toString().equals(mView.et_confirm_pass.text.toString())

        if (mView.et_pass.toString().isNullOrBlank())
        {
            mView.et_pass.setError(getString(R.string.fields_cant_be_empty))

            return
        }
        //.equals(mView.et_pass.text.toString()

        if (mView.et_pass.text.toString().length <8)
        {
            mView.et_pass.setError(getString(R.string.password_must_be))
            return
        }

        if(mView.et_confirm_pass.text.toString().isNullOrBlank()){
            mView.et_confirm_pass.setError(getString(R.string.fields_cant_be_empty))
            return
        }

        if(mView.et_confirm_pass.toString().length < 8){

            mView.et_confirm_pass.setError(getString(R.string.password_must_be))
            return
        }

        if(!mView.et_pass.text.toString().equals(mView.et_confirm_pass.text.toString())){
            mView.et_confirm_pass.setError(getString(R.string.confirm_pass_doesnt_match))
            return
        }

        else{

            changePassword()

        }
    }


    private fun changePassword(){

        lifecycleScope.launch {

            //   Toast.makeText(activity,token,Toast.LENGTH_LONG).show()
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    val token = PreferenceUtils.getString("token")
                    Log.e("token", token)
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        "${PreferenceUtils.getString("token")}",
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {

                                return apiInterFace.changePassword(
                                    JSONObject().apply {
                                        put("old_password", mView.et_old_pass.text.toString())
                                        put("new_password", mView.et_pass.text.toString())
                                        put(
                                            "confirm_password",
                                            mView.et_confirm_pass.text.toString()
                                        )
                                    }.toString().getJsonRequestBody()
                                )

                            }

                            override fun success(response: Response<CommonResponseDC>) {

                                if (response.body()?.status==1) {
                                    getActivityContext?.AddFragment(
                                        SuccessDialogueFragment(getString(R.string.password_reset_successfull),

                                            object : View.OnClickListener {
                                                override fun onClick(p0: View?) {
                                                    getActivityContext?.onBackPressed()

                                                }
                                            }
                                        ),
                                        SuccessDialogueFragment.javaClass.simpleName,
                                        false, false)
                                }
                                else {
//                                    Toast.makeText(activity, response.message(), Toast.LENGTH_LONG)
//                                        .show()
                                }


                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
                            }


                        }
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }


    fun showPassword1(){


        if(mView.et_old_pass.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
            mView.changepassvisibilty.setImageResource(R.drawable.password_show)
            mView.et_old_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
        else{
            mView.changepassvisibilty.setImageResource(R.drawable.passwod_hide)

            //Hide Password
            mView.et_old_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    fun showPassword2(){


        if(mView.et_pass.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
            mView.passpassvisibilty.setImageResource(R.drawable.password_show)
            mView.et_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
        else{
            mView.passpassvisibilty.setImageResource(R.drawable.passwod_hide)

            //Hide Password
            mView.et_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    fun showPassword3(){


        if(mView.et_confirm_pass.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
            mView.confirmpassvisibilty.setImageResource(R.drawable.password_show)
            mView.et_confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
        else{
            mView.confirmpassvisibilty.setImageResource(R.drawable.passwod_hide)

            //Hide Password
            mView.et_confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }


}