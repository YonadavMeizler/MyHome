package com.imq.myhome.Fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imq.myhome.Adapters.AdapterDevice;
import com.imq.myhome.Auxiliary.AuxiliaryBluetooth;
import com.imq.myhome.Interfaces.DeviceEditListener;
import com.imq.myhome.Interfaces.FragmentListener;
import com.imq.myhome.Objects.Device;
import com.imq.myhome.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class FragmentDevicesList extends Fragment implements DeviceEditListener {


    private static final String TAG = "MyHome";

    private static final int REQUEST_ENABLE_BT = 201;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
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
    private AuxiliaryBluetooth AUXBlue = new AuxiliaryBluetooth();
    private FragmentListener mListener;
    private AdapterDevice adapter;

    public FragmentDevicesList() {
    }

    public void setOnFragmentListener(FragmentListener onFragmentSwap) {
        this.mListener = onFragmentSwap;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_devices, container, false);

        Log.d(TAG, "My Home is up! starting to show devices.");

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);

        ArrayList<Device> devices = prepareData();

        RecyclerView recyclerView = v.findViewById(R.id.Device_List);
        adapter = new AdapterDevice(v.getContext(), devices);
        adapter.setOnEditDeviceListener(this);
        Log.d(TAG, "Number of Device detected: " + adapter.getItemCount());

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
    }

    private ArrayList<Device> prepareData() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ArrayList<Device> Devices = new ArrayList<>();
        if (bluetoothAdapter == null) {
            Log.d(TAG, "This device does not support Bluetooth");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    BluetoothClass BClass = device.getBluetoothClass();
                    Device device1 = new Device();
                    device1.setDevice_Name(device.getName());
                    device1.setDevice_Class(BClass);
                    device1.setDevice_Address(device.getAddress());
                    device1.setDevice_Type(device.getType());
                    device1.setDevice_BondState(device.getBondState());
                    device1.setDevice_Image(AUXBlue.getDevice_image(BClass));
                    Devices.add(device1);
                }
            }
        }
        return Devices;
    }

    @Override
    public void onEditClick(int position, Device device) {

        FragmentDeviceInfo Fragment = new FragmentDeviceInfo();
        Bundle args = new Bundle();
        args.putInt(FragmentDeviceInfo.ARG_DEVICEPOSITION, position);
        args.putParcelable(FragmentDeviceInfo.ARG_DEVICE, device);
        Fragment.setArguments(args);
        if (mListener != null) {
            mListener.onFragmentSwap(Fragment);
        } else {
            Log.d(TAG, "mListner is null");
        }

    }
}
