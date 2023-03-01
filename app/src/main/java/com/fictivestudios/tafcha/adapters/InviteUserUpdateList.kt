package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.models.event.event_item.User
import kotlinx.android.synthetic.main.item_invite_users.view.*


class InviteUserUpdateList(context: Context, private val mlist:ArrayList<User>) :
    RecyclerView.Adapter<InviteUserUpdateList.ViewHolder>(){

    val contex = context
   // val mInviteFriendsList=mInviteFriendsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_invite_users, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: InviteUserUpdateList.ViewHolder, position: Int) {
        holder.bind(mlist.get(position))
    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {

        fun bind(item:User){

           Glide.with(itemView.context).load(R.drawable.user_blank_profile).into(itemView.iv_user_dp)
           // MEDIA_URL
//            itemView.setOnClickListener {
//
//            }

        }

    }




}