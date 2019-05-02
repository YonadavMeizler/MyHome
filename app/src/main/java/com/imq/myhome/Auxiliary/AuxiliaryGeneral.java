package com.imq.myhome.Auxiliary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

    public Locale getLocal() {
        String local = Objects.requireNonNull(shared_preferences.getString("Speech_Language", "Locale.CANADA"));
        switch (local) {
            case "Locale.CANADA":
                return Locale.CANADA;
            case "Locale.CANADA_FRENCH":
                return Locale.CANADA_FRENCH;
            case "Locale.CHINA":
                return Locale.CHINA;
            case "Locale.CHINESE":
                return Locale.CHINESE;
            case "Locale.ENGLISH":
                return Locale.ENGLISH;
            case "Locale.FRANCE":
                return Locale.FRANCE;
            case "Locale.FRENCH":
                return Locale.FRENCH;
            case "Locale.GERMAN":
                return Locale.GERMAN;
            case "Locale.GERMANY":
                return Locale.GERMANY;
            case "Locale.ITALIAN":
                return Locale.ITALIAN;
            case "Locale.JAPAN":
                return Locale.JAPAN;
            case "Locale.JAPANESE":
                return Locale.JAPANESE;
            case "Locale.KOREA":
                return Locale.KOREA;
            case "Locale.KOREAN":
                return Locale.KOREAN;
            case "Locale.PRC":
                return Locale.PRC;
            case "Locale.ROOT":
                return Locale.ROOT;
            case "Locale.SIMPLIFIED_CHINESE":
                return Locale.SIMPLIFIED_CHINESE;
            case "Locale.TAIWAN":
                return Locale.TAIWAN;
            case "Locale.TRADITIONAL_CHINESE":
                return Locale.TRADITIONAL_CHINESE;
            case "Locale.UK":
                return Locale.UK;
            case "Locale.US":
                return Locale.US;
            default:
                return Locale.CANADA;
        }
    }

    public float getSpeechRate() {
        String Speech_Rate = Objects.requireNonNull(shared_preferences.getString("Speech_Rate", "1.1f"));
        return Float.valueOf(Speech_Rate);
    }

    public boolean isVoiceEnable() {
        return shared_preferences.getBoolean("Speech_guidence", true);
    }
}
