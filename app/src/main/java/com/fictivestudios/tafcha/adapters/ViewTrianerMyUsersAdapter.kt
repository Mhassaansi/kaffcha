package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity

import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment
import com.fictivestudios.tafcha.models.freinds.friendlist.FriendsData
import com.fictivestudios.tafcha.models.trainers.viewmyusers.MyUserItem
import com.fictivestudios.tafcha.models.trainers.viewmyusers.ViewMyUsers
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_challenge_friends.view.profile_image
import kotlinx.android.synthetic.main.item_challenge_friends.view.tv_user_name
import kotlinx.android.synthetic.main.item_trainer.view.*

class ViewTrianerMyUsersAdapter(context: Context, private val mlist:ArrayList<MyUserItem>): RecyclerView.Adapter<ViewTrianerMyUsersAdapter.ViewHolder>() {

    val contex = context
    var activity = contex as MainActivity

    var filteredList = ArrayList<MyUserItem>()

    init {
        filteredList = mlist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.itemView.profile_image.setOnClickListener {

            val activity = contex as MainActivity
            activity.
            replaceFragment(
                OtherUserFragment(true,mlist.get(position).id!!.toInt())
                ,OtherUserFragment(true,mlist.get(position).id!!.toInt()).javaClass.simpleName,
                true,true)
        }



        holder.itemView.tv_user_name.setOnClickListener {

            val activity = contex as MainActivity
            activity.
            replaceFragment(OtherUserFragment(true,mlist.get(position).id!!.toInt())
                ,OtherUserFragment(true,mlist.get(position).id!!.toInt()).javaClass.simpleName,
                true,true)
        }

        holder.bind(mlist.get(position))



    }


    override fun getItemCount(): Int { return filteredList.size }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: MyUserItem){

            itemView.tv_user_name.text = item.name
            if(item.image==null){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else
            Glide.with(itemView.context).load(MEDIA_URL+item.image).into(itemView.profile_image)

        }

    }
}

