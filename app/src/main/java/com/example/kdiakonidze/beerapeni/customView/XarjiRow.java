package com.example.kdiakonidze.beerapeni.customView;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.Xarji;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;

import java.util.ArrayList;

public class XarjiRow extends ConstraintLayout {

    private Context mContext;
    private static final String TAG = "XarjiRowTAG";
    private ImageButton btn_itemRemove;
    private Xarji xarji;

    public XarjiRow(Context context) {
        super(context);
    }

    public XarjiRow(Context context, final Xarji xarji, final LinearLayout linearConteiner, final ArrayList<Xarji> xarjebi) {
        super(context);
        mContext = context;
        this.xarji = xarji;
        initView();

        btn_itemRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                tempOrdersList.remove(order);
//                beerRowsConteiner.removeView(getView());
            }
        });
    }

    public XarjiRow(Context context, final Xarji xarji, final LinearLayout linearConteiner, final ArrayList<Xarji> xarjebi, final TextView tShowSum) {
        super(context);
        mContext = context;
        this.xarji = xarji;
        initView();

        btn_itemRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                xarjebi.remove(xarji);
                linearConteiner.removeView(getView());
                tShowSum.setText(MyUtil.floatToSmartStr(MyUtil.totalXarji(xarjebi)));
            }
        });
    }

    private void initView() {
        ConstraintLayout thisRootView = (ConstraintLayout) LayoutInflater.from(mContext).inflate(R.layout.xarji_row, this, true);

        TextView tComment = thisRootView.findViewById(R.id.t_xarj_row_comment);
        TextView tDistr = thisRootView.findViewById(R.id.t_xarj_user);
        TextView tAmount = thisRootView.findViewById(R.id.t_xarj_row_amount);
        btn_itemRemove = thisRootView.findViewById(R.id.btn_xarj_remove);

        tComment.setText(xarji.getComment());
        tAmount.setText(MyUtil.floatToSmartStr(xarji.getAmount()) + mContext.getString(R.string.lari));
        tDistr.setText(xarji.getDistrID());
    }

    public View getView() {
        return this;
    }
}