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
import com.fictivestudios.tafcha.Utils.PreferenceData
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.adapters.ResultHistoryAdapter
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_compelete.Compelete
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_compelete.CompeletedItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import kotlinx.android.synthetic.main.fragment_pending_history.view.*
import kotlinx.android.synthetic.main.fragment_result_history.view.*
import kotlinx.coroutines.launch
import retrofit2.Response


class ResultHistoryFragment : Fragment() {


    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_result_history, container, false)

        challengeResultList()

        return mView
    }


    private fun initHistoryResultRecyclerview(resultList:ArrayList<CompeletedItem>)
    {


        mView.rv_history_result.adapter = context?.let { ResultHistoryAdapter(it,resultList) }
        mView.rv_history_result.adapter?.notifyDataSetChanged()

    }


    private fun challengeResultList(){

        lifecycleScope.launch {
            if(Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<Compelete>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<Compelete> {

                                return apiInterFace.ChallengeResult("complete")

                            }

                            override fun success(response: Response<Compelete>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    initHistoryResultRecyclerview(response.body()!!.data as ArrayList<CompeletedItem>)

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