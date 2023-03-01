package com.fictivestudios.tafcha.fragments.reminder_result_reward

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_detail.ChallengeDeails
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent.ChallengeSent
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent.ChallengeSentItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_result_details.view.*
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.*
import kotlinx.coroutines.launch
import retrofit2.Response


class ResultDetailsFragment(var c_id:Int) : BaseFragment() {


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

        mView = inflater.inflate(R.layout.fragment_result_details, container, false)

        challengeDetail(c_id)

        return mView
    }

    private fun challengeDetail(c_id: Int){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<ChallengeDeails>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ChallengeDeails> {

                                return apiInterFace.challengeDetails(c_id)

                            }

                            override fun success(response: Response<ChallengeDeails>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    mView.tv_exercise_details.setText(response.body()?.data?.challenge_description)
                                    playVideo1(MEDIA_URL+response.body()?.data?.sender_video)
                                    playVideo2(MEDIA_URL+response.body()?.data?.reciever_video)

                                } else {

                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()?.message,
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

                } catch (e: Exception) {

                }

            }

        }

    }

    fun playVideo1(url:String){
        val uri: Uri = Uri.parse(url)
        mView.video_view_triner1.setVideoURI(uri)
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView( mView.video_view_triner1)
        mediaController.setMediaPlayer( mView.video_view_triner1)
        mView.video_view_triner1.setMediaController(mediaController)
        mView.video_view_triner1.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                Log.d("video", "setOnErrorListener ")
                return true
            }
        })
    }
    fun playVideo2(url:String){
        val uri: Uri = Uri.parse(url)
        mView.video_view_triner2.setVideoURI(uri)
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView( mView.video_view_triner2)
        mediaController.setMediaPlayer( mView.video_view_triner2)
        mView.video_view_triner2.setMediaController(mediaController)
        mView.video_view_triner2.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                Log.d("video", "setOnErrorListener ")
                return true
            }
        })
    }

}