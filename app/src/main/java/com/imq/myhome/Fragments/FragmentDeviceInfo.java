package com.imq.myhome.Fragments;

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
import com.imq.myhome.Objects.Device;
import com.imq.myhome.R;
import com.imq.myhome.Services.ServiceBluetooth;

public class FragmentDeviceInfo extends Fragment implements View.OnClickListener {

    private static final String TAG = "R2Y2";
    public final static String ARG_DEVICEPOSITION = "position";
    public final static String ARG_DEVICE = "device";
    private TextView Console_output;
    private Device mDevice;
    private ServiceBluetooth mBlueService = null;

    public FragmentDeviceInfo() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device_control, container, false);

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
        TextView BlueBonded = v.findViewById(R.id.DeviceBondedControl);
        TextView BlueType = v.findViewById(R.id.DeviceTypeControl);
        TextView BlueClass = v.findViewById(R.id.DeviceClassControl);
        TextView BlueMajorClass = v.findViewById(R.id.DeviceMajorClassControl);
        ImageView BlueImage = v.findViewById(R.id.DeviceImage);
        Button BlueButton1 = v.findViewById(R.id.DeviceButton);
        Button BlueButton2 = v.findViewById(R.id.DeviceButton2);
        Button BlueButton3 = v.findViewById(R.id.DeviceButton3);
        Console_output = v.findViewById(R.id.DeviceConsole);


        // setting initial data
        AuxiliaryBluetooth AuxBlue = new AuxiliaryBluetooth();

        Console_output.setMovementMethod(new ScrollingMovementMethod());
        BlueName.setText(mDevice.getDevice_Name());
        BlueAddress.setText(mDevice.getDevice_Address());
        BlueBonded.setText(AuxBlue.getDevice_BondState(mDevice.getDevice_BondState()));
        BlueType.setText(AuxBlue.getDevice_Type(mDevice.getDevice_Type()));
        BlueClass.setText(AuxBlue.getDevice_Class(mDevice.getDevice_Class()));
        BlueMajorClass.setText(AuxBlue.getDevice_MajorClass(mDevice.getDevice_Class()));
        BlueImage.setImageResource(mDevice.getDevice_Image());
        BlueImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        BlueButton1.setOnClickListener(this);
        BlueButton2.setOnClickListener(this);
        BlueButton3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DeviceButton:
                Console_output.append("\r\nButton 1 pressed");
                // Check that we're actually connected before trying anything

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
