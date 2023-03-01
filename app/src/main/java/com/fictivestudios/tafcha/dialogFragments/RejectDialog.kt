package com.fictivestudios.tafcha.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_success_dialogue.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class RejectDialog (var text:String, listener: View.OnClickListener?) : BaseFragment() {
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

        mView = inflater.inflate(R.layout.fragment_reject_dialog, container, false)

        mView.btn_continue.setOnClickListener(listener)



        mView.tv_cong.text = dialogueText



        return mView
    }
    companion object {

    }

}