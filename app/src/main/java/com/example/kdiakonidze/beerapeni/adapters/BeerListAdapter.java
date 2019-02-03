package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.BeerModel;
import com.example.kdiakonidze.beerapeni.models.Useri;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 12/14/2017.
 */

public class BeerListAdapter extends BaseAdapter {

    private ArrayList<BeerModel> beerList;
    private LayoutInflater inflater;
    ViewHolder viewHolder;

    public BeerListAdapter(Context context, ArrayList<BeerModel> beerList) {
        this.beerList = beerList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return beerList.size();
    }

    @Override
    public Object getItem(int i) {
        return beerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View listRowView;

        if (convertView == null) {
            listRowView = inflater.inflate(R.layout.beer_row, null);
            viewHolder = new ViewHolder();
            viewHolder.t_beerName = (TextView) listRowView.findViewById(R.id.t_beerrow_name);
            viewHolder.t_beerPr= (TextView) listRowView.findViewById(R.id.t_beerrow_pr);
            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        BeerModel beerModel = (BeerModel) getItem(i);
        viewHolder.t_beerName.setText(beerModel.getDasaxeleba());
        viewHolder.t_beerPr.setText(String.valueOf(beerModel.getFasi()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listRowView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(beerModel.getDisplayColor())));
        }else {
            listRowView.setBackgroundColor(Color.parseColor(beerModel.getDisplayColor()));
        }

        return listRowView;
    }

    private class ViewHolder {
        TextView t_beerName, t_beerPr;
    }
}
