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
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.fictivestudios.tafcha.models.trainers.allUsers.AllUsersForTrainers
import com.fictivestudios.tafcha.models.trainers.allUsers.UsersTrainers
import com.fictivestudios.tafcha.models.trainers.alltrainers.Alltrainers
import com.fictivestudios.tafcha.models.trainers.alltrainers.AlltrainersItem
import com.fictivestudios.tafcha.models.trainers.mytrainers.MyTrainerItem
import com.fictivestudios.tafcha.models.trainers.mytrainers.MyTrainers
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_invite_users.view.*
import kotlinx.coroutines.launch
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class InviteUsersFragment(var tariner:Boolean) : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {

        titlebar.setTitle(getActivityContext!!, getString(R.string.invite_users))

        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )

        titlebar.setBtnRight(R.drawable.notify_icon,
            object:View.OnClickListener{
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_invite_users, container, false)


            allUsers()

        return mView
    }

    private fun initUsersRecyclerview(friendList:ArrayList<UsersTrainers>?)
    {
        mView.rv_invite_friend.adapter = context?.let { InviteFriendsAdapter(it,friendList as ArrayList<UsersTrainers>) }
        mView.rv_invite_friend.adapter?.notifyDataSetChanged()

    }
        private fun allUsers(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<AllUsersForTrainers>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<AllUsersForTrainers> {
                                return apiInterFace.getViewAllUsers()
                            }

                            override fun success(response: Response<AllUsersForTrainers>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    initUsersRecyclerview(response.body()!!.data as ArrayList<UsersTrainers>)

                                } else {

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


}