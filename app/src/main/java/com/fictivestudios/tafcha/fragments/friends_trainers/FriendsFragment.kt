package com.fictivestudios.tafcha.fragments.friends_trainers

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.adapters.AddFriendsAdapter
import com.fictivestudios.tafcha.adapters.FriendsAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.TrainersAdapter
import com.fictivestudios.tafcha.models.freinds.allusers.AllUsers
import com.fictivestudios.tafcha.models.freinds.friendlist.FriendsData
import com.fictivestudios.tafcha.models.freinds.allusers.FriendAllUserItem
import com.fictivestudios.tafcha.models.freinds.friendlist.FreindList
import com.fictivestudios.tafcha.models.trainers.alltrainers.AlltrainersItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_friends.view.*
import kotlinx.android.synthetic.main.fragment_trainer_list.view.*
import kotlinx.coroutines.launch
import retrofit2.Response



class FriendsFragment : BaseFragment() {


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


       mView =  inflater.inflate(R.layout.fragment_friends, container, false)




        friendList()
        alluserFriendList()

        return mView
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initFriendsRecyclerview(friendList:ArrayList<FriendsData>?)
    {
       val adapter = context?.let { FriendsAdapter(it,friendList!!) }
        mView.rv_request_friend.adapter = adapter
        mView.rv_invite_friend.adapter?.notifyDataSetChanged()

//        val adapter =  context?.let { friendList?.let { it1 ->
//            FriendsAdapter(it,
//                it1
//            )
//        } }
//
//        mView.rv_request_friend.adapter = adapter
//        mView.rv_request_friend.adapter?.notifyDataSetChanged()







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


   private  fun initAddFriends(addFriends:List<FriendAllUserItem>?=null){
        mView.rv_invite_friend.adapter = context?.let { AddFriendsAdapter(it,addFriends!!) }
        mView.rv_invite_friend.adapter?.notifyDataSetChanged()

    }



    private fun alluserFriendList(){

        lifecycleScope.launch {
            if(Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<AllUsers>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<AllUsers> {
                                return apiInterFace.allUsers()
                            }

                            override fun success(response: Response<AllUsers>) {


                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    initAddFriends(response.body()!!.data as ArrayList<FriendAllUserItem>)
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
                                   Log.e("fjjfs",""+response.body()?.data)
                                   if(response.body()?.data!=null){
                                       initFriendsRecyclerview(response.body()?.data as ArrayList<FriendsData>)
                                   }
                                   else{
                                       Log.e("fjjfs",""+response.body()?.message)
                                       RetrofitSetup().hideLoader()
                                       Toast.makeText(requireActivity(), response.body()?.message, Toast.LENGTH_SHORT).show()
                                   }


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
                               Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
                           }

                       })
               } catch (e: Exception) {
                   e.printStackTrace()
               }
           }
       }
    }

}