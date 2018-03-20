package com.example.kdiakonidze.beerapeni;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

import com.example.kdiakonidze.beerapeni.adapters.SawyobiAdapter;
import com.example.kdiakonidze.beerapeni.models.Totalinout;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;


import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SawyobiPage extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btn_chawera, btn_beerleft, btn_beerright, btnK30dec, btnK30inc, btnK50dec, btnK50inc, btnK30dec_Kout, btnK30inc_Kout, btnK50dec_Kout, btnK50inc_Kout, btn_changeDate;
    private EditText eK30Count, eK50Count, eK30Count_Kout, eK50Count_Kout;
    private String sK30Count = "0", sK50Count = "0", sK30Count_Kout = "0", sK50Count_Kout = "0";
    private Calendar calendar;
    private TextView t_beerType, t_empty30, t_empty50;
    private CardView cardView_chamotana, cardView_wageba, cardView_nashti_full, cardView_nashti_empty;
    private TextInputLayout t_comment;
    private NonScrollListView nonScrollistView;
    private ProgressDialog progressDialog;
    private Boolean requestInProgres = false, requestNeeded = true;
    private RequestQueue queue;
    private CheckBox checkSawyobi;
    private ConstraintLayout constrCarieliBox;
    private ScrollView scrollView_sawy;

    String archeuli_tarigi, archeuli_dro;
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int beerIndex = 0, beerId = 0, int_emptyK30 = 0, int_emptyK50 = 0;
    private ArrayList<Totalinout> totalinfo;
    private SawyobiAdapter sawyobiAdapter;

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
            getnashtebi(archeuli_tarigi, chek_cindition());
        }
    };

    private String chek_cindition() {
        if (checkSawyobi.isChecked()) {
            return "1";
        } else {
            return "0";
        }
    }

    public void showtext(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener btn_add_dec = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_k30_dec:
                    eK30Count_Kout.setText(pliusMinusText(eK30Count_Kout.getText().toString(), false));
                    break;
                case R.id.btn_k30_inc:
                    eK30Count_Kout.setText(pliusMinusText(eK30Count_Kout.getText().toString(), true));
                    break;
                case R.id.btn_k50_dec:
                    eK50Count_Kout.setText(pliusMinusText(eK50Count_Kout.getText().toString(), false));
                    break;
                case R.id.btn_k50_inc:
                    eK50Count_Kout.setText(pliusMinusText(eK50Count_Kout.getText().toString(), true));
                    break;
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("comment", t_comment.getEditText().getText().toString());
        outState.putString("tarigi", archeuli_tarigi);
        outState.putString("dro", archeuli_dro);
        outState.putInt("beer", beerIndex);
        outState.putSerializable("data_array", totalinfo);
        outState.putString("k30", eK30Count.getText().toString());
        outState.putString("k50", eK50Count.getText().toString());
        outState.putString("k30out", eK30Count_Kout.getText().toString());
        outState.putString("k50out", eK50Count_Kout.getText().toString());
        outState.putInt("empty30", int_emptyK30);
        outState.putInt("empty50", int_emptyK50);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawyobi_page);

        initialization();

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

        if (savedInstanceState != null) {
            totalinfo = (ArrayList<Totalinout>) savedInstanceState.getSerializable("data_array");
            sawyobiAdapter = new SawyobiAdapter(getApplicationContext(), totalinfo);
            nonScrollistView.setAdapter(sawyobiAdapter);
            beerIndex = savedInstanceState.getInt("beer");
            t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
            beerId = Constantebi.ludiList.get(beerIndex).getId();
            t_comment.getEditText().setText(savedInstanceState.getString("comment"));
            archeuli_tarigi = savedInstanceState.getString("tarigi");
            archeuli_dro = savedInstanceState.getString("dro");

            sK30Count = savedInstanceState.getString("k30");
            sK50Count = savedInstanceState.getString("k50");
            sK30Count_Kout = savedInstanceState.getString("k30out");
            sK50Count_Kout = savedInstanceState.getString("k50out");

            Toast.makeText(this, savedInstanceState.getString("k30")+" "+savedInstanceState.getString("k30out"), Toast.LENGTH_SHORT).show();
            int_emptyK30 = savedInstanceState.getInt("empty30");
            int_emptyK50 = savedInstanceState.getInt("empty50");
            t_empty30.setText("30ლ : " + int_emptyK30);
            t_empty50.setText("50ლ : " + int_emptyK50);
            Date date1 = calendar.getTime();
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd").parse(archeuli_tarigi);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setTime(date1);
            calendar.add(Calendar.DATE, -1);
            btn_changeDate.setText(dateFormat.format(calendar.getTime()));
        } else {
            t_beerType.setText(Constantebi.ludiList.get(beerIndex).getDasaxeleba());
            beerId = Constantebi.ludiList.get(beerIndex).getId();
            totalinfo = new ArrayList<>();
            archeuli_tarigi = dateFormat.format(calendar.getTime());
            archeuli_dro = timeFormat.format(calendar.getTime());
            calendar.add(Calendar.HOUR, 24);
            archeuli_tarigi = dateFormat.format(calendar.getTime());
            getnashtebi(archeuli_tarigi, chek_cindition());
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
                    sendDataToDB(0, chamotana(), wageba(), chek_cindition());
                }
            }
        });

        checkSawyobi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getnashtebi(archeuli_tarigi, chek_cindition());
                if (b) {
                    constrCarieliBox.setVisibility(View.GONE);
                    scrollView_sawy.setBackgroundColor(getResources().getColor(R.color.color_ForChek));
                } else {
                    constrCarieliBox.setVisibility(View.VISIBLE);
                    scrollView_sawy.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });
    }

    private void initialization() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        cardView_nashti_full = (CardView) findViewById(R.id.card_nashti_savse);
        cardView_nashti_empty = (CardView) findViewById(R.id.card_nashti_carieli);
        cardView_chamotana = (CardView) findViewById(R.id.card_chamotana);
        cardView_wageba = (CardView) findViewById(R.id.card_wageba);
        t_beerType = (TextView) cardView_chamotana.findViewById(R.id.t_ludisDasaxeleba);
        t_comment = (TextInputLayout) findViewById(R.id.t_mitana_comment);
        btn_chawera = (Button) findViewById(R.id.btn_sawyobi_chawera);
        btn_changeDate = (Button) findViewById(R.id.btn_change_date);
        btn_beerleft = (Button) cardView_chamotana.findViewById(R.id.btn_beerleft);
        btn_beerright = (Button) cardView_chamotana.findViewById(R.id.btn_beerright);
        btnK30dec = (Button) cardView_chamotana.findViewById(R.id.btn_k30_dec);
        btnK30inc = (Button) cardView_chamotana.findViewById(R.id.btn_k30_inc);
        btnK50dec = (Button) cardView_chamotana.findViewById(R.id.btn_k50_dec);
        btnK50inc = (Button) cardView_chamotana.findViewById(R.id.btn_k50_inc);
        btnK30dec_Kout = (Button) cardView_wageba.findViewById(R.id.btn_k30_dec);
        btnK30inc_Kout = (Button) cardView_wageba.findViewById(R.id.btn_k30_inc);
        btnK50dec_Kout = (Button) cardView_wageba.findViewById(R.id.btn_k50_dec);
        btnK50inc_Kout = (Button) cardView_wageba.findViewById(R.id.btn_k50_inc);
        eK30Count = (EditText) cardView_chamotana.findViewById(R.id.edit_K30Count);
        eK50Count = (EditText) cardView_chamotana.findViewById(R.id.edit_K50Count);
        eK30Count_Kout = (EditText) cardView_wageba.findViewById(R.id.edit_K30Count);
        eK50Count_Kout = (EditText) cardView_wageba.findViewById(R.id.edit_K50Count);
        t_empty30 = (TextView) cardView_nashti_empty.findViewById(R.id.t_empty_k30);
        t_empty50 = (TextView) cardView_nashti_empty.findViewById(R.id.t_empty_k50);
        constrCarieliBox = (ConstraintLayout) findViewById(R.id.constr_carieli_box);
        scrollView_sawy = (ScrollView) findViewById(R.id.scrool_sawyobi);
        nonScrollistView = (NonScrollListView) cardView_nashti_full.findViewById(R.id.list_forNashtebi);
        checkSawyobi = (CheckBox) findViewById(R.id.checkBox_sawyobi);

        cardView_wageba.findViewById(R.id.btn_beerleft).setVisibility(View.GONE);
        cardView_wageba.findViewById(R.id.btn_beerright).setVisibility(View.GONE);
        cardView_wageba.findViewById(R.id.t_ludisDasaxeleba).setVisibility(View.GONE);
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

    private void sendDataToDB(final Integer editId, final String sChamotana, final String sWageba, final String chek) {
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id", editId.toString());
                params.put("chamotana", sChamotana);
                params.put("wageba", sWageba);

                params.put("distributor_id", Constantebi.USER_ID);
                params.put("comment", t_comment.getEditText().getText().toString());

                params.put("beer_type", String.valueOf(String.valueOf(beerId)));

                // chamotanili ksrebi
                params.put("k30", eK30Count.getText().toString());
                params.put("k50", eK50Count.getText().toString());

                // wagebuli kasrebi
                params.put("k30out", eK30Count_Kout.getText().toString());
                params.put("k50out", eK50Count_Kout.getText().toString());

                params.put("set_tarigi", archeuli_dro);
                params.put("chek", chek);

                params.toString();
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

                if (response.length() > 0) {
                    try {

                        for (int i = 0; i < response.length(); i++) {
                            String dasaxeleba = response.getJSONObject(i).getString("ludis_id"); // 0 - carieli kasrebi, sxva ludis saxeli iqneba
                            int k30s = response.getJSONObject(i).getInt("k30s");
                            int k50s = response.getJSONObject(i).getInt("k50s");
                            int k30r = response.getJSONObject(i).getInt("k30r");
                            int k50r = response.getJSONObject(i).getInt("k50r");

                            if (dasaxeleba.equals("0")) {
                                int_emptyK30 = k30r - k30s;
                                int_emptyK50 = k50r - k50s;
                                t_empty30.setText("30ლ : " + int_emptyK30);
                                t_empty50.setText("50ლ : " + int_emptyK50);
                            } else {
                                totalinfo.add(new Totalinout(dasaxeleba, k30s, k50s, k30r, k50r));
                            }
                        }

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
        if (eK30Count_Kout.getText().toString().equals("") && eK50Count_Kout.getText().toString().equals("")) {
            return "0";
        } else {
            return "1";
        }
    }

    private String chamotana() {
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
            detailPage.putExtra("chek", chek_cindition());
            startActivity(detailPage);
        }

        return super.onOptionsItemSelected(item);
    }
}