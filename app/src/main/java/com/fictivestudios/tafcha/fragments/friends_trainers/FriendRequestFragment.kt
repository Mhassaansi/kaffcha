package com.fictivestudios.tafcha.fragments.friends_trainers

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
import com.fictivestudios.tafcha.adapters.FriendsRequestAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.interfaces.ItemClickListner
import com.fictivestudios.tafcha.models.freinds.friendlist.FriendsData

import com.fictivestudios.tafcha.models.freinds.pending.PendingFriends
import com.fictivestudios.tafcha.models.freinds.pending.PendingFriendsData
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_friend_request.view.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception




class FriendRequestFragment : BaseFragment(),ItemClickListner {

    private lateinit var mView: View
     var pendingFreinds = ArrayList<PendingFriendsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.friends_request))
        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()}})

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
        mView = inflater.inflate(R.layout.fragment_friend_request, container, false)
        friendRequest()
        return mView
    }

    private fun initFriendsRecyclerview(requestFriendList:ArrayList<PendingFriendsData>)
    {
        mView.rv_friend_request.adapter = context?.let { FriendsRequestAdapter(it,requestFriendList,this) }
        mView.rv_friend_request.adapter?.notifyDataSetChanged()
    }



    private fun friendRequest(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<PendingFriends>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<PendingFriends> {
                                return apiInterFace.pendingFriendList()
                            }

                            override fun success(response: Response<PendingFriends>) {


                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    if(response.body()!!.data!=null){

                                        pendingFreinds = response.body()!!.data as ArrayList<PendingFriendsData>
                                        initFriendsRecyclerview(pendingFreinds)


                                    }
                                    else{
                                        Toast.makeText(
                                            requireActivity(),
                                            response.body()?.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }



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
                                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
                            }


                        })
                } catch (e: Exception) {
                    e.printStackTrace()

                }

            }
        }

    }

    override fun getItemPosition(pos: Int) {



    }


}