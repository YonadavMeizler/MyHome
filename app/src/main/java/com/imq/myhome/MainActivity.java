package com.imq.myhome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.imq.myhome.Fragments.FragmentDevicesList;
import com.imq.myhome.Fragments.FragmentSettings;
import com.imq.myhome.Interfaces.FragmentListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FragmentListener {

    private static final String TAG = "MyHome";

    private FloatingActionButton fab_menu, fab_settings, fab_devices;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Application started!");


        fab_menu = findViewById(R.id.fab_menu);
        fab_settings = findViewById(R.id.fab_settings);
        fab_devices = findViewById(R.id.fab_devices);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fab_menu.setOnClickListener(this);
        fab_settings.setOnClickListener(this);
        fab_devices.setOnClickListener(this);

        FragmentDevicesList MainFragment = new FragmentDevicesList();
        MainFragment.setOnFragmentListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, MainFragment).commit();

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
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    private void animateFab() {
        if (isOpen) {
            fab_menu.startAnimation(rotateForward);
            fab_settings.startAnimation(fabClose);
            fab_devices.startAnimation(fabClose);
            fab_settings.setClickable(false);
            fab_devices.setClickable(false);
            isOpen = false;
        } else {
            fab_menu.startAnimation(rotateBackward);
            fab_settings.startAnimation(fabOpen);
            fab_devices.startAnimation(fabOpen);
            fab_settings.setClickable(true);
            fab_devices.setClickable(true);
            isOpen = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_menu:
                animateFab();
                break;
            case R.id.fab_settings:
                FragmentSettings SettingsFragment = new FragmentSettings();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, SettingsFragment).addToBackStack(null).commit();
                animateFab();
                Log.d(TAG, "Setting started");
                break;
            case R.id.fab_devices:
                FragmentDevicesList DevicesFragment = new FragmentDevicesList();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, DevicesFragment).addToBackStack(null).commit();
                DevicesFragment.setOnFragmentListener(this);
                animateFab();
                Log.d(TAG, "Alerts started");
                break;
            default:
        }
    }

    @Override
    public void onFragmentSwap(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
