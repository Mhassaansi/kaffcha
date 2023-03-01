package com.fictivestudios.tafcha.fragments.reminder_result_reward

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
import com.fictivestudios.tafcha.adapters.ReminderAdapter


import com.fictivestudios.tafcha.interfaces.InviteFriendsList
import com.fictivestudios.tafcha.models.freinds.pending.PendingFriends
import com.fictivestudios.tafcha.models.freinds.pending.PendingFriendsData
import com.fictivestudios.tafcha.models.reminder.reminderlist.ReminderList
import com.fictivestudios.tafcha.models.reminder.reminderlist.ReminderListItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.android.synthetic.main.fragment_reminder.view.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception




class ReminderFragment : BaseFragment() {

    //,InviteFriendsList

   // var rid:Int=0
    var rList=ArrayList<ReminderListItem>()
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.reminder))

        titlebar.setBtnRight(R.drawable.reminder_icon,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext?.
                    replaceFragment(
                        CalenderFragment(false,0,"",""),
                        CalenderFragment(false,0,"","").javaClass.simpleName,
                        false,true)
                }
            })

        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_reminder, container, false)

        reminderList()

        return mView
    }

    private fun initReminderRecyclerview(reminderList:ArrayList<ReminderListItem>)
    {
        mView.rv_reminder.adapter = context?.let { ReminderAdapter(it,reminderList.asReversed()) }
        mView.rv_reminder.adapter?.notifyDataSetChanged()
    }

    private fun reminderList(){
        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<ReminderList>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ReminderList> {
                                return apiInterFace.viewReminderList()
                            }

                            override fun success(response: Response<ReminderList>) {


                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    rList = response.body()!!.data as ArrayList<ReminderListItem>
                                    initReminderRecyclerview(rList)
                                } else {
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

//    override fun getUser(pos: Int) {
//        rList.removeAt(pos)
//        rv_reminder.adapter!!.notifyItemRemoved(pos)
//    }
}