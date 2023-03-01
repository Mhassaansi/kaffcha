package com.fictivestudios.tafcha.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.fragments.profile.TrainerProfileFragment

import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.trainers.alltrainers.AlltrainersItem
import com.fictivestudios.tafcha.models.trainers.mytrainers.MyTrainerItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.*
import kotlinx.android.synthetic.main.item_pending.view.*
import kotlinx.android.synthetic.main.item_trainer.view.*
import kotlinx.android.synthetic.main.item_trainer.view.profile_image
import kotlinx.android.synthetic.main.item_trainer.view.tv_user_name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class TrainersAdapter (context: Context, private val mlist:ArrayList<MyTrainerItem>) :
   RecyclerView.Adapter<TrainersAdapter.ViewHolder>(){


    val contex = context
    var activity = contex as MainActivity





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trainer, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.btn_send_request.visibility = View.INVISIBLE

        holder.itemView.tv_user_name.setOnClickListener {
            activity.
            replaceFragment(
                TrainerProfileFragment(true,mlist.get(position).id!!.toInt())
                , TrainerProfileFragment(true,mlist.get(position).id!!.toInt()).javaClass.simpleName,
                true,true)
        }

           holder.bind(mlist.get(position))


        holder.itemView.profile_image.setOnClickListener {

            val activity = contex as MainActivity
            activity.
            replaceFragment(
                TrainerProfileFragment(true,mlist.get(position).id!!.toInt())
                , TrainerProfileFragment(true,mlist.get(position).id!!.toInt()).javaClass.simpleName,
                true,true)
        }




    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int { return mlist.size }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

        fun bind(item:MyTrainerItem){

          itemView.tv_user_name.text = item.name
            if(item.image==null){
                itemView.profile_image.setImageResource(R.drawable.user_blank_profile)
            }
            else
            Glide.with(itemView.context).load(MEDIA_URL+item.image).into(itemView.profile_image)




        }

    }



}