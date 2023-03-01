    package com.fictivestudios.tafcha.dialogFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.activities.MainActivity
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_success_dialogue.view.*
import kotlinx.android.synthetic.main.titlebar.view.*

    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SuccessDialogueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SuccessDialogueFragment(var text:String, listener: View.OnClickListener?) : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View
    var dialogueText:String = text
    var listener = listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_success_dialogue, container, false)

        mView.btn_continue.setOnClickListener(listener)

          /*  if (getActivityContext!=null)
            {
                getActivityContext?.onBackPressed()
            }
            else if (getRegisterationActivity!=null)
            {
                getRegisterationActivity?.onBackPressed()
            }*/


        mView.tv_cong.text = dialogueText



        return mView
    }

    companion object {

    }
}