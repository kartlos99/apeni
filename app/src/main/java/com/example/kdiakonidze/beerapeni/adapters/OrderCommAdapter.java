package com.example.kdiakonidze.beerapeni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.OrderCommentRowModel;
import com.example.kdiakonidze.beerapeni.utils.MyKeys;

import java.util.ArrayList;

public class OrderCommAdapter extends BaseAdapter {

    private ArrayList<OrderCommentRowModel> comments = new ArrayList<>();
    private LayoutInflater inflater;
    ViewHolder viewHolder;
    private Context context;

    public OrderCommAdapter(Context context, ArrayList<OrderCommentRowModel> comments) {
        this.comments.addAll(comments);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listRowView;

        if (convertView == null) {
            listRowView = inflater.inflate(R.layout.comments_row, null);
            viewHolder = new ViewHolder();
            viewHolder.t_objName = listRowView.findViewById(R.id.t_commrow_obj_name);
            viewHolder.t_comment = listRowView.findViewById(R.id.t_commrow_comment);
            viewHolder.img_direction = listRowView.findViewById(R.id.img_comment_direction);
            listRowView.setTag(viewHolder);
        } else {
            listRowView = convertView;
            viewHolder = (ViewHolder) listRowView.getTag();
        }

        OrderCommentRowModel rowModel = (OrderCommentRowModel) getItem(position);

        switch (rowModel.getComment_Of()) {
            case MyKeys.COMMENT_OBJ_ORDER:
                viewHolder.img_direction.setImageDrawable(context.getDrawable(R.drawable.ic_order_list));
                break;
            case MyKeys.COMMENT_OBJ_DELIVERY:
                viewHolder.img_direction.setImageDrawable(context.getDrawable(R.drawable.ic_delivery));
                break;
            case MyKeys.COMMENT_OBJ_K_OUT:
                viewHolder.img_direction.setImageDrawable(context.getDrawable(R.drawable.ic_delivery));
                break;
            case MyKeys.COMMENT_OBJ_M_OUT:
                viewHolder.img_direction.setImageDrawable(context.getDrawable(R.drawable.ic_delivery));
                break;
            default:
                viewHolder.img_direction.setImageDrawable(context.getDrawable(R.drawable.ic_action_list_white));
        }
        viewHolder.t_objName.setText(rowModel.getObjName());
        viewHolder.t_comment.setText(rowModel.getComment());

        return listRowView;
    }

    private class ViewHolder {
        TextView t_objName, t_comment;
        ImageView img_direction;
    }
}
