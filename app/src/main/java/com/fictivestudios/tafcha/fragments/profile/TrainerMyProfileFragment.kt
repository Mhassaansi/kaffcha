package com.fictivestudios.tafcha.fragments.profile

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.ClientAdapter
import com.fictivestudios.tafcha.fragments.friends_trainers.ClientListFragment


import com.fictivestudios.tafcha.models.profile.trainer_profile.TrainerProfileData
import com.fictivestudios.tafcha.models.profile.trainer_profile.UserX
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.*
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.video_view_triner
import kotlinx.coroutines.launch
import retrofit2.Response




class TrainerMyProfileFragment : BaseFragment() {


    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.profile))

        titlebar.setBtnRight(R.drawable.edit_profile_icon,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            EditProfileTrainerFragment(),
                            EditProfileTrainerFragment().javaClass.simpleName,
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
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_trainer_my_profile, container, false)
        getProfileData()
        mView.tv_see_all.setOnClickListener {
            getActivityContext
                ?.replaceFragment(
                    ClientListFragment(),
                    ClientListFragment.javaClass.simpleName,
                    true, true
                )
        }



        //initclientRecyclerview()

        return mView
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initclientRecyclerview(challengeFeedList:ArrayList<UserX>?)
    {
        mView.rv_client.adapter = context?.let { ClientAdapter(it,challengeFeedList!!) }
        mView.rv_client.adapter?.notifyDataSetChanged()

    }


private fun playVideo(url:String){
    val uri: Uri = Uri.parse(url)
    mView.video_view_triner.setVideoURI(uri)
    val mediaController = MediaController(this.activity)
    mediaController.setAnchorView(mView.video_view_triner)
    mediaController.setMediaPlayer(mView.video_view_triner)
    mView.video_view_triner.setMediaController(mediaController)
    mView.video_view_triner.setOnErrorListener(object : MediaPlayer.OnErrorListener {
        override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            Log.d("video", "setOnErrorListener ")
            return true
        }
    })

}


    private fun getProfileData(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    activity?.let {
                        RetrofitSetup().callApi(
                            it, true, PreferenceUtils.getString("token"),
                            object : CallHandler<Response<TrainerProfileData>> {
                                override suspend fun sendRequest(apiInterFace: ApiInterface): Response<TrainerProfileData> {

                                    return apiInterFace.getTrainerProfileData()

                                }

                                override fun success(response: Response<TrainerProfileData>) {
                                    if (response.body()?.status==1  && response.body()?.message != "record not found..!") {
                                        if(response.body()?.data?.user?.image==null){
                                          mView.profile_image.setImageResource(R.drawable.user_blank_profile)

                                        }
                                        else{
                                            Glide.with(activity)
                                                .load(MEDIA_URL+response.body()!!.data?.user?.image)
                                                .into(mView.profile_image)
                                        }


                                        mView.tv_user_name.setText(response.body()!!.data?.user?.name)
                                        mView.tv_email.setText(response.body()!!.data?.user?.email)
                                        mView.tv_bio_text.setText(response.body()!!.data?.user?.bio)
                                        playVideo(MEDIA_URL+response.body()!!.data?.user?.bio_video)
                                        initclientRecyclerview(response.body()?.data?.users as ArrayList<UserX>)
                                    } else if (response.body()!!.status == 0) {
                                        Toast.makeText(
                                            activity,
                                            response.body()!!.message,
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



}