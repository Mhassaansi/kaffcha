package com.fictivestudios.tafcha.fragments.Challenge_event_Leader

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.*
import com.fictivestudios.tafcha.adapters.InviteUserUpdateList
import com.fictivestudios.tafcha.adapters.InviteUsersAdapter
import com.fictivestudios.tafcha.dialogFragments.RejectDialog
import com.fictivestudios.tafcha.dialogFragments.SuccessDialogueFragment
import com.fictivestudios.tafcha.fragments.live_other.LiveEventFragment
import com.fictivestudios.tafcha.fragments.live_other.NotificationsFragment
import com.fictivestudios.tafcha.interfaces.InviteFriendsList
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.event.createevent.CreateEventResponse
import com.fictivestudios.tafcha.models.event.event_item.EventItem
import com.fictivestudios.tafcha.models.event.event_item.User
import com.fictivestudios.tafcha.models.trainers.viewmyusers.MyUserItem
import com.fictivestudios.tafcha.models.trainers.viewmyusers.ViewMyUsers
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.getFormDataBody
import com.fictivestudios.tafcha.networkSetup.getPartMap
import com.fictivestudios.tafcha.networkSetup.retrofitsetup.RetrofitSetup
import com.fictivestudios.tafcha.networkSetup.url.MEDIA_URL
import com.github.dhaval2404.imagepicker.ImagePicker
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_new_events.view.*
import kotlinx.android.synthetic.main.fragment_training2.view.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*


