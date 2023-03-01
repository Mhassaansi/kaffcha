package com.fictivestudios.tafcha.fragments.profile

import android.annotation.SuppressLint
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
import com.fictivestudios.tafcha.adapters.HistoryAdapter
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent.ChallengeSent
import com.fictivestudios.tafcha.models.profile.userprofile.UserProfileData
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile_user.view.*


import kotlinx.coroutines.launch
import retrofit2.Response


class ProfileUserFragment : BaseFragment() {


    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.profile))

        titlebar.setBtnRight(
            R.drawable.edit_profile_icon,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            EditProfileFragment(),
                            EditProfileFragment().javaClass.simpleName,
                            true, true
                        )
                }
            })

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
    ): View {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_profile_user, container, false)

        mView.tv_see_all.setOnClickListener {

            getActivityContext
                ?.replaceFragment(
                    HistoryFragment(),
                    HistoryFragment.javaClass.simpleName,
                    true, true
                )
        }

        getProfileDataUser()
        initHistoryRecyclerview()

        return mView
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initHistoryRecyclerview()
    {

      //  historyList:ArrayList<>
        val historyList=ArrayList<String>()
        historyList.add("Justin Wade")
        historyList.add("Justin Wade")
        historyList.add("Justin Wade")
        historyList.add("Justin Wade")
        historyList.add("Justin Wade")


        mView.rv_history.adapter = context?.let { HistoryAdapter(it,historyList) }
        mView.rv_history.adapter?.notifyDataSetChanged()
    }


    private fun getProfileDataUser(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    activity?.let {
                        RetrofitSetup().callApi(
                            it, true, PreferenceUtils.getString("token"),
                            object : CallHandler<Response<UserProfileData>> {
                                override suspend fun sendRequest(apiInterFace: ApiInterface): Response<UserProfileData> {
                                    return apiInterFace.getUserProfileData()
                                }

                                override fun success(response: Response<UserProfileData>) {
                                    if (response.body()?.status==1  && response.body()?.message != "record not found..!") {

                                        if(response.body()?.data?.user?.image != null){
                                            Glide.with(requireActivity())
                                                .load(MEDIA_URL+response.body()!!.data.user?.image)
                                                .into(mView.profile_image)
                                        }
                                        else{
                                            mView.profile_image.setImageResource(R.drawable.user_blank_profile)
                                        }

                                        mView.tv_user_name.setText(response.body()!!.data.user?.name)
                                        mView.tv_email.setText(response.body()!!.data.user?.email)
                                        mView.tv_age.setText(response.body()!!.data.user?.age + " Years")

                                        if(response.body()?.data?.win.toString() != "null"){

                                            mView.tv_won_count.setText(response.body()!!.data.win.toString())
                                        }
                                        else{
                                            mView.tv_won_count.setText("0")
                                        }
                                       if(response.body()!!.data.lose.toString() != "null"){
                                            mView.tv_last_count.setText(response.body()!!.data.lose.toString())
                                        }
                                        else{
                                            mView.tv_last_count.setText("0")
                                        }
                                        if(response.body()?.data?.die.toString() != "null"){
                                            mView.tv_draw_count.setText(response.body()!!.data.die.toString())
                                        }
                                        else{
                                            mView.tv_draw_count.setText("0")
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


    private fun challengeList(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<ChallengeSent>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ChallengeSent> {

                                return apiInterFace.challangeSentDetails("pending")

                            }

                            override fun success(response: Response<ChallengeSent>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    // initChallengeRecyclerview(response.body()!!.data as ArrayList<ChallengeSentListItem>)

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

                }

            }
        }
    }


}