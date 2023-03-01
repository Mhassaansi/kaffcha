package com.fictivestudios.tafcha.fragments.friends_trainers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.freinds.friendprofile.FriendProfile
import com.fictivestudios.tafcha.models.profile.trainer_user.TrianerUser
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_other_user.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception



class OtherUserFragment(var f_or_not:Boolean,val f_id:Int?=0) : BaseFragment() {

    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.profile))
if(f_or_not != true){
    titlebar.setBtnRight(R.drawable.send_request_white_icon,
        object:View.OnClickListener{
            override fun onClick(view: View?) {
                addFriend(f_id)
                (view as ImageView).setImageResource(R.drawable.request_sent_icon)

            }
        })
}

else{
        titlebar.setBtnRight(R.drawable.request_sent_icon,
        object :View.OnClickListener{
            override fun onClick(p0: View?) {

            }

        })

  //  (view as ImageView).setImageResource(R.drawable.request_sent_icon)


}



        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_other_user, container, false)
        firendsProfile(f_id!!)
        return mView
    }

    private fun firendsProfile(f_id:Int?=0){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<TrianerUser>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<TrianerUser> {

                                return apiInterFace.getTrainerUserData(f_id!!)

                            }

                            override fun success(response: Response<TrianerUser>) {
                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    mView.tv_user_name.setText(response.body()!!.data?.user?.name)
                                    mView.tv_email.setText(response.body()!!.data?.user?.email.toString())
                                    mView.tv_age.setText(response.body()!!.data?.user?.age + " Years")

                                    if(response.body()?.data?.user?.image != null){
                                        Glide.with(requireActivity()).load(
                                            MEDIA_URL+response.body()!!
                                                .data?.user?.image
                                        ).into(mView.profile_image)
                                    }
                                    else{

                                        mView.profile_image.setImageResource(R.drawable.user_blank_profile)

                                    }


                                    if(response.body()!!.data?.win.toString() != "null"){
                                        mView.tv_won_count.setText(response.body()!!.data?.win.toString())
                                    }
                                        else{
                                        mView.tv_won_count.setText("0")
                                        }
                                        if(response.body()!!.data?.lose.toString() != "null"){
                                            mView.tv_last_count.setText(response.body()!!.data?.lose.toString())
                                        }
                                    else{
                                            mView.tv_last_count.setText("0")
                                    }
                                            if(response.body()!!.data?.die.toString() != "null"){
                                                mView.tv_draw_count.setText(response.body()!!.data?.die.toString())
                                            }
                                    else{
                                                mView.tv_draw_count.setText("0")
                                    }



                                } else {
                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }


                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }

                        })

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun addFriend(f_id:Int?=0) {
        GlobalScope.launch(Dispatchers.IO) {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.addFriend(f_id!!)
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if ( response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    //  Toast.makeText(activity, PreferenceUtils.getString("token"), Toast.LENGTH_SHORT).show()
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //  addTrainerResponse=response.body().toString()
                                } else {
                                    Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun error(message: String) {

                            }

                        })


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}