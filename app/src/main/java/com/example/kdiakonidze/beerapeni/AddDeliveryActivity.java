package com.example.kdiakonidze.beerapeni;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class AddDeliveryActivity extends AppCompatActivity implements View.OnClickListener {

    TextView t_deliveryInfo, t_beerType, t_davalanebaM, t_davalanebaK, t_ludi_in;
    EditText eK30Count, eK50Count, eK30Count_Kout, eK50Count_Kout, eTakeMoney;
    Obieqti currObieqti;
    Integer beertype = 0, beerId = 0;
    Button btn_Done, btnBeerLeft, btnBeerRight, btnK30dec, btnK30inc, btnK50dec, btnK50inc, btnK30dec_Kout, btnK30inc_Kout, btnK50dec_Kout, btnK50inc_Kout;
    TextInputLayout t_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);

        Intent i = getIntent();
        Bundle importedBundle = i.getExtras();
        currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");

        initial_findviews();

        t_deliveryInfo.setText(currObieqti.getDasaxeleba());

        btnK30dec.setOnClickListener(this);
        btnK30inc.setOnClickListener(this);
        btnK50dec.setOnClickListener(this);
        btnK50inc.setOnClickListener(this);
        btnK30dec_Kout.setOnClickListener(this);
        btnK30inc_Kout.setOnClickListener(this);
        btnK50dec_Kout.setOnClickListener(this);
        btnK50inc_Kout.setOnClickListener(this);

        t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba() + "\n" + currObieqti.getFasebi().get(beertype));
        beerId = Constantebi.ludiList.get(beertype).getId();

        btnBeerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beertype--;
                if (beertype < 0) {
                    beertype = beertype + Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba() + "\n" + currObieqti.getFasebi().get(beertype));
                beerId = Constantebi.ludiList.get(beertype).getId();
                priceCalculation();
            }
        });

        btnBeerRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beertype++;
                if (beertype == Constantebi.ludiList.size()) {
                    beertype = beertype - Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beertype).getDasaxeleba() + "\n" + currObieqti.getFasebi().get(beertype));
                beerId = Constantebi.ludiList.get(beertype).getId();
                priceCalculation();
            }
        });

        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(eK30Count.getText().toString().equals("") && eK50Count.getText().toString().equals(""))) {
                    btn_Done.setEnabled(false);
                }

                if (!(eK30Count_Kout.getText().toString().equals("") && eK50Count_Kout.getText().toString().equals(""))) {
                    btn_Done.setEnabled(false);
                }

                if (!eTakeMoney.getText().toString().equals("")) {
                    btn_Done.setEnabled(false);
                }

                if(!btn_Done.isEnabled()){
                    sendDataToDB();
                }
            }
        });

        get_davalianeba();
    }

    private void priceCalculation() {
        String k30 = eK30Count.getText().toString();
        String k50 = eK50Count.getText().toString();
        if (k30.equals("")) {
            k30 = "0";
        }
        if (k50.equals("")) {
            k50 = "0";
        }
        double fasi = Integer.valueOf(k30) * 30 * currObieqti.getFasebi().get(beertype) + Integer.valueOf(k50) * 50 * currObieqti.getFasebi().get(beertype);
        t_ludi_in.setText(getResources().getString(R.string.ludis_shetana) + " (" + String.valueOf(fasi) + "₾)");
    }

    private void get_davalianeba() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constantebi.URL_GET_DAVALIANEBA;

        JsonArrayRequest request_davalianeba = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis davalianebis chamonaTvali yvela obieqtistvis

                if (response.length() > 0) {
                    Integer davalianebaM = 0, davalianebaK = 0;

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            if (response.getJSONObject(i).getInt("obj_id") == currObieqti.getId()) {
                                davalianebaM = response.getJSONObject(i).getInt("pr") - response.getJSONObject(i).getInt("pay");
                                davalianebaK = response.getJSONObject(i).getInt("k30in") - response.getJSONObject(i).getInt("k30out")
                                        + response.getJSONObject(i).getInt("k50in") - response.getJSONObject(i).getInt("k50out");
                            }

                            t_davalanebaM.setText("დავალიანება\n" + davalianebaM);
                            t_davalanebaK.setText("კასრი\n" + davalianebaK);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        queue.add(request_davalianeba);
    }

    private void initial_findviews() {
        btnBeerLeft = (Button) findViewById(R.id.btn_beerleft);
        btnBeerRight = (Button) findViewById(R.id.btn_beerright);
        btn_Done = (Button) findViewById(R.id.btn_beerInputDone);

        btnK30dec = (Button) findViewById(R.id.btn_k30_dec);
        btnK30inc = (Button) findViewById(R.id.btn_k30_inc);
        btnK50dec = (Button) findViewById(R.id.btn_k50_dec);
        btnK50inc = (Button) findViewById(R.id.btn_k50_inc);
        btnK30dec_Kout = (Button) findViewById(R.id.btn_k30_dec_KasriOut);
        btnK30inc_Kout = (Button) findViewById(R.id.btn_k30_inc_KasriOut);
        btnK50dec_Kout = (Button) findViewById(R.id.btn_k50_dec_KasriOut);
        btnK50inc_Kout = (Button) findViewById(R.id.btn_k50_inc_KasriOut);

        eK30Count = (EditText) findViewById(R.id.edit_K30Count);
        eK50Count = (EditText) findViewById(R.id.edit_K50Count);
        eK30Count_Kout = (EditText) findViewById(R.id.edit_K30Count_KasriOut);
        eK50Count_Kout = (EditText) findViewById(R.id.edit_K50Count_KasriOut);
        eTakeMoney = (EditText) findViewById(R.id.e_TakeMoney);

        t_beerType = (TextView) findViewById(R.id.t_ludisDasaxeleba);
        t_deliveryInfo = (TextView) findViewById(R.id.t_DeliveryInfo);
        t_davalanebaM = (TextView) findViewById(R.id.t_davalianebaM);
        t_davalanebaK = (TextView) findViewById(R.id.t_davalianebaK);
        t_ludi_in = (TextView) findViewById(R.id.t_mitana_ludi);
        t_comment = (TextInputLayout) findViewById(R.id.t_mitana_comment);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_k30_dec:
                eK30Count.setText(pliusMinusText(eK30Count.getText().toString(), false));
                break;
            case R.id.btn_k30_inc:
                eK30Count.setText(pliusMinusText(eK30Count.getText().toString(), true));
                break;
            case R.id.btn_k50_dec:
                eK50Count.setText(pliusMinusText(eK50Count.getText().toString(), false));
                break;
            case R.id.btn_k50_inc:
                eK50Count.setText(pliusMinusText(eK50Count.getText().toString(), true));
                break;
            case R.id.btn_k30_dec_KasriOut:
                eK30Count_Kout.setText(pliusMinusText(eK30Count_Kout.getText().toString(), false));
                break;
            case R.id.btn_k30_inc_KasriOut:
                eK30Count_Kout.setText(pliusMinusText(eK30Count_Kout.getText().toString(), true));
                break;
            case R.id.btn_k50_dec_KasriOut:
                eK50Count_Kout.setText(pliusMinusText(eK50Count_Kout.getText().toString(), false));
                break;
            case R.id.btn_k50_inc_KasriOut:
                eK50Count_Kout.setText(pliusMinusText(eK50Count_Kout.getText().toString(), true));
                break;
        }
        priceCalculation();
    }

    private void sendDataToDB() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // 1. ludis shetana
        StringRequest request_mitana = new StringRequest(Request.Method.POST, Constantebi.URL_INS_LUDISSHETANA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "მონაცემები ჩაწერილია!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                btn_Done.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (!(eK30Count.getText().toString().equals("") && eK50Count.getText().toString().equals(""))) {
                    params.put("mitana", "1");
                }else {
                    params.put("mitana", "0");
                }

                if (!(eK30Count_Kout.getText().toString().equals("") && eK50Count_Kout.getText().toString().equals(""))) {
                    params.put("kout", "1");
                }else {
                    params.put("kout", "0");
                }

                if (!eTakeMoney.getText().toString().equals("")) {
                    params.put("mout", "1");
                }else {
                    params.put("mout", "0");
                }

                params.put("obieqtis_id", currObieqti.getId().toString());
                params.put("distributor_id", Constantebi.USER_ID);
                params.put("comment", t_comment.getEditText().getText().toString());

                params.put("beer_type", String.valueOf(String.valueOf(beerId)));
                params.put("ert_fasi", currObieqti.getFasebi().get(beertype).toString());// chasasworebelia
                params.put("k30", eK30Count.getText().toString());
                params.put("k50", eK50Count.getText().toString());

                params.put("k30out", eK30Count_Kout.getText().toString());
                params.put("k50out", eK50Count_Kout.getText().toString());

                params.put("tanxa", eTakeMoney.getText().toString());

                params.toString();
                return params;
            }
        };

        queue.add(request_mitana);
    }
}
