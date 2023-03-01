package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.interfaces.ItemClickListner
import com.fictivestudios.tafcha.models.LanguageModel
import kotlinx.android.synthetic.main.item_language.view.*


class LanguageAdapter(private val mlist:ArrayList<LanguageModel>,itempos:ItemClickListner):RecyclerView.Adapter<ViewHolder>() {


//    val contex = context
//    var activity = contex as MainActivity

    val languageList = itempos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mlist.get(position))

        holder.itemView.setOnClickListener {
            languageList.getItemPosition(position)
        }
    }

    override fun getItemCount(): Int {
        return mlist.size
    }
}


class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
{
    fun bind(item:LanguageModel)
    {
        itemView.language_flag.setImageResource(item.flag!!.toInt())
        itemView.language_name.setText(item.lName)
    }
}