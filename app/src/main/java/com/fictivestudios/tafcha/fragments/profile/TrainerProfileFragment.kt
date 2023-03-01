package com.fictivestudios.tafcha.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.trainers.trainerprofile.TrainerProfile
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.profile_image
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.tv_bio_text
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.tv_email
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.tv_user_name
import kotlinx.android.synthetic.main.fragment_trainer_profile.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TrainerProfileFragment(var traineradd: Boolean, var t_id: Int? = 0) : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.profile))

        titlebar.setBtnBack(
            object : View.OnClickListener {
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
        mView = inflater.inflate(R.layout.fragment_trainer_profile, container, false)

        getTrainerProfileData(t_id!!)
        if (traineradd == true) {
            mView.btn_send_request.visibility = View.INVISIBLE
        } else {
            mView.btn_send_request.setOnClickListener {

                addTrainer(t_id!!)
            }
        }



        return mView
    }

    private fun getTrainerProfileData(t_id: Int? = 0) {

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    activity?.let {
                        RetrofitSetup().callApi(
                            it, true, PreferenceUtils.getString("token"),
                            object : CallHandler<Response<TrainerProfile>> {
                                override suspend fun sendRequest(apiInterFace: ApiInterface): Response<TrainerProfile> {
                                    return apiInterFace.trainerProfile(t_id!!)
                                }

                                override fun success(response: Response<TrainerProfile>) {
                                    if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                        response.let {
                                            response.body()?.data?.image?.let { it1 ->
                                                Glide.with(activity)
                                                    .load(MEDIA_URL + it.body()!!.data.image)
                                                    .into(mView.profile_image)
                                                mView.tv_user_name.text = it.body()!!.data.name.toString()
                                                mView.tv_email.text = it.body()!!.data.email.toString()
                                                mView.tv_age.text = it.body()!!.data.age.toString() + " Years"
                                                mView.tv_bio_text.text = it.body()!!.data.bio.toString()
                                            }
                                        }

                                    } else {
                                        Toast.makeText(
                                            activity,
                                            response.message(),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                                override fun error(message: String) {
                                    RetrofitSetup().hideLoader()
                                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG)
                                        .show()
                                }

                            })
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }

    fun addTrainer(t_id: Int = 0) {
        GlobalScope.launch(Dispatchers.IO) {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.addTrainer(t_id)
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //addTrainerResponse=response.body().toString()
                                } else {
                                    Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun error(message: String) {

                            }

                        })


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}