package com.imq.myhome;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.imq.myhome.Auxiliary.AuxiliaryBluetooth;
import com.imq.myhome.Auxiliary.AuxiliaryGeneral;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String TAG = "R2Y2";
    private static final int REQUEST_ENABLE_BT = 201;
    private static final int REQUEST_SPEECH_RECOGNIZER = 202;
    private BluetoothAdapter bluetoothAdapter;
    private ImageView logo;
    private TextView user_Name;
    private AuxiliaryBluetooth BTAux = new AuxiliaryBluetooth();
    private AuxiliaryGeneral GAux;
    private TextToSpeech mTts;

    private final BroadcastReceiver mBluetoothStateReciver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    BTAux.get_state(state);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d(TAG, "This device does not support Bluetooth");
            finish();
        }

        Log.d(TAG, "Application started!");
        Set_View();
        if (GAux.isVoiceEnable()) {
            Start_voice_greeting();
        }
    }


    private void Set_View() {
        TextView user_greeting = findViewById(R.id.Splash_screen_greeting);
        user_Name = findViewById(R.id.Splash_screen_name);
        Switch bluetooth_enable = findViewById(R.id.switch_bluetooth);
        logo = findViewById(R.id.Splash_Screen_Logo);

        run_animation();
        GAux = new AuxiliaryGeneral(this);

        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothStateReciver, BTIntent);

        user_Name.setText(GAux.Name(2));
        user_greeting.setText(GAux.Greeting());
        bluetooth_enable.setOnCheckedChangeListener(this);
        logo.setOnClickListener(this);

        if (!bluetoothAdapter.isEnabled()) {
            bluetooth_enable.setChecked(false);
        } else {
            bluetooth_enable.setChecked(true);
            Start_Application();
        }

        mTts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTts.setLanguage(GAux.getLocal());
                    mTts.setPitch(1.1f);
                }
            }
        });

    }


    private void startSpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Who are you?");
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                user_Name.setText(results.get(0));
            }
        }
    }

    private void run_animation() {
        Animation mScaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Animation mRotateAnimation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        AnimationSet mAnimations = new AnimationSet(true);
        mAnimations.setDuration(1000);
        mAnimations.setFillAfter(true);
        mAnimations.setInterpolator(new OvershootInterpolator(1.5f));
        mAnimations.addAnimation(mScaleAnimation);
        mAnimations.addAnimation(mRotateAnimation);

        logo.startAnimation(mAnimations);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBluetoothStateReciver);
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            Start_Application();
        } else {
            bluetoothAdapter.disable();
        }
    }

    private void Start_Application() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void Start_voice_greeting() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String text;
                if (bluetoothAdapter.isEnabled()) {
                    text = GAux.Greeting() + " " + GAux.Name(1);
                } else {
                    text = GAux.Greeting() + " " + GAux.Name(1) + ", Please Enable Bluetooth";
                }

                mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "myTTS");
            }
        }, 200);
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "Image Clicked!");
        run_animation();
    }
}
