package com.fictivestudios.tafcha.fragments.live_other

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.dialogFragments.RejectDialog
import com.fictivestudios.tafcha.fragments.profile.TrainerMyProfileFragment
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.leaderBorad.LeaderBorad
import com.fictivestudios.tafcha.models.leaderBorad.LeaderBoradItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_live_stream_page.view.*
import kotlinx.coroutines.launch
import retrofit2.Response


class LiveStreamPageFragment : BaseFragment() {


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

/*    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!,getString(R.string.home))


        titlebar.setBtnLeft(R.drawable.user_dp,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            TrainerMyProfileFragment(),
                            TrainerMyProfileFragment.javaClass.simpleName,
                            true, true
                        )

                }
            }
            ,true
        )


        titlebar.setBtnRight(R.drawable.notify_icon,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            NotificationsFragment(),
                            NotificationsFragment.javaClass.simpleName,
                            true, true
                        )
                }
            })


    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_live_stream_page, container, false)




        mView.iv_main_image.setOnClickListener {
//            getActivityContext?.AddFragment(
//                RejectDialog(getString(R.string.service),
//                    object : View.OnClickListener {
//                        override fun onClick(p0: View?) {
//                            requireActivity().onBackPressed()
//                        }
//                    }
//
//                ),
//                RejectDialog.javaClass.simpleName,
//                true, false)
          //  Toast.makeText(requireActivity(),"Service Not Available",Toast.LENGTH_LONG).show()
           // liveStreamStart()
            getActivityContext?.
            replaceFragment(LiveEventFragment(),
                LiveEventFragment().javaClass.simpleName,
                true,false)

        }

        return mView
    }





}

