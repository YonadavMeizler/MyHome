package com.imq.myhome.Adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.imq.myhome.Auxiliary.AuxiliaryBluetooth;
import com.imq.myhome.Interfaces.DeviceEditListener;
import com.imq.myhome.R;

import java.util.ArrayList;

public class AdapterDevice extends RecyclerView.Adapter<AdapterDevice.ViewHolder> {
    private static final String TAG = "R2Y2";
    private ArrayList<BluetoothDevice> mDevices;
    private Context mContext;
    private DeviceEditListener Edit_Click;
    private AuxiliaryBluetooth AuxB = new AuxiliaryBluetooth();

    public AdapterDevice(Context c, ArrayList<BluetoothDevice> devices) {
        mContext = c;
        mDevices = devices;

    }

    public void setOnEditDeviceListener(DeviceEditListener onEditDeviceClick) {
        this.Edit_Click = onEditDeviceClick;
    }

    @NonNull
    @Override
    public AdapterDevice.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, viewGroup, false);
        return new AdapterDevice.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.Device_image.setImageResource(AuxB.getDevice_image(mDevices.get(i)));
        viewHolder.Device_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.Device_name.setText(mDevices.get(i).getName());
        viewHolder.Device_class.setText(AuxB.getDevice_Class(mDevices.get(i)));
        viewHolder.Device_bond_status.setText(AuxB.getDevice_BondState(mDevices.get(i)));

        viewHolder.Device_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edit_Click != null) {
                    Edit_Click.onEditClick(i, mDevices.get(i));
                }
            }
        });

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
                                Toast.makeText(mContext, "Edit device " + " : " + i, Toast.LENGTH_LONG).show();
                                break;
                            case R.id.device_delete:
                                Toast.makeText(mContext, "Delete device " + " : " + i, Toast.LENGTH_LONG).show();
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
        return mDevices.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Device_name, Device_class, Device_bond_status;
        private ImageView Device_image, Device_actions;
        private ConstraintLayout Device_layout;

        ViewHolder(View view) {
            super(view);
            Device_image = view.findViewById(R.id.DeviceItemImage);
            Device_actions = view.findViewById(R.id.DeviceItemActions);

            Device_name = view.findViewById(R.id.DeviceItemName);
            Device_class = view.findViewById(R.id.DeviceItemClass);
            Device_bond_status = view.findViewById(R.id.DeviceItemBondStatus);

            Device_layout = view.findViewById(R.id.DeviceItemLayout);
        }
    }
}