package com.example.kdiakonidze.beerapeni.customView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.Xarji;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.GlobalServise;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;

import java.util.ArrayList;

public class XarjiRow extends ConstraintLayout implements GlobalServise.vListener {

    private Context mContext;
    private static final String TAG = "XarjiRowTAG";
    private ImageButton btn_itemRemove;
    private Xarji xarji;
    private ArrayList<Xarji> xarjebi;
    private LinearLayout linearConteiner;
    private TextView tShowSum;
    private Boolean canDel;

    public XarjiRow(Context context) {
        super(context);
    }

    public XarjiRow(final Context context, final Xarji xarji, final LinearLayout linearConteiner, final ArrayList<Xarji> xarjebi, final TextView tShowSum, Boolean canDel) {
        super(context);
        mContext = context;
        this.xarji = xarji;
        this.linearConteiner = linearConteiner;
        this.xarjebi = xarjebi;
        this.tShowSum = tShowSum;
        this.canDel = canDel;
        initView();

        btn_itemRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage(Constantebi.MSG_DEL);
                builder.setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GlobalServise globalServise = new GlobalServise(mContext);
                        globalServise.setChangeListener(XarjiRow.this);
                        globalServise.delXarjebi(xarji.getId());
                    }
                }).setNegativeButton("არა", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initView() {
        ConstraintLayout thisRootView = (ConstraintLayout) LayoutInflater.from(mContext).inflate(R.layout.xarji_row, this, true);

        TextView tComment = thisRootView.findViewById(R.id.t_xarj_row_comment);
        TextView tDistr = thisRootView.findViewById(R.id.t_xarj_user);
        TextView tAmount = thisRootView.findViewById(R.id.t_xarj_row_amount);
        btn_itemRemove = thisRootView.findViewById(R.id.btn_xarj_remove);
        if (!canDel)
            btn_itemRemove.setVisibility(INVISIBLE);

        tComment.setText(xarji.getComment());
        tAmount.setText(MyUtil.floatToSmartStr(xarji.getAmount()) + mContext.getString(R.string.lari));
        tDistr.setText(MyUtil.getUserName(Integer.parseInt(xarji.getDistrID())));
    }

    public View getView() {
        return this;
    }

    @Override
    public void onChange() {
        xarjebi.remove(xarji);
        linearConteiner.removeView(getView());
        tShowSum.setText(MyUtil.floatToSmartStr(MyUtil.totalXarji(xarjebi)) + mContext.getString(R.string.lari));
    }
}