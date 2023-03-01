package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.fictivestudios.tafcha.fragments.profile.TrainerMyProfileFragment
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_events_page.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EventsPageFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setHomeTitle(
            getString(R.string.home),

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            TrainerMyProfileFragment(),
                            TrainerMyProfileFragment().javaClass.simpleName,
                            true, true
                        )
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

        mView = inflater.inflate(R.layout.fragment_events_page, container, false)

        mView.iv_main_image.setOnClickListener {

            getActivityContext?.
            replaceFragment(EventFragment(),
                EventFragment().javaClass.simpleName,
                true,false)
        }




        return mView
    }

    companion object {}
}