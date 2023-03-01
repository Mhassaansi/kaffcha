package com.fictivestudios.tafcha.fragments.friends_trainers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.FriendsAdapter
import com.fictivestudios.tafcha.adapters.ViewTrianerMyUsersAdapter
import com.fictivestudios.tafcha.models.freinds.friendlist.FriendsData
import com.fictivestudios.tafcha.models.trainers.viewmyusers.MyUserItem
import com.fictivestudios.tafcha.models.trainers.viewmyusers.ViewMyUsers
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_users.view.*
import kotlinx.coroutines.launch
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UsersFragment : BaseFragment() {

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
        titlebar.setTitle(getActivityContext!!, getString(R.string.users))

        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }
        )

        titlebar.setBtnRight(R.drawable.invite_icon,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext?.
                    replaceFragment(InviteUsersFragment(false),
                        InviteUsersFragment(false).javaClass.simpleName,
                        true,false)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_users, container, false)

        getTranersUsers()
       // initUsersRecyclerview()

        return mView
    }


    private fun initUsersRecyclerview(viewMyUser:ArrayList<MyUserItem>?=null)
    {
        mView.rv_users.adapter = context?.let { ViewTrianerMyUsersAdapter(it,viewMyUser!! ) }
        mView.rv_users.adapter?.notifyDataSetChanged()

    }




    private fun getTranersUsers(){

        lifecycleScope.launch {

            try {

                RetrofitSetup().callApi(requireActivity(),true,PreferenceUtils.getString("token"),
                object :CallHandler<Response<ViewMyUsers>>{
                    override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ViewMyUsers> {

                        return apiInterFace.getViewUsers()

                    }

                    override fun success(response: Response<ViewMyUsers>) {

                        if(response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!"){


                            initUsersRecyclerview(response.body()!!.data as ArrayList<MyUserItem>)

                        }

                        else{

                            Toast.makeText(requireActivity(),response.body()?.message, Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun error(message: String) {
                        RetrofitSetup().hideLoader()
                        Toast.makeText(requireActivity(),"Error", Toast.LENGTH_LONG).show()
                    }

                })

            }
            catch (e:Exception){
                e.printStackTrace()
            }

        }

    }

    companion object {}
}