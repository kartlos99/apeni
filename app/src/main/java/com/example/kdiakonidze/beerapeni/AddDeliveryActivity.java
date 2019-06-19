package com.example.kdiakonidze.beerapeni;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.customView.BeerTempRow;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.SawyobiDetailRow;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDeliveryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView t_deliveryInfo, t_beerType, t_davalanebaM, t_davalanebaK, t_ludi_in, t_2title, t_3title;
    private EditText eK30Count, eK50Count, eK30Count_Kout, eK50Count_Kout, eTakeMoney;
    private Button btn_Done, btnBeerLeft, btnBeerRight, btnK30dec, btnK30inc, btnK50dec, btnK50inc, btnK30dec_Kout, btnK30inc_Kout, btnK50dec_Kout, btnK50inc_Kout, btn_changeDate;
    private CardView cardView_mitana, cardView_kout, cardView_mout;
    private TextInputLayout t_comment;
    private ProgressDialog progressDialog;
    private CheckBox chk_sachuqari, chk_ptichka;
    private FloatingActionButton fab_addBeer;
    private LinearLayout linear_BeerConteiner;

    private Context mContext;

    private ArrayList<Shekvetebi> tempInputList = new ArrayList<>();
    private SawyobiDetailRow row;
    private Boolean sawyobi = false;

    private Obieqti currObieqti;
    Calendar calendar;
    String archeuli_tarigi;
    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;

    private int editingId = 0;
    private Integer beerIndex = 0, beerId = 0;
    private String reason, operacia;
    private String initialComment = "";

    @Override
    protected void onResume() {
        super.onResume();
        if (currObieqti != null) {
            priceCalculation(eK30Count.getText().toString(), eK50Count.getText().toString());
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);
            archeuli_tarigi = timeFormat.format(calendar.getTime());
            btn_changeDate.setText(dateFormat.format(calendar.getTime()));
        }
    };

    private RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (t_comment.getEditText() != null) {
            outState.putString("comment", t_comment.getEditText().getText().toString());
        }
        outState.putString("dro", archeuli_tarigi);
        outState.putInt("beer", beerIndex);
        outState.putSerializable("obieqti", currObieqti);
        outState.putBoolean("sawy", sawyobi);
        outState.putSerializable("tempOrders", tempInputList);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);

        dateFormat = new SimpleDateFormat(getString(R.string.patern_date));
        timeFormat = new SimpleDateFormat(getString(R.string.patern_datetime));
        initial_findviews();
        mContext = this;

        calendar = Calendar.getInstance();
