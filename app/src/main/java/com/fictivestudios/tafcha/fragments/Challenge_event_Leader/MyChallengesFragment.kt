package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.adapters.MyChallengesAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_my_challenges.view.*


class MyChallengesFragment : BaseFragment() {


    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.my_challenges))
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
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_my_challenges, container, false)

        initChallengeRecyclerview()
        return mView
    }

    private fun initChallengeRecyclerview()
    {

        val challengeFeedList=ArrayList<String>()
        challengeFeedList.add("")
        challengeFeedList.add("")
        challengeFeedList.add("")

        mView.rv_my_chalanege.adapter = context?.let { MyChallengesAdapter(it,challengeFeedList) }
        mView.rv_my_chalanege.adapter?.notifyDataSetChanged()

    }

}