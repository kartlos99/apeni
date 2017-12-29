package com.example.kdiakonidze.beerapeni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

    private TextView t_deliveryInfo, t_beerType, t_davalanebaM, t_davalanebaK, t_ludi_in, t_1title, t_2title, t_3title;
    private EditText eK30Count, eK50Count, eK30Count_Kout, eK50Count_Kout, eTakeMoney;
    private Button btn_Done, btnBeerLeft, btnBeerRight, btnK30dec, btnK30inc, btnK50dec, btnK50inc, btnK30dec_Kout, btnK30inc_Kout, btnK50dec_Kout, btnK50inc_Kout;
    private CardView cardView_mitana, cardView_kout, cardView_mout;
    private TextInputLayout t_comment;
    private ProgressDialog progressDialog;

    private Obieqti currObieqti;

    private int editingId = 0;
    private Integer beerIndex = 0, beerId = 0;
    private String reason, operacia;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("comment", t_comment.getEditText().getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);
        initial_findviews();

        if (savedInstanceState != null) {
            t_comment.getEditText().setText(savedInstanceState.getString("comment"));
        }

        Intent i = getIntent();
        reason = i.getStringExtra(Constantebi.REASON);
        if (reason.equals(Constantebi.CREATE)) {
            Bundle importedBundle = i.getExtras();
            currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");
            t_deliveryInfo.setText(currObieqti.getDasaxeleba());
            t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba() + "\n" + currObieqti.getFasebi().get(beerIndex));
            beerId = Constantebi.ludiList.get(beerIndex).getId();
            get_davalianeba();
        } else {
            operacia = i.getStringExtra("operacia");
            editingId = i.getIntExtra("id", 0);
            if (operacia.equals(Constantebi.MITANA)) {
                t_2title.setVisibility(View.GONE);
                t_3title.setVisibility(View.GONE);
                cardView_kout.setVisibility(View.GONE);
                cardView_mout.setVisibility(View.GONE);
                get_info(operacia, editingId);
            }
            if (operacia.equals(Constantebi.K_OUT)) {
                t_ludi_in.setVisibility(View.GONE);
                t_3title.setVisibility(View.GONE);
                cardView_mitana.setVisibility(View.GONE);
                cardView_mout.setVisibility(View.GONE);
                get_info(operacia, editingId);
            }
            if (operacia.equals(Constantebi.M_OUT)) {
                t_ludi_in.setVisibility(View.GONE);
                t_2title.setVisibility(View.GONE);
                cardView_mitana.setVisibility(View.GONE);
                cardView_kout.setVisibility(View.GONE);
                eTakeMoney.setText(String.valueOf(i.getDoubleExtra("tanxa",0.0)));
                findObject(i.getIntExtra("objid", 0));
            }
        }

        btnK30dec.setOnClickListener(this);
        btnK30inc.setOnClickListener(this);
        btnK50dec.setOnClickListener(this);
        btnK50inc.setOnClickListener(this);
        btnK30dec_Kout.setOnClickListener(this);
        btnK30inc_Kout.setOnClickListener(this);
        btnK50dec_Kout.setOnClickListener(this);
        btnK50inc_Kout.setOnClickListener(this);

        btnBeerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beerIndex--;
                if (beerIndex < 0) {
                    beerIndex = beerIndex + Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba() + "\n" + currObieqti.getFasebi().get(beerIndex));
                beerId = Constantebi.ludiList.get(beerIndex).getId();
                priceCalculation();
            }
        });

        btnBeerRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beerIndex++;
                if (beerIndex == Constantebi.ludiList.size()) {
                    beerIndex = beerIndex - Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba() + "\n" + currObieqti.getFasebi().get(beerIndex));
                beerId = Constantebi.ludiList.get(beerIndex).getId();
                priceCalculation();
            }
        });

        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reason.equals(Constantebi.CREATE)) {
                    if (chekMitana().equals("1") || chekKout().equals("1") || chekMout().equals("1")) {
                        btn_Done.setEnabled(false);
                        sendDataToDB(0, chekMitana(), chekKout(), chekMout());
                    }
                } else {
                    if (operacia.equals(Constantebi.MITANA)) {
                        if (chekMitana().equals("1")) {
                            btn_Done.setEnabled(false);
                            sendDataToDB(editingId, "1", "0", "0");
                        }
                    }
                    if (operacia.equals(Constantebi.K_OUT)) {
                        if (chekKout().equals("1")) {
                            btn_Done.setEnabled(false);
                            sendDataToDB(editingId, "0", "1", "0");
                        }
                    }
                    if (operacia.equals(Constantebi.M_OUT)) {
                        if (chekMout().equals("1")) {
                            btn_Done.setEnabled(false);
                            sendDataToDB(editingId, "0", "0", "1");
                        }
                    }
                }
            }
        });


    }

    private void findObject(int objid) {
        for (int i = 0; i < Constantebi.OBIEQTEBI.size(); i++) {
            if (Constantebi.OBIEQTEBI.get(i).getId() == objid) {
                currObieqti = Constantebi.OBIEQTEBI.get(i);
                t_deliveryInfo.setText(currObieqti.getDasaxeleba());
            }
        }
    }

    private void get_info(final String operacia, int editingId) {

        JsonArrayRequest request_Record = new JsonArrayRequest(Constantebi.URL_GET_RECORD + "?table=" + operacia + "&id=" + editingId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length() > 0) {
                    if (operacia.equals(Constantebi.MITANA)) {
                        try {

                            eK30Count.setText(response.getJSONObject(0).getString("kasri30"));
                            eK50Count.setText(response.getJSONObject(0).getString("kasri50"));
                            t_comment.getEditText().setText(response.getJSONObject(0).getString("comment"));
                            beerId = response.getJSONObject(0).getInt("ludis_id");

                            for (int i = 0; i < Constantebi.OBIEQTEBI.size(); i++) {
                                if (Constantebi.OBIEQTEBI.get(i).getId() == response.getJSONObject(0).getInt("obieqtis_id")) {
                                    currObieqti = Constantebi.OBIEQTEBI.get(i);
                                }
                            }
                            for (int i = 0; i < Constantebi.ludiList.size(); i++) {
                                if (Constantebi.ludiList.get(i).getId() == beerId) {
                                    beerIndex = i;
                                }
                            }

                            t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba() + "\n" + currObieqti.getFasebi().get(beerIndex));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (response.length() > 0) {
                        if (operacia.equals(Constantebi.K_OUT)) {
                            try {
                                eK30Count_Kout.setText(response.getJSONObject(0).getString("kasri30"));
                                eK50Count_Kout.setText(response.getJSONObject(0).getString("kasri50"));
                                t_comment.getEditText().setText(response.getJSONObject(0).getString("comment"));

                                for (int i = 0; i < Constantebi.OBIEQTEBI.size(); i++) {
                                    if (Constantebi.OBIEQTEBI.get(i).getId() == response.getJSONObject(0).getInt("obieqtis_id")) {
                                        currObieqti = Constantebi.OBIEQTEBI.get(i);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    t_deliveryInfo.setText(currObieqti.getDasaxeleba());

                } else {
                    Toast.makeText(getApplicationContext(), "xx :/", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        progressDialog = ProgressDialog.show(this, "იტვირთება!", "დაელოდეთ!");
        queue.add(request_Record);
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
        double fasi = Integer.valueOf(k30) * 30 * currObieqti.getFasebi().get(beerIndex) + Integer.valueOf(k50) * 50 * currObieqti.getFasebi().get(beerIndex);
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

        t_2title = (TextView) findViewById(R.id.t_wamogebuliKasrebi);
        t_3title = (TextView) findViewById(R.id.textView2);

        cardView_mitana = (CardView) findViewById(R.id.cardView_add_Beer1);
        cardView_kout = (CardView) findViewById(R.id.cardView_TakeKasri);
        cardView_mout = (CardView) findViewById(R.id.card_tanxa);
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

    private void sendDataToDB(final Integer editId, final String sMitana, final String sKout, final String sMout) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // 1. ludis shetana
        StringRequest request_mitana = new StringRequest(Request.Method.POST, Constantebi.URL_INS_LUDISSHETANA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "მონაცემები ჩაწერილია! " + response, Toast.LENGTH_SHORT).show();
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

                params.put("id", editId.toString());
                params.put("mitana", sMitana);
                params.put("kout", sKout);
                params.put("mout", sMout);

                params.put("obieqtis_id", currObieqti.getId().toString());
                params.put("distributor_id", Constantebi.USER_ID);
                params.put("comment", t_comment.getEditText().getText().toString());

                params.put("beer_type", String.valueOf(String.valueOf(beerId)));
                params.put("ert_fasi", currObieqti.getFasebi().get(beerIndex).toString());// chasasworebelia
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

    private String chekMout() {
        if (eTakeMoney.getText().toString().equals("0")) {
            eTakeMoney.setText("");
        }
        if (eTakeMoney.getText().toString().equals("")) {
            return "0";
        } else {
            return "1";
        }
    }

    private String chekKout() {
        if (eK30Count_Kout.getText().toString().equals("0")) {
            eK30Count_Kout.setText("");
        }
        if (eK50Count_Kout.getText().toString().equals("0")) {
            eK50Count_Kout.setText("");
        }
        if (eK30Count_Kout.getText().toString().equals("") && eK50Count_Kout.getText().toString().equals("")) {
            return "0";
        } else {
            return "1";
        }
    }

    private String chekMitana() {
        if (eK30Count.getText().toString().equals("0")) {
            eK30Count.setText("");
        }
        if (eK50Count.getText().toString().equals("0")) {
            eK50Count.setText("");
        }
        if (eK30Count.getText().toString().equals("") && eK50Count.getText().toString().equals("")) {
            return "0";
        } else {
            return "1";
        }
    }
}
