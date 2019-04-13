package com.imq.myhome.Objects;

public class Device {
    private String Device_ID;
    private String Device_Name;
    private String Device_Class;
    private Integer Device_Value;

    private Integer Device_Image;

    public String getDevice_Name() {
        return Device_Name;
    }

    public void setDevice_Name(String Device_Name) {
        this.Device_Name = Device_Name;
    }

    public String getDevice_Class() {
        return Device_Class;
    }

    public void setDevice_Class(String Device_Class) {
        this.Device_Class = Device_Class;
    }

    public Integer getDevice_Value() {
        return Device_Value;
    }

    public void setDevice_Value(Integer Device_Value) {
        this.Device_Value = Device_Value;
    }

    public Integer getDevice_Image() {
        return Device_Image;
    }

    public void setDevice_Image(Integer Device_Image) {
        this.Device_Image = Device_Image;
    }

    public String getDevice_ID() {
        return Device_ID;
    }

    public void setDevice_ID(String Device_ID) {
        this.Device_ID = Device_ID;
    }
}
