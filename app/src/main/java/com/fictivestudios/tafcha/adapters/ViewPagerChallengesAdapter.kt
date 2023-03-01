package com.fictivestudios.tafcha.adapters

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.fragments.*
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.ChallengeReceivedFragment
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.ChallengeSentFragment

class ViewPagerChallengesAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm)  {

    val fm = fm
    override fun getCount(): Int {
        return 2;
    }



    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return ChallengeSentFragment()
            }
            1 -> {
                return ChallengeReceivedFragment()
            }


            else -> {
                return ChallengeSentFragment()
            }
        }
    }





}