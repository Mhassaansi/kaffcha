package com.fictivestudios.tafcha.adapters

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.fragments.*
import com.fictivestudios.tafcha.fragments.profile.PendingHistoryFragment
import com.fictivestudios.tafcha.fragments.reminder_result_reward.ResultHistoryFragment

class ViewPagerHistoryAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm)  {

    val fm = fm
    override fun getCount(): Int {
        return 2;
    }



    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return PendingHistoryFragment()
            }
            1 -> {
                return ResultHistoryFragment()
            }


            else -> {
                return PendingHistoryFragment()
            }
        }
    }





}