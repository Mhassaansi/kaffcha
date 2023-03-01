package com.fictivestudios.tafcha.fragments.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.Constants.Companion.IS_USER
import com.fictivestudios.tafcha.Utils.PreferenceData
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.adapters.LanguageAdapter
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.activities.RegisterationActivity
import com.fictivestudios.tafcha.dialogFragments.DeleteAccountDialouge
import com.fictivestudios.tafcha.dialogFragments.RejectDialog
import com.fictivestudios.tafcha.interfaces.ItemClickListner
import com.fictivestudios.tafcha.models.LanguageModel
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.content.Content

import com.fictivestudios.tafcha.models.logout.LogoutResponse
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_reminder.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class SettingsFragment : BaseFragment(),ItemClickListner{

    private val languageList = ArrayList<LanguageModel>()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.settings))

        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }
        )




        titlebar.setBtnRightUser(R.drawable.user_dp,
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    if (PreferenceUtils.getBoolean(IS_USER))
                    {
                        getActivityContext
                            ?.replaceFragment(
                                ProfileUserFragment(),
                                ProfileUserFragment().javaClass.simpleName,
                                true, true
                            )
                    }
                    else
                    {
                        getActivityContext
                        ?.replaceFragment(
                            TrainerMyProfileFragment(),
                            TrainerMyProfileFragment().javaClass.simpleName,
                            true, true
                        )

                    }

                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_settings, container, false)

        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), mGoogleSignInOptions)

        if(PreferenceUtils.getString("nStatus")=="on"){

            mView.nStatus.isChecked = true

        }
        else if(PreferenceUtils.getString("nStatus")=="off"){
            mView.nStatus.isChecked = false

        }
        else{
            mView.nStatus.isChecked = true

        }

        mView.btn_password_delete.setOnClickListener {

            getActivityContext?.AddFragment(
                DeleteAccountDialouge(
                    object : View.OnClickListener {
                        override fun onClick(p0: View?) {
                            deleteAccount()
                            getActivityContext?.
                            onBackPressed()
                        }
                    },
                    object : View.OnClickListener {
                        override fun onClick(p0: View?) {
                            getActivityContext?.
                            onBackPressed()
                        }
                    }

                ),
                DeleteAccountDialouge(View.OnClickListener {  },View.OnClickListener {  }).javaClass.simpleName,
                true, false)

        }



        mView.btn_password_reset.setOnClickListener {

            getActivityContext?.
            replaceFragment(
                UpdatePasswordFragment(),
                UpdatePasswordFragment().javaClass.simpleName,
                true,false)

        }

        aboutUs("about_us")
        mView.nStatus.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked){
                notifySatus("on")
            }
            else{
                notifySatus("off")
            }

        }

        mView.btn_logout.setOnClickListener {

            if(isSignedIn()){
                signOut()
            }
            else{
                logout()
            }

        }


        languageList.addAll(0,
            listOf(
                LanguageModel(R.drawable.us_flag_icon,"English"),
                LanguageModel(R.drawable.saudia,"Arabic"),
                LanguageModel(R.drawable.france,"French"),
                LanguageModel(R.drawable.russia,"Russian"),
                LanguageModel(R.drawable.germany,"Germane"),
                LanguageModel(R.drawable.china,"Chinese"),

            )

        )

        initLanguageSpinner(languageList)




        return mView
    }
    fun signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(requireActivity(),"Logout Successfully...",Toast.LENGTH_LONG).show()
            PreferenceData.clearPreference(requireContext())
            startActivity(Intent(context, RegisterationActivity::class.java))
            getActivityContext?.finish()
        }
    }
    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(requireActivity()) != null
    }

    private fun initLanguageSpinner(languageList:ArrayList<LanguageModel>)
    {
        val langaugeAadpetr = LanguageAdapter(languageList, this)
        mView.sp_language.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        mView.sp_language.adapter = langaugeAadpetr
    }

    private  fun logout(){

       lifecycleScope.launch {
           if (Constants.isNetworkConnected(requireActivity(), true)) {
               try {

                   RetrofitSetup().callApi(requireActivity(),
                       true,
                       PreferenceUtils.getString("token"),
                       object : CallHandler<Response<LogoutResponse>> {
                           override suspend fun sendRequest(apiInterFace: ApiInterface): Response<LogoutResponse> {
                               return apiInterFace.logout()
                           }

                           override fun success(response: Response<LogoutResponse>) {
                               if (response.body()?.status == 1 ) {
                                   PreferenceData.clearPreference(requireActivity())
                                   PreferenceUtils.saveBoolean(Constants.IS_LOGIN,false)
                                   PreferenceUtils.saveString("token",null.toString())
                                   PreferenceUtils.saveString("nStatus","")
                                   Toast.makeText(requireActivity(), response.body()?.message, Toast.LENGTH_LONG).show()
                                   startActivity(Intent(context, RegisterationActivity::class.java))
                                   mGoogleSignInClient.signOut()
                                   getActivityContext?.finish()

                               } else {
                                   Toast.makeText(
                                       requireActivity(), response.body()!!.message,
                                       Toast.LENGTH_LONG
                                   ).show()
                               }
                           }

                           override fun error(message: String) {
                               RetrofitSetup().hideLoader()
                               Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                           }

                       }
                   )


               } catch (e: Exception) {
                   e.printStackTrace()
               }
           }
       }

   }

    private  fun notifySatus(status:String?="off"){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.notyStatus(status!!)
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if (response.body()!!.status==1) {

                                    PreferenceUtils.saveString("nStatus",status!!)

                                    Toast.makeText(
                                        requireActivity(), response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()

                                } else {
                                    Toast.makeText(
                                        requireActivity(), response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }

                        }
                    )


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    override fun getItemPosition(pos: Int) {

      if(pos==0){

          Toast.makeText(requireActivity(),"You set English langauge",Toast.LENGTH_LONG).show()
          PreferenceUtils.saveString("language","en")
          setLocale(requireActivity(),"en")
      }
        else if (pos==1){
          Toast.makeText(requireActivity(),"You set Arabic langauge",Toast.LENGTH_LONG).show()
          PreferenceUtils.saveString("language","ar")
          setLocale(requireActivity(),"ar")
        }
      else if (pos==2){
          Toast.makeText(requireActivity(),"You set French langauge",Toast.LENGTH_LONG).show()
          PreferenceUtils.saveString("language","fr")
          setLocale(requireActivity(),"fr")
      }
      else if (pos==3){
          Toast.makeText(requireActivity(),"You set Russian langauge",Toast.LENGTH_LONG).show()
          PreferenceUtils.saveString("language","ru")
          setLocale(requireActivity(),"ru")
      }
      else if (pos==4){
          Toast.makeText(requireActivity(),"You set German langauge",Toast.LENGTH_LONG).show()
          PreferenceUtils.saveString("language","de")
          setLocale(requireActivity(),"de")
      }
      else if (pos==5){
          Toast.makeText(requireActivity(),"You set Chinese langauge",Toast.LENGTH_LONG).show()
          PreferenceUtils.saveString("language","zh")
          setLocale(requireActivity(),"zh")
      }
        else{

        }

    }

    fun setLocale(activity: Activity, languageCode: String?) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val resources = activity.resources
            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
            val refresh = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().finish()
            startActivity(refresh)
        }

     private fun aboutUs(aUs:String){

         lifecycleScope.launch {
             if (Constants.isNetworkConnected(requireActivity(), true)) {
                 try {

                     RetrofitSetup().callApi(requireActivity(),
                         true,
                         PreferenceUtils.getString("token"),
                         object : CallHandler<Response<Content>> {
                             override suspend fun sendRequest(apiInterFace: ApiInterface): Response<Content> {
                                 return apiInterFace.getContent(aUs)
                             }

                             override fun success(response: Response<Content>) {
                                 if (response.body()!!.status == 1) {
                                     if(response.body()?.data == null){
                                         mView.iv_about_text.setText(" ")
                                     }
                                     else{
                                         mView.iv_about_text.setText(response.body()!!.data.content)
                                     }

                                 } else {
                                     Toast.makeText(
                                         requireActivity(), "Error",
                                         Toast.LENGTH_LONG
                                     ).show()
                                 }
                             }

                             override fun error(message: String) {
                                 RetrofitSetup().hideLoader()
                                 Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                             }

                         }
                     )


                 } catch (e: Exception) {
                     e.printStackTrace()
                 }
             }
         }

     }


    private  fun deleteAccount(){

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.delaccount()
                            }

                            override fun success(response: Response<CommonResponseDC>) {
                                if (response.body()?.status == 1 ) {
                                    PreferenceData.clearPreference(requireActivity())
                                    PreferenceUtils.saveBoolean(Constants.IS_LOGIN,false)
                                    PreferenceUtils.saveString("token",null.toString())
                                    PreferenceUtils.saveString("nStatus","")
                                    Toast.makeText(
                                        requireActivity(), response.body()?.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(Intent(context, RegisterationActivity::class.java))
                                    getActivityContext?.finish()

                                } else {
                                    Toast.makeText(
                                        requireActivity(), response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }

                        }
                    )


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }



}