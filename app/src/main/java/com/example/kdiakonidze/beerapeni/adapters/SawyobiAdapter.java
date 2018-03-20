package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.SysClean;
import com.example.kdiakonidze.beerapeni.models.Totalinout;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 12.03.2018.
 */

public class SawyobiAdapter extends BaseAdapter {
    private ArrayList<Totalinout> mydata;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    public SawyobiAdapter(Context context, ArrayList<Totalinout> mydata) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listRowView;

        if (convertView == null) {
            listRowView = inflater.inflate(R.layout.saywobi_row, null);
            viewHolder = new ViewHolder();
            viewHolder.t_dasaxeleba = (TextView) listRowView.findViewById(R.id.t_dasaxeleba_sawy);
            viewHolder.t_30sawy= (TextView) listRowView.findViewById(R.id.t_30sawy);
            viewHolder.t_50sawy= (TextView) listRowView.findViewById(R.id.t_50sawy);
            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        Totalinout curritem = (Totalinout) getItem(position);
        viewHolder.t_dasaxeleba.setText(curritem.getLudi());
        viewHolder.t_30sawy.setText(String.valueOf(curritem.getK30s() - curritem.getK30r()));
        viewHolder.t_50sawy.setText(String.valueOf(curritem.getK50s() - curritem.getK50r()));

        return listRowView;
    }

    private class ViewHolder {
        TextView t_dasaxeleba, t_30sawy, t_50sawy;
    }
}
