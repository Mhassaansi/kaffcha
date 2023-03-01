package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.adapters.ViewPagerFeedsAdapter

import com.fictivestudios.tafcha.Utils.Titlebar

import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_challenges_feed.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.Response




class ChallengesFeedFragment : BaseFragment() {


    private var isFeedClick = true

    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.profile))

        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )

        titlebar.setBtnRight(R.drawable.add_icon,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
//                    getActivityContext?.
//                    replaceFragment(ChallengeFriendsFragment("",""),
//                        ChallengeFriendsFragment.javaClass.simpleName,true,false)

                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       mView = inflater.inflate(R.layout.fragment_challenges_feed, container, false)

        var adapter = ViewPagerFeedsAdapter(childFragmentManager)
        mView.view_pager_feed.adapter = adapter


        mView.view_pager_feed.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

                if (position == 0)
                {
                    isFeedClick = true
                    mView.btn_feed.setBackgroundResource(R.drawable.btn_squire_round_pink)
                    mView.btn_my_challenges.setBackgroundResource(R.drawable.btn_squire_round_gray)

                }
                else
                {
                    isFeedClick = false
                    mView.btn_my_challenges.setBackgroundResource(R.drawable.btn_squire_round_pink)
                    mView.btn_feed.setBackgroundResource(R.drawable.btn_squire_round_gray)


                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        mView.btn_feed.setOnClickListener {

            if(!isFeedClick)
            {
                isFeedClick = true
                mView.btn_feed.setBackgroundResource(R.drawable.btn_squire_round_pink)
                mView.btn_my_challenges.setBackgroundResource(R.drawable.btn_squire_round_gray)
                mView.view_pager_feed.setCurrentItem(0,true)

            }





        }

        mView.btn_my_challenges.setOnClickListener {

            if(isFeedClick)
            {
                isFeedClick = false
                mView.btn_my_challenges.setBackgroundResource(R.drawable.btn_squire_round_pink)
                mView.btn_feed.setBackgroundResource(R.drawable.btn_squire_round_gray)
                mView.view_pager_feed.setCurrentItem(0,true)

            }

            mView.view_pager_feed.setCurrentItem(1,true)

        }


        return mView
    }

    fun addFragment(frag: BaseFragment, tag: String, isAddToBackStack: Boolean, animate: Boolean) {




        val transaction =   getActivityContext?.supportFragmentManager?.beginTransaction()

        if (animate) {
            //transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
            transaction?.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
        transaction?.add(R.id.view_pager_feed, MyChallengesFragment())

        if (isAddToBackStack) {
            transaction?.addToBackStack(null)?.commit()
        } else {
            transaction?.commitAllowingStateLoss()
        }
    }


    companion object {}


}