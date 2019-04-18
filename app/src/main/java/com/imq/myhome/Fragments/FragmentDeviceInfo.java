package com.imq.myhome.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imq.myhome.Auxiliary.AuxiliaryBluetooth;
import com.imq.myhome.Objects.Device;
import com.imq.myhome.R;

public class FragmentDeviceInfo extends Fragment {

    public final static String ARG_DEVICEPOSITION = "position";
    public final static String ARG_DEVICE = "device";

    public FragmentDeviceInfo() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device_control, container, false);
        AuxiliaryBluetooth AuxBlue = new AuxiliaryBluetooth();

        TextView BlueName = v.findViewById(R.id.DeviceNameControl);
        TextView BlueAddress = v.findViewById(R.id.DeviceAddressControl);
        TextView BlueBonded = v.findViewById(R.id.DeviceBondedControl);
        TextView BlueType = v.findViewById(R.id.DeviceTypeControl);
        TextView BlueClass = v.findViewById(R.id.DeviceClassControl);
        TextView BlueMajorClass = v.findViewById(R.id.DeviceMajorClassControl);
        ImageView BlueImage = v.findViewById(R.id.DeviceImage);


        Device mDevice = getArguments().getParcelable(ARG_DEVICE);
        // int mDevicePosition = getArguments().getInt(ARG_DEVICEPOSITION);

        BlueName.setText(mDevice.getDevice_Name());
        BlueAddress.setText(mDevice.getDevice_Address());
        BlueBonded.setText(AuxBlue.getDevice_BondState(mDevice.getDevice_BondState()));
        BlueType.setText(AuxBlue.getDevice_Type(mDevice.getDevice_Type()));
        BlueClass.setText(AuxBlue.getDevice_Class(mDevice.getDevice_Class()));
        BlueMajorClass.setText(AuxBlue.getDevice_MajorClass(mDevice.getDevice_Class()));

        BlueImage.setImageResource(mDevice.getDevice_Image());
        BlueImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return v;

    }
}
