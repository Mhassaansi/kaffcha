package com.fictivestudios.tafcha.fragments.live_other

import android.app.AlertDialog
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Titlebar
import com.fictivestudios.tafcha.streaming.PublishTestListener
import com.fictivestudios.tafcha.streaming.tests.TestContent
import com.red5pro.streaming.R5Connection
import com.red5pro.streaming.R5Stream
import com.red5pro.streaming.R5StreamProtocol
import com.red5pro.streaming.config.R5Configuration
import com.red5pro.streaming.event.R5ConnectionEvent
import com.red5pro.streaming.event.R5ConnectionListener
import com.red5pro.streaming.media.R5AudioController
import com.red5pro.streaming.source.R5Camera
import com.red5pro.streaming.view.R5VideoView
import com.technado.demoapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_subscribe.view.*


class SubscribeFragment : BaseFragment(), R5ConnectionListener {

    private lateinit var mView: View
    var preview: R5VideoView? = null
    protected var subscribe: R5Stream? = null
    protected var cam: Camera? = null
    protected var camera: R5Camera? = null
    protected var camOrientation = 0
    var stream: String? = null
    protected var display: R5VideoView? = null
    var connectioned: R5Connection? = null


    override fun setTitlebar(titlebar: Titlebar) {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            mView =  inflater.inflate(R.layout.fragment_subscribe, container, false)

                  display = mView.videoPreview
            //find the view and attach the stream
           // display = requireView().findViewById<View>(R.id.videoView) as R5VideoView


            val handler = Handler()
            handler.postDelayed({
                Subscribe()

            }, 500)


            mView.btn_reject.setOnClickListener {
                onStop()
                getActivityContext?.onBackPressed()

            }

            return mView

        }


    fun Subscribe() {
        val b = requireActivity().packageName
        //Create the configuration from the tests.xml
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
        subscribe = R5Stream(connectioned)

        //Some devices can't handle rapid reuse of the audio controller, and will crash
        //Recreation of the controller assures that the example will always be stable
        subscribe!!.audioController = R5AudioController()
        subscribe!!.audioController.sampleRate = 44100
        subscribe!!.client = this
        subscribe!!.listener = this
//TestContent.GetPropertyInt("sample_rate")
        //show all logging
        subscribe!!.setLogLevel(R5Stream.LOG_LEVEL_DEBUG)

        //display.setZOrderOnTop(true);
        display?.attachStream(subscribe)
        display?.showDebugView(false)
        subscribe!!.play("stream1",false)
      //  TestContent.GetPropertyString("stream1"), TestContent.GetPropertyBool("hwAccel_on")
    }
   // TestContent.GetPropertyBool("debug_view")
    override fun onStop() {
        if (display != null) {
            display?.attachStream(null)
        }
        if (subscribe != null) {
            subscribe!!.stop()
            subscribe = null
        }
        super.onStop()
    }

    override fun onConnectionEvent(event: R5ConnectionEvent?) {
        Log.d("Subscriber", ":onConnectionEvent " + event?.name)
        Log.d("Publisher", ":onConnectionEvent " + event?.name + " - " + event?.message)

        if (event?.name === R5ConnectionEvent.LICENSE_ERROR.name) {
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
        }
        else if (event?.name == R5ConnectionEvent.START_STREAMING.name) {
//            subscribe.setFrameListener(new R5FrameListener() {
//                @Override
//                public void onFrameReceived(Object o, R5StreamFormat r5StreamFormat, int w, int h) {
//                    int format = r5StreamFormat.value(); // 2 - YUV_PLANAR
//                    if (r5StreamFormat.equals(R5StreamFormat.YUV_PLANAR)) {
//                        byte[][] yuv_frames = (byte[][]) o; // Cast and access data in 3 planes as byte array. (byte[3][])
//                    }
//                }
//            });
        }

    }


}








