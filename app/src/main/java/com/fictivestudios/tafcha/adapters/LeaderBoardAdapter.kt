package com.fictivestudios.tafcha.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity

import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment
import com.fictivestudios.tafcha.models.leaderBorad.LeaderBoradItem
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_leaderboard.view.*
import java.util.*
import kotlin.collections.ArrayList


class LeaderBoardAdapter (context: Context, private var mlist:ArrayList<LeaderBoradItem>)
    :RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>(), Filterable {

    val contex = context
    var filteredList = ArrayList<LeaderBoradItem>()
    val activity = contex as MainActivity

    init {
        filteredList = mlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mlist.get(position))

       holder.itemView.profile_image.setOnClickListener {
           activity.
           replaceFragment(
               OtherUserFragment(true,mlist.get(position).id)
               ,OtherUserFragment(true,0).javaClass.simpleName,
               true,true)
       }



        holder.itemView.tv_user_name.setOnClickListener {
            val activity = contex as MainActivity
            activity.
            replaceFragment(OtherUserFragment(true,mlist.get(position).id)
                ,OtherUserFragment(true,0).javaClass.simpleName,
                true,true)
        }



      //  val item = mlist.get(holder.adapterPosition)

           if(position == 0){
               holder.itemView.iv_post_icon.setImageResource(R.drawable.position_gold_icon)
           }
        else if(position == 1){
               holder.itemView.iv_post_icon.setImageResource(R.drawable.silver_position_icon)
        }

        else if (position == 2){
               holder.itemView.iv_post_icon.setImageResource(R.drawable.metal_postion_icon)
        }
        else{

               holder.itemView.iv_post_icon.visibility =  View.GONE

        }




    }


    override fun getItemCount(): Int { return filteredList.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        @SuppressLint("SetTextI18n")
        fun bind(item:LeaderBoradItem){

           Glide.with(itemView.context).load(MEDIA_URL+item.image).into(itemView.profile_image)
            itemView.tv_user_name.setText(item.name)
            itemView.tv_user_postion.setText((adapterPosition+1).toString())


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
                    val resultList = ArrayList<LeaderBoradItem>()
                    for (row in filteredList) {
                        if (row.name?.lowercase(Locale.ROOT)?.contains(charSearch.lowercase(Locale.ROOT))== true ) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mlist = results?.values as ArrayList<LeaderBoradItem>
                notifyDataSetChanged()
            }

        }
    }


}