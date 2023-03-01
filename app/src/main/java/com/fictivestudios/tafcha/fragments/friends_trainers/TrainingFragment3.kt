package com.fictivestudios.tafcha.fragments.friends_trainers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.dialogFragments.SuccessDialogueFragment
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_training3.view.*


class TrainingFragment3 : BaseFragment() {



    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.training))

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
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_training3, container, false)


        mView.btn_start.setOnClickListener {

         /*   getActivityContext?.
            AddFragment(
                SuccessDialogueFragment(getString( R.string.succesful_saved)),
                SuccessDialogueFragment.javaClass.simpleName,
                true,false)*/
        }
        return mView
    }


}