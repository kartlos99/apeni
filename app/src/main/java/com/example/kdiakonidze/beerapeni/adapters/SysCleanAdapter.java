package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.SysClean;

import java.util.ArrayList;

/**
 * Created by kartl on 03.01.2018.
 */

public class SysCleanAdapter extends BaseAdapter {

    ArrayList<SysClean> mydata;
    private LayoutInflater inflater;
    ViewHolder viewHolder;

    public SysCleanAdapter(Context context, ArrayList<SysClean> mydata) {
        this.mydata = mydata;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mydata.size();
    }

    @Override
    public Object getItem(int position) {
        return mydata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mydata.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listRowView;

        if (convertView == null) {
            listRowView = inflater.inflate(R.layout.sysclean_row, null);
            viewHolder = new ViewHolder();
            viewHolder.t_dasaxeleba = (TextView) listRowView.findViewById(R.id.t_dasaxeleba_cleaning);
            viewHolder.t_tarigi= (TextView) listRowView.findViewById(R.id.t_tarigi_cleaning);
            viewHolder.t_pastday= (TextView) listRowView.findViewById(R.id.t_pastday_cleaning);
            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        SysClean curritem = (SysClean) getItem(position);
        viewHolder.t_dasaxeleba.setText(curritem.getDasaxeleba());
        viewHolder.t_tarigi.setText(curritem.getTarigi());
        viewHolder.t_pastday.setText(String.valueOf(curritem.getDge()));

        return listRowView;
    }

    private class ViewHolder {
        TextView t_dasaxeleba, t_tarigi, t_pastday;
    }
}
