package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.Utils.URIPathHelper
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getFormDataBody
import com.fictivestudios.tafcha.networkSetup.getJsonRequestBody
import com.fictivestudios.tafcha.networkSetup.getPartMap
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.JsonObject
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_challenge_received_details.view.*
import kotlinx.android.synthetic.main.fragment_challenge_received_details.view.profile_image
import kotlinx.android.synthetic.main.fragment_challenge_received_details.view.tv_email
import kotlinx.android.synthetic.main.fragment_challenge_received_details.view.tv_user_name
import kotlinx.android.synthetic.main.fragment_challenge_received_details.view.video_view_triner
import kotlinx.android.synthetic.main.fragment_new_events.view.*
import kotlinx.android.synthetic.main.fragment_setup_profile_trainer.view.*
import kotlinx.android.synthetic.main.fragment_setup_profile_trainer.view.u_vid
import kotlinx.android.synthetic.main.fragment_trainer_my_profile.view.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.HashMap


class ChallengeReceivedDetailsFragment(val desp:String,val video:String,val c_id:Int=0,
                                       val img:String,val name:String,val email:String) :
    BaseFragment() {
    private val REQUEST_CODE_VIDEO: Int =1
    var vFile: MultipartBody.Part? = null
    val SELECT_VIDEO = 1
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mView = inflater.inflate(R.layout.fragment_challenge_received_details, container, false)


        mView.u_vid.setOnClickListener {
            openGalleryForVideo()
        }
        mView.tv_email.setText(email)
        mView.tv_user_name.setText(name)
        mView.tv_exercise_details.setText(desp)
        playVideo(MEDIA_URL+video)

        Glide.with(requireActivity()).load(MEDIA_URL+img).into(mView.profile_image)


        mView.btn_done.setOnClickListener {
            updateChallenge()
        }

//        mView.btn_reject_friend.setOnClickListener {
//            reject()
//        }

        return mView
    }

    private fun openGalleryForVideo() {
//        val intent = Intent()
//        intent.type = "video/*"
//        intent.action = Intent.ACTION_PICK
//        startActivityForResult(Intent.createChooser(intent, "Select Video"),REQUEST_CODE_VIDEO)

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

                val file: File = activity?.let { getMediaFilePathFor(uri!!, it) }?.let { File(it) }!!
                val requestBody: RequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
                vFile = MultipartBody.Part.createFormData("reciever_video", "reciever_video", requestBody)

            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playVideo(url:String){
        val uri: Uri = Uri.parse(url)

        mView.video_view_triner.setVideoURI(uri)
        val mediaController = MediaController(this.activity)
        mediaController.setAnchorView(mView.video_view_triner)
        mediaController.setMediaPlayer(mView.video_view_triner)
        mView.video_view_triner.setMediaController(mediaController)
        mView.video_view_triner.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                Log.d("video", "setOnErrorListener ")
                return true
            }
        })

    }


    fun getMediaFilePathFor(
        uri: Uri,
        context: Context
    ): String {
        val returnCursor =
            context.contentResolver.query(uri, null, null, null, null)
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
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

    private fun updateChallenge(){

        lifecycleScope.launch {
            val hashMap = HashMap<String, RequestBody>().apply {
                this["challenge_id"] = c_id.toString().getFormDataBody()
                this["type"] = "accept".getFormDataBody()
            }
            if(vFile==null){
                Toast.makeText(requireActivity(),"Please Select The Video",Toast.LENGTH_LONG).show()

            }
            else {
                if (Constants.isNetworkConnected(requireActivity(), true)) {

                    try {
                        RetrofitSetup().callApi(requireActivity(),
                            true,
                            PreferenceUtils.getString("token"),
                            object : CallHandler<Response<CommonResponseDC>> {
                                override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                    return apiInterFace.acceptChallenge(vFile, hashMap)
                                }

                                override fun success(response: Response<CommonResponseDC>) {
                                    if (response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                        Toast.makeText(
                                            requireActivity(),
                                            response.body()!!.message,
                                            Toast.LENGTH_LONG
                                        ).show()

                                        requireActivity().onBackPressed()

                                    } else {
                                        Toast.makeText(
                                            requireActivity(),
                                            response.body()!!.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                                override fun error(message: String) {
                                    RetrofitSetup().hideLoader()
                                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG)
                                        .show()
                                }


                            })

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }
            }
        }

    }





    fun playLocalVideo(uri:Uri){
        mView.video_view_triner1.setVideoURI(uri)
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView( mView.video_view_triner1)
        mediaController.setMediaPlayer( mView.video_view_triner1)
        mView.video_view_triner1.setMediaController(mediaController)


        mView.video_view_triner1.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                Log.d("video", "setOnErrorListener ")
                return true
            }
        })
    }

}