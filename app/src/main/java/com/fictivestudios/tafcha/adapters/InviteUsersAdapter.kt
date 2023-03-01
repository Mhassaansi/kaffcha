package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.interfaces.InviteFriendsList
import com.fictivestudios.tafcha.models.trainers.viewmyusers.MyUserItem
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_invite_users.view.*

class InviteUsersAdapter (context: Context, private val mlist:ArrayList<MyUserItem>,mInviteFriendsList: InviteFriendsList)
    :RecyclerView.Adapter<InviteUsersAdapter.ViewHolder>(){

    val contex = context
    val mInviteFriendsList=mInviteFriendsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_invite_users, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (position == mlist.size-1)
        {
         holder.itemView.iv_user_dp.setImageResource(R.drawable.invite)

        }

        holder.bind(mlist.get(position))

         holder.itemView.setOnClickListener {

             mInviteFriendsList.getUser(position)
         }


    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        fun bind(item:MyUserItem){
            if(item.image==null){
                itemView.iv_user_dp.setImageResource(R.drawable.user_blank_profile)
            }
            else
            Glide.with(itemView.context).load(MEDIA_URL+item.image).into(itemView.iv_user_dp)



        }

    }


}