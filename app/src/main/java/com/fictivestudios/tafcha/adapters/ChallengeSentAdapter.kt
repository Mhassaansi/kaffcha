package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.PendingDetailsFragment
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent.ChallengeSentItem
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_sent_challenge.view.*
import kotlinx.android.synthetic.main.item_sent_challenge.view.profile_image
import kotlinx.android.synthetic.main.item_sent_challenge.view.tv_user_name


class ChallengeSentAdapter (context: Context, val mlist:MutableList<ChallengeSentItem>)
    :RecyclerView.Adapter<ChallengeSentAdapter.ViewHolder>(){

    val contex = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sent_challenge, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {

            val activity = contex as MainActivity
            activity.
            replaceFragment(
                PendingDetailsFragment(mlist.get(position).challenge_description!!,mlist.get(position).sender_video!!,mlist.get(position).sender.name!!)
                ,PendingDetailsFragment(mlist.get(position).challenge_description!!,mlist.get(position).sender_video!!,mlist.get(position).sender.name!!).javaClass.simpleName,
                true,true)
        }

        holder.bind(mlist!!.get(position))


    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        fun bind(item: ChallengeSentItem){
            itemView.tv_user_name.text = item.reciever.name

            if(item.sender.image=="null"){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else{
                Glide.with(itemView.context).load(MEDIA_URL+item.reciever.image).into(itemView.profile_image)
            }
            itemView.tv_challenged_to_you.text = "You  Challenged "+ item.reciever.name
            itemView.tv_rewards.text="Rewards "+ item.points +" Points"


        }


    }


}