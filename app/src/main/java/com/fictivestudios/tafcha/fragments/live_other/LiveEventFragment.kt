package com.fictivestudios.tafcha.fragments.live_other

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Rect
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.streaming.PublishTestListener
import com.fictivestudios.tafcha.streaming.tests.PublishTest.PublishTest
import com.fictivestudios.tafcha.streaming.tests.TestContent
import com.fictivestudios.tafcha.streaming.tests.TestContent.GetPropertyInt
import com.red5pro.streaming.R5Connection
import com.red5pro.streaming.R5Stream
import com.red5pro.streaming.R5StreamProtocol
import com.red5pro.streaming.config.R5Configuration
import com.red5pro.streaming.event.R5ConnectionEvent
import com.red5pro.streaming.event.R5ConnectionListener
import com.red5pro.streaming.source.R5Camera
import com.red5pro.streaming.source.R5Microphone
import com.red5pro.streaming.view.R5VideoView
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_live_event.view.*


class LiveEventFragment : BaseFragment(), R5ConnectionListener, PublishTestListener {

    var preview: R5VideoView? = null
    protected var publishTestListener: PublishTestListener? = null
    protected var subscribe: R5Stream? = null
    protected var publish: R5Stream? = null
    protected var cam: Camera? = null
    protected var camera: R5Camera? = null
    protected var camOrientation = 0
    private var statproduct: Boolean? = false
    var stream: String? = null
    private var currentCamMode = 1
    var callstate: Boolean? = false
    var connectioned: R5Connection? = null


    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitle(getActivityContext!!, getString(R.string.live_stream))
        titlebar.setBtnBack(
            object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    getActivityContext
                        ?.onBackPressed()
                }
            }

        )

        titlebar.setBtnRight(R.drawable.notify_icon,
            object:View.OnClickListener{
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

    override fun onStart() {
        super.onStart()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mView = inflater.inflate(R.layout.fragment_live_event, container, false)

        preview = mView.videoPreview

        val handler = Handler()
        handler.postDelayed({
            publish()
        }, 500)


          mView.btn_reject.setOnClickListener {
              stopPublish()
              callstate = true
              Toast.makeText(requireActivity(),"Session has been ended",Toast.LENGTH_LONG).show()
              getActivityContext?.onBackPressed()
          }


        mView.btn_vide_call_stop.setOnClickListener {
            mView.btn_vide_call_stop.visibility = View.GONE
            mView.btn_vide_call.visibility = View.VISIBLE
            publish!!.restrainVideo(true)

        }

        mView.btn_vide_call.setOnClickListener {
            mView.btn_vide_call.visibility = View.GONE
            mView.btn_vide_call_stop.visibility = View.VISIBLE
            publish!!.restrainVideo(false)

        }

        mView.btn_mic.setOnClickListener {
            mView.btn_mic_stop.visibility = View.VISIBLE
            mView.btn_mic.visibility = View.GONE
            publish!!.restrainAudio(false)
        }

        mView.btn_mic_stop.setOnClickListener {
            mView.btn_mic_stop.visibility = View.GONE
            mView.btn_mic.visibility = View.VISIBLE
            publish!!.restrainAudio(true)

        }


        mView.btn_speaker.setOnClickListener(View.OnClickListener {
            mView.btn_speaker.visibility = View.GONE
            mView.btn_speaker_stop.visibility = View.VISIBLE
            cameraset(0)
        })

        mView.btn_speaker.setOnClickListener(View.OnClickListener {
            mView.btn_speaker.visibility = View.VISIBLE
            mView.btn_speaker_stop.visibility = View.GONE
            cameraset(1)
        })





        return mView
    }

    override fun onConnectionEvent(event: R5ConnectionEvent) {
        Log.d("Publisher", ":onConnectionEvent " + event.name + " - " + event.message)
        if (event.name === R5ConnectionEvent.LICENSE_ERROR.name) {
            val h = Handler(Looper.getMainLooper())
            h.post {
                val alertDialog = AlertDialog.Builder(requireActivity()).create()
                alertDialog.setTitle("Error")
                alertDialog.setMessage("License is Invalid")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            }
        } else if (event.name === R5ConnectionEvent.START_STREAMING.name) {


        } else if (event.name === R5ConnectionEvent.BUFFER_FLUSH_START.name) {
            if (publishTestListener != null) {
                publishTestListener!!.onPublishFlushBufferStart()
            }
        } else if (event.name === R5ConnectionEvent.BUFFER_FLUSH_EMPTY.name || event.name === R5ConnectionEvent.DISCONNECTED.name) {
            if (publishTestListener != null) {
                publishTestListener!!.onPublishFlushBufferComplete()
                publishTestListener = null
            }
        }
    }

    override fun onItemSelected(id: String?) {
    }

    private fun cameraset(no: Int) {

        val publishCam = publish!!.videoSource as R5Camera
        var newCam: Camera? = null
        publishCam.camera.stopPreview()
        publishCam.camera.release()
        newCam = openFrontFacingCameraGingerbread(no)
        if (newCam != null) {
            currentCamMode = Camera.CameraInfo.CAMERA_FACING_FRONT
        }
        if (newCam != null) {
            publishCam.camera = newCam
            publishCam.orientation = camOrientation
            newCam.setDisplayOrientation((camOrientation + 180) % 180)
            newCam.startPreview()
            applyDeviceRotation()

            cam = newCam
        }
    }

    protected fun publish() {
        val b = requireActivity().packageName

        //Create the configuration from the values.xml
        val config = R5Configuration(
            R5StreamProtocol.RTSP,
            "broadcasting.tafcha.com",
            8554,
            "live",
            0.5f)
        config.licenseKey = "WS5C-7ICR-V63W-62T0"
        config.bundleID = b
        connectioned = R5Connection(config)


        //setup a new stream using the connection
        publish = R5Stream(connectioned)
        publish!!.audioController.sampleRate = 44100
            //TestContent.GetPropertyInt("sample_rate")

        //show all logging
        publish!!.setLogLevel(R5Stream.LOG_LEVEL_DEBUG)
        if (true) {

           // TestContent.GetPropertyBool("audio_on")
            //attach a microphone
            attachMic()
        }
        preview!!.attachStream(publish)
        if (true) {
          //  TestContent.GetPropertyBool("video_on")
            //attach a camera video source
            attachCamera(1)
        }
        //  preview!!.showDebugView(TestContent.GetPropertyBool("debug_view"))
        publish!!.setListener(this)
        //TestContent.GetPropertyString("stream1
        //TestContent.SetPropertyString("record_mode", R5Stream.RecordType.Record.toString())
        publish!!.publish("stream1", getPublishRecordType())
        if (true) {
            cam!!.startPreview()
        }
    }

    fun getPublishRecordType(): R5Stream.RecordType? {
        val type = "Live"
        if (type == "Record") {
            return R5Stream.RecordType.Record
        } else if (type == "Record") {
            return R5Stream.RecordType.Record
        }
        return R5Stream.RecordType.Record
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @kotlin.jvm.JvmField
        var not_detail: LiveEventFragment? = null
        fun newInstance(): LiveEventFragment {
            return LiveEventFragment()
        }
    }

    protected fun attachCamera(no: Int) {
        cam = openFrontFacingCameraGingerbread(no)
        cam!!.setDisplayOrientation((camOrientation + 180) % 360)
        camera = R5Camera(cam, 640, 360)
        camera!!.bitrate = 750
        camera!!.orientation = camOrientation
        camera!!.framerate = 15

        // camera.setFacing(CameraSource.CAMERA_FACING_FRONT)


        publish!!.attachCamera(camera)


    }

    protected fun openFrontFacingCameraGingerbread(no: Int): Camera? {
        var cameraCount = 0
        var cam: Camera? = null
        val cameraInfo = Camera.CameraInfo()
        cameraCount = Camera.getNumberOfCameras()
        // for (camIdx in 0 until cameraCount) {
        Camera.getCameraInfo(no, cameraInfo)
        //if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
        try {
            cam = Camera.open(no)
            camOrientation = cameraInfo.orientation
            applyDeviceRotation()
            //  break
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        // }
        //  }
        return cam
    }


    protected fun applyDeviceRotation() {
        val rotation = requireActivity().windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 270
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 90
        }
        val screenSize = Rect()
        requireActivity().windowManager.defaultDisplay.getRectSize(screenSize)
        val screenAR = screenSize.width() * 1.0f / (screenSize.height() * 1.0f)
        if (screenAR > 1 && degrees % 180 == 0 || screenAR < 1 && degrees % 180 > 0) degrees += 180
        println("Apply Device Rotation: $rotation, degrees: $degrees")
        camOrientation += degrees
        camOrientation = camOrientation % 360
    }

    protected fun attachMic() {
        val mic = R5Microphone()
        publish!!.attachMic(mic)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopPublish()
    }

    fun stopPublish() {
        // publishTestListener = listener

        if (publish != null) {
            publish!!.stop()

            if (publish!!.videoSource != null) {
                val c = (publish!!.videoSource as R5Camera).camera
                c.stopPreview()
                c.release()
            }
            publish = null
        }

    }

    override fun onPublishFlushBufferStart() {
        if (publishTestListener != null) {
            publishTestListener!!.onPublishFlushBufferStart()
        }
    }

    override fun onPublishFlushBufferComplete() {
        if (publishTestListener != null) {
            publishTestListener!!.onPublishFlushBufferComplete()
            publishTestListener = null
        }
    }


}