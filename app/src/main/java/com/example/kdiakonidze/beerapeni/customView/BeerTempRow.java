package com.example.kdiakonidze.beerapeni.customView;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;

import java.util.ArrayList;

public class BeerTempRow extends ConstraintLayout {

    private Context mContext;
    private static final String TAG = "BeerTempRowTAG";
    private ImageButton btn_itemRemove;
    private Shekvetebi order;

    public BeerTempRow(Context context) {
        super(context);
    }

    public BeerTempRow(Context context, final Shekvetebi order, final LinearLayout beerRowsConteiner, final ArrayList<Shekvetebi> tempOrdersList) {
        super(context);
        mContext = context;
        this.order = order;
        initView();

        btn_itemRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tempOrdersList.remove(order);
                beerRowsConteiner.removeView(getView());
            }
        });
    }

    public BeerTempRow(Context context, final Shekvetebi order, final LinearLayout beerRowsConteiner, final ArrayList<Shekvetebi> tempOrdersList, final TextView tShowPrice) {
        super(context);
        mContext = context;
        this.order = order;
        initView();

        btn_itemRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tempOrdersList.remove(order);
                beerRowsConteiner.removeView(getView());
                tShowPrice.setText(String.format("%s (%s \u20BE )", getResources().getString(R.string.ludis_shetana), MyUtil.floatToSmartStr(MyUtil.tempListPrice(tempOrdersList))));
            }
        });
    }

    private void initView() {
        ConstraintLayout thisRootView = (ConstraintLayout) LayoutInflater.from(mContext).inflate(R.layout.beer_temp_row, this, true);

        ImageView imgBeerColor = thisRootView.findViewById(R.id.img_beercolor);
        TextView tBeerInfo = thisRootView.findViewById(R.id.t_beerinfo);
        btn_itemRemove = thisRootView.findViewById(R.id.btn_beerremove);

        imgBeerColor.setBackgroundColor(Color.parseColor(order.getColor()));
        tBeerInfo.setText(String.format("%s: %sx30  %sx50", order.getLudi(), MyUtil.floatToSmartStr(order.getK30wont() + order.getK30in()), MyUtil.floatToSmartStr(order.getK50wont()+ order.getK50in())));
    }

    public View getView() {
        return this;
    }
}