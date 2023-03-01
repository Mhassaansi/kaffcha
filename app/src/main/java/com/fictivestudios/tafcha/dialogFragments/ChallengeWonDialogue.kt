package com.fictivestudios.tafcha.dialogFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_challenge_won.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChallengeWonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChallengeWonFragment(isWon: Boolean) : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var isWon = isWon

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       mView = inflater.inflate(R.layout.fragment_challenge_won, container, false)


        if (isWon)
        {
            mView.iv_checked.setImageResource(R.drawable.checked_icon)
            mView.tv_challenge.text = getString(R.string.congratulations)
            mView.tv_challenge.setTextColor(resources.getColor(R.color.color_green))
            mView.tv_cong.text= "Challenge by John Smith won!!! Completion time 1:31 min"
            mView.tv_reward_point.visibility = View.VISIBLE
        }
        else
        {
            mView.iv_checked.setImageResource(R.drawable.lost_icon)
            mView.tv_challenge.text = getString(R.string.challenge_lost)
            mView.tv_challenge.setTextColor(resources.getColor(R.color.color_red))
            mView.tv_cong.text = "Challenge by John Smith lost!!! Your completion time 1:31 sec, Winner Full Name best Time 1:01 min"
            mView.tv_reward_point.visibility = View.GONE
        }


        mView.btn_continue.setOnClickListener {

                getActivityContext?.
                onBackPressed()

        }

        return mView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChallengeWonFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChallengeWonFragment(true).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}