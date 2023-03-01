package com.fictivestudios.tafcha.fragments.registarion_module

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.*
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.dialogFragments.*
import com.fictivestudios.tafcha.models.Login.LoginResultpojo
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.GoogleAuthProvider
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_pre_login.view.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.*


class PreLoginFragment : BaseFragment() {

    private lateinit var mView: View

    private var callbackManager: CallbackManager? = null
    private lateinit var preLoginBinding: View

    private val RC_SIGN_IN = 234
    lateinit var mGoogleSignInClient:GoogleSignInClient
    private lateinit var auth: FirebaseAuth

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

       mView = inflater.inflate(R.layout.fragment_pre_login, container, false)


        callbackManager = CallbackManager.Factory.create()

        initFaceBook()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("224259103846-uh0acpfc061ckgpvhij405uj1ck45t5t.apps.googleusercontent.com")
            .requestEmail()
            .build()
        auth = FirebaseAuth.getInstance()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        mView.linearLayoutCompatEmail.setOnClickListener {

            getRegisterationActivity?.
            AddFragment(
                DialogueAgreement(),
                DialogueAgreement().javaClass.simpleName,
                true,false)


        }
        mView.linearLayoutCompatGoogle.setOnClickListener {
            googleSignIn()

//            if(isSignedIn()){
//                startActivity(Intent(context, MainActivity::class.java))
//                requireActivity().finish()
//            }
//            else{
//                getRegisterationActivity()?.
//                replaceFragment(SelectUserFragment(true,false),
//                    SelectUserFragment(true,false).javaClass.simpleName,
//                    true,true)
//            }

//            getRegisterationActivity?.AddFragment(
//                RejectDialog(getString(R.string.service),
//                    object : View.OnClickListener {
//                        override fun onClick(p0: View?) {
//                            getRegisterationActivity?.
//                            onBackPressed()
//                        }
//                    }
//
//                ),
//                RejectDialog.javaClass.simpleName,
//                true, false)

        }
        mView.linearLayoutCompatFb.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("email","public_profile")
            )

//            getRegisterationActivity()?.
//            replaceFragment(SelectUserFragment(false,true),
//                SelectUserFragment(false,true).javaClass.simpleName,
//                true,true)
//            getRegisterationActivity?.AddFragment(
//                RejectDialog(getString(R.string.service),
//                    object : View.OnClickListener {
//                        override fun onClick(p0: View?) {
//                            getRegisterationActivity?.
//                            onBackPressed()
//                        }
//                    }
//
//                ),
//                RejectDialog.javaClass.simpleName,
//                true, false)
        }
        mView.btn_back.setOnClickListener {
            getRegisterationActivity?.
            onBackPressed()
        }

        return mView
    }



