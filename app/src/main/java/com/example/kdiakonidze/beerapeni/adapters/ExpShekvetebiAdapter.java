package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.ShekvetebiGR;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 18.04.2018.
 */

public class ExpShekvetebiAdapter extends BaseExpandableListAdapter {
    private ArrayList<ShekvetebiGR> shekvetebiArListGR;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private Boolean grouped = false;
    private Context context;

    public ExpShekvetebiAdapter(Context context, ArrayList<ShekvetebiGR> shekvetebiSorce, Boolean grouped) {
        shekvetebiArListGR = new ArrayList<>();
        shekvetebiArListGR.addAll(shekvetebiSorce);
        this.grouped = grouped;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return shekvetebiArListGR.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return shekvetebiArListGR.get(i).getChilds().size();
    }

    @Override
    public Object getGroup(int i) {
        return shekvetebiArListGR.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return shekvetebiArListGR.get(i).getChilds().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shekvetebi_gr_row, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.t_distrib_name);
        TextView t_ksum = (TextView) convertView.findViewById(R.id.t_kwont);
        title.setText(shekvetebiArListGR.get(groupPosition).getName());
        //t_ksum.setText(shekvetebiArListGR.get(groupPosition).getK30w() + " / " +shekvetebiArListGR.get(groupPosition).getK50w() + "   |   " +shekvetebiArListGR.get(groupPosition).getK30() + " / " +shekvetebiArListGR.get(groupPosition).getK50());
        String summingData = "";
        for (int i = 0; i < shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().size(); i++) {
            summingData += shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getLudi()
                    + " : " + shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getK30wont()
                    + "/" + shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getK50wont()
                    + " | " + shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getK30in()
                    + "/" + shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getK50in();
            summingData += "\n";
        }
        t_ksum.setText(summingData);
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
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
            viewHolder.t_comment = (TextView) listRowView.findViewById(R.id.t_orderlist_row_comment);
            viewHolder.ln_row = (LinearLayout) listRowView.findViewById(R.id.linear_order_row);

            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        Shekvetebi shekveta = (Shekvetebi) getChild(i,i1);
        ShekvetebiGR gr = (ShekvetebiGR) getGroup(i);

        if (grouped) {
            viewHolder.t_distributor.setVisibility(View.GONE);
            viewHolder.t_comment.setVisibility(View.GONE);
        } else {
            viewHolder.t_distributor.setVisibility(View.VISIBLE);
            if (!shekveta.getDistrib_Name().equals(gr.getName())) {
                viewHolder.t_distributor.setText(shekveta.getDistrib_Name());
            }else {
                viewHolder.t_distributor.setText("");
            }
        }
        viewHolder.t_obieqti.setText(shekveta.getObieqti());
        viewHolder.t_ludi.setText(shekveta.getLudi());
        viewHolder.t_k30wont.setText("" + shekveta.getK30wont());
        viewHolder.t_k50wont.setText("" + shekveta.getK50wont());
        viewHolder.t_k30in.setText("" + shekveta.getK30in());
        viewHolder.t_k50in.setText("" + shekveta.getK50in());
        viewHolder.t_comment.setText(shekveta.getComment());

        if (grouped) {
            if (shekveta.getK30in() + shekveta.getK50in() < shekveta.getK30wont() + shekveta.getK50wont()) {
                viewHolder.ln_row.setBackgroundColor(context.getResources().getColor(R.color.color_orderRed));
            }else {
                viewHolder.ln_row.setBackgroundColor(Color.TRANSPARENT);
            }
            viewHolder.t_obieqti.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

            if (shekveta.getChk().equals("1")) {
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_order_circle,0,0,0);
            }else {
                viewHolder.t_obieqti.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
            viewHolder.t_ludi.setBackgroundColor(Color.parseColor(shekveta.getColor()));

        }else {
            viewHolder.t_ludi.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.ln_row.setBackgroundColor(Color.TRANSPARENT);
            if (shekveta.getChk().equals("1")) {
//                viewHolder.t_obieqti.setBackgroundColor(context.getResources().getColor(R.color.colorCardview_2));

//                Drawable drawable = context.getResources().getDrawable(R.drawable.ic_order_circle);
//                Drawable dr = new ScaleDrawable(drawable,0,8,8).getDrawable();
//                dr.setBounds(0,0,8,8);
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_order_circle,0,0,0);
            }else {
                viewHolder.t_obieqti.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }

            if(!shekveta.getComment().isEmpty()){
                viewHolder.t_distributor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_comment_icon,0,0,0);
            }else {
                viewHolder.t_distributor.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
            listRowView.setBackgroundColor(Color.parseColor(shekveta.getColor()));
        }

        return listRowView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void reNewData(ArrayList<ShekvetebiGR> newOrderData, Boolean gr) {
        this.grouped = gr;
        shekvetebiArListGR.clear();
        shekvetebiArListGR.addAll(newOrderData);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView t_obieqti, t_ludi, t_k30in, t_k50in, t_k30wont, t_k50wont, t_distributor, t_comment;
        LinearLayout ln_row;
    }
}
