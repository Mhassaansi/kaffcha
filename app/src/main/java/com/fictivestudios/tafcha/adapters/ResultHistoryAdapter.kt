package com.fictivestudios.tafcha.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ch.qos.logback.core.util.Loader.getResources
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.PreferenceData
import com.fictivestudios.tafcha.Utils.PreferenceUtils.getString
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.reminder_result_reward.ResultDetailsFragment
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_compelete.CompeletedItem
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_add_friend.view.*
import kotlinx.android.synthetic.main.item_challenge_request.view.*
import kotlinx.android.synthetic.main.item_challenge_request.view.profile_image
import kotlinx.android.synthetic.main.item_challenge_request.view.tv_user_name
import kotlinx.android.synthetic.main.item_result.view.*


class ResultHistoryAdapter (context: Context, private val mlist:ArrayList<CompeletedItem>)
    :RecyclerView.Adapter<ResultHistoryAdapter.ViewHolder>(){

    val contex = context
    private val loginData by lazy { PreferenceData.retrieveLoginData(contex) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            val activity = contex as MainActivity

            holder.bind(mlist.get(position))

            holder.itemView.setOnClickListener {
                activity.replaceFragment(
                        ResultDetailsFragment(mlist.get(position).challenge_id!!),
                        ResultDetailsFragment(mlist.get(position).challenge_id!!).javaClass.simpleName,
                        true, true
                    )
            }

        }



//        if(mlist.get(position).oponent_data.id == loginData.id){
//
//
//        }


          if(mlist.get(position).score=="i am winner"){

          }
        else{
            holder.itemView.tv_status.setBackgroundResource(R.drawable.round_red)
              holder.itemView.tv_status.setTextColor(ContextCompat.getColor(contex,R.color.white))
            holder.itemView.tv_status.setText(contex.getResources().getString(R.string.lost))
        }

    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        fun bind(item:CompeletedItem){

           itemView.tv_user_name.text = item.oponent_data.name
            Glide.with(itemView.context).load(MEDIA_URL+item.oponent_data.image).into(itemView.profile_image)
           // itemView.tv_rewards.text = "50 Points"
        }

    }


}