//        calendar.add(Calendar.HOUR, 4);
        archeuli_tarigi = timeFormat.format(calendar.getTime());

        if (savedInstanceState != null) {

            currObieqti = (Obieqti) savedInstanceState.getSerializable("obieqti");
            if (t_comment.getEditText() != null) {
                t_comment.getEditText().setText(savedInstanceState.getString("comment"));
            }
            archeuli_tarigi = savedInstanceState.getString("dro");
            btn_changeDate.setText(archeuli_tarigi);
            sawyobi = savedInstanceState.getBoolean("sawy");

            beerIndex = savedInstanceState.getInt("beer");
            if (sawyobi) {
                t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
            } else {
                t_beerType.setText(String.format("%s\n%s", Constantebi.ludiList.get(beerIndex).getDasaxeleba(), currObieqti.getFasebi().get(beerIndex)));
            }
            beerId = Constantebi.ludiList.get(beerIndex).getId();

            tempInputList = MyUtil.objToOrderList(savedInstanceState.getSerializable("tempOrders"));
            for (Shekvetebi item : tempInputList) {
                BeerTempRow beerTempRow = new BeerTempRow(mContext, item, linear_BeerConteiner, tempInputList, t_ludi_in);
                linear_BeerConteiner.addView(beerTempRow);
            }
        }

        Intent i = getIntent();
        reason = i.getStringExtra(Constantebi.REASON);
        if (reason.equals(Constantebi.CREATE)) {
            Bundle importedBundle = i.getExtras();
            if (importedBundle != null) {
                currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");
                if (currObieqti != null) {
                    t_deliveryInfo.setText(currObieqti.getDasaxeleba());
                    t_beerType.setText(String.format("%s\n%s", Constantebi.ludiList.get(beerIndex).getDasaxeleba(), currObieqti.getFasebi().get(beerIndex)));
                }
                beerId = Constantebi.ludiList.get(beerIndex).getId();
                get_davalianeba();
            }
        } else {
            operacia = i.getStringExtra("operacia");
            editingId = i.getIntExtra("id", 0);
            archeuli_tarigi = i.getStringExtra("tarigi");
            btn_changeDate.setText(archeuli_tarigi);

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
                chk_sachuqari.setVisibility(View.INVISIBLE);
                get_info(operacia, editingId);
            }
            if (operacia.equals(Constantebi.M_OUT)) {
                t_ludi_in.setVisibility(View.GONE);
                t_2title.setVisibility(View.GONE);
                cardView_mitana.setVisibility(View.GONE);
                cardView_kout.setVisibility(View.GONE);
                chk_sachuqari.setVisibility(View.INVISIBLE);
                eTakeMoney.setText(String.valueOf(i.getFloatExtra("tanxa", 0.0f)));
                findObject(i.getIntExtra("objid", 0));
            }

            // es 2 sawyobshi shetana gatanistvis
            if (operacia.equals(Constantebi.SAWYOBSHI_SHETANA)) {
                t_2title.setVisibility(View.GONE);
                t_3title.setVisibility(View.GONE);
                cardView_kout.setVisibility(View.GONE);
                cardView_mout.setVisibility(View.GONE);
                if (i.getExtras() != null) {
                    Bundle bundle = i.getExtras().getBundle("data");
                    row = (SawyobiDetailRow) (bundle != null ? bundle.getSerializable("edit_row") : null);
                    if (row != null) {
                        showRow(row);
                    }
                }
                sawyobi = true;
            }
            if (operacia.equals(Constantebi.SAWYOBIDAN_GATANA)) {
                t_ludi_in.setVisibility(View.GONE);
                t_3title.setVisibility(View.GONE);
                cardView_mitana.setVisibility(View.GONE);
                cardView_mout.setVisibility(View.GONE);
                if (i.getExtras() != null) {
                    Bundle bundle = i.getExtras().getBundle("data");
                    row = (SawyobiDetailRow) (bundle != null ? bundle.getSerializable("edit_row") : null);
                    if (row != null) {
                        showRow(row);
                    }
                }
                sawyobi = true;
            }

            fab_addBeer.setVisibility(View.GONE);
            linear_BeerConteiner.setVisibility(View.GONE);
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
                beerId = Constantebi.ludiList.get(beerIndex).getId();
                if (sawyobi) {
                    t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
                } else {
                    t_beerType.setText(String.format("%s\n%s", Constantebi.ludiList.get(beerIndex).getDasaxeleba(), currObieqti.getFasebi().get(beerIndex)));
                    priceCalculation(eK30Count.getText().toString(), eK50Count.getText().toString());
                }
            }
        });

        btnBeerRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beerIndex++;
                if (beerIndex == Constantebi.ludiList.size()) {
                    beerIndex = beerIndex - Constantebi.ludiList.size();
                }
                beerId = Constantebi.ludiList.get(beerIndex).getId();
                if (sawyobi) {
                    t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
                } else {
                    t_beerType.setText(String.format("%s\n%s", Constantebi.ludiList.get(beerIndex).getDasaxeleba(), currObieqti.getFasebi().get(beerIndex)));
                    priceCalculation(eK30Count.getText().toString(), eK50Count.getText().toString());
                }
            }
        });

        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reason.equals(Constantebi.CREATE)) {
                    if (tempInputList.size() == 0 && chekMitanaFealds().equals("1")) {
                        addAnotherBeer();
                    }

                    if (chekMitana().equals("1") || chekKout().equals("1") || chekMout().equals("1")) {
                        btn_Done.setEnabled(false);
                        sendDataToDB(0, chekMitana(), chekKout(), chekMout(), false);
                    } else {
                        Toast.makeText(mContext, getString(R.string.no_val_to_save), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (operacia.equals(Constantebi.MITANA)) {
                        if (chekMitana().equals("1")) {
                            btn_Done.setEnabled(false);
                            sendDataToDB(editingId, "1", "0", "0", false);
                        }
                    }
                    if (operacia.equals(Constantebi.K_OUT)) {
                        if (chekKout().equals("1")) {
                            btn_Done.setEnabled(false);
                            sendDataToDB(editingId, "0", "1", "0", false);
                        }
                    }
                    if (operacia.equals(Constantebi.M_OUT)) {
                        if (chekMout().equals("1")) {
                            btn_Done.setEnabled(false);
                            sendDataToDB(editingId, "0", "0", "1", false);
                        }
                    }
                    if (operacia.equals(Constantebi.SAWYOBSHI_SHETANA)) {
                        if (chekMitana().equals("1")) {  // carielo rom ar iyos velebi
                            btn_Done.setEnabled(false);
                            sendDataToDB(Integer.parseInt(row.getId()), "0", "0", "0", true);
                        }
                    }
                    if (operacia.equals(Constantebi.SAWYOBIDAN_GATANA)) {
                        if (chekKout().equals("1")) {
                            btn_Done.setEnabled(false);
                            sendDataToDB(Integer.parseInt(row.getId()), "0", "0", "0", true);
                        }
                    }
                }
            }
        });

        btn_changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddDeliveryActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        chk_sachuqari.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!sawyobi) {
                    priceCalculation(eK30Count.getText().toString(), eK50Count.getText().toString());
                    if (t_comment.getEditText().getText().toString().equals("") && b) {
                        t_comment.getEditText().setText("საჩუქრად!");
                    }
                    if (t_comment.getEditText().getText().toString().equals("საჩუქრად!") && !b) {
                        t_comment.getEditText().setText("");
                    }
                }
            }
        });

        fab_addBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnotherBeer();
            }
        });
    }

    void addAnotherBeer() {
        if (chekForExists(Constantebi.ludiList.get(beerIndex).getDasaxeleba())) {
            Toast.makeText(getApplicationContext(), R.string.alredy_in_list, Toast.LENGTH_SHORT).show();
        } else {
            if ((eK30Count.getText().toString().equals("0") || eK30Count.getText().toString().isEmpty())
                    && (eK50Count.getText().toString().equals("0") || eK50Count.getText().toString().isEmpty())) {
                Toast.makeText(getApplicationContext(), R.string.msg_enterKasrQuantity, Toast.LENGTH_SHORT).show();
            } else {
                Shekvetebi newInput = new Shekvetebi("",
                        Constantebi.ludiList.get(beerIndex).getDasaxeleba(),
                        Float.valueOf(eK30Count.getText().toString().isEmpty() ? "0" : eK30Count.getText().toString()),
                        Float.valueOf(eK50Count.getText().toString().isEmpty() ? "0" : eK50Count.getText().toString()), 0, 0
                );
                newInput.setColor(Constantebi.ludiList.get(beerIndex).getDisplayColor());
                newInput.setBeer_id(Constantebi.ludiList.get(beerIndex).getId());
                newInput.setComment(currObieqti.getFasebi().get(beerIndex).toString()); // droebit aq vinaxavt fass

                BeerTempRow beerTempRow = new BeerTempRow(mContext, newInput, linear_BeerConteiner, tempInputList, t_ludi_in);
                linear_BeerConteiner.addView(beerTempRow);
                tempInputList.add(newInput);

                eK30Count.setText("");
                eK50Count.setText("");
                priceCalculation("", "");
            }
        }
    }

    boolean chekForExists(String ludi) {
        for (Shekvetebi currorder : tempInputList) {
            if (currorder.getLudi().equals(ludi)) {
                return true;
            }
        }
        return false;
    }

    private void showRow(SawyobiDetailRow row) {
        chk_sachuqari.setVisibility(View.INVISIBLE);
        if (t_comment.getEditText() != null)
            t_comment.getEditText().setText(row.getComment());
        beerId = row.getLudisID();

        if (beerId != 0) {
            t_ludi_in.setText("ჩამოტანა");
            eK30Count.setText(MyUtil.floatToSmartStr(row.getK30()));
            eK50Count.setText(MyUtil.floatToSmartStr(row.getK50()));
            for (int i = 0; i < Constantebi.ludiList.size(); i++) {
                if (Constantebi.ludiList.get(i).getId() == beerId) {
                    beerIndex = i;
                }
            }
            t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
        } else {
            t_2title.setText("წაღება");
            eK30Count_Kout.setText(MyUtil.floatToSmartStr(row.getK30()));
            eK50Count_Kout.setText(MyUtil.floatToSmartStr(row.getK50()));
            chk_ptichka.setVisibility(View.INVISIBLE);
        }

        archeuli_tarigi = row.getTarigi();
        btn_changeDate.setText(archeuli_tarigi);

        if (row.getChek().equals("1")) {
            chk_ptichka.setChecked(true);
        } else {
            chk_ptichka.setChecked(false);
        }

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

                            eK30Count.setText(MyUtil.floatToSmartStr(Float.valueOf(response.getJSONObject(0).getString("kasri30"))));
                            eK50Count.setText(MyUtil.floatToSmartStr(Float.valueOf(response.getJSONObject(0).getString("kasri50"))));
                            initialComment = response.getJSONObject(0).getString("comment");
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
                            t_beerType.setText(String.format("%s\n%s", Constantebi.ludiList.get(beerIndex).getDasaxeleba(), currObieqti.getFasebi().get(beerIndex)));
                            if (response.getJSONObject(0).getString("chek").equals("1")) {
                                chk_ptichka.setChecked(true);
                            } else {
                                chk_ptichka.setChecked(false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    if (operacia.equals(Constantebi.K_OUT)) {
                        try {
                            eK30Count_Kout.setText(MyUtil.floatToSmartStr(Float.valueOf(response.getJSONObject(0).getString("kasri30"))));
                            eK50Count_Kout.setText(MyUtil.floatToSmartStr(Float.valueOf(response.getJSONObject(0).getString("kasri50"))));
                            initialComment = response.getJSONObject(0).getString("comment");

                            for (int i = 0; i < Constantebi.OBIEQTEBI.size(); i++) {
                                if (Constantebi.OBIEQTEBI.get(i).getId() == response.getJSONObject(0).getInt("obieqtis_id")) {
                                    currObieqti = Constantebi.OBIEQTEBI.get(i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (t_comment.getEditText() != null)
                        t_comment.getEditText().setText(initialComment);
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

    private void priceCalculation(String k30text, String k50text) {
        float k30v, k50v;
        k30v = k30text.isEmpty() ? 0 : Float.parseFloat(k30text);
        k50v = k50text.isEmpty() ? 0 : Float.parseFloat(k50text);

        float fasi = 0.0f;
        if (!chk_sachuqari.isChecked()) {
            fasi = (k30v * 30 * currObieqti.getFasebi().get(beerIndex)) + (k50v * 50 * currObieqti.getFasebi().get(beerIndex));
        }
        fasi += MyUtil.tempListPrice(tempInputList);
        t_ludi_in.setText(String.format("%s (%s \u20BE )", getResources().getString(R.string.ludis_shetana), MyUtil.floatToSmartStr(fasi)));
        Log.d("PrCalc:", String.format("%s %s f-%s", k30v, k50v, fasi));
    }

    private void get_davalianeba() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constantebi.URL_GET_DAVALIANEBA;

        JsonArrayRequest request_davalianeba = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis davalianebis chamonaTvali yvela obieqtistvis

                if (response.length() > 0) {
                    float davalianebaM = 0, davalianebaK = 0;

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            if (response.getJSONObject(i).getInt("obj_id") == currObieqti.getId()) {
                                davalianebaM = (float) response.getJSONObject(i).getDouble("pr") - (float) response.getJSONObject(i).getDouble("pay");
                                davalianebaK = (float) response.getJSONObject(i).getDouble("k30in") - (float) response.getJSONObject(i).getDouble("k30out")
                                        + (float) response.getJSONObject(i).getDouble("k50in") - (float) response.getJSONObject(i).getDouble("k50out");
                            }

                            t_davalanebaM.setText(String.format("%s%n%s %s", getString(R.string.davalianeba), MyUtil.floatToSmartStr(davalianebaM), getString(R.string.lari)));
                            t_davalanebaK.setText(String.format("%s%n%s", getString(R.string.kasri), MyUtil.floatToSmartStr(davalianebaK)));

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
        btnBeerLeft = findViewById(R.id.btn_beerleft);
        btnBeerRight = findViewById(R.id.btn_beerright);
        btn_Done = findViewById(R.id.btn_beerInputDone);
        btn_changeDate = findViewById(R.id.btn_change_date);
        if (Constantebi.USER_TYPE.equals(Constantebi.USER_TYPE_user))
            btn_changeDate.setEnabled(false);

        btnK30dec = findViewById(R.id.btn_k30_dec);
        btnK30inc = findViewById(R.id.btn_k30_inc);
        btnK50dec = findViewById(R.id.btn_k50_dec);
        btnK50inc = findViewById(R.id.btn_k50_inc);
        btnK30dec_Kout = findViewById(R.id.btn_k30_dec_KasriOut);
        btnK30inc_Kout = findViewById(R.id.btn_k30_inc_KasriOut);
        btnK50dec_Kout = findViewById(R.id.btn_k50_dec_KasriOut);
        btnK50inc_Kout = findViewById(R.id.btn_k50_inc_KasriOut);

        eK30Count = findViewById(R.id.edit_K30Count);
        eK50Count = findViewById(R.id.edit_K50Count);
        eK30Count_Kout = findViewById(R.id.edit_K30Count_KasriOut);
        eK50Count_Kout = findViewById(R.id.edit_K50Count_KasriOut);
        eTakeMoney = findViewById(R.id.e_TakeMoney);

        t_beerType = findViewById(R.id.t_ludisDasaxeleba);
        t_deliveryInfo = findViewById(R.id.t_DeliveryInfo);
        t_davalanebaM = findViewById(R.id.t_davalianebaM);
        t_davalanebaK = findViewById(R.id.t_davalianebaK);
        t_ludi_in = findViewById(R.id.t_mitana_ludi);
        t_comment = findViewById(R.id.t_mitana_comment);

//        t_1title = findViewById(R.id.t_mitana_ludi);
        t_2title = findViewById(R.id.t_wamogebuliKasrebi);
        t_3title = findViewById(R.id.textView2);

        cardView_mitana = findViewById(R.id.cardView_add_Beer1);
        cardView_kout = findViewById(R.id.cardView_TakeKasri);
        cardView_mout = findViewById(R.id.card_tanxa);
        chk_sachuqari = findViewById(R.id.ckbox_sachuqari);
        chk_ptichka = findViewById(R.id.mitanis_ptichka);

        fab_addBeer = findViewById(R.id.btn_anotherBeer);
        linear_BeerConteiner = findViewById(R.id.linear_conteiner);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_k30_dec:
                eK30Count.setText(MyUtil.pliusMinusText(eK30Count.getText().toString(), getString(R.string.minusi)));
                break;
            case R.id.btn_k30_inc:
                eK30Count.setText(MyUtil.pliusMinusText(eK30Count.getText().toString(), getString(R.string.pliusi)));
                break;
            case R.id.btn_k50_dec:
                eK50Count.setText(MyUtil.pliusMinusText(eK50Count.getText().toString(), getString(R.string.minusi)));
                break;
            case R.id.btn_k50_inc:
                eK50Count.setText(MyUtil.pliusMinusText(eK50Count.getText().toString(), getString(R.string.pliusi)));
                break;
            case R.id.btn_k30_dec_KasriOut:
                eK30Count_Kout.setText(MyUtil.pliusMinusText(eK30Count_Kout.getText().toString(), getString(R.string.minusi)));
                break;
            case R.id.btn_k30_inc_KasriOut:
                eK30Count_Kout.setText(MyUtil.pliusMinusText(eK30Count_Kout.getText().toString(), getString(R.string.pliusi)));
                break;
            case R.id.btn_k50_dec_KasriOut:
                eK50Count_Kout.setText(MyUtil.pliusMinusText(eK50Count_Kout.getText().toString(), getString(R.string.minusi)));
                break;
            case R.id.btn_k50_inc_KasriOut:
                eK50Count_Kout.setText(MyUtil.pliusMinusText(eK50Count_Kout.getText().toString(), getString(R.string.pliusi)));
                break;
        }
        if (!sawyobi)
            priceCalculation(eK30Count.getText().toString(), eK50Count.getText().toString());
    }

    private void sendDataToDB(final Integer editId, final String sMitana, final String sKout, final String sMout, final Boolean sawyobi) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url;
        if (sawyobi) {
            url = Constantebi.URL_INS_SAWYOBI;
        } else {
            url = Constantebi.URL_INS_LUDISSHETANA;
        }

        // ludis shetana, kasris ageba, fulis ageba
        StringRequest request_mitana = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "მონაცემები ჩაწერილია! " + response, Toast.LENGTH_SHORT).show();
                Log.d("resp", response);
                if (!sawyobi) {
                    OrdersActivity.chamosatvirtia = true; // mitanas rom davakreqtirebt, amit mixvdebarom ganaaxlos shekvetebis gverdi
                    if (t_comment.getEditText() != null)
                        if (!t_comment.getEditText().getText().toString().equals(initialComment)) {
                            MyUtil util = new MyUtil(getApplicationContext());
                            util.notifyFirebase();
                            MainActivity.NEED_COMENTS_UPDATE = true;
                        }
                } else {
                    SawyobiList.chamosatvirtia = true;
                }
                setRequestedOrientation(Constantebi.screenDefOrientation);
                onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString() + " -", Toast.LENGTH_SHORT).show();
                setRequestedOrientation(Constantebi.screenDefOrientation);
                btn_Done.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", editId.toString());
                params.put("mitana", sMitana);
                params.put("kout", sKout);
                params.put("mout", sMout);
                if (sawyobi) {
                    if (row.getLudisID() == 0) {
                        params.put("wageba", "1");
                    } else {
                        params.put("chamotana", "1");
                    }
                    if (chk_ptichka.isChecked()) {
                        params.put("chek", "1");
                    } else {
                        params.put("chek", "0");
                    }
                } else {
                    params.put("obieqtis_id", currObieqti.getId().toString());
                    if (chk_ptichka.isChecked()) {
                        params.put("chek", "1");
                    }
                }

                params.put("distributor_id", Constantebi.USER_ID);
                if (t_comment.getEditText() != null)
                    params.put("comment", t_comment.getEditText().getText().toString());

                // *********

                if (reason.equals(Constantebi.CREATE)) {
                    for (int i = 0; i < tempInputList.size(); i++) {
                        params.put("k30[" + i + "]", MyUtil.floatToSmartStr(tempInputList.get(i).getK30in()));
                        params.put("k50[" + i + "]", MyUtil.floatToSmartStr(tempInputList.get(i).getK50in()));
                        params.put("beer_id[" + i + "]", String.valueOf(tempInputList.get(i).getBeer_id()));
                        params.put("ert_fasi[" + i + "]", chk_sachuqari.isChecked() ? "0" : tempInputList.get(i).getComment());
                    }
                } else {
                    params.put("beer_id", String.valueOf(beerId));
                    params.put("k30", eK30Count.getText().toString());
                    params.put("k50", eK50Count.getText().toString());
                    if (!sawyobi) {
                        params.put("ert_fasi", currObieqti.getFasebi().get(beerIndex).toString());
                    }
                }

                params.put("k30out", eK30Count_Kout.getText().toString());
                params.put("k50out", eK50Count_Kout.getText().toString());

                params.put("tanxa", eTakeMoney.getText().toString());
                params.put("set_tarigi", archeuli_tarigi);

                return params;
            }
        };

        request_mitana.setRetryPolicy(mRetryPolicy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request_mitana);
    }

    private String chekMout() {
        if (eTakeMoney.getText().toString().equals("0")) {
            eTakeMoney.setText("");
        }
        return (eTakeMoney.getText().toString().equals("") ? "0" : "1");
    }

    private String chekKout() {
        if (eK30Count_Kout.getText().toString().equals("0")) {
            eK30Count_Kout.setText("");
        }
        if (eK50Count_Kout.getText().toString().equals("0")) {
            eK50Count_Kout.setText("");
        }
        return (eK30Count_Kout.getText().toString().equals("") && eK50Count_Kout.getText().toString().equals("") ? "0" : "1");
    }

    private String chekMitana() {
        if (reason.equals(Constantebi.CREATE)) {
            return tempInputList.size() > 0 ? "1" : "0";
        } else {
            if (eK30Count.getText().toString().equals("0")) {
                eK30Count.setText("");
            }
            if (eK50Count.getText().toString().equals("0")) {
                eK50Count.setText("");
            }
            return (eK30Count.getText().toString().equals("") && eK50Count.getText().toString().equals("") ? "0" : "1");
        }
    }

    private String chekMitanaFealds() {
        if (eK30Count.getText().toString().equals("0")) {
            eK30Count.setText("");
        }
        if (eK50Count.getText().toString().equals("0")) {
            eK50Count.setText("");
        }
        return (eK30Count.getText().toString().equals("") && eK50Count.getText().toString().equals("") ? "0" : "1");
    }
}
