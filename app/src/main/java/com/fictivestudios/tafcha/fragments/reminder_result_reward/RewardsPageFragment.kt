package com.fictivestudios.tafcha.fragments.reminder_result_reward

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R

import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.fictivestudios.tafcha.fragments.profile.ProfileUserFragment
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_rewards_page.view.*


class RewardsPageFragment : BaseFragment() {


    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setHomeTitle(
            getString(R.string.home),

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            ProfileUserFragment(),
                            ProfileUserFragment().javaClass.simpleName,
                            true, true
                        )
                }
            },
            R.drawable.user_dp,

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            NotificationsFragment(),
                            NotificationsFragment().javaClass.simpleName,
                            true, true
                        )
                }
            },
            R.drawable.notify_icon
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_rewards_page, container, false)

        mView.iv_main_image.setOnClickListener {

            getActivityContext?.
            replaceFragment(RewardsFragment(),
                RewardsFragment().javaClass.simpleName,
                true,false)
        }

       return mView
    }


}