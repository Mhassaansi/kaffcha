package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.ChallengeReceivedDetailsFragment
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_recived.ChallengeReceivedItem
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_recieved_challenge.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChallengeReceivedAdapter (context: Context,val mlist:MutableList<ChallengeReceivedItem>?)
    :RecyclerView.Adapter<ChallengeReceivedAdapter.ViewHolder>(){

    val contex = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recieved_challenge, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.itemView.setOnClickListener {
            val activity = contex as MainActivity
            activity.
            replaceFragment(
                ChallengeReceivedDetailsFragment(
                    mlist?.get(position)?.challenge_description!!,
                    mlist.get(position).sender_video!!,
                    mlist.get(position).id?.toInt()!!,
                    mlist.get(position).sender.image!!,
                    mlist.get(position).sender.name!!,
                    mlist.get(position).sender.email!!

                )

                ,ChallengeReceivedDetailsFragment(
                    mlist.get(position).challenge_description!!,
                    mlist.get(position).sender_video!!,
                    mlist.get(position).id!!.toInt()!!,
                    mlist.get(position).sender.image!!,
                    mlist.get(position).sender.name!!,
                    mlist.get(position).sender.email!!
                ).javaClass.simpleName,
                true,true)
        }
        holder.bind(mlist!!.get(position))




    }


    override fun getItemCount(): Int { return mlist!!.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: ChallengeReceivedItem){


            if(item.sender.name == "null"){
                itemView.tv_user_name.text = ""
            }
            else{

                  itemView.tv_user_name.text=item.sender.name
           }



            if(item.sender.image == "null"){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)

            }
            else{
                Glide.with(itemView.context).load(MEDIA_URL+item.sender.image).into(itemView.profile_image)
            }


            itemView.tv_rewards.text="Rewards "+ item.points +" Points"
            itemView.tv_challenged_to_you.text = item.sender.name+"  Challenged you"

        }
    }


}