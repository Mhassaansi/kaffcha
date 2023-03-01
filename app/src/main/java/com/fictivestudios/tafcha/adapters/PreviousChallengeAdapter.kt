package com.fictivestudios.tafcha.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity

import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment
import kotlinx.android.synthetic.main.item_challenge_in_progress.view.*


class PreviousChallengeAdapter (context: Context, private val mlist:ArrayList<String>)
    :RecyclerView.Adapter<PreviousChallengeAdapter.ViewHolder>(){

    val contex = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge_in_progress, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


       holder.itemView.profile_image.setOnClickListener {

           val activity = contex as MainActivity
           activity.
           replaceFragment(
               OtherUserFragment(true,0)
               ,OtherUserFragment(true,0).javaClass.simpleName,
               true,true)
       }



        holder.itemView.tv_user_name.setOnClickListener {

            val activity = contex as MainActivity
            activity.
            replaceFragment(OtherUserFragment(true,0)
                ,OtherUserFragment(true,0).javaClass.simpleName,
                true,true)
        }











    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

    }


}