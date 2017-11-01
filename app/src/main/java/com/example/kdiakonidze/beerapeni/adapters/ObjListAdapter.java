package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 11/1/2017.
 */

public class ObjListAdapter extends BaseAdapter {
    private ArrayList<Obieqti> myObjList;
    private Context context;
    private LayoutInflater inflater;
    ViewHolder viewHolder;

    public ObjListAdapter(Context context, ArrayList<Obieqti> objList) {
        this.context = context;
        myObjList = objList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myObjList.size();
    }

    @Override
    public Object getItem(int i) {
        return myObjList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View listRowView;

        if(convertView == null){
            listRowView = inflater.inflate(R.layout.obj_list_row, null );
            viewHolder = new ViewHolder();
            viewHolder.t_objName = (TextView) listRowView.findViewById(R.id.t_objName);
            listRowView.setTag(viewHolder);
        }else{
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        Obieqti obieqti = (Obieqti) getItem(i);
        viewHolder.t_objName.setText(obieqti.getDasaxeleba());

        return listRowView;
    }

    private class ViewHolder{
        TextView t_objName;
    }
}
