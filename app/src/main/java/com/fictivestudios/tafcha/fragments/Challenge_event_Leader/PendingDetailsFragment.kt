package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_pending_details.view.*
import kotlinx.android.synthetic.main.fragment_pending_details.view.tv_user_name
import kotlinx.android.synthetic.main.fragment_pending_details.view.video_view_triner
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.*


class PendingDetailsFragment(val desp:String,val video:String,val s_name:String) : BaseFragment() {


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

        mView =inflater.inflate(R.layout.fragment_pending_details, container, false)

        mView.tv_user_name.setText(s_name+" Challenge you")
        mView.tv_exercise_details.setText(desp)
        playVideo(MEDIA_URL+video)

        return mView
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


}