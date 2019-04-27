package com.imq.myhome.Fragments;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imq.myhome.Auxiliary.AuxiliaryBluetooth;
import com.imq.myhome.R;
import com.imq.myhome.Services.ServiceBluetooth;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Objects;

public class FragmentDeviceInfo extends Fragment implements View.OnClickListener {
    // Debugging
    private static final String TAG = "R2Y2";

    public final static String ARG_DEVICEPOSITION = "position";
    public final static String ARG_DEVICE = "device";
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private TextView Console_output, BlueBonded;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            if (msg.what == 1) {
                // Update view component text, this is allowed.
                Console_output.append("\r\n" + msg.obj);
            }
        }
    };

    private BluetoothDevice mDevice;
    private final BroadcastReceiver mBluetoothStateBond = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String Bondstate = BTAux.getDevice_BondState(device);
                    BlueBonded.setText(Bondstate);
                    Console_output.append("\r\n" + Bondstate);
                }
            }
        }

    };


    private AuxiliaryBluetooth BTAux = new AuxiliaryBluetooth();
    private EditText Message;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mBluetoothStateBond);
    }

    public FragmentDeviceInfo() {
    }

    //private ServiceBluetooth mBluetoothConnection;
    private ServiceBluetooth mBluetoothConnection;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device_control, container, false);

        IntentFilter BTIntent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        Objects.requireNonNull(getActivity()).registerReceiver(mBluetoothStateBond, BTIntent);

        // int mDevicePosition = getArguments().getInt(ARG_DEVICEPOSITION);
        if (getArguments() != null) {
            mDevice = getArguments().getParcelable(ARG_DEVICE);
            set_view(v);
            mBluetoothConnection = new ServiceBluetooth(mHandler);

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
        Message = v.findViewById(R.id.DeviceEditTextMessage);
        ImageView BlueImage = v.findViewById(R.id.DeviceImage);
        Button BlueButton1 = v.findViewById(R.id.DeviceButtonPair);
        Button BlueButton2 = v.findViewById(R.id.DeviceButtonUnPair);
        Button BlueButton3 = v.findViewById(R.id.DeviceButtonConnection);
        Button BlueButton4 = v.findViewById(R.id.DeviceButtonSendMessage);
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
        BlueButton4.setOnClickListener(this);
    }

    private void start_bluetooth_connection() {
        mBluetoothConnection.startClient(mDevice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DeviceButtonPair:
                Console_output.append("\r\nPair device");
                // Check that we're actually connected before trying anything
                bluetoothAdapter.cancelDiscovery();
                mDevice.createBond();

                break;
            case R.id.DeviceButtonUnPair:
                Console_output.append("\r\nUnpair device");
                bluetoothAdapter.cancelDiscovery();
                try {
                    Method m = mDevice.getClass().getMethod("removeBond", (Class[]) null);
                    m.invoke(mDevice, (Object[]) null);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
                break;
            case R.id.DeviceButtonConnection:
                Console_output.append("\r\nStart bluetooth connection");
                start_bluetooth_connection();
                Console_output.append("\r\nbluetooth connection started!");
                break;
            case R.id.DeviceButtonSendMessage:

                String Text = Message.getText().toString();
                //Console_output.append("\r\nSending message: " + Text);
                byte[] bytes = Text.getBytes(Charset.defaultCharset());

                //tringBuilder SB = new StringBuilder();
                mBluetoothConnection.write(bytes);

                break;
            default:
                break;
        }
    }
}
