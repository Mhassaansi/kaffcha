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
import com.fictivestudios.tafcha.adapters.ChallengesFeedAdapter
import com.fictivestudios.tafcha.Utils.Titlebar

import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.models.feed.Feed
import com.fictivestudios.tafcha.models.feed.FeedItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class FeedFragment : BaseFragment() {


    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.challenge_feed))
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


        mView = inflater.inflate(R.layout.fragment_feed, container, false)
        feed()
        return mView
    }

    private fun initChallengeFeedRecyclerview(feedData:ArrayList<FeedItem>?)
    {
        mView.rv_chalanege_feed.adapter = context?.let { ChallengesFeedAdapter(it,feedData!!.asReversed()) }
        mView.rv_chalanege_feed.adapter?.notifyDataSetChanged()

    }




    private fun feed(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<Feed>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<Feed> {

                                return apiInterFace.viewFeed()

                            }

                            override fun success(response: Response<Feed>) {
                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    initChallengeFeedRecyclerview(response.body()!!.data as ArrayList<FeedItem>)
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