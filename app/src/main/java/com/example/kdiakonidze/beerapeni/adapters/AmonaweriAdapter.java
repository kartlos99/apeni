package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Amonaweri;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by k.diakonidze on 11/15/2017.
 */

public class AmonaweriAdapter extends BaseAdapter {
    private ArrayList<Amonaweri> amonaweriList;
    private LayoutInflater layoutInflater;
    private int location;
    private Boolean grouped;
    Context context;

    public AmonaweriAdapter(Context context, ArrayList<Amonaweri> amonaweriList, int location, Boolean isGrouped) {
        this.amonaweriList = amonaweriList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.location = location;
        grouped = isGrouped;
        this.context = context;
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
        ViewHolderAmo viewHolder;

        if (convertView == null) {
            listRowView = layoutInflater.inflate(R.layout.amonaweri_list_row, null);
            viewHolder = new ViewHolderAmo();

            viewHolder.t_p1 = listRowView.findViewById(R.id.t_amon_list_tarigi);
            viewHolder.t_p2 = listRowView.findViewById(R.id.t_amon_list_in);
            viewHolder.t_p3 = listRowView.findViewById(R.id.t_amon_list_out);
            viewHolder.t_p4 = listRowView.findViewById(R.id.t_amon_list_balance);
            viewHolder.t_comment = listRowView.findViewById(R.id.t_amonaweri_row_comment);

            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolderAmo) listRowView.getTag();
        }

        Amonaweri currItem = (Amonaweri) getItem(i);
        viewHolder.t_p1.setText(currItem.getTarigi());
        if (location == 0) {
            DecimalFormat df = new DecimalFormat("#0.00");
            if (currItem.getPrice() == 0) {
                viewHolder.t_p2.setText("-");
            } else {
                viewHolder.t_p2.setText(df.format(currItem.getPrice()));
            }
            if (currItem.getPay() == 0) {
                viewHolder.t_p3.setText("-");
            }else {
                viewHolder.t_p3.setText(df.format(currItem.getPay()));
            }

            viewHolder.t_p4.setText(df.format(currItem.getBalance()));
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

        viewHolder.t_comment.setText(currItem.getComment());
        if(grouped){
            viewHolder.t_comment.setVisibility(View.GONE);
        }else {
            if(currItem.getComment().isEmpty() ){
//                viewHolder.t_p1.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                viewHolder.t_p3.setTextColor(Color.BLACK);
                viewHolder.t_p2.setTextColor(Color.BLACK);
            }else {
//                Drawable drawable = context.getResources().getDrawable(R.drawable.ic_comment_icon);
//                ScaleDrawable scaleDrawable = new ScaleDrawable(drawable, Gravity.CENTER, 0.4f, 0.8f);
//                viewHolder.t_p1.setBackground(scaleDrawable.getDrawable());
                viewHolder.t_p3.setTextColor(Color.MAGENTA);
                viewHolder.t_p2.setTextColor(Color.MAGENTA);
//                viewHolder.t_p1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_comment_icon,0,0,0);
            }
        }
        return listRowView;
    }

    private class ViewHolderAmo {
        TextView t_p1, t_p2, t_p3, t_p4, t_comment;
    }
}
