//
// Copyright © 2015 Infrared5, Inc. All rights reserved.
//
// The accompanying code comprising examples for use solely in conjunction with Red5 Pro (the "Example Code")
// is  licensed  to  you  by  Infrared5  Inc.  in  consideration  of  your  agreement  to  the  following
// license terms  and  conditions.  Access,  use,  modification,  or  redistribution  of  the  accompanying
// code  constitutes your acceptance of the following license terms and conditions.
//
// Permission is hereby granted, free of charge, to you to use the Example Code and associated documentation
// files (collectively, the "Software") without restriction, including without limitation the rights to use,
// copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The Software shall be used solely in conjunction with Red5 Pro. Red5 Pro is licensed under a separate end
// user  license  agreement  (the  "EULA"),  which  must  be  executed  with  Infrared5,  Inc.
// An  example  of  the EULA can be found on our website at: https://account.red5pro.com/assets/LICENSE.txt.
//
// The above copyright notice and this license shall be included in all copies or portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,  INCLUDING  BUT
// NOT  LIMITED  TO  THE  WARRANTIES  OF  MERCHANTABILITY, FITNESS  FOR  A  PARTICULAR  PURPOSE  AND
// NONINFRINGEMENT.   IN  NO  EVENT  SHALL INFRARED5, INC. BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM,  OUT  OF  OR  IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.fictivestudios.tafcha.streaming.tests.SubscribeMuteTest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fictivestudios.tafcha.R;
import com.fictivestudios.tafcha.streaming.TestDetailFragment;
import com.fictivestudios.tafcha.streaming.tests.TestContent;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import com.red5pro.streaming.media.R5AudioController;
import com.red5pro.streaming.view.R5VideoView;


public class SubscribeMuteTest extends TestDetailFragment implements R5ConnectionListener {
    protected R5VideoView display;
    protected R5Stream subscribe;
    protected boolean mIsMuted = false;

	protected void showToast (String message) {
		final CharSequence text = message;
		final Context context = getContext();
		final int duration = Toast.LENGTH_SHORT;
		try {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			});
		} catch (Exception e) {
			// Most likely have moved away from activity back to main listing on Event.CLOSE.
			e.printStackTrace();
		}
	}

    @Override
    public void onConnectionEvent(R5ConnectionEvent event) {
        Log.d("Subscriber", ":onConnectionEvent " + event.name());
		String msg = event.message;
		showToast(msg == null ? event.name() : msg);

        if (event.name() == R5ConnectionEvent.LICENSE_ERROR.name()) {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(SubscribeMuteTest.this.getActivity()).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("License is Invalid");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",
                            new DialogInterface.OnClickListener()

                            {
                                public void onClick (DialogInterface dialog,int which){
                                    dialog.dismiss();
                                }
                            }

                    );
                    alertDialog.show();
                }
            });
        }
        else if (event.name() == R5ConnectionEvent.VIDEO_RENDER_START.name()) {
            Log.d("SubscribeTest", "SUPPORT-482 " + event.message);
        }
        if (event.name() == R5ConnectionEvent.START_STREAMING.name()){
//            subscribe.setFrameListener(new R5FrameListener() {
//                @Override
//                public void onBytesReceived(byte[] bytes, int i, int i1) {
//                    Uncomment for framelistener performance test
//                }
//            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.subscribe_mute_test, container, false);

        //find the view and attach the stream
        display = (R5VideoView) view.findViewById(R.id.videoView);

        Button endButton = (Button) view.findViewById(R.id.muteButton);
        endButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if( event.getAction() == MotionEvent.ACTION_UP ){
                    if (subscribe != null) {
                        mIsMuted = !mIsMuted;
                        subscribe.audioController.setPlaybackGain(mIsMuted ? 0.0f : 1.0f);
                    }
                    return true;
                }
                return false;
            }
        });

        Subscribe();

        return view;
    }

    public void Subscribe(){

        //Create the configuration from the tests.xml
        R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,
                TestContent.GetPropertyString("host"),
                TestContent.GetPropertyInt("port"),
                TestContent.GetPropertyString("context"),
                TestContent.GetPropertyFloat("subscribe_buffer_time"));
        config.setLicenseKey(TestContent.GetPropertyString("license_key"));
        config.setBundleID(getActivity().getPackageName());

        R5Connection connection = new R5Connection(config);

        //setup a new stream using the connection
        subscribe = new R5Stream(connection);

        //Some devices can't handle rapid reuse of the audio controller, and will crash
        //Recreation of the controller assures that the example will always be stable
        subscribe.audioController = new R5AudioController();
        subscribe.audioController.sampleRate = TestContent.GetPropertyInt("sample_rate");

        subscribe.client = this;
        subscribe.setListener(this);

        //show all logging
        subscribe.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

        //display.setZOrderOnTop(true);
        display.attachStream(subscribe);

        display.showDebugView(TestContent.GetPropertyBool("debug_view"));


        subscribe.play(TestContent.GetPropertyString("stream1"), TestContent.GetPropertyBool("hwAccel_on"));

    }

    protected void updateOrientation(int value) {
        value += 90;
        Log.d("SubscribeTest", "update orientation to: " + value);
        display.setStreamRotation(value);
    }

    public void onMetaData(String metadata) {
        Log.d("SubscribeTest", "Metadata receieved: " + metadata);
        String[] props = metadata.split(";");
        for (String s : props) {
            String[] kv = s.split("=");
            if (kv[0].equalsIgnoreCase("orientation")) {
                updateOrientation(Integer.parseInt(kv[1]));
            }
        }
    }

    public void onStreamSend(String msg){
        Log.d("SubscribeTest", "GOT MSG");
    }

    @Override
    public void onStop() {
        if(subscribe != null) {
            subscribe.stop();
            subscribe = null;
        }

        if (display != null) {
            display.attachStream(null);
        }

        super.onStop();
    }
}
