package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Amonaweri;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 11/15/2017.
 */

public class AmonaweriAdapter extends BaseAdapter {
    private ArrayList<Amonaweri> amonaweriList;
    private LayoutInflater layoutInflater;
    private ViewHolderAmo viewHolder;
    private int location = -1;

    public AmonaweriAdapter(Context context, ArrayList<Amonaweri> amonaweriList, int location) {
        this.amonaweriList = amonaweriList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.location = location;
    }

    @Override
    public int getCount() {
        return amonaweriList.size();
    }

    @Override
    public Object getItem(int i) {
        return amonaweriList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View listRowView;

        if (convertView == null) {
            listRowView = layoutInflater.inflate(R.layout.amonaweri_list_row, null);
            viewHolder = new ViewHolderAmo();

            viewHolder.t_p1 = (TextView) listRowView.findViewById(R.id.t_amon_list_tarigi);
            viewHolder.t_p2 = (TextView) listRowView.findViewById(R.id.t_amon_list_in);
            viewHolder.t_p3 = (TextView) listRowView.findViewById(R.id.t_amon_list_out);
            viewHolder.t_p4 = (TextView) listRowView.findViewById(R.id.t_amon_list_balance);

            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolderAmo) listRowView.getTag();
        }

        Amonaweri currItem = (Amonaweri) getItem(i);
        viewHolder.t_p1.setText(currItem.getTarigi());
        if (location == 0) {
            if (currItem.getPrice() == 0) {
                viewHolder.t_p2.setText("-");
            } else {
                viewHolder.t_p2.setText(String.valueOf(currItem.getPrice()));
            }
            if (currItem.getPay() == 0) {
                viewHolder.t_p3.setText("-");
            }else {
                viewHolder.t_p3.setText(String.valueOf(currItem.getPay()));
            }

            viewHolder.t_p4.setText(String.valueOf(currItem.getBalance()));
        }
        if (location == 1) {
            if (currItem.getK_in() == 0) {
                viewHolder.t_p2.setText("-");
            } else {
                viewHolder.t_p2.setText(String.valueOf(currItem.getK_in()));
            }
            if (currItem.getK_out() == 0) {
                viewHolder.t_p3.setText("-");
            } else {
                viewHolder.t_p3.setText(String.valueOf(currItem.getK_out()));
            }
            viewHolder.t_p4.setText(String.valueOf(currItem.getK_balance()));
        }

        return listRowView;
    }

    private class ViewHolderAmo {
        TextView t_p1, t_p2, t_p3, t_p4;
    }

    public int getLocation() {
        return location;
    }
}
