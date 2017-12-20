package com.example.kdiakonidze.beerapeni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.Useri;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import java.util.HashMap;
import java.util.Map;

public class AddOrderActivity extends AppCompatActivity {

    private TextView t_OrderInfo, t_beerType;
    private EditText eK30Count, eK50Count, e_comment;
    private Obieqti currObieqti;
    private Integer beertype = 0, beerId = 0;
    private Button btn_newOrderDone, btnBeerLeft, btnBeerRight, btnK30dec, btnK30inc, btnK50dec, btnK50inc;
    private CheckBox chBox_orderChek;
    private Spinner sp_order_distrib;
    private Toolbar toolbar;
    private String reason;
    private Shekvetebi shekveta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        t_OrderInfo = (TextView) findViewById(R.id.t_orderInfo);
        eK30Count = (EditText) findViewById(R.id.edit_K30Count);
        eK50Count = (EditText) findViewById(R.id.edit_K50Count);
        btn_newOrderDone = (Button) findViewById(R.id.btn_orderDone);
        btnBeerLeft = (Button) findViewById(R.id.btn_beerleft);
        btnBeerRight = (Button) findViewById(R.id.btn_beerright);
        t_beerType = (TextView) findViewById(R.id.t_ludisDasaxeleba);

        btnK30dec = (Button) findViewById(R.id.btn_k30_dec);
        btnK30inc = (Button) findViewById(R.id.btn_k30_inc);
        btnK50dec = (Button) findViewById(R.id.btn_k50_dec);
        btnK50inc = (Button) findViewById(R.id.btn_k50_inc);
        chBox_orderChek = (CheckBox) findViewById(R.id.checkbox_orderchek);
        sp_order_distrib = (Spinner) findViewById(R.id.sp_order_carrying);
        e_comment = (EditText) findViewById(R.id.e_order_comment);
        toolbar = (Toolbar) findViewById(R.id.toolbar_addorder);

        SpinnerAdapter spAdapter = new ArrayAdapter<Useri>(this, android.R.layout.simple_list_item_1, Constantebi.USERsLIST);
        sp_order_distrib.setAdapter(spAdapter);

        Intent i = getIntent();
        reason = i.getStringExtra(Constantebi.REASON);
        if (reason.equals(Constantebi.NEW_ORDER)) {
            Bundle importedBundle = i.getExtras();
            currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");
            t_OrderInfo.setText(currObieqti.getDasaxeleba());
            t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba());
            beerId = Constantebi.ludiList.get(beertype).getId();
        }
        if (reason.equals(Constantebi.EDIT)) {
            shekveta = (Shekvetebi) i.getSerializableExtra("obj");
            shevseba(shekveta);
        }

        toolbar.setTitle(reason);
        setSupportActionBar(toolbar);


        btnK30dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK30Count.setText(pliusMinusText(eK30Count.getText().toString(), false));
            }
        });

        btnK30inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK30Count.setText(pliusMinusText(eK30Count.getText().toString(), true));
            }
        });

        btnK50dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK50Count.setText(pliusMinusText(eK50Count.getText().toString(), false));
            }
        });

        btnK50inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK50Count.setText(pliusMinusText(eK50Count.getText().toString(), true));
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
                sendDataToDB(Constantebi.URL_INS_SHEKVETA);
            }
        });

    }

    private void shevseba(Shekvetebi shekveta) {
        e_comment.setText(shekveta.getComment());
        eK30Count.setText(String.valueOf(shekveta.getK30wont()));
        eK50Count.setText(String.valueOf(shekveta.getK50wont()));
        if (shekveta.getChk().equals("1")) {
            chBox_orderChek.setChecked(true);
        } else {
            chBox_orderChek.setChecked(false);
        }
        int index = 0;
        for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
            if(Constantebi.USERsLIST.get(i).getName().equals(shekveta.getDistrib_Name())){
                index = i;
            }
        }
        sp_order_distrib.setSelection(index);
        t_OrderInfo.setText(shekveta.getObieqti());
        t_beerType.setText((shekveta.getLudi()));
        beerId = findBeerId(shekveta.getLudi());
    }

    private Integer findBeerId(String ludi) {
        for(int i=0; i<Constantebi.ludiList.size();i++){
            if(Constantebi.ludiList.get(i).getDasaxeleba().equals(ludi)){
                beertype = i;
                return Constantebi.ludiList.get(i).getId();
            }
        }
        return 0;
    }

    private String pliusMinusText(String stringNaomber, boolean oper) {
        // oper   (true = +) (false = -)
        if (stringNaomber.equals("")) {
            stringNaomber = "0";
        }
        int ii = Integer.valueOf(stringNaomber);

        if (oper) {
            ii++;
        } else {
            if (ii > 0) {
                ii--;
            }
        }
        return String.valueOf(ii);
    }

    private void sendDataToDB(String url) {
        // aq vagzavnit monacemebs chasawerad
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("moqmedeba", reason);
                if(reason.equals(Constantebi.EDIT)){
                    params.put("order_id", String.valueOf(shekveta.getOrder_id()));
                }else {
                    params.put("obieqtis_id", String.valueOf(currObieqti.getId()));
                }
                params.put("k30", eK30Count.getText().toString());
                params.put("k50", eK50Count.getText().toString());
                params.put("beer_type", String.valueOf(beerId));
                params.put("comment", e_comment.getText().toString());
                params.put("distributor_id", String.valueOf(Constantebi.USERsLIST.get(sp_order_distrib.getSelectedItemPosition()).getId()));
                if (chBox_orderChek.isChecked()) {
                    params.put("chek", "1");
                } else {
                    params.put("chek", "0");
                }

                params.toString();
                return params;
            }
        };

        queue.add(request);
    }
}
