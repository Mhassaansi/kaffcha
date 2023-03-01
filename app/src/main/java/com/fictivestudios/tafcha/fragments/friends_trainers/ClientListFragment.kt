package com.fictivestudios.tafcha.fragments.friends_trainers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.FriendsAdapter
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_client_list.view.*
import kotlinx.android.synthetic.main.fragment_users.view.*
import kotlinx.android.synthetic.main.fragment_users.view.rv_users


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ClientListFragment : BaseFragment() {

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
        titlebar.setTitle(getActivityContext!!, getString(R.string.client_list))

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

        mView = inflater.inflate(R.layout.fragment_client_list, container, false)

      //  initUsersRecyclerview()

        return mView
    }


//    private fun initUsersRecyclerview()
//    {
//
//        val friendList=ArrayList<String>()
//        friendList.add("Justin Wade")
//        friendList.add("Justin Wade")
//        friendList.add("Justin Wade")
//
//        mView.rv_clients_list.adapter = context?.let { FriendsAdapter(it,friendList) }
//        mView.rv_clients_list.adapter?.notifyDataSetChanged()
//
//    }

    companion object {}
}