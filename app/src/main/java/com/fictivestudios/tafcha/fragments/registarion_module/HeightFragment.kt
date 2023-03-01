package com.fictivestudios.tafcha.fragments.registarion_module

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.interfaces.FragmentBListener
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_height.view.*


class HeightFragment() : BaseFragment() {




    private lateinit var mView: View
    private var listener: FragmentBListener? = null
   // var mCallback: MyInterface? = null

   // private var fragmentCallback:FragmentCallBack? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_height, container, false)


        //fragmentCallback = requireActivity() as FragmentCallBack

        mView.btn_continue.setOnClickListener {
         Constants.weight =  mView.seekbar_weight.progress
            Constants.height =  mView.seekbar_height.progress

           // listener?.onInputBSent(Constants.weight);
//Toast.makeText(requireContext(),""+Constants.weight + Constants.height,Toast.LENGTH_LONG ).show()
//            PreferenceUtils.saveInt("weight",weight)
//            PreferenceUtils.saveInt("height",height)
            requireActivity().
            onBackPressed()
        }



        return mView


    }

//    fun setFragmentCallback(callback: FragmentCallBack) {
//        this.fragmentCallback = callback
//    }
//    override fun onDestroy() {
//        fragmentCallback?.onDataSent(mView.seekbar_height.progress,mView.seekbar_weight.progress)
//        super.onDestroy()
//    }
//override fun onAttach(context: Context) {
//    super.onAttach(context)
//    listener = if (context is FragmentBListener) {
//        context
//    } else {
//        throw RuntimeException(
//            context.toString()
//                    + " must implement FragmentBListener"
//        )
//    }
//}
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }


}