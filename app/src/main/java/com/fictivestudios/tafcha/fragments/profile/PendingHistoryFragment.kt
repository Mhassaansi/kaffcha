package com.fictivestudios.tafcha.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.PendingHistoryAdapter
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_pending.ChallengePending
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_pending.ChallengePendingItem
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent.ChallengeSent
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_pending_history.view.*
import kotlinx.coroutines.launch
import retrofit2.Response


class PendingHistoryFragment : BaseFragment() {


    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       mView = inflater.inflate(R.layout.fragment_pending_history, container, false)

        challengeList()
        return mView
    }

    private fun initHistoryRecyclerview(pendingHistory:ArrayList<ChallengePendingItem>)
    {
        mView.rv_history_pending.adapter = context?.let { PendingHistoryAdapter(it,pendingHistory) }
        mView.rv_history_pending.adapter?.notifyDataSetChanged()

    }

    private fun challengeList(){

        lifecycleScope.launch {
            if(Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<ChallengePending>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ChallengePending> {

                                return apiInterFace.ChallengePending("pending")

                            }

                            override fun success(response: Response<ChallengePending>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    initHistoryRecyclerview(response.body()?.data as ArrayList<ChallengePendingItem>)

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

                }
            }
        }

    }


}