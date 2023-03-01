package com.fictivestudios.tafcha.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.reminder_result_reward.CalenderFragment

import com.fictivestudios.tafcha.interfaces.InviteFriendsList
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.event.event_item.EventItem
import com.fictivestudios.tafcha.models.event.event_item.User
import com.fictivestudios.tafcha.models.reminder.reminderlist.ReminderListItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_event_details.view.*
import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_reminder.view.*
import kotlinx.android.synthetic.main.item_reminder.view.tv_date
import kotlinx.android.synthetic.main.item_training_expand.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ReminderAdapter (context: Context, private val mlist:MutableList<ReminderListItem>)
    :RecyclerView.Adapter<ReminderAdapter.ViewHolder>(){

    val contex = context
    var activity = contex as MainActivity
//    ,reminderList: InviteFriendsList
//    val rList=reminderList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.itemView.setOnClickListener {
//            deleteReminder(mlist.get(position).id)
//            rList.getUser(position)

            activity.replaceFragment(CalenderFragment(true,mlist.get(position).id!!,mlist.get(position).description!!,mlist.get(position).reminder_date!!),
            CalenderFragment(true,mlist.get(position).id!!,mlist.get(position).description!!,mlist.get(position).reminder_date!!).
            javaClass.simpleName,false,true)
        }
        holder.bind(mlist.get(position))


    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        fun bind(item:ReminderListItem){
            itemView.tv_date.text=item.reminder_date
            itemView.tv_reminder_txt.text=item.description

        }

    }





}