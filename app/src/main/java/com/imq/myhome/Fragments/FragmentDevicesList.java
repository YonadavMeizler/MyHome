package com.imq.myhome.Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
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
import com.imq.myhome.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class FragmentDevicesList extends Fragment implements DeviceEditListener {

    // Debugging
    private static final String TAG = "R2Y2";

    private static final int REQUEST_ENABLE_BT = 201;
    private AdapterDevice adapter;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private AuxiliaryBluetooth AUXBlue = new AuxiliaryBluetooth();
    private ArrayList<BluetoothDevice> Devices = new ArrayList<>();

    private final BroadcastReceiver mBluetoothDiscoveringReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!checkDeviceInTheList(device)) {
                    Devices.add(device);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    private FragmentListener mListener;


    public FragmentDevicesList() {
    }

    public void setOnFragmentListener(FragmentListener onFragmentSwap) {
        this.mListener = onFragmentSwap;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_devices, container, false);
        Devices = new ArrayList<>();
        prepareData();

        RecyclerView recyclerView = v.findViewById(R.id.Device_List);
        adapter = new AdapterDevice(v.getContext(), Devices);
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
        Objects.requireNonNull(getActivity()).unregisterReceiver(mBluetoothDiscoveringReceiver);
    }

    private void prepareData() {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "This device does not support Bluetooth");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            Scan_Unpaired_devices();
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    Devices.add(device);
                }
            }
        }

    }

    private void Scan_Unpaired_devices() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "Canceling Discovery.");
        }
        checkBTpermission();
        bluetoothAdapter.startDiscovery();
        IntentFilter BTIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mBluetoothDiscoveringReceiver, BTIntent);
        Log.d(TAG, "Scaning for unpaired devices");
    }

    private void checkBTpermission() {
        int permissionCheck = Objects.requireNonNull(getActivity()).checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        permissionCheck += getActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        if (permissionCheck != 0) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    private boolean checkDeviceInTheList(BluetoothDevice DeviceToFind) {
        if (Devices.contains(DeviceToFind)) {
            Log.d(TAG, "device " + DeviceToFind.getName() + " is already in the list");
            return true;
        } else {
            Log.d(TAG, "device " + DeviceToFind.getName() + " is not in the list");
            return false;
        }
    }

    @Override
    public void onEditClick(int position, BluetoothDevice device) {

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
