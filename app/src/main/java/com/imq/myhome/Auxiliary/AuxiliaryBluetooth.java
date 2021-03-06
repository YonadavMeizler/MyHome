package com.imq.myhome.Auxiliary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.imq.myhome.R;

public class AuxiliaryBluetooth {

    private static final String TAG = "R2Y2";

    public AuxiliaryBluetooth() {
    }

    public String getDevice_BondState(BluetoothDevice device) {
        int bondstate = device.getBondState();
        String BondStateString = "";
        switch (bondstate) {
            case BluetoothDevice.BOND_BONDED:
                BondStateString = "Bonded - Paired";
                break;
            case BluetoothDevice.BOND_BONDING:
                BondStateString = "Bonding - Pairing";
                break;
            case BluetoothDevice.BOND_NONE:
                BondStateString = "Not Bonded - Not Paired";
                break;
            default:
                break;
        }
        Log.d(TAG, "Device Bond state: " + BondStateString);
        return BondStateString;
    }

    public String getDevice_Type(BluetoothDevice device) {
        int bondtype = device.getType();
        String TypeString = "";
        switch (bondtype) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                TypeString = "Bluetooth device type, Classic - BR/EDR devices";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                TypeString = "Bluetooth device type, Dual Mode - BR/EDR/LE";
                break;
            case BluetoothDevice.DEVICE_TYPE_LE:
                TypeString = "Bluetooth device type, Low Energy - LE-only";
                break;
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                TypeString = "Bluetooth device type, Unknown";
                break;
            default:
                break;
        }
        Log.d(TAG, "Device Type: " + TypeString);
        return TypeString;
    }

    public int getDevice_image(BluetoothDevice device) {
        BluetoothClass device_class = device.getBluetoothClass();
        int Image_res;
        switch (device_class.getDeviceClass()) {

            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER:
                Image_res = R.drawable.ic_camera;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                Image_res = R.drawable.ic_car;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                Image_res = R.drawable.ic_phone_bluetooth;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
                Image_res = R.drawable.ic_headset;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO:
                Image_res = R.drawable.ic_equalizer;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
                Image_res = R.drawable.ic_speaker;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE:
                Image_res = R.drawable.ic_mic;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
                Image_res = R.drawable.ic_play;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
                Image_res = R.drawable.ic_tv;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED:
                Image_res = R.drawable.ic_mic;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VCR:
                Image_res = R.drawable.ic_tv;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA:
                Image_res = R.drawable.ic_camera;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
                Image_res = R.drawable.ic_people;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                Image_res = R.drawable.ic_speaker;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY:
                Image_res = R.drawable.ic_toys;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
                Image_res = R.drawable.ic_tv;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                Image_res = R.drawable.ic_headset;
                break;
            case BluetoothClass.Device.COMPUTER_DESKTOP:
                Image_res = R.drawable.ic_tv;
                break;
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
                Image_res = R.drawable.ic_phone;
                break;
            case BluetoothClass.Device.COMPUTER_LAPTOP:
                Image_res = R.drawable.ic_laptop;
                break;
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
                Image_res = R.drawable.ic_phone;
                break;
            case BluetoothClass.Device.COMPUTER_SERVER:
                Image_res = R.drawable.ic_router;
                break;
            case BluetoothClass.Device.COMPUTER_UNCATEGORIZED:
                Image_res = R.drawable.ic_tv;
                break;
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                Image_res = R.drawable.ic_watch;
                break;
            case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE:
                Image_res = R.drawable.ic_health;
                break;
            case BluetoothClass.Device.HEALTH_DATA_DISPLAY:
                Image_res = R.drawable.ic_health;
                break;
            case BluetoothClass.Device.HEALTH_GLUCOSE:
                Image_res = R.drawable.ic_health;
                break;
            case BluetoothClass.Device.HEALTH_PULSE_OXIMETER:
                Image_res = R.drawable.ic_health;
                break;
            case BluetoothClass.Device.HEALTH_PULSE_RATE:
                Image_res = R.drawable.ic_health;
                break;
            case BluetoothClass.Device.HEALTH_THERMOMETER:
                Image_res = R.drawable.ic_ac;
                break;
            case BluetoothClass.Device.HEALTH_UNCATEGORIZED:
                Image_res = R.drawable.ic_health;
                break;
            case BluetoothClass.Device.HEALTH_WEIGHING:
                Image_res = R.drawable.ic_health;
                break;
            case BluetoothClass.Device.PHONE_CELLULAR:
                Image_res = R.drawable.ic_phone;
                break;
            case BluetoothClass.Device.PHONE_CORDLESS:
                Image_res = R.drawable.ic_phone_bluetooth;
                break;
            case BluetoothClass.Device.PHONE_ISDN:
                Image_res = R.drawable.ic_phone;
                break;
            case BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY:
                Image_res = R.drawable.ic_phone;
                break;
            case BluetoothClass.Device.PHONE_SMART:
                Image_res = R.drawable.ic_phone;
                break;
            case BluetoothClass.Device.PHONE_UNCATEGORIZED:
                Image_res = R.drawable.ic_phone;
                break;
            case BluetoothClass.Device.TOY_CONTROLLER:
                Image_res = R.drawable.ic_toys;
                break;
            case BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE:
                Image_res = R.drawable.ic_toys;
                break;
            case BluetoothClass.Device.TOY_GAME:
                Image_res = R.drawable.ic_toys;
                break;
            case BluetoothClass.Device.TOY_ROBOT:
                Image_res = R.drawable.ic_android;
                break;
            case BluetoothClass.Device.TOY_UNCATEGORIZED:
                Image_res = R.drawable.ic_toys;
                break;
            case BluetoothClass.Device.TOY_VEHICLE:
                Image_res = R.drawable.ic_toys;
                break;
            case BluetoothClass.Device.WEARABLE_GLASSES:
                Image_res = R.drawable.ic_transfer;
                break;
            case BluetoothClass.Device.WEARABLE_HELMET:
                Image_res = R.drawable.ic_transfer;
                break;
            case BluetoothClass.Device.WEARABLE_JACKET:
                Image_res = R.drawable.ic_transfer;
                break;
            case BluetoothClass.Device.WEARABLE_PAGER:
                Image_res = R.drawable.ic_transfer;
                break;
            case BluetoothClass.Device.WEARABLE_UNCATEGORIZED:
                Image_res = R.drawable.ic_transfer;
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                Image_res = R.drawable.ic_watch;
                break;
            default:
                Image_res = R.drawable.ic_lightbulb;
                break;
        }
        return Image_res;
    }

    public String getDevice_Class(BluetoothDevice device) {
        BluetoothClass device_class = device.getBluetoothClass();
        String ClassString;
        switch (device_class.getDeviceClass()) {
            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER:
                ClassString = "AUDIO VIDEO CAMCORDER";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                ClassString = "AUDIO VIDEO CAR AUDIO";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                ClassString = "AUDIO VIDEO HANDSFREE";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
                ClassString = "AUDIO VIDEO HEADPHONES";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO:
                ClassString = "AUDIO VIDEO HIFI AUDIO";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
                ClassString = "AUDIO VIDEO LOUDSPEAKER";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE:
                ClassString = "AUDIO VIDEO MICROPHONE";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
                ClassString = "AUDIO VIDEO PORTABLE AUDIO";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
                ClassString = "AUDIO VIDEO SET TOP BOX";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED:
                ClassString = "AUDIO VIDEO UNCATEGORIZED";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VCR:
                ClassString = "AUDIO VIDEO VCR";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA:
                ClassString = "AUDIO VIDEO CAMERA";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
                ClassString = "AUDIO VIDEO CONFERENCING";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                ClassString = "UDIO VIDEO DISPLAY AND LOUDSPEAKER";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY:
                ClassString = "AUDIO VIDEO GAMING TOY";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
                ClassString = "AUDIO VIDEO MONITOR";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                ClassString = "AUDIO VIDEO WEARABLE HEADSET";
                break;
            case BluetoothClass.Device.COMPUTER_DESKTOP:
                ClassString = "COMPUTER DESKTOP";
                break;
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
                ClassString = "COMPUTER HANDHELD PC PDA";
                break;
            case BluetoothClass.Device.COMPUTER_LAPTOP:
                ClassString = "COMPUTER LAPTOP";
                break;
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
                ClassString = "COMPUTER PALM SIZE PC PDA";
                break;
            case BluetoothClass.Device.COMPUTER_SERVER:
                ClassString = "COMPUTER SERVER";
                break;
            case BluetoothClass.Device.COMPUTER_UNCATEGORIZED:
                ClassString = "COMPUTER UNCATEGORIZED";
                break;
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                ClassString = "COMPUTER WEARABLE";
                break;
            case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE:
                ClassString = "HEALTH BLOOD PRESSURE";
                break;
            case BluetoothClass.Device.HEALTH_DATA_DISPLAY:
                ClassString = "HEALTH DATA DISPLAY";
                break;
            case BluetoothClass.Device.HEALTH_GLUCOSE:
                ClassString = "HEALTH GLUCOSE";
                break;
            case BluetoothClass.Device.HEALTH_PULSE_OXIMETER:
                ClassString = "HEALTH PULSE OXIMETER";
                break;
            case BluetoothClass.Device.HEALTH_PULSE_RATE:
                ClassString = "HEALTH PULSE RATE";
                break;
            case BluetoothClass.Device.HEALTH_THERMOMETER:
                ClassString = "HEALTH THERMOMETER";
                break;
            case BluetoothClass.Device.HEALTH_UNCATEGORIZED:
                ClassString = "HEALTH UNCATEGORIZED";
                break;
            case BluetoothClass.Device.HEALTH_WEIGHING:
                ClassString = "HEALTH WEIGHING";
                break;
            case BluetoothClass.Device.PHONE_CELLULAR:
                ClassString = "PHONE CELLULAR";
                break;
            case BluetoothClass.Device.PHONE_CORDLESS:
                ClassString = "PHONE CORDLESS";
                break;
            case BluetoothClass.Device.PHONE_ISDN:
                ClassString = "PHONE ISDN";
                break;
            case BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY:
                ClassString = "PHONE MODEM OR GATEWAY";
                break;
            case BluetoothClass.Device.PHONE_SMART:
                ClassString = "PHONE SMART";
                break;
            case BluetoothClass.Device.PHONE_UNCATEGORIZED:
                ClassString = "PHONE UNCATEGORIZED";
                break;
            case BluetoothClass.Device.TOY_CONTROLLER:
                ClassString = "TOY CONTROLLER";
                break;
            case BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE:
                ClassString = "TOY DOLL ACTION FIGURE";
                break;
            case BluetoothClass.Device.TOY_GAME:
                ClassString = "TOY GAME";
                break;
            case BluetoothClass.Device.TOY_ROBOT:
                ClassString = "TOY ROBOT";
                break;
            case BluetoothClass.Device.TOY_UNCATEGORIZED:
                ClassString = "TOY UNCATEGORIZED";
                break;
            case BluetoothClass.Device.TOY_VEHICLE:
                ClassString = "TOY EHICLE";
                break;
            case BluetoothClass.Device.WEARABLE_GLASSES:
                ClassString = "WEARABLE GLASSES";
                break;
            case BluetoothClass.Device.WEARABLE_HELMET:
                ClassString = "WEARABLE HELMET";
                break;
            case BluetoothClass.Device.WEARABLE_JACKET:
                ClassString = "WEARABLE JACKET";
                break;
            case BluetoothClass.Device.WEARABLE_PAGER:
                ClassString = "WEARABLE PAGER";
                break;
            case BluetoothClass.Device.WEARABLE_UNCATEGORIZED:
                ClassString = "WEARABLE UNCATEGORIZED";
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                ClassString = "WEARABLE WRIST WATCH";
                break;
            default:
                ClassString = "UNCATEGORIZED";
                break;
        }
        Log.d(TAG, "Device Class: " + ClassString);
        return ClassString;
    }


    public String getDevice_MajorClass(BluetoothDevice device) {
        int Majorclass = device.getBluetoothClass().getMajorDeviceClass();
        String MajorClass;
        switch (Majorclass) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                MajorClass = "AUDIO VIDEO";
                break;
            case BluetoothClass.Device.Major.COMPUTER:
                MajorClass = "COMPUTER";
                break;
            case BluetoothClass.Device.Major.HEALTH:
                MajorClass = "HEALTH";
                break;
            case BluetoothClass.Device.Major.IMAGING:
                MajorClass = "IMAGING";
                break;
            case BluetoothClass.Device.Major.MISC:
                MajorClass = "MISC";
                break;
            case BluetoothClass.Device.Major.NETWORKING:
                MajorClass = "NETWORKING";
                break;
            case BluetoothClass.Device.Major.PERIPHERAL:
                MajorClass = "PERIPHERAL";
                break;
            case BluetoothClass.Device.Major.PHONE:
                MajorClass = "PHONE";
                break;
            case BluetoothClass.Device.Major.TOY:
                MajorClass = "TOY";
                break;
            case BluetoothClass.Device.Major.UNCATEGORIZED:
                MajorClass = "UNCATEGORIZED";
                break;
            case BluetoothClass.Device.Major.WEARABLE:
                MajorClass = "WEARABLE";
                break;
            default:
                MajorClass = "UNCATEGORIZED";
                break;
        }
        Log.d(TAG, "Device Major Class: " + MajorClass);
        return MajorClass;
    }

    public void get_state(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                Log.d(TAG, "STATE OFF");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Log.d(TAG, "STATE TURNING OFF");
                break;
            case BluetoothAdapter.STATE_ON:
                Log.d(TAG, "STATE ON");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                Log.d(TAG, "STATE TURNING ON");
                break;
            default:
                break;
        }
    }

    public void get_scan_mode(int state) {
        switch (state) {
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                Log.d(TAG, "Discoverability Enable, Able to receive connections.");
                break;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                Log.d(TAG, "Discoverability disable, Able to receive connections.");
                break;
            case BluetoothAdapter.SCAN_MODE_NONE:
                Log.d(TAG, "Discoverability disable, Not able to receive connections.");
                break;
            case BluetoothAdapter.STATE_CONNECTED:
                Log.d(TAG, "connected.");
                break;
            case BluetoothAdapter.STATE_CONNECTING:
                Log.d(TAG, "Connecting...");
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                Log.d(TAG, "Disconnected.");
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                Log.d(TAG, "Disconnecting...");
                break;
            default:
                break;
        }
    }
}