@SuppressLint("NotifyDataSetChanged")
class NewEventsFragment(var u_event: Boolean, var event_id: Int = 0, var u_id: Int) :
    BaseFragment(), InviteFriendsList {

    private val loginData by lazy { PreferenceData.retrieveLoginData(requireActivity()) }

    //    lateinit var datePicker: DatePickerHelper
    var image: ObservableField<String> = ObservableField("")
    var uidList = ArrayList<Int>()
    lateinit var datePicker: DatePickeEvent
    var userList = ArrayList<MyUserItem>()
    var updateUserList = ArrayList<User>()


    val SELECT_IMAGE = 1

    private lateinit var mView: View

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.events))
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


        mView = inflater.inflate(R.layout.fragment_new_events, container, false)

        mView.upload_img_event.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_IMAGE)
        }

        datePicker = DatePickeEvent(requireContext(), true)




        mView.date.setOnClickListener {
            showDatePickerDialog()
        }

        mView.time.setOnClickListener {
            timePicker()
        }


        if(u_event == true && loginData.id == u_id){
            mView.btn_create_new.setText(R.string.update)
            eventDetails()

            mView.btn_create_new.setOnClickListener{
                updateEvent()
            }
        }
        else if(u_event==true && loginData.id != u_id ){
            eventDetails()
            mView.btn_create_new.visibility = View.INVISIBLE
            mView.btn_join.visibility = View.INVISIBLE
        }
        else{
            mView.btn_create_new.setText(R.string.create)
            mView.btn_join.visibility = View.INVISIBLE
            getTranersUsers()
            mView.btn_create_new.setOnClickListener {
                createEvent()
            }


        }




        mView.btn_join.setOnClickListener {
            liveStreamStart()
            getActivityContext
                ?.replaceFragment(
                    LiveEventFragment(),
                    LiveEventFragment().javaClass.simpleName,
                    true, true
                )
        }
        return mView

    }


    private fun timePicker() {
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                mView.time.text = String.format("%d : %d", hourOfDay, minute)
            }
        }, hour, minute, false)

        mView.time.setOnClickListener({ v ->
            mTimePicker.show()
        })
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.showDialog(d, m, y, object : DatePickeEvent.Callback {
            @SuppressLint("SetTextI18n")
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                mView.date.text = "${year}-${monthStr}-${dayStr}"

            }
        })
    }

    private fun initUpdateUserRecyler(updateUser: ArrayList<User>? = null) {
        mView.rv_invite_users_event.adapter =
            context?.let { InviteUserUpdateList(it, updateUser!!) }
        mView.rv_invite_users_event.adapter?.notifyDataSetChanged()
    }

    private fun initUsersRecyclerview(usersList: ArrayList<MyUserItem>? = null) {
        mView.rv_invite_users_event.adapter =
            context?.let { InviteUsersAdapter(it, usersList!!, this) }
        mView.rv_invite_users_event.adapter?.notifyDataSetChanged()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri = data!!.data
            if (requestCode == SELECT_IMAGE) {
                image.set(activity?.let { getMediaFilePathFor(uri!!, it) })
                mView.upload_img_event.setImageURI(uri)
                Log.d("ioooossss", "" + image.get())

            } else {

            }
            // Use Uri object instead of File to avoid storage permissions
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun liveStreamStart(){

        lifecycleScope.launch {

            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {

                                return apiInterFace.startLiveStream(event_id)

                            }

                            override fun success(response: Response<CommonResponseDC>) {

                                if (response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message,
                                        Toast.LENGTH_LONG
                                    ).show()

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
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }


                        })

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }

    private fun getTranersUsers() {

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<ViewMyUsers>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<ViewMyUsers> {

                                return apiInterFace.getViewUsers()

                            }

                            override fun success(response: Response<ViewMyUsers>) {

                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                    userList = response.body()!!.data as ArrayList<MyUserItem>

                                    initUsersRecyclerview(userList)

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
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }

                        })

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }

    private fun eventDetails() {

        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<EventItem>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<EventItem> {
                                return apiInterFace.viewEvent(event_id)
                            }

                            override fun success(response: Response<EventItem>) {


                                if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    Log.d("event_data", response.body()!!.data.toString())
                                    // Toast.makeText(requireActivity(), response.body()!!.data.toString(), Toast.LENGTH_LONG).show()
                                    Glide.with(requireActivity())
                                        .load(MEDIA_URL + response.body()!!.data.event.image)
                                        .into(mView.upload_img_event)
                                    mView.et_training_name.setText(response.body()!!.data.event.title.toString())
                                    // mView.iv_training_time.setText(response.body()!!.data.event.time + " Hours".toString())
                                    mView.date.text = response.body()!!.data.event.date.toString()
                                    mView.time.text = response.body()!!.data.event.time.toString()

                                    updateUserList =
                                        response.body()!!.data.event.users

                                    initUpdateUserRecyler(updateUserList)
                                    //initEventDetaislRecyclerview(response.body()!!.data.event.users as ArrayList<User>)

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
                                //  Toast.makeText(requireActivity(), response.body()!!.data.toString(), Toast.LENGTH_LONG).show()
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                            }


                        })

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }

    private fun updateEvent() {


        lifecycleScope.launch {
            if (Constants.isNetworkConnected(requireActivity(), true)) {

                var part: MultipartBody.Part? = null
                val hashMap = HashMap<String, RequestBody>().apply {
                    this["title"] = mView.et_training_name.text.toString().getFormDataBody()
                    this["date"] = mView.date.text.toString().getFormDataBody()
                    this["time"] = mView.time.text.toString().getFormDataBody()
                    this["event_id"] = event_id.toString().getFormDataBody()
                    for (i in updateUserList.indices) {
                        //Toast.makeText(requireActivity(), i.toString(), Toast.LENGTH_LONG).show()
                        this["users_id[${i}]"] = updateUserList[i].id.toString().getFormDataBody()
                    }
                    if (!image.get().isNullOrEmpty()) {
                        Log.d("ioooossss", "" + image.get())
                        part = File(image.get() ?: "").getPartMap("image")
                    }

                }
                try {

                    RetrofitSetup().callApi(requireActivity(),
                        true,
                        PreferenceUtils.getString("token"),
                        object : CallHandler<Response<CommonResponseDC>> {
                            override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CommonResponseDC> {
                                return apiInterFace.updateEvent(part, hashMap)

                            }

                            override fun success(response: Response<CommonResponseDC>) {

                                if (response.body()?.status == 1  && response.body()?.message != "record not found..!") {
                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()?.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else if (response.body()?.status == 0) {
                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()?.message,
                                        Toast.LENGTH_LONG
                                    ).show()
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

    private fun createEvent() {

        lifecycleScope.launch {


            if (uidList == null) {
                Toast.makeText(requireActivity(), R.string.user_selct, Toast.LENGTH_LONG).show()
            } else {


                if (Constants.isNetworkConnected(requireActivity(), true)) {

                    var part: MultipartBody.Part? = null
                    val hashMap = HashMap<String, RequestBody>().apply {
                        this["title"] = mView.et_training_name.text.toString().getFormDataBody()
                        this["date"] = mView.date.text.toString().getFormDataBody()
                        this["time"] = mView.time.text.toString().getFormDataBody()
                        for (i in uidList.indices) {
                            this["users_id[${i}]"] = uidList[i].toString().getFormDataBody()
                        }
                        if (!image.get().isNullOrEmpty()) {
                            part = File(image.get() ?: "").getPartMap("image")
                        }
                    }

                    try {

                        RetrofitSetup().callApi(requireActivity(),
                            true,
                            PreferenceUtils.getString("token"),
                            object : CallHandler<Response<CreateEventResponse>> {
                                override suspend fun sendRequest(apiInterFace: ApiInterface): Response<CreateEventResponse> {

                                    return apiInterFace.createEvent(part, hashMap)

                                }

                                override fun success(response: Response<CreateEventResponse>) {

                                    if (response.body()?.data != null && response.body()?.status == 1  && response.body()?.message != "record not found..!") {

                                        getActivityContext
                                            ?.AddFragment(
                                                SuccessDialogueFragment(getString(R.string.your_event_has_been_created),
                                                    object : View.OnClickListener {
                                                        override fun onClick(p0: View?) {
                                                            getActivityContext?.replaceFragment(
                                                                EventsPageFragment(),
                                                                EventsPageFragment().javaClass.simpleName,
                                                                false,
                                                                true
                                                            )
                                                        }
                                                    }

                                                ),
                                                SuccessDialogueFragment.javaClass.simpleName,
                                                true, false
                                            )


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

    override fun getUser(pos: Int) {
        uidList.add(userList.get(pos).id!!)
        Toast.makeText(
            requireActivity(),
            "" + userList.get(pos).name + " Added",
            Toast.LENGTH_LONG
        ).show()
    }
}