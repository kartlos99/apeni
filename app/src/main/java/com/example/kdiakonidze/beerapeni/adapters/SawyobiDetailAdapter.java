package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.SawyobiDetailRow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by k.diakonidze on 16.03.2018.
 */

public class SawyobiDetailAdapter extends BaseAdapter {
    private ArrayList<SawyobiDetailRow> mydata;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SawyobiDetailAdapter(Context context, ArrayList<SawyobiDetailRow> mydata) {
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
            listRowView = inflater.inflate(R.layout.sawyobi_detail_row, null);
            viewHolder = new ViewHolder();

            viewHolder.ln_row = (LinearLayout) listRowView.findViewById(R.id.linear_sawyobi_list_row);

            viewHolder.t_tarigi = (TextView) listRowView.findViewById(R.id.t_tarigi_sawylist);
            viewHolder.t_distributor = (TextView) listRowView.findViewById(R.id.t_distrib);
            viewHolder.t_ludi = (TextView) listRowView.findViewById(R.id.t_ludi_sawylist);
            viewHolder.t_k30in= (TextView) listRowView.findViewById(R.id.t_k30in_sawylist);
            viewHolder.t_k50in= (TextView) listRowView.findViewById(R.id.t_k50in_sawylist);
            viewHolder.t_k30out= (TextView) listRowView.findViewById(R.id.t_k30out_sawylist);
            viewHolder.t_k50out= (TextView) listRowView.findViewById(R.id.t_k50out_sawylist);
            viewHolder.t_comment = (TextView) listRowView.findViewById(R.id.t_sawylist_row_comment);

            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        SawyobiDetailRow row = (SawyobiDetailRow) getItem(position);

        String shortDate = "";
        try {
            shortDate = dateFormat.format(timeFormat.parse(row.getTarigi()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.t_tarigi.setText(shortDate);
        viewHolder.t_ludi.setText(row.getLudi());
        if (row.getLudi().equals("-")){
            viewHolder.t_k30out.setText("" + row.getK30());
            viewHolder.t_k50out.setText("" + row.getK50());
            viewHolder.t_k30in.setText("");
            viewHolder.t_k50in.setText("");
        }else{
            viewHolder.t_k30out.setText("");
            viewHolder.t_k50out.setText("");
            viewHolder.t_k30in.setText("" + row.getK30());
            viewHolder.t_k50in.setText("" + row.getK50());
        }
        viewHolder.t_comment.setText(row.getComment());
        viewHolder.t_distributor.setText(row.getDistributor());


            viewHolder.ln_row.setBackgroundColor(Color.TRANSPARENT);
            if (row.getChek().equals("1")) {
                viewHolder.t_tarigi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_order_circle,0,0,0);
            }else {
                viewHolder.t_tarigi.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.t_tarigi.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }

            if(!row.getComment().isEmpty()){
                viewHolder.t_distributor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_comment_icon,0,0,0);
            }else {
                viewHolder.t_distributor.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }


        return listRowView;
    }

    private class ViewHolder {
        TextView t_tarigi, t_ludi, t_k30in, t_k50in, t_k30out, t_k50out, t_distributor, t_comment;
        LinearLayout ln_row;
    }
}
