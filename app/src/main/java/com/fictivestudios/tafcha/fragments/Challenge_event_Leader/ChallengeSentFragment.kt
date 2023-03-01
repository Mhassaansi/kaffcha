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
import com.fictivestudios.tafcha.adapters.ChallengeSentAdapter
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent.ChallengeSent
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent.ChallengeSentItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_challenge_sent.view.*
import kotlinx.coroutines.launch
import retrofit2.Response



class ChallengeSentFragment : BaseFragment() {


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
        mView = inflater.inflate(R.layout.fragment_challenge_sent, container, false)
        challengeList()
        return mView
    }


    private fun initChallengeRecyclerview(challengeSentList: ArrayList<ChallengeSentItem>?)
    {
        mView.rv_challenge_sent.adapter = context?.let { ChallengeSentAdapter(it, challengeSentList!!.asReversed()) }
        mView.rv_challenge_sent.adapter?.notifyDataSetChanged()
    }


    private fun challengeList(){

            lifecycleScope.launch {
                if (Constants.isNetworkConnected(requireActivity(), true)) {
                    try {
                        RetrofitSetup().callApi(requireActivity(),
                            true,
                            PreferenceUtils.getString("token"),
                            object : CallHandler<Response<ChallengeSent>> {
                                override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ChallengeSent> {

                                    return apiInterFace.challangeSentDetails("sent")

                                }

                                override fun success(response: Response<ChallengeSent>) {

                                    if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                        initChallengeRecyclerview(response.body()!!.data as ArrayList<ChallengeSentItem>)

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
                                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG)
                                        .show()
                                }


                            })

                    } catch (e: Exception) {

                    }

                }

            }

    }

}