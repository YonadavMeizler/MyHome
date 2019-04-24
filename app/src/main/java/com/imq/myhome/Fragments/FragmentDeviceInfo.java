package com.imq.myhome.Fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imq.myhome.Auxiliary.AuxiliaryBluetooth;
import com.imq.myhome.R;

import java.util.Objects;

public class FragmentDeviceInfo extends Fragment implements View.OnClickListener {

    private static final String TAG = "R2Y2";
    public final static String ARG_DEVICEPOSITION = "position";
    public final static String ARG_DEVICE = "device";
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private TextView Console_output, BlueBonded;
    private BluetoothDevice mDevice;

    private AuxiliaryBluetooth BTAux = new AuxiliaryBluetooth();

    private final BroadcastReceiver mBluetoothStateBond = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String Bondstate = BTAux.getDevice_BondState(device);
                BlueBonded.setText(Bondstate);
                Console_output.append("\r\n" + Bondstate);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mBluetoothStateBond);
    }

    public FragmentDeviceInfo() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device_control, container, false);

        IntentFilter BTIntent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(mBluetoothStateBond, BTIntent);

        // int mDevicePosition = getArguments().getInt(ARG_DEVICEPOSITION);
        if (getArguments() != null) {
            mDevice = getArguments().getParcelable(ARG_DEVICE);
            set_view(v);

        }

        return v;

    }

    private void set_view(View v) {
        //Find view elements
        TextView BlueName = v.findViewById(R.id.DeviceNameControl);
        TextView BlueAddress = v.findViewById(R.id.DeviceAddressControl);
        BlueBonded = v.findViewById(R.id.DeviceBondedControl);
        TextView BlueType = v.findViewById(R.id.DeviceTypeControl);
        TextView BlueClass = v.findViewById(R.id.DeviceClassControl);
        TextView BlueMajorClass = v.findViewById(R.id.DeviceMajorClassControl);
        ImageView BlueImage = v.findViewById(R.id.DeviceImage);
        Button BlueButton1 = v.findViewById(R.id.DeviceButtonPair);
        Button BlueButton2 = v.findViewById(R.id.DeviceButton2);
        Button BlueButton3 = v.findViewById(R.id.DeviceButton3);
        Console_output = v.findViewById(R.id.DeviceConsole);


        Console_output.setMovementMethod(new ScrollingMovementMethod());
        BlueName.setText(mDevice.getName());
        BlueAddress.setText(mDevice.getAddress());
        BlueBonded.setText(BTAux.getDevice_BondState(mDevice));
        BlueType.setText(BTAux.getDevice_Type(mDevice));
        BlueClass.setText(BTAux.getDevice_Class(mDevice));
        BlueMajorClass.setText(BTAux.getDevice_MajorClass(mDevice));
        BlueImage.setImageResource(BTAux.getDevice_image(mDevice));
        BlueImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        BlueButton1.setOnClickListener(this);
        BlueButton2.setOnClickListener(this);
        BlueButton3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DeviceButtonPair:
                Console_output.append("\r\nPairing device");
                // Check that we're actually connected before trying anything
                bluetoothAdapter.cancelDiscovery();
                mDevice.createBond();

                break;
            case R.id.DeviceButton2:
                Console_output.append("\r\nButton 2 pressed");
                break;
            case R.id.DeviceButton3:
                Console_output.append("\r\nButton 3 pressed");
                break;
            default:
                break;
        }
    }
}
