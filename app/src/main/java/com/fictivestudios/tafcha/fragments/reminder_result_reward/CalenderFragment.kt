package com.fictivestudios.tafcha.fragments.reminder_result_reward

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.Constants.Companion.dateWithTime
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.reminder.ReminderResponse.ReminderResponse
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_calender.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response


class CalenderFragment(var uReminder:Boolean,var rId:Int,var rDesp:String,var rDate:String) : BaseFragment() {

    var date: String? = null
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.calendar))

        titlebar.setBtnBack(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }
        )

        titlebar.setBtnRight(
            R.drawable.notify_icon,
            object : View.OnClickListener {
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
       

        mView = inflater.inflate(R.layout.fragment_calender, container, false)




        if (uReminder) {

            mView.btn_del.visibility = View.VISIBLE
            mView.r_note.setText(rDesp)
            mView.btn_done.setText(R.string.update)

            mView.btn_done.setOnClickListener {

                updateReminder()

            }
        } else {
            mView.btn_done.setOnClickListener {
                validation()
            }

        }
        setupCalendar()



        mView.btn_del.setOnClickListener {
            deleteReminder(rId)
        }


        return mView
    }

    private fun setupCalendar() {
        mView.calender_view.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "Selected date is " + year + "-" + (month + 1) + "-" + dayOfMonth
            // Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            date = "" + year + "-" + (month + 1) + "-" + dayOfMonth
           // Toast.makeText(context, date.toString(), Toast.LENGTH_SHORT).show()
            var time : String = " "
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if(time.length == 1){

                    date += " "
                    dateWithTime().split(" ").toTypedArray()
             //     Toast.makeText(getActivityContext,""+time[1],Toast.LENGTH_LONG).show()
                }
                else{
                    dateWithTime().split(" ").toTypedArray()
                    date += " " + time[1]
                }



            } else {

                val separated: Array<String> = dateWithTime().split(" ").toTypedArray()

                try {
                    if (!separated[1].isEmpty()) {
                        date += " " + separated[1]
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        }
    }


    private fun validation(): Boolean {

        if (mView.r_note.text.toString().isBlank()) {
            Toast.makeText(context, "The description field is required.", Toast.LENGTH_SHORT).show()
            return false
        } else {
            createReminder()
            return true
        }
    }

    private fun date(): String {
        if (date == null) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateWithTime()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        } else {
            return date.toString()
        }

    }

    private fun createReminder() {

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {


                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<ReminderResponse>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ReminderResponse> {
                                return apiInterFace.createReminder(
                                    JSONObject().apply {
                                        put("reminder_date", date())
                                        put("description", mView.r_note.text.toString())
                                    }.toString().getJsonRequestBody()
                                )
                            }


                            override fun success(response: Response<ReminderResponse>) {
                                if (response.body()?.status == 1 && response.body()?.message != "record not found..!") {

                                    getActivityContext?.replaceFragment(
                                        ReminderFragment(),
                                        ReminderFragment().javaClass.simpleName,
                                        false, false
                                    )


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

    private fun deleteReminder(reminder_id: Int? = 0) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.deleteReminder(reminder_id!!)
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if (response.body()?.status == 1 && response.body()?.message != "record not found..!") {
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    requireActivity().onBackPressed()
                                } else {
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun error(message: String) {

                                RetrofitSetup().hideLoader()
                                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                            }

                        })
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        }
    }

    private fun updateReminder() {

        //Toast.makeText(context, date(), Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {


                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.updateReminder(
                                    JSONObject().apply {
                                        put("reminder_date", date())
                                        put("description", mView.r_note.text.toString())
                                        put("reminder_id", rId)
                                    }.toString().getJsonRequestBody()
                                )
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if (response.body()?.status == 1 && response.body()?.message != "record not found..!") {

                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()

                                    getActivityContext?.replaceFragment(
                                        ReminderFragment(),
                                        ReminderFragment().javaClass.simpleName,
                                        false, true
                                    )


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


}

