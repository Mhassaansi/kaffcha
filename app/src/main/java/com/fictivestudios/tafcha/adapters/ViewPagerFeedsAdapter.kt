package com.fictivestudios.tafcha.adapters

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.fragments.*
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.FeedFragment
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.MyChallengesFragment

class ViewPagerFeedsAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm)  {

    val fm = fm
    override fun getCount(): Int {
        return 2;
    }



    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return FeedFragment()
            }
            1 -> {
                return MyChallengesFragment()
            }


            else -> {
                return MyChallengesFragment()
            }
        }
    }


/*    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Tab 1"
            }
            1 -> {
                return "Tab 2"
            }
            2 -> {
                return "Tab 3"
            }
        }
        return super.getPageTitle(position)
    }*/


   /* fun replaceFragment(
        frag: Fragment,
        tag: String,
        isAddToBackStack: Boolean,
        animate: Boolean
    ) {


        val transaction = fm.beginTransaction()

        if (animate) {
            //transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
            transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
        transaction.replace(R.id.main_view_container, frag)

        if (isAddToBackStack) {
            transaction.addToBackStack(null).commit()
        } else {
            transaction.commitAllowingStateLoss()
        }
    }*/


}