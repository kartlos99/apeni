package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by k.diakonidze on 10/30/2017.
 */

public class ShekvetebiAdapter extends BaseAdapter{
    private ArrayList<Shekvetebi> shekvetebiArrayList;
    private Context context;
    LayoutInflater inflater;
    ViewHolder viewHolder;

    public ShekvetebiAdapter(Context context, ArrayList<Shekvetebi> shekvetebi) {
        shekvetebiArrayList = shekvetebi;
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

        if(convertView == null){
            listRowView = inflater.inflate(R.layout.shekvetebi_row, null );
            viewHolder = new ViewHolder();

            viewHolder.t_obieqti = (TextView) listRowView.findViewById(R.id.t_obieqti_orderlist);
            viewHolder.t_ludi = (TextView) listRowView.findViewById(R.id.t_ludi_orderlist);
            viewHolder.t_k30in = (TextView) listRowView.findViewById(R.id.t_k30in_orderlist);
            viewHolder.t_k50in = (TextView) listRowView.findViewById(R.id.t_k50in_orderlist);
            viewHolder.t_k30wont = (TextView) listRowView.findViewById(R.id.t_k30wont_orderlist);
            viewHolder.t_k50wont = (TextView) listRowView.findViewById(R.id.t_k50wont_orderlist);

            listRowView.setTag(viewHolder);
        }else{

            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        Shekvetebi shekveta = (Shekvetebi) getItem(i);
        viewHolder.t_obieqti.setText(shekveta.getObieqti());
        viewHolder.t_ludi.setText(shekveta.getLudi());
        viewHolder.t_k30wont.setText(""+shekveta.getK30wont());
        viewHolder.t_k50wont.setText(""+shekveta.getK50wont());
        viewHolder.t_k30in.setText(""+shekveta.getK30in());
        viewHolder.t_k50in.setText(""+shekveta.getK50in());

        return listRowView;
    }

    private class ViewHolder{
        TextView t_obieqti, t_ludi, t_k30in, t_k50in, t_k30wont, t_k50wont;
    }
}
