package com.wenjian.commonskill.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjian.commonskill.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: SimpleAdapter
 * Date: 2018/6/12
 *
 * @author jian.wen@ubtrobot.com
 */

public class SimpleAdapter extends BaseAdapter {

    private List<BluetoothDevice> mData = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;


    public void setData(List<BluetoothDevice> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addDevice(BluetoothDevice device) {
        mData.add(device);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item_device, parent, false);
            holder = new ViewHolder(inflate);
            inflate.setTag(holder);
            convertView = inflate;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bind(mData.get(position));

        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{

        void onItemClick(BluetoothDevice device);
    }


    class ViewHolder{

        View itemView;
        private final TextView mTvAddress;
        private final TextView mTvName;

        ViewHolder(View itemView) {
            this.itemView = itemView;
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvName = itemView.findViewById(R.id.tv_name);

        }

        void bind(final BluetoothDevice item) {
            mTvAddress.setText(item.getAddress());
            mTvName.setText(item.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(item);
                    }
                }
            });
        }

    }
}
