package com.fictivestudios.tafcha.fragments.registarion_module

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.*
import com.fictivestudios.tafcha.Utils.Constants.Companion.isNetworkConnected
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.models.Login.LoginResultpojo
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getFormDataBody
import com.fictivestudios.tafcha.networkSetup.getPartMap
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.github.dhaval2404.imagepicker.ImagePicker
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_setup_profile_trainer.view.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*


class SetupProfileTrainerFragment(isGoogle:Boolean,isFaceBook:Boolean,val role:String) : BaseFragment() {

    private val loginData by lazy { PreferenceData.retrieveLoginData(requireActivity()) }


    val SELECT_VIDEO = 1
    val SELECT_IMAGE = 2
    val WandH = 3
    var image: ObservableField<String> = ObservableField("")
    var video_SIZE: Long = 0
    var Imgae_size: Long = 0
    var vFile: MultipartBody.Part? = null

    lateinit var datePicker: DatePickerHelper
    lateinit var mView: View

    override fun setTitlebar(titlebar: Titlebar) {
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_setup_profile_trainer, container, false)
        datePicker = DatePickerHelper(requireContext(), true)

        //viewUserDetails()

        mView.u_vid.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "video/*"
            startActivityForResult(intent, SELECT_VIDEO)

        }


        mView.btn_contiue.setOnClickListener {
            //  signupApi()
            validation()
        }

        mView.et_dob.setOnClickListener {
            showDatePickerDialog()
        }


        mView.imgLogo.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_IMAGE)
        }


        mView.btn_back.setOnClickListener {
            getRegisterationActivity?.onBackPressed()
        }
        return mView
    }


//    fun viewUserDetails(){
//
//        mView.et_full_name.setText(loginData.name)
//        mView.et_dob.setText(loginData.date_of_birth)
//        mView.et_age.setText(loginData.age)
//        mView.et_wieght.setText(loginData.weight)
//        mView.et_height.setText(loginData.height)
//        mView.et_goal.setText(loginData.goal)
//        mView.et_bio.setText(loginData.phone)
//        mView.et_bio.setText(loginData.bio)
//        loginData.image?.let { mView.imgLogo.setImageResource(it.toInt()) }
//
//
//    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)

        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                mView.et_dob.setText("${dayStr}-${monthStr}-${year}")
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (requestCode == SELECT_VIDEO) {
                Toast.makeText(context, "Video Selected Successfully", Toast.LENGTH_SHORT).show()

                playLocalVideo(uri!!)

                val file: File = activity?.let { getMediaFilePathFor(uri, it) }?.let { File(it) }!!
                val requestBody: RequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
                vFile = createFormData("bio_video", "bio_video", requestBody)

            } else {
                image.set(activity?.let { getMediaFilePathFor(uri!!, it) })
                mView.imgLogo.setImageURI(uri)
            }
            // Use Uri object instead of File to avoid storage permissions
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

    }

    private fun signupApi() = getFirebaseToken { token ->
        lifecycleScope.launch {
            var part: MultipartBody.Part? = null
            val hashMap = HashMap<String, RequestBody>().apply {
                this["role"] = role.getFormDataBody()
                this["name"] = mView.et_full_name.text.toString().getFormDataBody()
                this["date_of_birth"] = mView.et_dob.text.toString().getFormDataBody()
                this["age"] = mView.et_age.text.toString().getFormDataBody()
                this["weight"] = mView.et_wieght.text.toString().getFormDataBody()
                this["height"] = mView.et_height.text.toString().getFormDataBody()
                this["phone"] = mView.et_phone.text.toString().getFormDataBody()
                this["bio"] = mView.et_bio.text.toString().getFormDataBody()
                this["goal"] = mView.et_goal.text.toString().getFormDataBody()
            }
            if (image.get().isNullOrEmpty()) {
                Toast.makeText(context, getString(R.string.image_cant_be_empty), Toast.LENGTH_SHORT)
                    .show()

            } else if (vFile == null) {
                Toast.makeText(context, getString(R.string.video_cant_be_empty), Toast.LENGTH_SHORT)
                    .show()
            } else {
                part = File(image.get() ?: "").getPartMap("image")
                if (isNetworkConnected(requireActivity(), true)) {

                    try {


                        RetrofitSetup().callApi(getRegisterationActivity!!,
                            true,
                            PreferenceUtils.getString("token"),
                            object : CallHandler<Response<LoginResultpojo>> {
                                override suspend fun sendRequest(apiInterface: ApiInterface): Response<LoginResultpojo> {
                                    return apiInterface.SignupTrainer(part, vFile, hashMap)
                                }

                                override fun success(response: Response<LoginResultpojo>) {
                                    if (response.body()!!.status == 0) {
                                        Toast.makeText(
                                            activity,
                                            response.body()!!.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else if (response.body()!!.status == 1) {
                                        PreferenceData.storeLoginData(
                                            requireActivity(),
                                            response.body()!!.data
                                        )
                                        startActivity(Intent(context, MainActivity::class.java))
                                        getRegisterationActivity?.finish()
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            response.message(),
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


    fun validation() {
        if (mView.et_full_name.text.isNullOrBlank()) {
            mView.et_full_name.error = getString(R.string.fields_cant_be_empty)
            return
        } else if (mView.et_dob.text.isNullOrBlank()) {
            mView.et_dob.error = getString(R.string.fields_cant_be_empty)
            return
        } else if (mView.et_age.text.isNullOrBlank()) {
            mView.et_age.error = getString(R.string.fields_cant_be_empty)
            return
        } else if (mView.et_wieght.text.isNullOrBlank()) {
            mView.et_wieght.error = getString(R.string.fields_cant_be_empty)
            return
        } else if (mView.et_height.text.isNullOrBlank()) {
            mView.et_height.error = getString(R.string.fields_cant_be_empty)
            return
        } else if (mView.et_goal.text.isNullOrBlank()) {
            mView.et_goal.error = getString(R.string.fields_cant_be_empty)
            return
        } else if (mView.et_phone.text.isNullOrBlank()) {
            mView.et_phone.error = getString(R.string.fields_cant_be_empty)
            return
        } else if (mView.et_bio.text.isNullOrBlank()) {
            mView.et_bio.error = getString(R.string.fields_cant_be_empty)
            return
        } else {
            signupApi()
        }


    }


    fun playLocalVideo(uri: Uri) {
        mView.video_view_triner.setVideoURI(uri)
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView(mView.video_view_triner)
        mediaController.setMediaPlayer(mView.video_view_triner)
        mView.video_view_triner.setMediaController(mediaController)
    }

}