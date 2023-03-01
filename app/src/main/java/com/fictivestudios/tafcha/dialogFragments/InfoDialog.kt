package com.technado.demoapp.dialogFragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fictivestudios.tafcha.R

class InfoDialog(var infoText: String) : DialogFragment() {
    var mView: View? = null

/*    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView// = inflater.inflate(R.layout.fragment_info_dialog, container, false)

        getDialog()?.getWindow()?.getAttributes()?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mView?.tvInfo?.text = infoText
        mView?.cvClose?.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })

        return mView
    }*/
}