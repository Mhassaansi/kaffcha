package com.fictivestudios.tafcha.fragments.profile


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
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
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.*
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.models.Login.LoginResultpojo
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getFormDataBody
import com.fictivestudios.tafcha.networkSetup.getPartMap
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.github.dhaval2404.imagepicker.ImagePicker
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_profile_trainer.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile_trainer.view.video_view_triner
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*





class EditProfileTrainerFragment : BaseFragment() {


    val SELECT_VIDEO = 1
    val SELECT_IMAGE = 2
    private lateinit var mView: View
    private val loginData by lazy { PreferenceData.retrieveLoginData(requireActivity()) }
    lateinit var datePicker: DatePickerHelper
    var image: ObservableField<String> = ObservableField("")
    var vFile: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.edit_profile))
        titlebar.setBtnBack(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )

        titlebar.setBtnRight(R.drawable.notify_icon,
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.replaceFragment(
                            NotificationsFragment(),
                            NotificationsFragment().javaClass.simpleName,
                            true, true
                        )
                }
            })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_edit_profile_trainer, container, false)

        datePicker = DatePickerHelper(requireContext(), true)

        loadProfileData()

        mView.btn_contiue.setOnClickListener {
            updateProfile()
        }

        mView.u_vid.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "video/*"
            startActivityForResult(intent, SELECT_VIDEO)
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

        return mView
    }


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

                if (uri != null) {
                    playLocalVideo(uri)
                }

                Toast.makeText(context, "Video Selected Successfully", Toast.LENGTH_SHORT).show()
                val file: File = File(activity?.let { getMediaFilePathFor(uri!!, it) })
                val requestBody: RequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
                vFile = MultipartBody.Part.createFormData("bio_video", "bio_video", requestBody)
            }
            else {
                image.set(activity?.let { getMediaFilePathFor(uri!!, it) })
                mView.imgLogo.setImageURI(uri)
                Toast.makeText(context, "Image Selected Successfully", Toast.LENGTH_SHORT).show()
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
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



    private fun updateProfile()  {
        lifecycleScope.launch {
            var part: MultipartBody.Part? = null
            val hashMap = HashMap<String, RequestBody>().apply {
                this["name"] = mView.et_full_name.text.toString().getFormDataBody()
                this["date_of_birth"] = mView.et_dob.text.toString().getFormDataBody()
                this["age"] = mView.et_age.text.toString().getFormDataBody()
                this["weight"] = mView.et_wieght.text.toString().getFormDataBody()
                this["height"] = mView.et_height.text.toString().getFormDataBody()
                this["phone"] = mView.et_phone.text.toString().getFormDataBody()
                this["bio"] = mView.et_bio.text.toString().getFormDataBody()
                this["goal"] = mView.et_goal.text.toString().getFormDataBody()
            }
            if (!image.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + image.get())
                part = File(image.get() ?: "").getPartMap("image")
            }
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {
                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<LoginResultpojo>> {
                            override suspend fun sendRequest(apiInterface: ApiInterface): Response<LoginResultpojo> {
                                return apiInterface.updateTrainerProfile(part, vFile, hashMap)
                            }

                            override fun success(response: Response<LoginResultpojo>) {
                                if (response.body()!!.status == 0) {
                                    Toast.makeText(
                                        activity,
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                } else if (response.body()?.status == 1) {
                                    Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG).show()
                                    PreferenceData.storeLoginData(requireActivity(), response.body()?.data)
                                    requireActivity().onBackPressed()

                                } else {
                                    Toast.makeText(activity, response.message(), Toast.LENGTH_LONG)
                                        .show()
                                }
                            }

                            override fun error(message: String) {

                                RetrofitSetup().hideLoader()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }
                        })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    private fun loadProfileData() {

        mView.et_full_name.setText(loginData.name)
        mView.et_dob.setText(loginData.date_of_birth)
        mView.et_age.setText(loginData.age)
        mView.et_height.setText(loginData.height)
        mView.et_wieght.setText(loginData.weight)
        mView.et_phone.setText(loginData.phone)
        mView.et_goal.setText(loginData.goal)
        mView.et_bio.setText(loginData.bio)
        if(loginData.image == null){
            mView.imgLogo.setImageResource(R.drawable.user_blank_profile)
        }
        else{
            Glide.with(activity).load(MEDIA_URL+loginData.image)
                .into(mView.imgLogo)
        }
        if(loginData.bio_video != null){
            playVideo1(MEDIA_URL+loginData.bio_video)
        }
        else{

        }


    }
    fun playLocalVideo(uri:Uri){
        mView.video_view_triner.setVideoURI(uri)
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView( mView.video_view_triner)
        mediaController.setMediaPlayer( mView.video_view_triner)
        mView.video_view_triner.setMediaController(mediaController)
    }
    fun playVideo1(url:String){
        val uri: Uri = Uri.parse(url)
        mView.video_view_triner.setVideoURI(uri)
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView( mView.video_view_triner)
        mediaController.setMediaPlayer( mView.video_view_triner)
        mView.video_view_triner.setMediaController(mediaController)
        mView.video_view_triner.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                Log.d("video", "setOnErrorListener ")
                return true
            }
        })
    }

}

