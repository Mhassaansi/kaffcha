package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

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
import com.fictivestudios.tafcha.adapters.ChallengeReceivedAdapter

import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_recived.ChallengeReceivedItem
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_recived.challengeRecived
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_challenge_received.view.*
import kotlinx.android.synthetic.main.fragment_upload_challeng.view.*
import kotlinx.coroutines.launch
import retrofit2.Response




class ChallengeReceivedFragment : BaseFragment() {



    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

        titlebar.setMainTitle(
            getString(R.string.challenges),

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            },
            R.drawable.btn_back_arrow,

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            ChallengeFriendsFragment(),
                            ChallengeFriendsFragment().javaClass.simpleName,
                            true, true
                        )
                }
            },
            R.drawable.add_icon
        )



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       mView = inflater.inflate(R.layout.fragment_challenge_received, container, false)
        challengeList()



        return mView
    }

    private fun initChallengeRecyclerview(challengeRecivedList:ArrayList<ChallengeReceivedItem>)
    {



        mView.rv_challenge_received.adapter = context?.let { ChallengeReceivedAdapter(it,challengeRecivedList?.asReversed()) }
        mView.rv_challenge_received.adapter?.notifyDataSetChanged()

    }

    private fun challengeList(){

        lifecycleScope.launch {
            if(Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<challengeRecived>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<challengeRecived> {

                                return apiInterFace.ChallangeRecivedDetails("recieved")

                            }

                            override fun success(response: Response<challengeRecived>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    initChallengeRecyclerview(response.body()!!.data as ArrayList<ChallengeReceivedItem>)

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