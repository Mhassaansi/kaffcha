package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.tafcha.R
import kotlinx.android.synthetic.main.item_history_extand.view.*


class HistoryExpandedAdapter (context: Context, private val mlist:ArrayList<String>) :RecyclerView.Adapter<HistoryExpandedAdapter.ViewHolder>(){

    private var isExpanded = false

    val contex = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_extand, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.itemView.btn_arrow_down.setOnClickListener {

            if (!isExpanded)
            {
                isExpanded = true
                expandText(holder.itemView)
            }
            else
            {
                isExpanded = false
                hideText(holder.itemView)

            }

        }


    }



    private fun hideText(itemView: View)
    {
        itemView.btn_arrow_down.setImageResource(R.drawable.arrow_right_icon)
        itemView.expand_layout.visibility = View.GONE
    }

    private fun expandText(itemView: View)
    {
        itemView.btn_arrow_down.setImageResource(R.drawable.arrow_down_icon)
        itemView.expand_layout.visibility = View.VISIBLE
    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

    }


}