package com.fictivestudios.tafcha.adapters

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.fragments.*
import com.fictivestudios.tafcha.fragments.friends_trainers.FriendRequestFragment
import com.fictivestudios.tafcha.fragments.friends_trainers.FriendsFragment

class ViewPagerFriendsAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm)  {

    val fm = fm
    override fun getCount(): Int {
        return 2;
    }



    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return FriendsFragment()
            }
            1 -> {
                return FriendRequestFragment()
            }


            else -> {
                return FriendsFragment()
            }
        }
    }





}