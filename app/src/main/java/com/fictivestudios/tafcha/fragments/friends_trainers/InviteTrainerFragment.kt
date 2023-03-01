package com.fictivestudios.tafcha.fragments.friends_trainers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.InviteFriendsAdapter
import com.fictivestudios.tafcha.adapters.InviteTrainerAdapter
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.models.trainers.allUsers.AllUsersForTrainers
import com.fictivestudios.tafcha.models.trainers.allUsers.UsersTrainers
import com.fictivestudios.tafcha.models.trainers.alltrainers.Alltrainers
import com.fictivestudios.tafcha.models.trainers.alltrainers.AlltrainersItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_invite_users.view.*
import kotlinx.android.synthetic.main.invite_trainer.view.*
import kotlinx.coroutines.launch
import retrofit2.Response

class InviteTrainerFragment : BaseFragment()  {
    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.invite_trainers))

        titlebar.setBtnBack(
            object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )

        titlebar.setBtnRight(
            R.drawable.notify_icon,
            object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            NotificationsFragment(),
                            NotificationsFragment().javaClass.simpleName,
                            true, true
                        )
                }
            })
    }
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.invite_trainer, container, false)


            allTrainers()

        return mView
    }

    private fun initUsersRecyclerview(allTrainerList:ArrayList<AlltrainersItem>?)
    {
        mView.rv_invite_trainer.adapter = context?.let { InviteTrainerAdapter(it,allTrainerList as ArrayList<AlltrainersItem>) }
        mView.rv_invite_trainer.adapter?.notifyDataSetChanged()

    }

    private fun allTrainers(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireContext(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<Alltrainers>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<Alltrainers> {
                                return apiInterFace.allTrainerList()
                            }

                            override fun success(response: Response<Alltrainers>) {

                                if (response.body()?.data != null && response.body()?.status == 1) {
                                    initUsersRecyclerview(response.body()!!.data as ArrayList<AlltrainersItem>)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        response.message(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }


                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }

                        })

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }


    }
}