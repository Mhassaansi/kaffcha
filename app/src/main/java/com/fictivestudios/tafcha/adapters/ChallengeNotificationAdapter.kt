package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.dialogFragments.ChallengeWonFragment
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.ChallengesFragment
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.FeedFragment
import com.fictivestudios.tafcha.fragments.friends_trainers.FriendsMainFragment
import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment
import com.fictivestudios.tafcha.fragments.friends_trainers.UsersFragment
import com.fictivestudios.tafcha.fragments.live_other.SubscribeFragment
import com.fictivestudios.tafcha.fragments.profile.HistoryFragment
import com.fictivestudios.tafcha.fragments.reminder_result_reward.ReminderFragment
import com.fictivestudios.tafcha.models.ChallengeFriend
import com.fictivestudios.tafcha.models.notification.NotificationItem

import kotlinx.android.synthetic.main.item_challenge_friends.view.profile_image
import kotlinx.android.synthetic.main.item_challenge_friends.view.tv_user_name
import kotlinx.android.synthetic.main.item_challenge_notification.view.*

class ChallengeNotificationAdapter (context: Context, private val mlist:MutableList<NotificationItem>)
    :RecyclerView.Adapter<ChallengeNotificationAdapter.ViewHolder>(){

    val contex = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge_notification, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = contex as MainActivity


        holder.bind(mlist.get(position))


        holder.itemView.notiCard.setOnClickListener {

            if(mlist.get(position).notification_type=="Challenge Recieved"){
                activity.
                replaceFragment(
                    ChallengesFragment()
                    , ChallengesFragment().javaClass.simpleName,
                    true,true)
            }
            else if(mlist.get(position).notification_type=="Challenge Accept") {
                activity
                    .replaceFragment(
                        FeedFragment(),
                        FeedFragment().javaClass.simpleName,
                        true, true
                    )
            }
            else if(mlist.get(position).notification_type=="Challenge Winner"){
                activity.
                replaceFragment(
                    HistoryFragment()
                    ,HistoryFragment().javaClass.simpleName,
                    true,true)
            }
            else if(mlist.get(position).notification_type=="Challenge Runner"){
                activity.
                replaceFragment(
                    HistoryFragment()
                    ,HistoryFragment().javaClass.simpleName,
                    true,true)
            }
            else if(mlist.get(position).notification_type=="Reminder"){
                activity.
                replaceFragment(
                    ReminderFragment()
                    ,ReminderFragment().javaClass.simpleName,
                    true,true)
            }
            else if(mlist.get(position).notification_type=="Request Recieved"){
                activity.
                replaceFragment(
                    FriendsMainFragment()
                    ,FriendsMainFragment().javaClass.simpleName,
                    true,true)
            }
            else if(mlist.get(position).notification_type=="Request Accept"){
                activity.
                replaceFragment(
                    FriendsMainFragment()
                    ,FriendsMainFragment().javaClass.simpleName,
                    true,true)
            }
            else if(mlist.get(position).notification_type=="Follow Notification"){
                activity.
                replaceFragment(
                    UsersFragment()
                    ,UsersFragment().javaClass.simpleName,
                    true,true)
            }

            else if(mlist.get(position).notification_type=="Trainer Live Notification"){
                activity.
                replaceFragment(
                    SubscribeFragment()
                    ,SubscribeFragment().javaClass.simpleName,
                    true,true)
            }
            else if(mlist.get(position).notification_type=="Event Started Trainer"){
            }
            else if(mlist.get(position).notification_type=="Event Created"){
            }

        }



    }


    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: NotificationItem)
        {
            itemView.tv_user_name.text = item.title
            itemView.tv_challenged_to_you.text = item.description


        }

    }


}