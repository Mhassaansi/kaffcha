package com.fictivestudios.tafcha.fragments.profile

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


import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings_page.view.*


class SettingsPageFragment : BaseFragment() {


    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

        titlebar.setHomeTitle(
            getString(R.string.home),

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    if (PreferenceUtils.getBoolean(Constants.IS_USER))
                    {
                        getActivityContext
                            ?.replaceFragment(
                                ProfileUserFragment(),
                                ProfileUserFragment().javaClass.simpleName,
                                true, true
                            )
                    }
                    else
                    {
                        getActivityContext
                            ?.replaceFragment(
                                TrainerMyProfileFragment(),
                                TrainerMyProfileFragment().javaClass.simpleName,
                                true, true
                            )

                    }
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

        mView = inflater.inflate(R.layout.fragment_settings_page, container, false)

        mView.iv_main_image.setOnClickListener {

            getActivityContext?.
            replaceFragment(
                SettingsFragment(),
                SettingsFragment().javaClass.simpleName,
                true,false)
        }

        return mView


    }


}