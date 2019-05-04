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
    private LinearLayout beerRowsConteiner;
    private ConstraintLayout thisRootView;
    private ImageButton btn_itemRemove;
    private ImageView imgBeerColor;
    private TextView tBeerInfo;

    private Shekvetebi order;
    private ArrayList<Shekvetebi> tempOrdersList = new ArrayList<>();

    private static final String TAG = "BeerTempRowTAG";


    public BeerTempRow(Context context) {
        super(context);
    }

    public BeerTempRow(Context context, final Shekvetebi order, final LinearLayout beerRowsConteiner, final ArrayList<Shekvetebi> tempOrdersList) {
        super(context);
        mContext = context;
        this.order = order;
        this.beerRowsConteiner = beerRowsConteiner;
        this.tempOrdersList = tempOrdersList;

        initView();

        btn_itemRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                beerRowsConteiner.removeView(getView());
                tempOrdersList.remove(order);
            }
        });
    }

    private void initView() {
        Log.d(TAG, " initView - myListItem");
        thisRootView = (ConstraintLayout) LayoutInflater.from(mContext).inflate(R.layout.beer_temp_row, this, true);

        imgBeerColor = thisRootView.findViewById(R.id.img_beercolor);
        tBeerInfo = thisRootView.findViewById(R.id.t_beerinfo);
        btn_itemRemove = thisRootView.findViewById(R.id.btn_beerremove);

        imgBeerColor.setBackgroundColor(Color.parseColor(order.getColor()));
        tBeerInfo.setText(String.format("%s: %s(x30)  %s(x50)", order.getLudi(), MyUtil.floatToSmartStr(order.getK30wont()), MyUtil.floatToSmartStr(order.getK50wont())));

    }

    public View getView() {
        return this;
    }
}
