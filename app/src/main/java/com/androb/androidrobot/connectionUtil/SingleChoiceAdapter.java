package com.androb.androidrobot.connectionUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androb.androidrobot.R;

import java.util.List;

/**
 * Created by kaki on 2018/04/03.
 */

public class SingleChoiceAdapter extends BaseAdapter {

    Context context;
//    List<Brand> brandsList;
    List<String> deviceList;
    LayoutInflater mInflater;

    public SingleChoiceAdapter(Context context, List<String> mList){
        this.context = context;
        this.deviceList = mList;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.single_choice, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.id_name);
            viewHolder.select = (RadioButton)convertView.findViewById(R.id.id_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(deviceList.get(position));
//        if (selectPosition == position) {
//            viewHolder.select.setChecked(true);
//        }
//        else {
//            viewHolder.select.setChecked(false);
//        }

        return convertView;
    }
}

