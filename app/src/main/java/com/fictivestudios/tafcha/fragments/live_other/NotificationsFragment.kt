package com.fictivestudios.tafcha.fragments.live_other

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.ChallengeNotificationAdapter
import com.fictivestudios.tafcha.adapters.NotificationAdapter
import com.fictivestudios.tafcha.models.notification.Notification
import com.fictivestudios.tafcha.models.notification.NotificationItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import kotlinx.coroutines.launch
import retrofit2.Response


class NotificationsFragment : BaseFragment() {

    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.notifications))
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
        mView = inflater.inflate(R.layout.fragment_notifications, container, false)
        notification()
        return mView
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun initTrainingRecyclerview(notificationList:ArrayList<NotificationItem>?)
    {
        mView.rv_challenges_noti.adapter = context?.let { ChallengeNotificationAdapter(it,notificationList!!.asReversed()) }
        mView.rv_challenges_noti.adapter?.notifyDataSetChanged()

    }


    private fun notification(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),true,PreferenceUtils.getString("token"),
                        object :CallHandler<Response<Notification>>{
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<Notification> {

                              return  apiInterFace.notification()

                            }

                            override fun success(response: Response<Notification>) {
                                if(response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!"){

                                    initTrainingRecyclerview(response.body()!!.data as ArrayList<NotificationItem>?)

                                }
                                else{

                                    Toast.makeText(requireActivity(),response.body()?.message,Toast.LENGTH_LONG).show()

                                }

                            }

                            override fun error(message: String) {

                                Toast.makeText(requireActivity(),"Error",Toast.LENGTH_LONG).show()

                            }


                        }
                        )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }


}