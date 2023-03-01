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
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.lifecycleScope
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.*
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.models.Login.LoginResultpojo

import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getFormDataBody
import com.fictivestudios.tafcha.networkSetup.getPartMap
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.github.dhaval2404.imagepicker.ImagePicker
import com.technado.demoapp.base.BaseFragment

import kotlinx.android.synthetic.main.fragment_signup_user.view.*
import kotlinx.android.synthetic.main.fragment_signup_user.view.btn_back
import kotlinx.android.synthetic.main.fragment_signup_user.view.et_age
import kotlinx.android.synthetic.main.fragment_signup_user.view.et_dob
import kotlinx.android.synthetic.main.fragment_signup_user.view.et_full_name
import kotlinx.android.synthetic.main.fragment_signup_user.view.et_goal
import kotlinx.android.synthetic.main.fragment_signup_user.view.et_phone
import kotlinx.android.synthetic.main.fragment_signup_user.view.et_wieght
import kotlinx.android.synthetic.main.fragment_signup_user.view.imgLogo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*



class SignupUserFragment(isGoogle:Boolean,isFaceBook:Boolean,val role:String) : BaseFragment() {

    val SELECT_IMAGE = 2
    private lateinit var mView: View
    var image: ObservableField<String> = ObservableField("")
    lateinit var datePicker: DatePickerHelper
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

        mView = inflater.inflate(R.layout.fragment_signup_user, container, false)


        mView.et_wieght.setText(Constants.weight.toString())
         datePicker = DatePickerHelper(requireContext(),true)




        mView.btn_signup.setOnClickListener {
            validateFields()

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
            getRegisterationActivity?.
            onBackPressed()
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

    private fun validateFields() {

        if (mView.et_full_name.text.toString().isBlank()||
            mView.et_age.text.toString().isBlank()||
            mView.et_goal.text.toString().isBlank()||
            mView.et_wieght.text.toString().isBlank()||
            mView.et_phone.text.toString().isBlank())

        {
            Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT).show()
            return
        }
        else if (image==null){
            Toast.makeText(context, getString(R.string.image_cant_be_empty), Toast.LENGTH_SHORT).show()
            return
        }

        else
        {
            signupUserApi()
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri = data!!.data
            if (requestCode == SELECT_IMAGE) {
                image.set(activity?.let { getMediaFilePathFor(uri!!, it) })
                mView.imgLogo.setImageURI(uri)
                Log.d("ioooossss", "" + image.get())

            } else {

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signupUserApi() = getFirebaseToken { token ->
        lifecycleScope.launch {
            var part: MultipartBody.Part? = null
            val hashMap = HashMap<String, RequestBody>().apply {
                this["role"] = role.getFormDataBody()
                this["name"] = mView.et_full_name.text.toString().getFormDataBody()
                this["date_of_birth"] = mView.et_dob.text.toString().getFormDataBody()
                this["age"] = mView.et_age.text.toString().getFormDataBody()
                this["weight"] = mView.et_wieght.text.toString().getFormDataBody()
                this["goal"] = mView.et_goal.text.toString().getFormDataBody()
                this["phone"] = mView.et_phone.text.toString().getFormDataBody()
            }
            if (!image.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + image.get())
                part = File(image.get() ?: "").getPartMap("profile_image")
            }
            if(Constants.isNetworkConnected(requireActivity(), true)) {
            try {
                RetrofitSetup().callApi(getRegisterationActivity!!, true, "${PreferenceUtils.getString("token")}",
                    object : CallHandler<Response<LoginResultpojo>> {
                        override suspend fun sendRequest(apiInterface: ApiInterface): Response<LoginResultpojo> {
                            return apiInterface.SignupUser(part,hashMap)
                        }
                        override fun success(response: Response<LoginResultpojo>) {
                            if (response.body()!!.status == 0) {
                                Toast.makeText(
                                    activity,
                                    response.body()!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (response.body()?.status==1  && response.body()?.message != "record not found..!") {
                                PreferenceData.storeLoginData(requireActivity(), response.body()!!.data)
                                startActivity(Intent(context, MainActivity::class.java))
                                getRegisterationActivity?.finish()
                            }
                        }
                        override fun error(message: String) {
                            RetrofitSetup().hideLoader()
                            Toast.makeText(requireActivity(),"Error",Toast.LENGTH_LONG).show()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }}
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




}