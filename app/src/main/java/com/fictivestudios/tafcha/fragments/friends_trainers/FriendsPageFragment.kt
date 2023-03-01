package com.fictivestudios.tafcha.fragments.friends_trainers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.fictivestudios.tafcha.fragments.profile.ProfileUserFragment
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_page_friends.view.*



class FriendsPageFragment : BaseFragment() {
    // TODO: Rename and change types of parameters

    private lateinit var mView: View



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

        titlebar.setHomeTitle(

            getString(R.string.home),

            //buttonLeft
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

            //buttonRight
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


        mView = inflater.inflate(R.layout.fragment_page_friends, container, false)

        mView.iv_main_image.setOnClickListener {

            getActivityContext?.
            replaceFragment(
                FriendsMainFragment(),
                FriendsMainFragment().javaClass.simpleName,
                true,false)


    }
      /*  mView.iv_main_image.setOnLongClickListener{
            getActivityContext?.
        replaceFragment(
            FriendsMainFragment(),
            FriendsMainFragment().javaClass.simpleName,
            true,false)
            true
        }*/

        return mView
    }



}