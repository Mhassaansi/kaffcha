package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment

import com.fictivestudios.tafcha.models.freinds.friendlist.FriendsData
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL

import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_training_expand.view.*
import java.util.*
import kotlin.collections.ArrayList

class FriendsAdapter (context: Context, private var mlist:ArrayList<FriendsData>)
    :RecyclerView.Adapter<FriendsAdapter.ViewHolder>()
    , Filterable
{
    val contex = context
    var activity = contex as MainActivity

    var filteredList = ArrayList<FriendsData>()

    init {
        filteredList = mlist
        notifyDataSetChanged()
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


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:FriendsData){

            itemView.tv_user_name.text = item.name
            if(item.image==null){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else
            Glide.with(itemView.context).load(MEDIA_URL+item.image).into(itemView.profile_image)

        }

    }



    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val charSearch = constraint.toString()
                if (charSearch== null || charSearch.isEmpty()) {
                    // filteredList = mList
                    filterResults.count = filteredList.size
                    filterResults.values = filteredList
                }

                else {
                    val resultList = ArrayList<FriendsData>()
                    for (row in filteredList) {
                        if (row.name?.lowercase(Locale.ROOT)?.contains(charSearch.lowercase(Locale.ROOT)) == true) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }

                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mlist = results?.values as ArrayList<FriendsData>
                notifyDataSetChanged()
            }

        }

        }


}