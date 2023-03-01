package com.fictivestudios.tafcha.fragments.friends_trainers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.adapters.ViewPagerFriendsAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.models.freinds.allusers.AllUsers
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_friends_main.view.*
import kotlinx.coroutines.launch
import retrofit2.Response



class FriendsMainFragment : BaseFragment() {



    private var isFriendClick = true
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

        titlebar.setTitle(getActivityContext!!, getString(R.string.friends))

        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )

        titlebar.setBtnRight(R.drawable.notify_icon,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            NotificationsFragment(),
                            NotificationsFragment().javaClass.simpleName,
                            true, true
                        )
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       mView = inflater.inflate(R.layout.fragment_friends_main, container, false)

        var adapter = ViewPagerFriendsAdapter(childFragmentManager)
        mView.view_pager_friends.adapter = adapter


        mView.view_pager_friends.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

                if (position == 0)
                {
                    isFriendClick = true
                    mView.btn_friends.setBackgroundResource(R.drawable.btn_squire_round_pink)
                    mView.btn_friend_req.setBackgroundResource(R.drawable.btn_squire_round_gray)

                }
                else
                {
                    isFriendClick = false
                    mView.btn_friend_req.setBackgroundResource(R.drawable.btn_squire_round_pink)
                    mView.btn_friends.setBackgroundResource(R.drawable.btn_squire_round_gray)


                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        mView.btn_friends.setOnClickListener {

            if(!isFriendClick)
            {
                isFriendClick = true
                mView.btn_friends.setBackgroundResource(R.drawable.btn_squire_round_pink)
                mView.btn_friend_req.setBackgroundResource(R.drawable.btn_squire_round_gray)
                mView.view_pager_friends.setCurrentItem(0,true)
            }
        }

        mView.btn_friend_req.setOnClickListener {

            if(isFriendClick)
            {
                isFriendClick = false
                mView.btn_friend_req.setBackgroundResource(R.drawable.btn_squire_round_pink)
                mView.btn_friends.setBackgroundResource(R.drawable.btn_squire_round_gray)
                mView.view_pager_friends.setCurrentItem(0,true)

            }

            mView.view_pager_friends.setCurrentItem(1,true)

        }



        return mView
    }



}