package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment

import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.freinds.allusers.FriendAllUserItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_add_friend.view.*
import kotlinx.android.synthetic.main.item_add_friend.view.btn_send_request
import kotlinx.android.synthetic.main.item_challenge_friends.view.profile_image
import kotlinx.android.synthetic.main.item_challenge_friends.view.tv_user_name
import kotlinx.android.synthetic.main.item_trainer.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception




class AddFriendsAdapter (context: Context, private val mlist:List<FriendAllUserItem>)
    :RecyclerView.Adapter<AddFriendsAdapter.ViewHolder>(){

    val contex = context
    var activity = contex as MainActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_friend, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


       holder.itemView.profile_image.setOnClickListener {

           val activity = contex as MainActivity
           activity.
           replaceFragment(
               OtherUserFragment(true,mlist.get(position).id)
               ,OtherUserFragment(true,mlist.get(position).id).javaClass.simpleName,
               true,true)
       }



        holder.itemView.tv_user_name.setOnClickListener {

            val activity = contex as MainActivity
            activity.
            replaceFragment(OtherUserFragment(true,mlist.get(position).id)
                ,OtherUserFragment(true,mlist.get(position).id).javaClass.simpleName,
                true,true)
        }


        holder.bind(mlist.get(position))

        holder.itemView.btn_send_request.setOnClickListener {

            addFriend(mlist.get(position).id)

            holder.itemView.btn_send_request.setBackgroundResource(R.drawable.request_sent_icon)
        }


    }


    override fun getItemCount(): Int { return mlist.size }




    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:FriendAllUserItem){
            itemView.tv_user_name.text = item.name
            if(item.image==null){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else{
                Glide.with(itemView.context).load(MEDIA_URL+item.image).into(itemView.profile_image)
            }


        }
    }

    fun addFriend(f_id:Int?=0){
        GlobalScope.launch(Dispatchers.IO) {
            if (Constants.isNetworkConnected(contex, true)) {
                try {
                    RetrofitSetup().callApi(activity, true, PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.addFriend(f_id!!)
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if ( response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    //  Toast.makeText(activity, PreferenceUtils.getString("token"), Toast.LENGTH_SHORT).show()
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //  addTrainerResponse=response.body().toString()
                                } else {
                                    Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT)
                                        .show()
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