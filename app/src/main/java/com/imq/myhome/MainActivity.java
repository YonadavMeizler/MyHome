package com.imq.myhome;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.imq.myhome.Auxiliary.AuxiliaryBluetooth;
import com.imq.myhome.Fragments.FragmentDevicesList;
import com.imq.myhome.Fragments.FragmentSettings;
import com.imq.myhome.Interfaces.FragmentListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FragmentListener {

    private static final String TAG = "R2Y2";
    private static final int REQUEST_ENABLE_BT = 201;
    private final BroadcastReceiver mBluetoothDiscoveringReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(TAG, "device Name: " + deviceName + " device Address: " + deviceHardwareAddress);
            }
        }
    };
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen = false;
    private FloatingActionButton fab_menu, fab_settings, fab_devices, fab_enable_disable_bluetooth, fab_scan;
    private BluetoothAdapter bluetoothAdapter;
    private AuxiliaryBluetooth BTAux = new AuxiliaryBluetooth();
    private final BroadcastReceiver mBluetoothStateReciver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                BTAux.get_state(state);
            }
        }
    };
    private final BroadcastReceiver mBluetoothScanMode = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                BTAux.get_scan_mode(state);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Application started!");


        fab_menu = findViewById(R.id.fab_menu);
        fab_settings = findViewById(R.id.fab_settings);
        fab_devices = findViewById(R.id.fab_devices);
        fab_enable_disable_bluetooth = findViewById(R.id.fab_enable_disable_bluetooth);
        fab_scan = findViewById(R.id.fab_discover);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d(TAG, "This device does not support Bluetooth");
            finish();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                fab_enable_disable_bluetooth.setImageResource(R.drawable.ic_bluetooth_disabled);
            } else {
                fab_enable_disable_bluetooth.setImageResource(R.drawable.ic_bluetooth);
            }
        }

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fab_menu.setOnClickListener(this);
        fab_settings.setOnClickListener(this);
        fab_devices.setOnClickListener(this);
        fab_enable_disable_bluetooth.setOnClickListener(this);
        fab_scan.setOnClickListener(this);

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
            fab_enable_disable_bluetooth.startAnimation(fabClose);
            fab_scan.startAnimation(fabClose);
            fab_scan.setClickable(false);
            fab_enable_disable_bluetooth.setClickable(false);
            fab_settings.setClickable(false);
            fab_devices.setClickable(false);
            isOpen = false;
        } else {
            fab_menu.startAnimation(rotateBackward);
            fab_settings.startAnimation(fabOpen);
            fab_devices.startAnimation(fabOpen);
            fab_enable_disable_bluetooth.startAnimation(fabOpen);
            fab_scan.startAnimation(fabOpen);
            fab_scan.setClickable(true);
            fab_enable_disable_bluetooth.setClickable(true);
            fab_settings.setClickable(true);
            fab_devices.setClickable(true);
            isOpen = true;
        }
    }

    @Override
    public void onClick(View view) {
        FragmentDevicesList DevicesFragment = new FragmentDevicesList();
        FragmentSettings SettingsFragment = new FragmentSettings();
        switch (view.getId()) {
            case R.id.fab_menu:
                animateFab();
                break;
            case R.id.fab_settings:

                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, SettingsFragment).addToBackStack(null).commit();
                animateFab();
                Log.d(TAG, "Setting started");
                break;
            case R.id.fab_devices:
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, DevicesFragment).addToBackStack(null).commit();
                DevicesFragment.setOnFragmentListener(this);
                animateFab();
                Log.d(TAG, "Device List started");
                break;
            case R.id.fab_enable_disable_bluetooth:
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    registerReceiver(mBluetoothStateReciver, BTIntent);
                    fab_enable_disable_bluetooth.setImageResource(R.drawable.ic_bluetooth);

                } else {
                    bluetoothAdapter.disable();

                    IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    registerReceiver(mBluetoothStateReciver, BTIntent);
                    fab_enable_disable_bluetooth.setImageResource(R.drawable.ic_bluetooth_disabled);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, DevicesFragment).addToBackStack(null).commit();
                DevicesFragment.setOnFragmentListener(this);
                animateFab();
                Log.d(TAG, "Enabling/Disabling Bluetooth");
                break;

            case R.id.fab_discover:

                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BluetoothAdapter.ERROR);
                startActivityForResult(discoverableIntent, REQUEST_ENABLE_BT);

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBluetoothScanMode, BTIntent);
                animateFab();
                Log.d(TAG, "Making device discoverable for 120 second");

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
