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
import com.fictivestudios.tafcha.adapters.TrainingAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment

import com.fictivestudios.tafcha.models.excercies.ExerCiesList
import com.fictivestudios.tafcha.models.excercies.ExerciesItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_training.view.*
import kotlinx.coroutines.launch
import retrofit2.Response




class TrainingFragment : BaseFragment() {



    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

        titlebar.setMainTitle(
            getString(R.string.training),

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            },
            R.drawable.btn_back_arrow,

            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            NotificationsFragment(),
                            NotificationsFragment().javaClass.simpleName,
                            true, true
                        )
                }
            },
            R.drawable.notify_icon
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_training, container, false)
        exerciesList()
        return mView
    }

    private fun initTrainingRecyclerview(trainingList:ArrayList<ExerciesItem>)
    {


        mView.rv_training.adapter = context?.let { TrainingAdapter(it,trainingList) }
        mView.rv_training.adapter?.notifyDataSetChanged()
    }

    private fun exerciesList(){

        lifecycleScope.launch {
            if(Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<ExerCiesList>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ExerCiesList> {

                                return apiInterFace.exerciesList()

                            }

                            override fun success(response: Response<ExerCiesList>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    initTrainingRecyclerview(response.body()!!.data as ArrayList<ExerciesItem>)

                                } else {

                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
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


}