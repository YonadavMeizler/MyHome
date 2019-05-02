package com.imq.myhome.Fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.imq.myhome.R;
import com.imq.myhome.Services.ServiceTTS;

import java.util.Objects;

public class FragmentSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String TAG = "R2Y2";

    private boolean mBound = false;
    private ServiceTTS mService;
    private Preference test_voice, Speech_Rate, Speech_Language, Speech_guidence;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServiceTTS.LocalBinder binder = (ServiceTTS.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        test_voice = findPreference("Speech_test");
        Speech_Rate = findPreference("Speech_Rate");
        Speech_Language = findPreference("Speech_Language");
        //Speech_guidence = findPreference("Speech_guidence");
        test_voice.setOnPreferenceClickListener(this);
        //Speech_guidence.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("Speech_test")) {
            StartVoiceService();
            Log.d(TAG, "starting voice test");
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.getKey().equals("Speech_guidence")) {
            if (Speech_guidence.isEnabled()) {
                Speech_Language.setEnabled(false);
                Speech_Rate.setEnabled(false);
                test_voice.setEnabled(false);
            } else {
                Speech_Language.setEnabled(true);
                Speech_Rate.setEnabled(true);
                test_voice.setEnabled(true);
            }
        }
        return false;
    }

    private void StartVoiceService() {
        Thread t = new Thread() {
            public void run() {
                Log.d(TAG, "starting service");
                Intent intent = new Intent(getActivity(), ServiceTTS.class);
                //Objects.requireNonNull(getActivity()).bindService(intent, connection, Context.BIND_AUTO_CREATE);
                Objects.requireNonNull(getActivity()).startService(intent);
            }
        };
        t.start();
    }
}
