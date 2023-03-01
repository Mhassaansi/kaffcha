package com.fictivestudios.tafcha.adapters

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.feed.FeedItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import kotlinx.android.synthetic.main.fragment_challenge_received_details.view.tv_user_name
import kotlinx.android.synthetic.main.item_challeneges_feed.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class ChallengesFeedAdapter (context: Context, private val mlist:MutableList<FeedItem>?)
    :RecyclerView.Adapter<ChallengesFeedAdapter.ViewHolder>(){

    val contex = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challeneges_feed, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mlist!!.get(position))






        if(mlist.get(position).voter == null){


        }

       else if(mlist.get(position).voter.challenge_giver_id==null && mlist.get(position).voter.challenge_reciever_id!=null){
             holder.itemView.like_user_2.setImageResource(R.drawable.like_icon)
         }

         else if((mlist.get(position).voter.challenge_giver_id!=null && mlist.get(position).voter.challenge_reciever_id==null)){
             holder.itemView.like_user_1.setImageResource(R.drawable.like_icon)
         }
         else{


         }


        holder.itemView.like_user_1.setOnClickListener {
            holder.itemView.like_user_1.setImageResource(R.drawable.like_icon)
            holder.itemView.like_user_2.setImageResource(R.drawable.like_grey)
            vote(mlist.get(position).id!!.toInt(),"challenger")

        }

        holder.itemView.like_user_2.setOnClickListener {
            holder.itemView.like_user_2.setImageResource(R.drawable.like_icon)
            holder.itemView.like_user_1.setImageResource(R.drawable.like_grey)
            vote(mlist.get(position).id!!.toInt(),"accepter")
        }
    }

    override fun getItemCount(): Int { return mlist!!.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        fun bind(item:FeedItem){

            itemView.tv_user_name.text = item.sender.name
            itemView.tv_desc.text = item.challenge_description
            val date = item.created_at
            itemView.tv_date.text = date?.split("T")!!.toTypedArray()[0]
            playVideo1(MEDIA_URL+item.sender_video)
            playVideo2(MEDIA_URL+item.reciever_video)
            itemView.tv_name_1.text = item.sender.name
            itemView.tv_name_2.text = item.reciever.name
        }
        fun playVideo1(url:String){
            val uri: Uri = Uri.parse(url)
            itemView.video_view_triner1.setVideoURI(uri)
            val mediaController = MediaController(itemView.context)
            mediaController.setAnchorView( itemView.video_view_triner1)
            mediaController.setMediaPlayer( itemView.video_view_triner1)
            itemView.video_view_triner1.setMediaController(mediaController)
            //mView.video_view_triner.start()
        }
        fun playVideo2(url:String){
            val uri: Uri = Uri.parse(url)
            itemView.video_view_triner2.setVideoURI(uri)
            val mediaController = MediaController(itemView.context)
            mediaController.setAnchorView( itemView.video_view_triner2)
            mediaController.setMediaPlayer( itemView.video_view_triner2)
            itemView.video_view_triner2.setMediaController(mediaController)

        }
    }

    private fun vote(c_id:Int,vote:String){

        GlobalScope.launch {
            if (Constants.isNetworkConnected(contex, true)) {
                try {

                    RetrofitSetup().callApi(contex, true, PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.vote(c_id, vote)
                            }

                            override fun success(response: Response<CommonResponseDC>) {

                                if (response.body()?.status == 1) {

//                                    if(vote=="challenger"){
//
//                                    }
//                                    else{
//
//
//                                    }

                                    Toast.makeText(
                                        contex,
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        contex,
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(contex, "Error", Toast.LENGTH_LONG).show()
                            }


                        })

                } catch (e: Exception) {
                    e.printStackTrace()

                }

            }
        }

    }


}