package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.EventDetailsFragment
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.NewEventsFragment
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC

import com.fictivestudios.tafcha.models.event.event_list.EventListItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.currentevent_item.view.*
import kotlinx.android.synthetic.main.item_challenge_friends.view.*
import kotlinx.android.synthetic.main.item_events.view.*
import kotlinx.android.synthetic.main.item_events.view.tv_event_name
import kotlinx.android.synthetic.main.item_events.view.tv_time
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class CurrentAdapter (context: Context, private val mlist:ArrayList<EventListItem>?)
    : RecyclerView.Adapter<CurrentAdapter.ViewHolder>(){

    val contex = context
    var activity = context as MainActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currentevent_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
//            activity
//                .replaceFragment(
//                    EventDetailsFragment(mlist?.get(position)?.id!!),
//                    EventDetailsFragment().javaClass.simpleName,
//                    true, true
//                )

            activity.replaceFragment(
                NewEventsFragment(true,mlist?.get(position)?.id!!,0),
                NewEventsFragment(true,mlist?.get(position)?.id!!,0).javaClass.simpleName,true,true)
        }


        holder.bind(mlist!!.get(position))


    }


    override fun getItemCount(): Int { return mlist!!.size }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: EventListItem) {

            itemView.tv_event_name.text = item.title
            itemView.tv_time.text = item.time
            if(item.image==null){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else
            Glide.with(itemView.context)
                .load(MEDIA_URL+item.image)
                .into(itemView.img_current_event)



        }
    }




}

