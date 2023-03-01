package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.adapters.LeaderBoardAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.models.excercies.ExerCiesList
import com.fictivestudios.tafcha.models.excercies.ExerciesItem
import com.fictivestudios.tafcha.models.leaderBorad.LeaderBorad
import com.fictivestudios.tafcha.models.leaderBorad.LeaderBoradItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup

import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_friends.view.*
import kotlinx.android.synthetic.main.fragment_leaderboard.view.*
import kotlinx.android.synthetic.main.fragment_leaderboard.view.tv_search
import kotlinx.coroutines.launch
import retrofit2.Response


class LeaderboardFragment : BaseFragment() {

    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.leader_board))

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
       mView =  inflater.inflate(R.layout.fragment_leaderboard, container, false)

        leaderBoradList()

        return mView
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initTrainingRecyclerview(leaderBoardList:ArrayList<LeaderBoradItem>)
    {
        val adapter = context?.let { LeaderBoardAdapter(it,leaderBoardList) }
        mView.rv_leaderboard.adapter = adapter
        mView.rv_leaderboard.adapter?.notifyDataSetChanged()


        //mView.rv_request_friend.adapter?.notifyDataSetChanged()

        mView.tv_search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter?.filter?.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun leaderBoradList(){

        lifecycleScope.launch {

            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<LeaderBorad>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<LeaderBorad> {

                                return apiInterFace.leaderBorad()

                            }

                            override fun success(response: Response<LeaderBorad>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    initTrainingRecyclerview(response.body()?.data as ArrayList<LeaderBoradItem>)

                                } else {

                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()?.message,
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