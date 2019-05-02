package com.imq.myhome.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.imq.myhome.Auxiliary.AuxiliaryGeneral;

import java.util.Locale;

public class ServiceTTS extends Service implements TextToSpeech.OnInitListener {

    private static final String TAG = "R2Y2";
    private final IBinder binder = new LocalBinder();
    private AuxiliaryGeneral GAux;
    private TextToSpeech mTts;

    public ServiceTTS() {
        Log.d(TAG, "Pending Intent");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "Service bind");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        StartTTS();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created!");
        StartTTS();
    }

    private void StartTTS() {
        GAux = new AuxiliaryGeneral(this);
        mTts = new TextToSpeech(this, this);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        Log.v(TAG, "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(GAux.getLocal());
            for (Locale voice : mTts.getAvailableLanguages()) {
                Log.v(TAG, voice.getDisplayCountry());
            }
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                mTts.setSpeechRate(GAux.getSpeechRate());
                if (GAux.isVoiceEnable()) {
                    Test_voice();
                }
            }

        } else {
            Log.d(TAG, "Could not initialize TextToSpeech.");
        }
    }

    public void Test_voice() {
        Log.d(TAG, "Testing Voice");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String test = "Testing: 1, 2, 3";
                mTts.speak(test, TextToSpeech.QUEUE_FLUSH, null, TAG);
            }
        }, 200);
    }

    public void say_Message(final String message) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTts.speak(message, TextToSpeech.QUEUE_FLUSH, null, TAG);
            }
        }, 200);
    }

    public class LocalBinder extends Binder {
        public ServiceTTS getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServiceTTS.this;
        }
    }
}