//    private fun isSignedIn(): Boolean {
//        return GoogleSignIn.getLastSignedInAccount(requireActivity()) != null
//    }





    private fun googleSignIn() {
        //getting the google signin intent
        val signInIntent = mGoogleSignInClient!!.signInIntent
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode === RC_SIGN_IN) {
            // similar condition for facebook
            //Getting the GoogleSignIn Task
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                //authenticating with firebase
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
        else{
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }

    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        AuthWithGoogle(acct)
    }
    private fun AuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    val mUserG = FirebaseAuth.getInstance().currentUser
                    mUserG!!.getIdToken(true)
                        .addOnCompleteListener(object : OnCompleteListener<GetTokenResult?> {
                            override fun onComplete(task: Task<GetTokenResult?>) {
                                val idToken: String = task.getResult()!!.getToken().toString()
                                Log.e("gdg", "Token:"+idToken)
                                Log.e("gdg", "Token:"+acct.idToken)

                                val uid : String = acct.id.toString()

                                    socialLogin(uid,"google",mUserG.displayName!!,mUserG.email!!,mUserG.photoUrl!!.toString(),"")


                            }
                        })
                } else {
                    Toast.makeText(requireContext(), "" + task.exception!!.message, Toast.LENGTH_SHORT).show()
                    Log.e("exception: ",""+ task.exception!!.localizedMessage)

                }
            }
    }


    private fun initFaceBook(){


        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                //getFacbookParam(loginResult.accessToken)
                handleFacebookAccessToken(loginResult.accessToken)
            }
            override fun onCancel() {
                Toast.makeText(requireContext(), "Please Try again", Toast.LENGTH_SHORT).show()
            }
            override fun onError(error: FacebookException) {
                Log.e("gdg", error.localizedMessage)
                LoginManager.getInstance().logOut()
                Toast.makeText(requireContext(), "" +error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun getFacbookParam(accesToken:AccessToken)
    {
        Log.d("accessToken",accesToken.token.toString())

        var request = GraphRequest.newMeRequest(
            accesToken,
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(
                    `object`: JSONObject?,
                    response: GraphResponse?
                ) {
                    // Application code

                    var email = `object`?.getString("email")
                    // var email = `object`!!.getString("email")
                    socialLogin(accesToken.userId,"facebook","",email.toString(),"","")
                }
            })
        val parameters = Bundle()
        parameters.putString("fields", "email")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.e("gdg", "handleFacebookAccessToken:${token.token}")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val mUser = FirebaseAuth.getInstance().currentUser
                    mUser!!.getIdToken(true)
                        .addOnCompleteListener(object : OnCompleteListener<GetTokenResult?> {
                            override fun onComplete(task: Task<GetTokenResult?>) {
                                val idToken: String = task.getResult()!!.getToken().toString()
                                Log.e("gdg", "Token:"+idToken)
                                socialLogin(idToken,"facebook","",mUser.email!!,"","")
                            }
                        })
                } else {

                    Toast.makeText(requireContext(), "" +task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun socialLogin(idToken:String,type:String,displayName:String,email:String,image:String,phNo:String) = getFirebaseToken { fcmToken ->

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(getRegisterationActivity!!, true, "",
                        object : CallHandler<Response<LoginResultpojo>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<LoginResultpojo> {

                                return apiInterFace.socialLogin(
                                    JSONObject().apply {
                                        put("access_token",idToken)
                                        put("provider",type)
                                        put("device_type", "android")
                                        put("device_token", fcmToken)
                                        put("displayName",displayName)
                                        put("email",email)
                                        put("photoURL",image)
                                        put("phoneNumber",phNo)
                                    }.toString().getJsonRequestBody()
                                )
                            }

                            override fun success(response: Response<LoginResultpojo>) {
                                if(response.body()?.status==1  && response.body()?.message != "record not found..!"){

                                   if(response.body()?.data?.is_profile_complete == 1){
                                        PreferenceUtils.saveString("token", response.body()?.bearer_token.toString())
                                        PreferenceData.storeLoginData(requireActivity(), response.body()?.data)
                                        startActivity(Intent(context, MainActivity::class.java))
                                        requireActivity().finish()
                                    }
                                    else{
                                        if(type=="google"){
                                            PreferenceUtils.saveString("token", response.body()?.bearer_token.toString())
                                            PreferenceData.storeLoginData(requireActivity(), response.body()?.data)
                                            getRegisterationActivity()?.
                                            replaceFragment(SelectUserFragment(true,false),
                                                SelectUserFragment(true,false).javaClass.simpleName,true,true)

                                        }
                                        else{
                                            PreferenceUtils.saveString("token", response.body()?.bearer_token.toString())
                                            PreferenceData.storeLoginData(requireActivity(), response.body()?.data)
                                            getRegisterationActivity()?.
                                            replaceFragment(SelectUserFragment(false,true),
                                                SelectUserFragment(true,false).javaClass.simpleName,true,true)


                                        }


                                    }



                                }
                                else{
                                    Toast.makeText(requireActivity(),response.body()?.message,
                                        Toast.LENGTH_LONG).show()
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
            else{
                Toast.makeText(requireActivity(), R.string.internetconnection, Toast.LENGTH_LONG).show()
            }
        }
    }



}