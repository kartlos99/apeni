package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by k.diakonidze on 10/30/2017.
 */

public class ShekvetebiAdapter extends BaseAdapter {
    private ArrayList<Shekvetebi> shekvetebiArrayList;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private Boolean grouped = false;
    private Context context;

    public ShekvetebiAdapter(Context context, ArrayList<Shekvetebi> shekvetebi, Boolean grouped) {
        shekvetebiArrayList = new ArrayList<>();
        shekvetebiArrayList.addAll(shekvetebi);
        this.grouped = grouped;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return shekvetebiArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return shekvetebiArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View listRowView;

        if (convertView == null) {
            listRowView = inflater.inflate(R.layout.shekvetebi_row, null);
            viewHolder = new ViewHolder();

            viewHolder.t_obieqti = (TextView) listRowView.findViewById(R.id.t_obieqti_orderlist);
            viewHolder.t_ludi = (TextView) listRowView.findViewById(R.id.t_ludi_orderlist);
            viewHolder.t_k30in = (TextView) listRowView.findViewById(R.id.t_k30in_orderlist);
            viewHolder.t_k50in = (TextView) listRowView.findViewById(R.id.t_k50in_orderlist);
            viewHolder.t_k30wont = (TextView) listRowView.findViewById(R.id.t_k30wont_orderlist);
            viewHolder.t_k50wont = (TextView) listRowView.findViewById(R.id.t_k50wont_orderlist);
            viewHolder.t_distributor = (TextView) listRowView.findViewById(R.id.t_shek_list_distrib);
            viewHolder.ln_row = (LinearLayout) listRowView.findViewById(R.id.linear_order_row);

            listRowView.setTag(viewHolder);
        } else {

            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        Shekvetebi shekveta = (Shekvetebi) getItem(i);

        if (grouped) {
            viewHolder.t_distributor.setVisibility(View.GONE);
        } else {
            viewHolder.t_distributor.setVisibility(View.VISIBLE);
            viewHolder.t_distributor.setText(shekveta.getDistrib_Name());
        }
        viewHolder.t_obieqti.setText(shekveta.getObieqti());
        viewHolder.t_ludi.setText(shekveta.getLudi());
        viewHolder.t_k30wont.setText("" + shekveta.getK30wont());
        viewHolder.t_k50wont.setText("" + shekveta.getK50wont());
        viewHolder.t_k30in.setText("" + shekveta.getK30in());
        viewHolder.t_k50in.setText("" + shekveta.getK50in());



        if (grouped) {
            if (shekveta.getK30in() + shekveta.getK50in() < shekveta.getK30wont() + shekveta.getK50wont()) {
                viewHolder.ln_row.setBackgroundColor(context.getResources().getColor(R.color.color_orderRed));
            }else {
                viewHolder.ln_row.setBackgroundColor(Color.TRANSPARENT);
            }
            viewHolder.t_obieqti.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }else {
            viewHolder.ln_row.setBackgroundColor(Color.TRANSPARENT);
            if (shekveta.getChk().equals("1")) {
//                viewHolder.t_obieqti.setBackgroundColor(context.getResources().getColor(R.color.colorCardview_2));

//                Drawable drawable = context.getResources().getDrawable(R.drawable.ic_order_circle);
//                Drawable dr = new ScaleDrawable(drawable,0,8,8).getDrawable();
//                dr.setBounds(0,0,8,8);
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_order_circle,0);
            }else {
                viewHolder.t_obieqti.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
        }

        return listRowView;
    }

    public void reNewData(ArrayList<Shekvetebi> newOrderData, Boolean gr) {
        this.grouped = gr;
        shekvetebiArrayList.clear();
        shekvetebiArrayList.addAll(newOrderData);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView t_obieqti, t_ludi, t_k30in, t_k50in, t_k30wont, t_k50wont, t_distributor;
        LinearLayout ln_row;
    }
}
