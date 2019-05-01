package com.imq.myhome.Auxiliary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AuxiliaryGeneral {

    private static final int HONORIFIC_PLUS_LAST_NAME = 1;
    private static final int HONORIFIC_PLUS_LAST_AND_FIRST_NAME = 2;
    private static final int LAST_NAME = 3;
    private static final int FIRST_NAME = 4;
    private static final int LAST_AND_FIRST_NAME = 5;

    private Context mContext;
    private SharedPreferences shared_preferences;

    public AuxiliaryGeneral(Context context) {
        this.mContext = context;
        this.shared_preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public boolean isVoiceEnable() {
        return shared_preferences.getBoolean("Speech_guidence", true);
    }

    public String Name(int type) {
        //get app preference
        String user_name_first_pref = Objects.requireNonNull(shared_preferences.getString("user_name_first", ""));
        String user_name_last_pref = Objects.requireNonNull(shared_preferences.getString("user_name_last", ""));
        String user_honorifics_pref = Objects.requireNonNull(shared_preferences.getString("user_honorifics", ""));


        switch (type) {
            case HONORIFIC_PLUS_LAST_NAME:
                return user_honorifics_pref + " " + user_name_last_pref;
            case HONORIFIC_PLUS_LAST_AND_FIRST_NAME:
                return user_honorifics_pref + " " + user_name_first_pref + " " + user_name_last_pref;
            case LAST_NAME:
                return user_name_last_pref;
            case FIRST_NAME:
                return user_name_first_pref;
            case LAST_AND_FIRST_NAME:
                return user_name_first_pref + " " + user_name_last_pref;
            default:
                return user_honorifics_pref + " " + user_name_last_pref;
        }
        /*if (user_name_pref.equals("")) {
            startSpeechRecognizer();
        } else {

        }*/

    }

    public String Greeting() {
        String greeting;
        //Get the time of day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        //Set greeting
        if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening";
        } else if (hour >= 21 && hour < 24) {
            greeting = "Good Night";
        } else {
            greeting = "Good Morning";
        }
        return greeting;
    }
}
