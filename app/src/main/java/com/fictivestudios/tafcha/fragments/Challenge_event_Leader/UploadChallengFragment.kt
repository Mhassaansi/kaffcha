package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.Constants.Companion.exdesp
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.Utils.URIPathHelper
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.dialogFragments.DialogueDescriptionFragment
import com.fictivestudios.tafcha.dialogFragments.SuccessDialogueFragment
import com.fictivestudios.tafcha.interfaces.Exercies
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.excercies.ExerciesItem
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getFormDataBody
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_setup_profile_trainer.view.video_view_triner
import kotlinx.android.synthetic.main.fragment_upload_challeng.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class UploadChallengFragment(var f_id:Int, var desp:String,var img:String,var email:String,var name:String) : BaseFragment(),Exercies {


    val SELECT_VIDEO = 1
    var vFile: MultipartBody.Part?=null
    var ex_description:String = ""

    private lateinit var mView: View
    override fun onResume() {
        super.onResume()



    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.challenge))

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


        mView = inflater.inflate(R.layout.fragment_upload_challeng, container, false)

        setData()



        mView.u_vid.setOnClickListener {
            openGalleryForVideo()
        }


mView.upload_exercies.setOnClickListener {
mView.upload_exercies.isClickable = false
    getActivityContext
        ?.AddFragment(
            DialogueDescriptionFragment( this),
            DialogueDescriptionFragment(this).javaClass.simpleName,
            true, true
        )


}
        mView.btn_challenge_friend.setOnClickListener {
              if(vFile==null){
                  Toast.makeText(context, "kindly select video", Toast.LENGTH_SHORT).show()
              }
            else{
                  uploadChallagne(f_id)
            }
        }
        return mView
    }


    private fun setData(){
        Glide.with(requireActivity()).load(MEDIA_URL+img).into(mView.profile_image)
        mView.tv_user_name.setText(name)
        mView.tv_email.setText(email)

        if(desp==""){
            mView.upload_exercies.visibility = View.VISIBLE
            mView.tv_exercise_details.setText(desp)

        }
        else{
            mView.upload_exercies.visibility = View.INVISIBLE
            mView.tv_exercise_details.setText(desp)
        }

    }

    private fun openGalleryForVideo() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "video/*"
        startActivityForResult(intent, SELECT_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri = data!!.data
            if (requestCode == SELECT_VIDEO) {

                playLocalVideo(uri!!)

                val file:File = File(activity?.let { getMediaFilePathFor(uri!!, it) })
                val requestBody: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
                vFile = MultipartBody.Part.createFormData("sender_video", "challenge_video", requestBody)

                // video_SIZE = checkVideosize(videoFile)
                Toast.makeText(context,"video has been selected", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
            }
            // Use Uri object instead of File to avoid storage permissions
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun playLocalVideo(uri:Uri){
        mView.video_view_triner1.setVideoURI(uri)
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView( mView.video_view_triner1)
        mediaController.setMediaPlayer( mView.video_view_triner1)
        mView.video_view_triner1.setMediaController(mediaController)
    }

    fun getMediaFilePathFor(
        uri: Uri,
        context: Context
    ): String {
        val returnCursor =
            context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream =
                context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()
            //int bufferSize = 1024;
            val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size %d", "" + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Size %s", file.path)
            Log.e("File Size %d", "" + file.length())
        } catch (e: java.lang.Exception) {
            Log.e("File Size %s", e.message!!)
        }
        return file.path
    }

    private fun uploadChallagne(f_id:Int?=0){

        lifecycleScope.launch(Dispatchers.IO) {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    val hashMap = HashMap<String, RequestBody>().apply {
                        this["challenge_description"] = mView.tv_exercise_details.text.toString().getFormDataBody()
                        this["friend_id"] = f_id.toString().getFormDataBody()
                    }

                    RetrofitSetup().callApi(requireActivity(), true, PreferenceUtils.getString("token"),
                        object : CallHandler<retrofit2.Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): retrofit2.Response<CommonResponseDC> {
                                return apiInterFace.createChallage(vFile, hashMap)
                            }

                            override fun success(response: retrofit2.Response<CommonResponseDC>) {

                                    if(response.body()?.status == 1  && response.body()?.message != "record not found..!"){

                                     //   Toast.makeText(requireActivity(),getString(R.string.thank_you_your_challenge_has_been_sent),Toast.LENGTH_LONG).show()
                                        //getActivityContext?.onBackPressed()
                                        getActivityContext()?.AddFragment(
                                            SuccessDialogueFragment(requireActivity().getString(R.string.thank_you_your_challenge_has_been_sent),
                                                object : View.OnClickListener {
                                                    override fun onClick(p0: View?) {
                                                        getActivityContext?.onBackPressed()

                                                    }}),
                                            SuccessDialogueFragment.javaClass.simpleName,
                                            false, false)

                                    }


                                    else {
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun error(message: String) {
                                RetrofitSetup().hideLoader()
                                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                            }

                        }
                    )


                } catch (e: Exception) {
                    e.printStackTrace()

                }

            }
        }
    }

    override fun getExercies(desp: String) {

        mView.tv_exercise_details.setText(desp)
        this.desp =desp
        mView.upload_exercies.isClickable = true

    }


}