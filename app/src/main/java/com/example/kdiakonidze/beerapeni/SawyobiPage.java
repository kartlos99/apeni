package com.example.kdiakonidze.beerapeni;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

import com.example.kdiakonidze.beerapeni.adapters.SawyobiAdapter;
import com.example.kdiakonidze.beerapeni.customView.BeerTempRow;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.Totalinout;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SawyobiPage extends AppCompatActivity implements View.OnClickListener {

    // widgets
    private Toolbar toolbar;
    private Button btn_chawera, btn_beerleft, btn_beerright, btnK30dec, btnK30inc, btnK50dec, btnK50inc, btnK30dec_Kout, btnK30inc_Kout, btnK50dec_Kout, btnK50inc_Kout, btn_changeDate, btn_emptyK_list;
    private EditText eK30Count, eK50Count, eK30Count_Kout, eK50Count_Kout;
    private TextView t_beerType, t_empty30, t_empty50;
    private EditText t_comment;
    private NonScrollListView nonScrollistView;
    private ProgressDialog progressDialog;
    private CheckBox checkSawyobi;
    private ConstraintLayout constrCarieliBox;
    private ScrollView scrollView_sawy;
    private FloatingActionButton fab_addBeer;
    private LinearLayout linear_BeerConteiner;

    // variables
    private Context mContext;

    private ArrayList<Shekvetebi> tempBeerList = new ArrayList<>();
    private RequestQueue queue;
    private Calendar calendar;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateFormat;
    private ArrayList<Totalinout> totalinfo;
    private SawyobiAdapter sawyobiAdapter;

    private int beerIndex = 0, beerId = 0;
    private String sK30Count = "", sK50Count = "", sK30Count_Kout = "", sK50Count_Kout = "";
    private String archeuli_tarigi, archeuli_dro;
    private Boolean requestInProgres = false, requestNeeded = true;
    private float int_emptyK30 = 0, int_emptyK50 = 0, k30at_obj = 0, k50at_obj = 0;

    private RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);

            archeuli_dro = timeFormat.format(calendar.getTime());
            btn_changeDate.setText(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.HOUR, 24);
            archeuli_tarigi = dateFormat.format(calendar.getTime());
            getnashtebi(archeuli_tarigi, chek_condition());
        }
    };

    private String chek_condition() {
        return checkSawyobi.isChecked() ? "1" : "0";
    }

    public void showtext(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener btn_add_dec = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_k30_dec:
                    eK30Count_Kout.setText(MyUtil.pliusMinusText(eK30Count_Kout.getText().toString(), getString(R.string.minusi)));
                    break;
                case R.id.btn_k30_inc:
                    eK30Count_Kout.setText(MyUtil.pliusMinusText(eK30Count_Kout.getText().toString(), getString(R.string.pliusi)));
                    break;
                case R.id.btn_k50_dec:
                    eK50Count_Kout.setText(MyUtil.pliusMinusText(eK50Count_Kout.getText().toString(), getString(R.string.minusi)));
                    break;
                case R.id.btn_k50_inc:
                    eK50Count_Kout.setText(MyUtil.pliusMinusText(eK50Count_Kout.getText().toString(), getString(R.string.pliusi)));
                    break;
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("comment", t_comment.getText().toString());
        outState.putString("tarigi", archeuli_tarigi);
        outState.putString("dro", archeuli_dro);
        outState.putInt("beer", beerIndex);
        outState.putSerializable("data_array", totalinfo);
        outState.putString("k30", eK30Count.getText().toString());
        outState.putString("k50", eK50Count.getText().toString());
        outState.putString("k30out", eK30Count_Kout.getText().toString());
        outState.putString("k50out", eK50Count_Kout.getText().toString());
        outState.putFloat("empty30", int_emptyK30);
        outState.putFloat("empty50", int_emptyK50);
        outState.putFloat("k30at", k30at_obj);
        outState.putFloat("k50at", k50at_obj);
        outState.putSerializable("templist", tempBeerList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eK30Count.setText(sK30Count);
        eK50Count.setText(sK50Count);
        eK30Count_Kout.setText(sK30Count_Kout);
        eK50Count_Kout.setText(sK50Count_Kout);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawyobi_page);
        dateFormat = new SimpleDateFormat(getString(R.string.patern_date));
        timeFormat = new SimpleDateFormat(getString(R.string.patern_datetime));

        initialization();
        mContext = this;

        btn_emptyK_list.setEnabled(false);

        btnK30dec.setOnClickListener(this);
        btnK30inc.setOnClickListener(this);
        btnK50dec.setOnClickListener(this);
        btnK50inc.setOnClickListener(this);
        btnK30dec_Kout.setOnClickListener(btn_add_dec);
        btnK30inc_Kout.setOnClickListener(btn_add_dec);
        btnK50dec_Kout.setOnClickListener(btn_add_dec);
        btnK50inc_Kout.setOnClickListener(btn_add_dec);

        //-------------------------------

        queue = Volley.newRequestQueue(this);
        calendar = Calendar.getInstance();
        toolbar.setTitle(R.string.sawyobi);
        setSupportActionBar(toolbar);

        totalinfo = new ArrayList<>();

        if (savedInstanceState != null) {
//            totalinfo = (ArrayList<Totalinout>) savedInstanceState.getSerializable("data_array");
            Object listObj = savedInstanceState.getSerializable("data_array");
            if (listObj instanceof List) {
                for (int i = 0; i < ((List) listObj).size(); i++) {
                    Object item = ((List) listObj).get(i);
                    if (item instanceof Totalinout) {
                        totalinfo.add((Totalinout) item);
                    }
                }
            }
            sawyobiAdapter = new SawyobiAdapter(getApplicationContext(), totalinfo);
            nonScrollistView.setAdapter(sawyobiAdapter);
            beerIndex = savedInstanceState.getInt("beer");
            t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
            beerId = Constantebi.ludiList.get(beerIndex).getId();
            t_comment.setText(savedInstanceState.getString("comment"));
            archeuli_tarigi = savedInstanceState.getString("tarigi");
            archeuli_dro = savedInstanceState.getString("dro");

            sK30Count = savedInstanceState.getString("k30");
            sK50Count = savedInstanceState.getString("k50");
            sK30Count_Kout = savedInstanceState.getString("k30out");
            sK50Count_Kout = savedInstanceState.getString("k50out");

            //Toast.makeText(this, savedInstanceState.getString("k30") + " " + savedInstanceState.getString("k30out"), Toast.LENGTH_SHORT).show();
            int_emptyK30 = savedInstanceState.getFloat("empty30");
            int_emptyK50 = savedInstanceState.getFloat("empty50");
            k30at_obj = savedInstanceState.getFloat("k30at");
            k50at_obj = savedInstanceState.getFloat("k50at");
            vizualizacia();
            Date date1 = calendar.getTime();
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd").parse(archeuli_tarigi);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setTime(date1);
            calendar.add(Calendar.DATE, -1);
            btn_changeDate.setText(dateFormat.format(calendar.getTime()));
            tempBeerList = MyUtil.objToOrderList(savedInstanceState.getSerializable("templist"));
            for (Shekvetebi item : tempBeerList) {
                BeerTempRow beerTempRow = new BeerTempRow(mContext, item, linear_BeerConteiner, tempBeerList);
                linear_BeerConteiner.addView(beerTempRow);
            }
        } else {
            t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
            beerId = Constantebi.ludiList.get(beerIndex).getId();
            archeuli_tarigi = dateFormat.format(calendar.getTime());
            archeuli_dro = timeFormat.format(calendar.getTime());
            calendar.add(Calendar.HOUR, 24);
            archeuli_tarigi = dateFormat.format(calendar.getTime());
            getnashtebi(archeuli_tarigi, chek_condition());
        }


        btn_beerleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beerIndex--;
                if (beerIndex < 0) {
                    beerIndex = beerIndex + Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
                beerId = Constantebi.ludiList.get(beerIndex).getId();

            }
        });

        btn_beerright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beerIndex++;
                if (beerIndex == Constantebi.ludiList.size()) {
                    beerIndex = beerIndex - Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
                beerId = Constantebi.ludiList.get(beerIndex).getId();
            }
        });

        btn_changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SawyobiPage.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        btn_chawera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chamotana().equals("1") || wageba().equals("1")) {
                    btn_chawera.setEnabled(false);
                    sendDataToDB(chamotana(), wageba(), chek_condition());
                }
            }
        });

        checkSawyobi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getnashtebi(archeuli_tarigi, chek_condition());
                if (b) {
                    constrCarieliBox.setVisibility(View.GONE);
                    scrollView_sawy.setBackgroundColor(getResources().getColor(R.color.color_ForChek));
                } else {
                    constrCarieliBox.setVisibility(View.VISIBLE);
                    scrollView_sawy.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        btn_emptyK_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] items = new CharSequence[Constantebi.OBIEQTEBI.size()];
                for (int i = 0; i < Constantebi.OBIEQTEBI.size(); i++) {
                    items[i] = Constantebi.OBIEQTEBI.get(i).getDasaxeleba() + "  :  " + Constantebi.OBIEQTEBI.get(i).getValiK30() + " / " + Constantebi.OBIEQTEBI.get(i).getValiK50();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(SawyobiPage.this);

                builder.setTitle("ობიექტებზე დატოვებული კასრები 30/50")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();

                //Toast.makeText(getApplicationContext(), listK, Toast.LENGTH_LONG).show();
            }
        });

        fab_addBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                        BeerTempRow beerTempRow = new BeerTempRow(mContext, newInput, linear_BeerConteiner, tempBeerList);
                        linear_BeerConteiner.addView(beerTempRow);
                        tempBeerList.add(newInput);

                        eK30Count.setText("");
                        eK50Count.setText("");
                    }
                }
            }
        });

        get_davalianeba();
    }

    boolean chekForExists(String ludi) {
        for (Shekvetebi currorder : tempBeerList) {
            if (currorder.getLudi().equals(ludi)) {
                return true;
            }
        }
        return false;
    }

    private void initialization() {
        toolbar = findViewById(R.id.tool_bar);
        CardView cardView_nashti_full = findViewById(R.id.card_nashti_savse);
        CardView cardView_nashti_empty = findViewById(R.id.card_nashti_carieli);
        CardView cardView_chamotana = findViewById(R.id.card_chamotana);
        CardView cardView_wageba = findViewById(R.id.card_wageba);
        t_beerType = cardView_chamotana.findViewById(R.id.t_ludisDasaxeleba);
        t_comment = ((TextInputLayout) findViewById(R.id.t_mitana_comment)).getEditText();
        btn_chawera = findViewById(R.id.btn_sawyobi_chawera);
        btn_changeDate = findViewById(R.id.btn_change_date);
        btn_beerleft = cardView_chamotana.findViewById(R.id.btn_beerleft);
        btn_beerright = cardView_chamotana.findViewById(R.id.btn_beerright);
        btnK30dec = cardView_chamotana.findViewById(R.id.btn_k30_dec);
        btnK30inc = cardView_chamotana.findViewById(R.id.btn_k30_inc);
        btnK50dec = cardView_chamotana.findViewById(R.id.btn_k50_dec);
        btnK50inc = cardView_chamotana.findViewById(R.id.btn_k50_inc);
        btnK30dec_Kout = cardView_wageba.findViewById(R.id.btn_k30_dec);
        btnK30inc_Kout = cardView_wageba.findViewById(R.id.btn_k30_inc);
        btnK50dec_Kout = cardView_wageba.findViewById(R.id.btn_k50_dec);
        btnK50inc_Kout = cardView_wageba.findViewById(R.id.btn_k50_inc);
        eK30Count = cardView_chamotana.findViewById(R.id.edit_K30Count);
        eK50Count = cardView_chamotana.findViewById(R.id.edit_K50Count);
        eK30Count_Kout = cardView_wageba.findViewById(R.id.edit_K30Count);
        eK50Count_Kout = cardView_wageba.findViewById(R.id.edit_K50Count);
        t_empty30 = cardView_nashti_empty.findViewById(R.id.t_empty_k30);
        t_empty50 = cardView_nashti_empty.findViewById(R.id.t_empty_k50);
        constrCarieliBox = findViewById(R.id.constr_carieli_box);
        scrollView_sawy = findViewById(R.id.scrool_sawyobi);
        nonScrollistView = cardView_nashti_full.findViewById(R.id.list_forNashtebi);
        checkSawyobi = findViewById(R.id.checkBox_sawyobi);
        btn_emptyK_list = findViewById(R.id.btn_emptyK_list);

        cardView_wageba.findViewById(R.id.btn_beerleft).setVisibility(View.GONE);
        cardView_wageba.findViewById(R.id.btn_beerright).setVisibility(View.GONE);
        cardView_wageba.findViewById(R.id.t_ludisDasaxeleba).setVisibility(View.GONE);

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
    }

    private void sendDataToDB(final String sChamotana, final String sWageba, final String chek) {
        //RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // kasrebis chamotana da gagzavna sawyobidan
        StringRequest request_mitana = new StringRequest(Request.Method.POST, Constantebi.URL_INS_SAWYOBI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "მონაცემები ჩაწერილია! " + response, Toast.LENGTH_SHORT).show();
                setRequestedOrientation(Constantebi.screenDefOrientation);
                onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString() + " -", Toast.LENGTH_SHORT).show();
                setRequestedOrientation(Constantebi.screenDefOrientation);
                btn_chawera.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", "0");
                params.put("chamotana", sChamotana);
                params.put("wageba", sWageba);

                params.put("distributor_id", Constantebi.USER_ID);
                params.put("comment", t_comment.getText().toString());

                // chamotanili ksrebi
                for (int i = 0; i < tempBeerList.size(); i++) {
                    params.put("k30[" + i + "]", MyUtil.floatToSmartStr(tempBeerList.get(i).getK30in()));
                    params.put("k50[" + i + "]", MyUtil.floatToSmartStr(tempBeerList.get(i).getK50in()));
                    params.put("beer_id[" + i + "]", String.valueOf(tempBeerList.get(i).getBeer_id()));
                }

                // wagebuli kasrebi
                params.put("k30out", eK30Count_Kout.getText().toString());
                params.put("k50out", eK50Count_Kout.getText().toString());

                params.put("set_tarigi", archeuli_dro);
                params.put("chek", chek);

                return params;
            }
        };

        request_mitana.setRetryPolicy(mRetryPolicy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request_mitana);
    }

    private void getnashtebi(String tildate, String chek) {

        //Toast.makeText(SawyobiPage.this, "?tarigi="+tildate, Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constantebi.URL_GET_NASHTI + "?tarigi=" + tildate + "&chek=" + chek, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                totalinfo.clear();
                k30at_obj = 0;
                k50at_obj = 0;

                if (response.length() > 0) {
                    try {

                        for (int i = 0; i < response.length(); i++) {
                            String dasaxeleba = response.getJSONObject(i).getString("ludis_id"); // 0 - carieli kasrebi, sxva ludis saxeli iqneba
                            float k30s = (float) response.getJSONObject(i).getDouble("k30s");
                            float k50s = (float) response.getJSONObject(i).getDouble("k50s");
                            float k30r = (float) response.getJSONObject(i).getDouble("k30r");
                            float k50r = (float) response.getJSONObject(i).getDouble("k50r");

                            if (dasaxeleba.equals("0")) {
                                int_emptyK30 = k30r - k30s;
                                int_emptyK50 = k50r - k50s;
                                k30at_obj -= k30r;
                                k50at_obj -= k50r;
                            } else {
                                totalinfo.add(new Totalinout(dasaxeleba, k30s, k50s, k30r, k50r));
                                k30at_obj += k30r;
                                k50at_obj += k50r;
                            }
                        }

                        vizualizacia();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SawyobiPage.this, "ragac veraa rigze sawyobshi", Toast.LENGTH_SHORT).show();
                }

                sawyobiAdapter = new SawyobiAdapter(getApplicationContext(), totalinfo);
                nonScrollistView.setAdapter(sawyobiAdapter);

                if (!(progressDialog == null)) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                requestInProgres = false;
                setRequestedOrientation(Constantebi.screenDefOrientation);

                dasasruli();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SawyobiPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                setRequestedOrientation(Constantebi.screenDefOrientation);
                progressDialog.dismiss();
                requestInProgres = false;
                error.printStackTrace();
            }
        });

        if (!requestInProgres && requestNeeded) {
            progressDialog = ProgressDialog.show(this, "იტვირთება!", "დაელოდეთ!");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            queue.add(jsonArrayRequest);
            requestInProgres = true;
        }
        requestNeeded = true;
    }

    private void vizualizacia() {
        t_empty30.setText(String.format("30ლ :\n%s (+%s)", MyUtil.floatToSmartStr(int_emptyK30), MyUtil.floatToSmartStr(k30at_obj)));
        t_empty50.setText(String.format("50ლ :\n%s (+%s)", MyUtil.floatToSmartStr(int_emptyK50), MyUtil.floatToSmartStr(k50at_obj)));
    }

    private void dasasruli() {
        if (!(progressDialog == null)) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        requestInProgres = false;
        setRequestedOrientation(Constantebi.screenDefOrientation);
    }

    private String wageba() {
        if (eK30Count_Kout.getText().toString().equals("0")) {
            eK30Count_Kout.setText("");
        }
        if (eK50Count_Kout.getText().toString().equals("0")) {
            eK50Count_Kout.setText("");
        }
        if (eK30Count_Kout.getText().toString().isEmpty() && eK50Count_Kout.getText().toString().isEmpty()) {
            return "0";
        } else {
            return "1";
        }
    }

    private String chamotana() {
        return tempBeerList.size() > 0 ? "1" : "0";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sawyobi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.item_sawy_list) {
            Intent detailPage = new Intent(getApplicationContext(), SawyobiList.class);
            //intent_editObj.putExtra(Constantebi.REASON, Constantebi.EDIT);
            detailPage.putExtra("tarigi", archeuli_tarigi);
            detailPage.putExtra("chek", chek_condition());
            startActivity(detailPage);
        }

        return super.onOptionsItemSelected(item);
    }

    private void get_davalianeba() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constantebi.URL_GET_DAVALIANEBA;

        JsonArrayRequest request_davalianeba = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis davalianebis chamonaTvali yvela obieqtistvis

                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            for (int j = 0; j < Constantebi.OBIEQTEBI.size(); j++) {

                                if (response.getJSONObject(i).getInt("obj_id") == Constantebi.OBIEQTEBI.get(j).getId()) {
                                    Constantebi.OBIEQTEBI.get(j).setValiM(response.getJSONObject(i).getInt("pr") - response.getJSONObject(i).getInt("pay"));
                                    Constantebi.OBIEQTEBI.get(j).setValiK30(response.getJSONObject(i).getInt("k30in") - response.getJSONObject(i).getInt("k30out"));
                                    Constantebi.OBIEQTEBI.get(j).setValiK50(response.getJSONObject(i).getInt("k50in") - response.getJSONObject(i).getInt("k50out"));
                                }
                            }

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                    btn_emptyK_list.setEnabled(true);
                }
//                requestCount--;
//                if (requestCount == 0) {
                setRequestedOrientation(Constantebi.screenDefOrientation);
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                requestCount = 0;
                setRequestedOrientation(Constantebi.screenDefOrientation);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

//        requestCount++;
        queue.add(request_davalianeba);
    }

}