package com.example.kdiakonidze.beerapeni;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.customView.BeerTempRow;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOrderActivity extends AppCompatActivity {

    private TextView t_OrderInfo, t_beerType;
    private EditText eK30Count, eK50Count, e_comment;
    private Button btn_newOrderDone;
    private FloatingActionButton fab_addBeer;
    private CheckBox chBox_orderChek;
    private Spinner sp_order_distrib;
    private LinearLayout linear_BeerConteiner;

    private Context mContext;

    private ArrayList<Shekvetebi> tempOrdersList = new ArrayList<>();
    private String reason;
    private Obieqti currObieqti;
    private Integer beertype = 0, beerId = 0;
    private Shekvetebi shekveta;
    int defOrientation;
    private ArrayList<Shekvetebi> shekvetebiArrayList = new ArrayList<>();

    private RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("beer", beertype);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        defOrientation = getRequestedOrientation();
        Button btnBeerLeft, btnBeerRight, btnK30dec, btnK30inc, btnK50dec, btnK50inc;
        mContext = this;
        chekLocalData();

        t_OrderInfo = findViewById(R.id.t_orderInfo);
        eK30Count = findViewById(R.id.edit_K30Count);
        eK50Count = findViewById(R.id.edit_K50Count);
        btn_newOrderDone = findViewById(R.id.btn_orderDone);
        btnBeerLeft = findViewById(R.id.btn_beerleft);
        btnBeerRight = findViewById(R.id.btn_beerright);
        t_beerType = findViewById(R.id.t_ludisDasaxeleba);
        fab_addBeer = findViewById(R.id.btn_anotherBeer);
        linear_BeerConteiner = findViewById(R.id.linear_conteiner);

        btnK30dec = findViewById(R.id.btn_k30_dec);
        btnK30inc = findViewById(R.id.btn_k30_inc);
        btnK50dec = findViewById(R.id.btn_k50_dec);
        btnK50inc = findViewById(R.id.btn_k50_inc);
        chBox_orderChek = findViewById(R.id.checkbox_orderchek);
        sp_order_distrib = findViewById(R.id.sp_order_carrying);
        e_comment = findViewById(R.id.e_order_comment);
        Toolbar toolbar = findViewById(R.id.toolbar_addorder);

        SpinnerAdapter spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Constantebi.USERsLIST);
        sp_order_distrib.setAdapter(spAdapter);

        if (savedInstanceState != null) {
            beertype = savedInstanceState.getInt("beer");
            if (Constantebi.ludiList.size() > 0) {
                t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba());
                beerId = Constantebi.ludiList.get(beertype).getId();
            } else {
                Toast.makeText(getApplicationContext(), "restart the app!", Toast.LENGTH_LONG).show();
            }
        }

        Intent intent = getIntent();
        reason = intent.getStringExtra(Constantebi.REASON);
        if (reason.equals(Constantebi.NEW_ORDER)) {
            Bundle importedBundle = intent.getExtras();
            if (importedBundle != null) {
                currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");
                if (currObieqti != null) {
                    t_OrderInfo.setText(currObieqti.getDasaxeleba());
                }
                shekvetebiArrayList.clear();
                Object ordListObj = importedBundle.getSerializable("orderArray");
                if (ordListObj instanceof List) {
                    for (int i = 0; i < ((List) ordListObj).size(); i++) {
                        Object item = ((List) ordListObj).get(i);
                        if (item instanceof Shekvetebi) {
                            shekvetebiArrayList.add((Shekvetebi) item);
                        }
                    }
                }
            }
            t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba());
            beerId = Constantebi.ludiList.get(beertype).getId();
            chBox_orderChek.setChecked(currObieqti.getChek().equals("1"));
        }
        if (reason.equals(Constantebi.EDIT)) {
            shekveta = (Shekvetebi) intent.getSerializableExtra("obj");
            shevseba(shekveta);
        }

        toolbar.setTitle(reason);
        setSupportActionBar(toolbar);


        btnK30dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK30Count.setText(MyUtil.pliusMinusText(eK30Count.getText().toString(), getString(R.string.minusi)));
            }
        });

        btnK30inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK30Count.setText(MyUtil.pliusMinusText(eK30Count.getText().toString(), getString(R.string.pliusi)));
            }
        });

        btnK50dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK50Count.setText(MyUtil.pliusMinusText(eK50Count.getText().toString(), getString(R.string.minusi)));
            }
        });

        btnK50inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK50Count.setText(MyUtil.pliusMinusText(eK50Count.getText().toString(), getString(R.string.pliusi)));
            }
        });

        btnBeerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beertype--;
                if (beertype < 0) {
                    beertype = beertype + Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba());
                beerId = Constantebi.ludiList.get(beertype).getId();
            }
        });

        btnBeerRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beertype++;
                if (beertype == Constantebi.ludiList.size()) {
                    beertype = beertype - Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba());
                beerId = Constantebi.ludiList.get(beertype).getId();
            }
        });

        btn_newOrderDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tempOrdersList.size() > 0 && reason.equals(Constantebi.NEW_ORDER)) {
                    sendDataToDB();
                    btn_newOrderDone.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(), "nothing to save", Toast.LENGTH_LONG).show();
                }

                if (reason.equals(Constantebi.EDIT)){
                    sendDataToDB();
                    btn_newOrderDone.setEnabled(false);
                }
            }
        });

        int index = 0;
        for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
            if (Constantebi.USERsLIST.get(i).getUsername().equals(Constantebi.USER_USERNAME)) {
                index = i;
            }
        }
        sp_order_distrib.setSelection(index);

        fab_addBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chekForOrderExists(currObieqti.getDasaxeleba(), Constantebi.ludiList.get(beertype).getDasaxeleba())) {
                    Toast.makeText(getApplicationContext(), R.string.msg_orderAlreadyExist, Toast.LENGTH_LONG).show();
//                    onBackPressed();
                } else {
                    if ((eK30Count.getText().toString().equals("0") || eK30Count.getText().toString().isEmpty())
                            && (eK50Count.getText().toString().equals("0") || eK50Count.getText().toString().isEmpty())) {
                        Toast.makeText(getApplicationContext(), R.string.msg_enterKasrQuantity, Toast.LENGTH_LONG).show();
                    } else {
                        Shekvetebi newOrder = new Shekvetebi("",
                                Constantebi.ludiList.get(beertype).getDasaxeleba(), 0, 0,
                                Float.valueOf(eK30Count.getText().toString().isEmpty()? "0" : eK30Count.getText().toString()),
                                Float.valueOf(eK50Count.getText().toString().isEmpty()? "0" : eK50Count.getText().toString())
                        );
                        newOrder.setColor(Constantebi.ludiList.get(beertype).getDisplayColor());
                        newOrder.setBeer_id(Constantebi.ludiList.get(beertype).getId());

                        BeerTempRow beerTempRow = new BeerTempRow(mContext, newOrder, linear_BeerConteiner, tempOrdersList);
                        tempOrdersList.add(newOrder);

                        linear_BeerConteiner.addView(beerTempRow);
                        eK30Count.setText("");
                        eK50Count.setText("");
                    }
                }

            }
        });

    }

    boolean chekForOrderExists(String obieqti, String ludi) {
        for (int i = 0; i < shekvetebiArrayList.size(); i++) {
            if (shekvetebiArrayList.get(i).getObieqti().equals(obieqti) && shekvetebiArrayList.get(i).getLudi().equals(ludi)) {
                if (shekvetebiArrayList.get(i).getK30wont() + shekvetebiArrayList.get(i).getK50wont() > shekvetebiArrayList.get(i).getK30in() + shekvetebiArrayList.get(i).getK50in()) {
                    return true;
                }
            }
        }
        for(Shekvetebi currorder : tempOrdersList ){
            if (currorder.getLudi().equals(ludi)){
                return true;
            }
        }
        return false;
    }

    private void shevseba(Shekvetebi shekveta) {
        e_comment.setText(shekveta.getComment());
        eK30Count.setText(MyUtil.floatToSmartStr(shekveta.getK30wont()));
        eK50Count.setText(MyUtil.floatToSmartStr(shekveta.getK50wont()));
        if (shekveta.getChk().equals("1")) {
            chBox_orderChek.setChecked(true);
        } else {
            chBox_orderChek.setChecked(false);
        }
        int index = 0;
        for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
            if (Constantebi.USERsLIST.get(i).getName().equals(shekveta.getDistrib_Name())) {
                index = i;
            }
        }
        sp_order_distrib.setSelection(index);
        t_OrderInfo.setText(shekveta.getObieqti());
        t_beerType.setText((shekveta.getLudi()));
        beerId = findBeerId(shekveta.getLudi());
    }

    private Integer findBeerId(String ludi) {
        for (int i = 0; i < Constantebi.ludiList.size(); i++) {
            if (Constantebi.ludiList.get(i).getDasaxeleba().equals(ludi)) {
                beertype = i;
                return Constantebi.ludiList.get(i).getId();
            }
        }
        return 0;
    }

    private void sendDataToDB() {
        // aq vagzavnit monacemebs chasawerad
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Constantebi.URL_INS_SHEKVETA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                if (response.equals("ჩაწერილია!") || response.equals("შეკვეთა დაკორექტირდა!")) {
                    OrdersActivity.chamosatvirtia = true;
                    onBackPressed();
                }
                btn_newOrderDone.setEnabled(true);
                setRequestedOrientation(defOrientation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRequestedOrientation(defOrientation);
                Toast.makeText(getApplicationContext(), error.getMessage() + " -", Toast.LENGTH_SHORT).show();
                btn_newOrderDone.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("moqmedeba", reason);
                if (reason.equals(Constantebi.EDIT)) {
                    params.put("order_id", String.valueOf(shekveta.getOrder_id()));
                } else {
                    params.put("obieqtis_id", String.valueOf(currObieqti.getId()));
                }
                params.put("k30", eK30Count.getText().toString());
                params.put("k50", eK50Count.getText().toString());
                params.put("beer_type", String.valueOf(beerId));
                params.put("comment", e_comment.getText().toString());
                params.put("distributor_id", String.valueOf(Constantebi.USERsLIST.get(sp_order_distrib.getSelectedItemPosition()).getId()));
                params.put("chek", (chBox_orderChek.isChecked() ? "1" : "0"));
                return params;
            }
        };

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        request.setRetryPolicy(mRetryPolicy);
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chekLocalData();
    }

    private void chekLocalData() {
        if (Constantebi.ludiList.size() == 0 || Constantebi.OBIEQTEBI.size() == 0) {
            Toast.makeText(this, "restart the app!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
