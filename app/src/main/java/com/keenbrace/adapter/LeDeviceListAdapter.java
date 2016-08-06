package com.keenbrace.adapter;

import java.util.ArrayList;

import com.keenbrace.R;
import com.keenbrace.services.BluetoothConstant;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mLeDevices;
    Context context;

    public LeDeviceListAdapter(Context context) {
        super();
        this.context = context;
        mLeDevices = new ArrayList<BluetoothDevice>();

    }

    public void addDevice(BluetoothDevice device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
            notifyDataSetChanged();
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context, R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.rl_device = (RelativeLayout) view
                    .findViewById(R.id.rl_device);
            viewHolder.deviceName = (TextView) view
                    .findViewById(R.id.device_name);
            viewHolder.connState = (TextView) view
                    .findViewById(R.id.conn_state);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            BluetoothDevice device = mLeDevices.get(i);

            String deviceName = "";
            //String mac = device.getAddress();
            if (device != null)
                deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName + "      rssi :  " + BluetoothConstant.devRssiValues.get(device.getAddress())/* + "(" + mac + ")"*/);
            //else
              //  viewHolder.deviceName.setText("" + mac);

            //viewHolder.connState.setText(" RSSI:" + BluetoothConstant.devRssiValues.get(device.getAddress()));
            if (BluetoothConstant.mConnected) {
                if(deviceName.equals(BluetoothConstant.mdevice.getName()))
                {
                    viewHolder.connState.setText("connected");
                }
                else
                {
                    viewHolder.connState.setText("disconnected");
                }
            }
            else
            {
                viewHolder.connState.setText("disconnected");
            }


        } catch (Exception e) {
        }
        return view;
    }

    static class ViewHolder {
        TextView deviceName;
        TextView connState;
        RelativeLayout rl_device;
    }
}
