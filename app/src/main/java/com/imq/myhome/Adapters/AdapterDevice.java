package com.imq.myhome.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.imq.myhome.Objects.Device;
import com.imq.myhome.R;

import java.util.ArrayList;

public class AdapterDevice extends RecyclerView.Adapter<AdapterDevice.ViewHolder> {
    private static final String TAG = "myHome";
    private ArrayList<Device> mDevice;
    private Context mContext;

    public AdapterDevice(Context c, ArrayList<Device> devices) {
        mContext = c;
        mDevice = devices;
    }

    @NonNull
    @Override
    public AdapterDevice.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, viewGroup, false);
        return new AdapterDevice.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.Device_image.setImageResource((mDevice.get(i).getDevice_Image()));
        viewHolder.Device_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.Device_class.setText(String.format("Class: %s", mDevice.get(i).getDevice_Class()));
        viewHolder.Device_value.setText(String.valueOf(mDevice.get(i).getDevice_Value()));
        viewHolder.Device_name.setText(mDevice.get(i).getDevice_Name());

        viewHolder.Device_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.menu_device, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.device_edit:

                                //Or Some other code you want to put here.. This is just an example.
                                Toast.makeText(mContext, "Edit device " + " : " + i, Toast.LENGTH_LONG).show();

                                break;
                            case R.id.device_delete:

                                Toast.makeText(mContext, "delete device " + " : " + i, Toast.LENGTH_LONG).show();

                                break;

                            default:
                                break;
                        }
                        return true;
                    }

                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDevice.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Device_name, Device_value, Device_class;
        private ImageView Device_image, Device_actions;

        ViewHolder(View view) {
            super(view);
            Device_image = view.findViewById(R.id.DeviceImage);
            Device_actions = view.findViewById(R.id.DeviceActions);

            Device_class = view.findViewById(R.id.DeviceClass);
            Device_value = view.findViewById(R.id.DeviceValue);
            Device_name = view.findViewById(R.id.DeviceName);

        }
    }
}