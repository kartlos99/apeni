package com.example.kdiakonidze.beerapeni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import java.util.HashMap;
import java.util.Map;

public class AddOrderActivity extends AppCompatActivity {

    TextView t_OrderInfo, t_beerType;
    EditText eK30Count, eK50Count;
    Obieqti currObieqti;
    Integer beertype = 0;
    Button btn_newOrderDone, btnBeerLeft, btnBeerRight, btnK30dec, btnK30inc, btnK50dec, btnK50inc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        Intent i = getIntent();
        Bundle importedBundle = i.getExtras();
        currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");

        t_OrderInfo = (TextView) findViewById(R.id.t_orderInfo);
        t_OrderInfo.setText(currObieqti.getDasaxeleba());

        eK30Count = (EditText) findViewById(R.id.edit_K30Count);
        eK50Count = (EditText) findViewById(R.id.edit_K50Count);
        btn_newOrderDone = (Button) findViewById(R.id.btn_orderDone);
        btnBeerLeft = (Button) findViewById(R.id.btn_beerleft);
        btnBeerRight = (Button) findViewById(R.id.btn_beerright);
        t_beerType = (TextView) findViewById(R.id.t_ludisDasaxeleba);
        t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba());

        btnK30dec = (Button) findViewById(R.id.btn_k30_dec);
        btnK30inc = (Button) findViewById(R.id.btn_k30_inc);
        btnK50dec = (Button) findViewById(R.id.btn_k50_dec);
        btnK50inc = (Button) findViewById(R.id.btn_k50_inc);


        btnK30dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK30Count.setText(pliusMinusText(eK30Count.getText().toString(),false));
            }
        });

        btnK30inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK30Count.setText(pliusMinusText(eK30Count.getText().toString(),true));
            }
        });

        btnK50dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK50Count.setText(pliusMinusText(eK50Count.getText().toString(),false));
            }
        });

        btnK50inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eK50Count.setText(pliusMinusText(eK50Count.getText().toString(),true));
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
            }
        });

        btn_newOrderDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToDB(Constantebi.URL_INS_SHEKVETA);
            }
        });


    }

    private String pliusMinusText(String stringNaomber, boolean oper) {
        // oper   (true = +) (false = -)
        if (stringNaomber.equals("")){
            stringNaomber="0";
        }
        int ii = Integer.valueOf(stringNaomber);

        if(oper){
            ii++;
        }else{
            if (ii > 0){
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

                params.put("obieqtis_id", String.valueOf(currObieqti.getId()));
                params.put("k30", eK30Count.getText().toString());
                params.put("k50", eK50Count.getText().toString());
                params.put("beer_type", String.valueOf(beertype + 1));
                params.put("comment", "comentaris grafaa dasamatebeli");

                params.toString();
                return params;
            }
        };

        queue.add(request);
    }
}
