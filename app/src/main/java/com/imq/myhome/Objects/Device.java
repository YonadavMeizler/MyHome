package com.imq.myhome.Objects;

import android.bluetooth.BluetoothClass;
import android.os.Parcel;
import android.os.Parcelable;

import com.imq.myhome.R;

public class Device implements Parcelable {
    private String Device_Name;
    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
    private BluetoothClass Device_Class;
    private String Device_Address;
    private Integer Device_Type;
    private Integer Device_BondState;
    private Integer Device_Image = R.drawable.ic_lightbulb;

    public Device() {
    }

    private Device(Parcel in) {
        Device_Name = in.readString();
        Device_Class = in.readParcelable(BluetoothClass.class.getClassLoader());
        Device_Address = in.readString();
        if (in.readByte() == 0) {
            Device_Type = null;
        } else {
            Device_Type = in.readInt();
        }
        if (in.readByte() == 0) {
            Device_BondState = null;
        } else {
            Device_BondState = in.readInt();
        }
        if (in.readByte() == 0) {
            Device_Image = null;
        } else {
            Device_Image = in.readInt();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Device_Name);
        dest.writeParcelable(Device_Class, flags);
        dest.writeString(Device_Address);
        if (Device_Type == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Device_Type);
        }
        if (Device_BondState == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Device_BondState);
        }
        if (Device_Image == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Device_Image);
        }
    }

    public String getDevice_Name() {
        return Device_Name;
    }

    public void setDevice_Name(String device_Name) {
        Device_Name = device_Name;
    }

    public BluetoothClass getDevice_Class() {
        return Device_Class;
    }

    public void setDevice_Class(BluetoothClass device_Class) {
        Device_Class = device_Class;
    }

    public String getDevice_Address() {
        return Device_Address;
    }

    public void setDevice_Address(String device_Address) {
        Device_Address = device_Address;
    }

    public Integer getDevice_Type() {
        return Device_Type;
    }

    public void setDevice_Type(Integer device_type) {
        Device_Type = device_type;
    }

    public Integer getDevice_BondState() {
        return Device_BondState;
    }

    public void setDevice_BondState(Integer device_BondState) {
        Device_BondState = device_BondState;
    }

    public Integer getDevice_Image() {
        return Device_Image;
    }

    public void setDevice_Image(Integer device_Image) {
        Device_Image = device_Image;
    }


}
