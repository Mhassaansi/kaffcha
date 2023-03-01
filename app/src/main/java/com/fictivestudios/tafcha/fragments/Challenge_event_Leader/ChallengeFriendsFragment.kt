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
import com.fictivestudios.tafcha.adapters.ChallengeFriendsAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.freinds.friendlist.FreindList
import com.fictivestudios.tafcha.models.freinds.friendlist.FriendsData
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getFormDataBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_challenge_friends.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.lang.Exception




class ChallengeFriendsFragment(var desp:String?="",var img:String?="",var email:String?="",var name:String?="") : BaseFragment() {

   // var vFile:MultipartBody.Part,var description:String

    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.challenge_a_friend))

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

        mView = inflater.inflate(R.layout.fragment_challenge_friends, container, false)
        friendList()
        return mView
    }

    private fun initFriendListRecyclerview(friendList:ArrayList<FriendsData>)
    {
        mView.rv_challlenge_friends.adapter = context?.let { ChallengeFriendsAdapter(it,friendList,desp.toString(),img.toString(),email.toString(),name.toString()) }
        mView.rv_challlenge_friends.adapter?.notifyDataSetChanged()
    }
//,vFile,description

    private fun friendList(){
        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireContext(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<FreindList>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<FreindList> {
                                return apiInterFace.friendList()
                            }

                            override fun success(response: Response<FreindList>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    // initAddFriends()
                                    initFriendListRecyclerview(response.body()?.data as ArrayList<FriendsData>)

                                } else {
                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()?.message,
                                        Toast.LENGTH_SHORT
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