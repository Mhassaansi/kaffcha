package com.fictivestudios.tafcha.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.Challenge_event_Leader.UploadChallengFragment
import com.fictivestudios.tafcha.fragments.friends_trainers.OtherUserFragment
import com.fictivestudios.tafcha.models.freinds.friendlist.FriendsData
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.item_challenge_friends.view.*

class ChallengeFriendsAdapter(
    context: Context,
    private val mlist: ArrayList<FriendsData>,
    var desp: String,
    var img: String,
    var email: String,
    var name: String

)
//var  vFile: MultipartBody.Part,
//var   description: String
    : RecyclerView.Adapter<ChallengeFriendsAdapter.ViewHolder>() {

    val contex = context
    val activity = context as MainActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge_friends, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (desp == "") {
            desp = ""
        }
        if (img == "") {
            img = mlist.get(position).image.toString()
        }
        if (name == "") {
            name = mlist.get(position).name.toString()

        }
        if (email == "") {

            email = ""
        }

        holder.itemView.profile_image.setOnClickListener {

            val activity = contex as MainActivity
            activity.replaceFragment(
                OtherUserFragment(true, 0), OtherUserFragment(true, 0).javaClass.simpleName,
                true, true
            )
        }

        holder.bind(mlist.get(position))

        holder.itemView.btn_challenge.setOnClickListener {

            mlist.get(position).id?.let { it1 ->
                UploadChallengFragment(
                    it1,
                    desp,
                    img,
                    email,
                    name
                ).javaClass.simpleName
            }
                ?.let { it2 ->
                    activity.AddFragment(
                        UploadChallengFragment(mlist.get(position).id!!, desp, img, email, name),
                        it2,
                        false, true
                    )
                }


        }



        holder.itemView.tv_user_name.setOnClickListener {

            val activity = contex as MainActivity
            activity.replaceFragment(
                OtherUserFragment(true, 0), OtherUserFragment(true, 0).javaClass.simpleName,
                true, true
            )
        }

        val layoutDirection = contex.resources.configuration.layoutDirection
        val viewLayoutDirection = holder.layoutPosition
        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {

            holder.itemView.tv_user_name.gravity = Gravity.END

        }


    }


    override fun getItemCount(): Int {
        return mlist.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FriendsData) {
            itemView.tv_user_name.text = item.name
            if (item.image == null) {
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            } else
                Glide.with(itemView.context).load(MEDIA_URL + item.image)
                    .into(itemView.profile_image)
        }

    }


}