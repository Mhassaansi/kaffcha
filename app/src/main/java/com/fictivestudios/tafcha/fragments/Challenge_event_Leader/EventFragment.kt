package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

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
import com.fictivestudios.tafcha.adapters.CurrentAdapter
import com.fictivestudios.tafcha.adapters.EventListAdapter
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.fictivestudios.tafcha.models.event.event_list.EventList
import com.fictivestudios.tafcha.models.event.event_list.EventListItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_event.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class EventFragment : BaseFragment() {

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
        titlebar.setTitle(getActivityContext!!, getString(R.string.events))
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


        mView = inflater.inflate(R.layout.fragment_event, container, false)

        viewHistoryEvents()

        mView.btn_create_new.setOnClickListener {
            getActivityContext
                ?.replaceFragment(
                    NewEventsFragment(false,0,0),
                    NewEventsFragment(false,0,0).javaClass.simpleName,
                    true, true
                )
        }

        return mView
    }


    private fun initEventRecyclerview(eventList:ArrayList<EventListItem>?)
    {
        mView.rv_event_history.adapter = context?.let { EventListAdapter(it, eventList!!.asReversed()) }
        mView.rv_event_history.adapter?.notifyDataSetChanged()

    }

    private fun viewHistoryEvents(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<EventList>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<EventList> {
                                return apiInterFace.viewEventsHistory("history")
                            }

                            override fun success(response: Response<EventList>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    initEventRecyclerview(response.body()!!.data as ArrayList<EventListItem>?)

                                } else if (response.body()?.status == 0) {

                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()

                                }

                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }


                        })


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }


    }


}