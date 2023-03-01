package com.fictivestudios.tafcha.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.models.content.Content
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_privacy_policy.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class PrivacyPolicyFragment : BaseFragment() {


    lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        mView = inflater.inflate(R.layout.fragment_privacy_policy, container, false)



        mView.btn_back.setOnClickListener {
            getRegisterationActivity
                ?.onBackPressed()
        }

        privacyPolicy("pp")

        return mView
    }

    private fun privacyPolicy(aUs:String){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<Content>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<Content> {
                                return apiInterFace.getContent(aUs)
                            }

                            override fun success(response: Response<Content>) {
                                if (response.body()?.status == 1) {


                                    mView.pp_text.setText(response.body()?.data?.content)

                                } else {
                                    Toast.makeText(
                                        requireActivity(), "Error",
                                        Toast.LENGTH_LONG
                                    ).show()
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
        }

    }

}