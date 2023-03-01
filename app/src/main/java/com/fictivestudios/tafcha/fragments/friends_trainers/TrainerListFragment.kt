package com.fictivestudios.tafcha.fragments.friends_trainers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.adapters.TrainersAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.InviteTrainerAdapter
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
import kotlinx.android.synthetic.main.fragment_trainer_list.view.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception



class TrainerListFragment : BaseFragment() {


    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.trainers))

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
                    replaceFragment(InviteTrainerFragment(),
                        InviteTrainerFragment().javaClass.simpleName,
                        true,true)
                }
            })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_trainer_list, container, false)
        myTrainersTrainers()
        return mView
    }
    private fun initTrainingRecyclerview(trainerList:ArrayList<MyTrainerItem>)
    {
        mView.rv_trainer_list.adapter = context?.let { TrainersAdapter(it,trainerList) }
        mView.rv_trainer_list.adapter?.notifyDataSetChanged()
    }



    private fun myTrainersTrainers(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireContext(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<MyTrainers>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<MyTrainers> {
                                return apiInterFace.myTrainerList()
                            }

                            override fun success(response: Response<MyTrainers>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    initTrainingRecyclerview(response.body()!!.data as ArrayList<MyTrainerItem>)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        response.body()?.message,
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