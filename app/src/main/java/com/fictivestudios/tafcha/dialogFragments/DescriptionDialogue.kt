package com.fictivestudios.tafcha.dialogFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants.Companion.exdesp
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.interfaces.Exercies
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_dialogue_description.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DialogueDescriptionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogueDescriptionFragment( val exercies :  Exercies) : BaseFragment() {
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

    }
init {


}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment







        mView = inflater.inflate(R.layout.fragment_dialogue_description, container, false)

        mView.btn_done.setOnClickListener {

                exercies.getExercies(mView.et_description.text.toString())

            getActivityContext
                ?.onBackPressed()


        }


        return mView
    }

   companion object{}

}