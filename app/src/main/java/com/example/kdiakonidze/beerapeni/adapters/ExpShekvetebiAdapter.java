package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.ShekvetebiGR;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 18.04.2018.
 */

public class ExpShekvetebiAdapter extends BaseExpandableListAdapter {
    private ArrayList<ShekvetebiGR> shekvetebiArListGR;
    private LayoutInflater inflater;
    private Boolean grouped;
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shekvetebi_gr_row, null);
        }

        TextView title = convertView.findViewById(R.id.t_distrib_name);
        TextView t_ksum = convertView.findViewById(R.id.t_kwont);
        title.setText(shekvetebiArListGR.get(groupPosition).getName());
        //t_ksum.setText(shekvetebiArListGR.get(groupPosition).getK30w() + " / " +shekvetebiArListGR.get(groupPosition).getK50w() + "   |   " +shekvetebiArListGR.get(groupPosition).getK30() + " / " +shekvetebiArListGR.get(groupPosition).getK50());
//        String summingData = "";
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().size(); i++) {
            sBuilder.append(shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getLudi());
            sBuilder.append(" : ");
            sBuilder.append(MyUtil.floatToSmartStr(shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getK30wont()));
            sBuilder.append(" / ");
            sBuilder.append(MyUtil.floatToSmartStr(shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getK50wont()));
//            sBuilder.append(" | ");
//            sBuilder.append(MyUtil.floatToSmartStr(shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getK30in()));
//            sBuilder.append(" / ");
//            sBuilder.append(MyUtil.floatToSmartStr(shekvetebiArListGR.get(groupPosition).getGrHeadOrderSum().get(i).getK50in()));
            sBuilder.append("\n");
        }
        t_ksum.setText(sBuilder.toString());
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
        View listRowView;
        ViewHolder viewHolder;

        if (convertView == null) {
            listRowView = inflater.inflate(R.layout.shekvetebi_row, null);
            viewHolder = new ViewHolder();

            viewHolder.t_obieqti = listRowView.findViewById(R.id.t_obieqti_orderlist);
            viewHolder.t_ludi = listRowView.findViewById(R.id.t_ludi_orderlist);
            viewHolder.t_k30in = listRowView.findViewById(R.id.t_k30in_orderlist);
            viewHolder.t_k50in = listRowView.findViewById(R.id.t_k50in_orderlist);
            viewHolder.t_k30wont = listRowView.findViewById(R.id.t_k30wont_orderlist);
            viewHolder.t_k50wont = listRowView.findViewById(R.id.t_k50wont_orderlist);
            viewHolder.t_distributor = listRowView.findViewById(R.id.t_shek_list_distrib);
            viewHolder.t_comment = listRowView.findViewById(R.id.t_orderlist_row_comment);
            viewHolder.ln_row = listRowView.findViewById(R.id.linear_order_row);
            viewHolder.img_BeerColor = listRowView.findViewById(R.id.img_beer_color);
            viewHolder.img_comment = listRowView.findViewById(R.id.img_comment);

            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        Shekvetebi shekveta = (Shekvetebi) getChild(i, i1);
        ShekvetebiGR gr = (ShekvetebiGR) getGroup(i);

        if (shekveta.getComment().isEmpty()) {
            viewHolder.img_comment.setVisibility(View.GONE);
        } else {
            viewHolder.img_comment.setVisibility(View.VISIBLE);
        }

        if (grouped) {
            viewHolder.t_distributor.setVisibility(View.GONE);
            viewHolder.t_comment.setVisibility(View.GONE);
        } else {
            viewHolder.t_distributor.setVisibility(View.VISIBLE);
            if (!shekveta.getDistrib_Name().equals(gr.getName())) {
                viewHolder.t_distributor.setText(shekveta.getDistrib_Name());
            } else {
                viewHolder.t_distributor.setText("");
            }
        }
        viewHolder.t_obieqti.setText(shekveta.getObieqti());
        viewHolder.t_ludi.setText(shekveta.getLudi());
        viewHolder.t_k30wont.setText(MyUtil.floatToSmartStr(shekveta.getK30wont()));
        viewHolder.t_k50wont.setText(MyUtil.floatToSmartStr(shekveta.getK50wont()));
        viewHolder.t_k30in.setText(MyUtil.floatToSmartStr(shekveta.getK30in()));
        viewHolder.t_k50in.setText(MyUtil.floatToSmartStr(shekveta.getK50in()));
        viewHolder.t_comment.setText(shekveta.getComment());

        viewHolder.t_k30wont.setTextColor(shekveta.getK30wont() == 0 ? context.getResources().getColor(R.color.not_imp_color) : Color.BLACK);
        viewHolder.t_k50wont.setTextColor(shekveta.getK50wont() == 0 ? context.getResources().getColor(R.color.not_imp_color) : Color.BLACK);
        viewHolder.t_k30in.setTextColor(shekveta.getK30in() == 0 ? context.getResources().getColor(R.color.not_imp_color) : Color.BLACK);
        viewHolder.t_k50in.setTextColor(shekveta.getK50in() == 0 ? context.getResources().getColor(R.color.not_imp_color) : Color.BLACK);

        if (grouped) {
            if (Float.compare(shekveta.getK30in() + shekveta.getK50in(), shekveta.getK30wont() + shekveta.getK50wont()) < 0) {
                viewHolder.ln_row.setBackgroundColor(context.getResources().getColor(R.color.color_orderRed));
            } else {
                viewHolder.ln_row.setBackgroundColor(Color.TRANSPARENT);
            }
            viewHolder.t_obieqti.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            if (shekveta.getChk().equals("1")) {
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle, 0, 0, 0);
            } else {
                viewHolder.t_obieqti.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            viewHolder.img_BeerColor.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.img_BeerColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(shekveta.getColor())));
            } else {
                viewHolder.img_BeerColor.setBackgroundColor(Color.parseColor(shekveta.getColor()));
            }
        } else {
            viewHolder.img_BeerColor.setVisibility(View.INVISIBLE);
            viewHolder.t_ludi.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.ln_row.setBackgroundColor(Color.TRANSPARENT);
            if (shekveta.getChk().equals("1")) {
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle, 0, 0, 0);
            } else {
                viewHolder.t_obieqti.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.t_obieqti.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
        ConstraintLayout ln_row;
        ImageView img_BeerColor, img_comment;
    }
}
