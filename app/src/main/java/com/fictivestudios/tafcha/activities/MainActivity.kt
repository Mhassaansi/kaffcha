package com.fictivestudios.tafcha.activities

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.fictivestudios.tafcha.Notification.FCMEnums
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants.Companion.IS_USER
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.adapters.TrainerMenuAdapter
import com.fictivestudios.tafcha.adapters.UserMenuAdapter
import com.fictivestudios.tafcha.fragments.friends_trainers.*
import com.fictivestudios.tafcha.fragments.live_other.HomeFragment
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.technado.demoapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dialogue_description.*


class MainActivity : BaseActivity()
{
   lateinit var extras:Bundle
    var nKey:String?=null
  //  val mBundle = Bundle()

    override fun onNewIntent(intent: Intent?) {
        extras= intent?.extras!!
        super.onNewIntent(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        if(intent.extras!=null) {
            extras = intent.extras!!
            if (extras.getString("type") != null) {
              nKey = extras.getString("type")!!
                Log.e("Text777777777777777", "" + nKey)
                PreferenceUtils.saveString("Noti",nKey!!)
            }

        }
            else
                nKey= PreferenceUtils.getString("Noti")


        if(nKey!="" && nKey!=null) {
        when(nKey){

                    FCMEnums.CHALLENGE_Recieved.value -> {

                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("ChallengesFragment"), HomeFragment("ChallengesFragment").javaClass.simpleName,true,true)

                    }
                    FCMEnums.CHALLENGE_Accept.value -> {

                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("FeedFragment"), HomeFragment("FeedFragment").javaClass.simpleName,true,true)
                    }
                    FCMEnums.CHALLENGE_WON.value -> {

                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("HistoryFragment"), HomeFragment("HistoryFragment").javaClass.simpleName,true,true)
                    }
                    FCMEnums.CHALLENGE_LOST.value -> {

                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("HistoryFragment"), HomeFragment("HistoryFragment").javaClass.simpleName,true,true)
                    }
                    FCMEnums.EVENT_CREATE.value -> {
                        setMainFrameLayoutID()
                        AddFragment(HomeFragment(""), HomeFragment("").javaClass.simpleName,true,true)
                    }
                    FCMEnums.EVENT_STRAT_TIME.value ->  {
                        setMainFrameLayoutID()
                        AddFragment(HomeFragment(""), HomeFragment("").javaClass.simpleName,true,true)
                    }
                    FCMEnums.LIVE_STREAM_STRATED.value ->  {

                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("subscriber"), HomeFragment("subscriber").javaClass.simpleName,true,true)
                    }
                    FCMEnums.REMINDER.value ->  {


                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("ReminderFragment"),
                            HomeFragment("ReminderFragment").javaClass.simpleName,true,true)


                    }
                    FCMEnums.FRIEND_REQUEST_Recieved.value -> {
                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("FriendsMainFragment1"),
                            HomeFragment("FriendsMainFragment1").javaClass.simpleName,true,true)
                    }
                    FCMEnums.FRIEND_REQUEST_ACCEPT.value -> {

                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("FriendsMainFragment"), HomeFragment("FriendsMainFragment").javaClass.simpleName,true,true)
                    }
                    FCMEnums.ADD_TRAINER.value ->{

                        setMainFrameLayoutID()
                        AddFragment(HomeFragment("UsersFragment"), HomeFragment("UsersFragment").javaClass.simpleName,true,true)
                    }

            FCMEnums.VOTE_RECIVED.value ->{

                setMainFrameLayoutID()
                AddFragment(HomeFragment("VOTE"), HomeFragment("VOTE").javaClass.simpleName,true,true)
            }

                    else -> {
                        setMainFrameLayoutID()
                        replaceFragment(HomeFragment(""), HomeFragment("").javaClass.simpleName,true,true)
                    }
                }
        }

        else{

            if(checkPermissions()){
                setMainFrameLayoutID()
                replaceFragment(HomeFragment(""), HomeFragment("").javaClass.simpleName,true,true)

            }
            else{
                requestPermissions()
                setMainFrameLayoutID()
                replaceFragment(HomeFragment(""), HomeFragment("").javaClass.simpleName,true,true)

            }

        }



    }


    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            }
            else{
                showSnackBar("Open the Setting", this)

            }
        }
    }
    private fun requestPermissions() {

        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA), 100)
    }

    fun showSnackBar(message: String?, activity: Activity?) {
        if (null != activity && null != message) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT
            ).setAction(message, View.OnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity.getPackageName(), null)
                intent.data = uri
                startActivity(intent)
            }).show()
        }
    }


//   private fun requestPermission(){
//
//       Dexter.withContext(this)
//           .withPermissions(
//               Manifest.permission.CAMERA,
//               Manifest.permission.READ_EXTERNAL_STORAGE,
//               Manifest.permission.RECORD_AUDIO
//           ).withListener(object : MultiplePermissionsListener {
//               override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//
//                   if (report.areAllPermissionsGranted()) {
//                       //Toast.makeText(this@MainActivity,"Permission Granted",Toast.LENGTH_LONG).show()
//                   }
//
//                   // check for permanent denial of any permission
//                   if (report.isAnyPermissionPermanentlyDenied()) {
//                       showSettingsDialog();
//                   }
//               }
//
//               override fun onPermissionRationaleShouldBeShown(
//                   permissions: List<PermissionRequest?>?,
//                   token: PermissionToken?
//               ) {
//
//                   token?.continuePermissionRequest();
//
//               }
//           }).check()
//   }


//    private fun showSettingsDialog() {
//        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@MainActivity)
//        builder.setTitle("Need Permissions")
//        builder.setCancelable(false)
//        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
//        builder.setPositiveButton("GOTO SETTINGS",
//            DialogInterface.OnClickListener { dialog, which ->
//                dialog.cancel()
//                openSettings()
//            })
////        builder.setNegativeButton("Cancel",
////            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
//        builder.show()
//    }


    // navigating user to app settings
//    private fun openSettings() {
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val uri: Uri = Uri.fromParts("package", packageName, null)
//        intent.data = uri
//        startActivityForResult(intent, 101)
//    }

    private fun setViewPager() {
        if(PreferenceUtils.getBoolean(IS_USER))
        {
            viewPagerMain.adapter = UserMenuAdapter(supportFragmentManager)
        }
        else
        {
            viewPagerMain.adapter = TrainerMenuAdapter(supportFragmentManager)
        }
    }

    override fun setMainFrameLayoutID() {

       mainFrameLayoutID = R.id.main_view_container
    }

    fun getTitlebar(): Titlebar {
        return main_title_bar
    }


    override fun onResume() {
        super.onResume()
        Log.d("onResume","onResume()")
    }


    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount==1){
            PreferenceUtils.saveString("Noti","")
                finish()
        }
        else
         super.onBackPressed()
    }


}