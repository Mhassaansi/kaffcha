package com.fictivestudios.tafcha.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment
import com.fictivestudios.tafcha.models.profile.trainer_profile.UserX
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_client.view.*
import kotlinx.android.synthetic.main.item_client.view.profile_image
import kotlinx.android.synthetic.main.item_client.view.tv_user_name


class ClientAdapter (context: Context, private val mlist:ArrayList<UserX>?)
    :RecyclerView.Adapter<ClientAdapter.ViewHolder>(){

    val contex = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_client, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


       holder.itemView.profile_image.setOnClickListener {

           val activity = contex as MainActivity
           activity.
           replaceFragment(
               OtherUserFragment(true,mlist?.get(position)?.id)
               ,OtherUserFragment(true,mlist?.get(position)?.id).javaClass.simpleName,
               true,true)
       }

        holder.bind(mlist!!.get(position))



    }


    override fun getItemCount(): Int { return mlist!!.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        fun bind(item:UserX){
            if(item.image==null){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else
            Glide.with(itemView.context).load(MEDIA_URL+item.image).into(itemView.profile_image)
            itemView.tv_user_name.setText(item.name)


        }

    }


}