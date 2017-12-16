package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Useri;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 12/14/2017.
 */

public class UserListAdapter extends BaseAdapter {

    private ArrayList<Useri> users;
    private LayoutInflater inflater;
    ViewHolder viewHolder;

    public UserListAdapter(Context context, ArrayList<Useri> users) {
        this.users = users;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View listRowView;

        if (convertView == null) {
            listRowView = inflater.inflate(R.layout.users_row, null);
            viewHolder = new ViewHolder();
            viewHolder.t_username = (TextView) listRowView.findViewById(R.id.t_ur_username);
            viewHolder.t_name = (TextView) listRowView.findViewById(R.id.t_ur_name);
            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        Useri useri = (Useri) getItem(i);
        viewHolder.t_username.setText(useri.getUsername());
        viewHolder.t_name.setText(useri.getName());

        return listRowView;
    }

    private class ViewHolder {
        TextView t_username, t_name;
    }
}
