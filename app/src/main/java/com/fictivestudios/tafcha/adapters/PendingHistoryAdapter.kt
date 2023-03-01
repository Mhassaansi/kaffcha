package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_pending.ChallengePendingItem
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_pending.view.*
import kotlinx.android.synthetic.main.item_pending.view.profile_image
import kotlinx.android.synthetic.main.item_pending.view.tv_challenged_to_you
import kotlinx.android.synthetic.main.item_pending.view.tv_rewards
import kotlinx.android.synthetic.main.item_pending.view.tv_user_name
import kotlinx.android.synthetic.main.item_sent_challenge.view.*


class PendingHistoryAdapter (context: Context, val mlist:ArrayList<ChallengePendingItem>)
    :RecyclerView.Adapter<PendingHistoryAdapter.ViewHolder>(){

    val contex = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pending, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val activity = contex as MainActivity
        holder.bind(mlist.get(position))

    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:ChallengePendingItem){
            itemView.tv_user_name.text =  item.sender.name
            itemView.tv_challenged_to_you.text = item.reciever.name+" Challeng you"
            itemView.tv_rewards.text = "Rewards "+ item.points +" Points"
            if(item.sender.image==null){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else
            Glide.with(itemView.context).load(MEDIA_URL+item.sender.image).into(itemView.profile_image)

        }
    }


}