package com.fictivestudios.tafcha.fragments.live_other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils

import com.fictivestudios.tafcha.adapters.UserMenuAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.TrainerMenuAdapter
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.ChallengesFragment
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.FeedFragment
import com.fictivestudios.tafcha.fragments.friends_trainers.*
import com.fictivestudios.tafcha.fragments.profile.HistoryFragment
import com.fictivestudios.tafcha.fragments.profile.ProfileUserFragment
import com.fictivestudios.tafcha.fragments.reminder_result_reward.ReminderFragment
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.viewPagerMain






class HomeFragment(var nType:String) : BaseFragment() {
    var notificationFlag = -1
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

        mView = inflater.inflate(R.layout.fragment_home, container, false)
        setViewPager()
        return mView
    }

    private fun setViewPager() {
        if(PreferenceUtils.getBoolean(Constants.IS_USER))
        {
            mView.viewPagerMain.adapter = UserMenuAdapter(childFragmentManager)


            if(PreferenceUtils.getString("Noti") == "Request Recieved" ||
                PreferenceUtils.getString("Noti") == "Request Accept"){
                PreferenceUtils.saveString("Noti","")
                getActivityContext
                    ?.replaceFragment(
                        FriendsMainFragment(),
                        FriendsMainFragment().javaClass.simpleName,
                        true, true)


            }
            else  if(PreferenceUtils.getString("Noti")=="Challenge Recieved"){
                PreferenceUtils.saveString("Noti","")
                getActivityContext
                    ?.replaceFragment(
                        ChallengesFragment(),
                        ChallengesFragment().javaClass.simpleName,
                        true, true
                    )


            }
            else if(PreferenceUtils.getString("Noti")=="Challenge Accept"){
                PreferenceUtils.saveString("Noti","")
                getActivityContext
                    ?.replaceFragment(
                        FeedFragment(),
                        FeedFragment().javaClass.simpleName,
                        true, true
                    )


            }
            else if(PreferenceUtils.getString("Noti") == "Challenge Winner" ||
                PreferenceUtils.getString("Noti") == "Challenge Runner"){
                PreferenceUtils.saveString("Noti","")
                getActivityContext
                    ?.replaceFragment(
                        HistoryFragment(),
                        HistoryFragment().javaClass.simpleName,
                        true, true
                    )

            }
            else if(PreferenceUtils.getString("Noti")=="Reminder"){
                PreferenceUtils.saveString("Noti","")
                getActivityContext
                    ?.replaceFragment(
                        ReminderFragment(),
                        ReminderFragment().javaClass.simpleName,
                        true, true
                    )
            }
            else if(PreferenceUtils.getString("Noti")=="Vote Recieved"){
                PreferenceUtils.saveString("Noti","")
                getActivityContext
                    ?.replaceFragment(
                        FeedFragment(),
                        FeedFragment().javaClass.simpleName,
                        true, true
                    )
            }
            else if(PreferenceUtils.getString("Noti")=="Trainer Live Notification"){
                PreferenceUtils.saveString("Noti","")

                getActivityContext?.
                replaceFragment(
                    SubscribeFragment()
                    ,SubscribeFragment().javaClass.simpleName,
                    true,true)


            }
            else{
               PreferenceUtils.saveString("Noti","")
//                getActivityContext?.
//                replaceFragment(
//                    TrainingPageFragment()
//                    , TrainingPageFragment().javaClass.simpleName,
//                    true,true)

            }
        }
        else
        {
            mView.viewPagerMain.adapter = TrainerMenuAdapter(childFragmentManager)
            if(PreferenceUtils.getString("Noti")=="Follow Notification"){
                PreferenceUtils.saveString("Noti","")
                getActivityContext
                    ?.replaceFragment(
                        UsersFragment(),
                        UsersFragment().javaClass.simpleName,
                        true, true)
            }
            else{
                PreferenceUtils.saveString("Noti","")
//                getActivityContext
//                    ?.replaceFragment(
//                        UsersPageFragment(),
//                        UsersPageFragment().javaClass.simpleName,
//                        true, true)
            }
        }
    }





}