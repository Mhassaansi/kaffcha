package com.fictivestudios.tafcha.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment
import com.fictivestudios.tafcha.interfaces.ItemClickListner

import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.freinds.friendlist.FriendsData
import com.fictivestudios.tafcha.models.freinds.pending.PendingFriends
import com.fictivestudios.tafcha.models.freinds.pending.PendingFriendsData
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_challenge_friends.view.profile_image
import kotlinx.android.synthetic.main.item_challenge_friends.view.tv_user_name
import kotlinx.android.synthetic.main.item_friend_request.view.*
import kotlinx.android.synthetic.main.item_training_expand.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class FriendsRequestAdapter (context: Context, private var mlist:ArrayList<PendingFriendsData>?,itemPos: ItemClickListner)
    :RecyclerView.Adapter<FriendsRequestAdapter.ViewHolder>(){

    val contex = context
    var activity = contex as MainActivity
    var itemPos = itemPos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend_request, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


       holder.itemView.profile_image.setOnClickListener {

           val activity = contex as MainActivity
           activity.
           replaceFragment(
               OtherUserFragment(false,mlist?.get(position)?.id)
               ,OtherUserFragment(false,mlist?.get(position)?.id).javaClass.simpleName,
               true,true)
       }



        holder.itemView.tv_user_name.setOnClickListener {

            val activity = contex as MainActivity
            activity.
            replaceFragment(OtherUserFragment(false,mlist?.get(position)?.id)
                ,OtherUserFragment(false,mlist?.get(position)?.id).javaClass.simpleName,
                true,true)
        }

        mlist?.let { holder.bind(it.get(position)) }

        holder.itemView.btn_accept.setOnClickListener {
            holder.itemView.btn_accept.isEnabled = false
            holder.itemView.btn_decline.isEnabled = false
            friendRequestStatus("accept", mlist?.get(position)?.id)
        }
        holder.itemView.btn_decline.setOnClickListener {
            holder.itemView.btn_accept.isEnabled = false
            holder.itemView.btn_decline.isEnabled = false
            friendRequestStatus("reject", mlist?.get(position)?.id)
        }


        itemPos.getItemPosition(position)

    }


    override fun getItemCount(): Int { return mlist!!.size}

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        fun bind(item:PendingFriendsData){

            itemView.tv_user_name.setText(item.name)
            if(item.image==null){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else
            Glide.with(itemView.context).load(MEDIA_URL+item.image).into(itemView.profile_image)
        }

    }

    private fun friendRequestStatus(r_status:String?="",r_id:Int?=0){

        GlobalScope.launch {
            if (Constants.isNetworkConnected(contex, true)) {
                try {

                    RetrofitSetup().callApi(contex, true, PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {

                                return apiInterFace.updateRequest(r_id!!, r_status!!)

                            }

                            override fun success(response: Response<CommonResponseDC>) {

                                if (response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    Toast.makeText(contex, response.body()!!.message, Toast.LENGTH_LONG).show()

                                } else {

                                    Toast.makeText(contex,response.body()!!.message, Toast.LENGTH_LONG).show()

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


    private fun friendRequest(){

        GlobalScope.launch {
            if (Constants.isNetworkConnected(activity, true)) {
                try {
                    RetrofitSetup().callApi(activity,
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<PendingFriends>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<PendingFriends> {
                                return apiInterFace.pendingFriendList()
                            }

                            override fun success(response: Response<PendingFriends>) {


                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    //initFriendsRecyclerview(response.body()!!.data as ArrayList<PendingFriendsData>)
                                           mlist = response.body()!!.data as ArrayList<PendingFriendsData>

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


}