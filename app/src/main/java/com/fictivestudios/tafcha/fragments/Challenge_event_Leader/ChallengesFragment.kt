package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.ViewPagerChallengesAdapter
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_challenges.view.*






class ChallengesFragment : BaseFragment() {
    private var isSentClick: Boolean = false



    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

        titlebar.setMainTitle(
            getString(R.string.challenges),

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            },
            R.drawable.btn_back_arrow,

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            ChallengeFriendsFragment(),
                            ChallengeFriendsFragment().javaClass.simpleName,
                            false, true
                        )
                }
            },
            R.drawable.add_icon
        )



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_challenges, container, false)

        val adapter = ViewPagerChallengesAdapter(childFragmentManager)
        mView.view_pager_challenge.adapter = adapter


        mView.view_pager_challenge.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

                if (position == 0)
                {
                    isSentClick = true
                    mView.btn_sent.setBackgroundResource(R.drawable.btn_squire_round_pink)
                    mView.btn_my_received.setBackgroundResource(R.drawable.btn_squire_round_gray)

                }
                else
                {
                    isSentClick = false
                    mView.btn_my_received.setBackgroundResource(R.drawable.btn_squire_round_pink)
                    mView.btn_sent.setBackgroundResource(R.drawable.btn_squire_round_gray)


                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        mView.btn_sent.setOnClickListener {

            if(!isSentClick)
            {
                isSentClick = true
                mView.btn_sent.setBackgroundResource(R.drawable.btn_squire_round_pink)
                mView.btn_my_received.setBackgroundResource(R.drawable.btn_squire_round_gray)
                mView.view_pager_challenge.setCurrentItem(0,true)

            }





        }

        mView.btn_my_received.setOnClickListener {

            if(isSentClick)
            {
                isSentClick = false
                mView.btn_my_received.setBackgroundResource(R.drawable.btn_squire_round_pink)
                mView.btn_sent.setBackgroundResource(R.drawable.btn_squire_round_gray)
                mView.view_pager_challenge.setCurrentItem(0,true)

            }

            mView.view_pager_challenge.setCurrentItem(1,true)

        }



        return mView
    }

    companion object {}
}