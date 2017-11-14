package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.SaleInfo;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 11/13/2017.
 */

public class DaySalesAdapter extends BaseAdapter {
    private ArrayList<SaleInfo> salesList;
    private LayoutInflater layoutInflater;
    private DaySalesAdapter.ViewHolder viewHolder;

    public DaySalesAdapter(Context context, ArrayList<SaleInfo> salesList) {
        this.salesList = salesList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return salesList.size();
    }

    @Override
    public Object getItem(int i) {
        return salesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View listRowView;

        if(convertView == null){
            listRowView = layoutInflater.inflate(R.layout.sales_row, null );
            viewHolder = new ViewHolder();

            viewHolder.t_dasaxeleba = (TextView) listRowView.findViewById(R.id.t_beerName);
            viewHolder.t_litraji = (TextView) listRowView.findViewById(R.id.t_litraji);

            listRowView.setTag(viewHolder);
        }else{
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        SaleInfo saleInfo = (SaleInfo) getItem(i);
        viewHolder.t_dasaxeleba.setText(saleInfo.getBeerName());
        viewHolder.t_litraji.setText(""+saleInfo.getLitraji());

        return listRowView;
    }

    private class ViewHolder {
        TextView t_dasaxeleba, t_litraji;
    }
}
