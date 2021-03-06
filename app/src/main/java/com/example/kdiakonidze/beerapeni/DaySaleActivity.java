package com.example.kdiakonidze.beerapeni;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.DaySalesAdapter;
import com.example.kdiakonidze.beerapeni.customView.XarjiRow;
import com.example.kdiakonidze.beerapeni.models.SaleInfo;
import com.example.kdiakonidze.beerapeni.models.Xarji;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.GlobalServise;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;
import com.example.kdiakonidze.beerapeni.utils.XarjebiDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaySaleActivity extends AppCompatActivity implements XarjebiDialog.xarjListener, GlobalServise.vListener {

    private int screenDefOrientation;
    private float k30empty = 0.0f, k50empty = 0.0f;
    private float takeMoney = 0.0f;
    private String archeuli_dge;
    private String distr_id = "0";
    private Boolean requestInProgres = false, requestNeeded = true;
    private Boolean xarjListExpanded = false;

    private ArrayList<SaleInfo> salesDay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DaySalesAdapter salesAdapter;
    private ProgressDialog progressDialog;
    private RequestQueue queue;
    private Context mContext;

    private Button btn_setDate;
    private TextView t_k30count, t_k50count, t_laricount, t_takeMoney;
    private ImageButton btnExpXarj;
    private LinearLayout linearXarjConteiner;
    private Spinner sp_distr;
    private NonScrollListView nonScrolSaleslistView;
    private TextView tXarjiSum, tXelze;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);
            archeuli_dge = dateFormat.format(calendar.getTime());
            btn_setDate.setText(archeuli_dge);
            getsales(archeuli_dge, distr_id);
//            tTarigi.setText("დღიური რეალიზაცია\nთარიღი " + archeuli_dge);
            startPos();
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tarigi", archeuli_dge);
        outState.putSerializable("sales_list", salesDay);
        outState.putBoolean("progress", requestInProgres);
        outState.putInt("distrId", sp_distr.getSelectedItemPosition());
        outState.putFloat("takemoney", takeMoney);
        outState.putFloat("k30e", k30empty);
        outState.putFloat("k50e", k50empty);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_sale);
        dateFormat = new SimpleDateFormat(getString(R.string.patern_date));
        screenDefOrientation = getRequestedOrientation();

        mContext = this;
        queue = Volley.newRequestQueue(mContext);

        btn_setDate = findViewById(R.id.btn_tarigi);
        Button btn_back = findViewById(R.id.btn_day_back);
        Button btn_fwd = findViewById(R.id.btn_day_forward);
        t_k30count = findViewById(R.id.t_p3_k30_count);
        t_k50count = findViewById(R.id.t_p3_k50_count);
        t_laricount = findViewById(R.id.t_p3_lari_count);
        t_takeMoney = findViewById(R.id.t_agebuli_tanxa);
        sp_distr = findViewById(R.id.sp_distributori);
        nonScrolSaleslistView = findViewById(R.id.sales_list1);
        tXarjiSum = findViewById(R.id.t_xarj_sum);
        tXelze = findViewById(R.id.t_xelze);
        linearXarjConteiner = findViewById(R.id.linear_xarjebi);
        btnExpXarj = findViewById(R.id.btn_xarj_expand);

        salesDay = new ArrayList<>();

        calendar = Calendar.getInstance();
//        calendar.add(Calendar.HOUR, 4);

        final ArrayList<String> distributors = new ArrayList<>();
        distributors.add("ყველა");
        for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
            distributors.add(Constantebi.USERsLIST.get(i).getName());
        }
        SpinnerAdapter spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, distributors);
        sp_distr.setAdapter(spAdapter);

        if (Constantebi.USER_TYPE.equals(Constantebi.USER_TYPE_user)) {
            int index = 0;
            for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
                if (Constantebi.USERsLIST.get(i).getUsername().equals(Constantebi.USER_USERNAME)) {
                    index = i;
                    distr_id = String.valueOf(Constantebi.USERsLIST.get(i).getId());
                }
            }
            sp_distr.setSelection(index + 1);
            sp_distr.setEnabled(false);
        }

        if (savedInstanceState != null) { // recreate moxda
            archeuli_dge = savedInstanceState.getString("tarigi");
            requestNeeded = false;
            requestInProgres = savedInstanceState.getBoolean("progress");
            if (requestInProgres) {
                progressDialog = ProgressDialog.show(this, "იტვირთება!", "დაელოდეთ!");
            }
            salesDay.clear();
            Object listObj = savedInstanceState.getSerializable("sales_list");
            if (listObj instanceof List) {
                for (int i = 0; i < ((List) listObj).size(); i++) {
                    Object item = ((List) listObj).get(i);
                    if (item instanceof SaleInfo) {
                        salesDay.add((SaleInfo) item);
                    }
                }
            }
            salesAdapter = new DaySalesAdapter(getApplicationContext(), salesDay);
            nonScrolSaleslistView.setAdapter(salesAdapter);
            takeMoney = savedInstanceState.getFloat("takemoney");
            k30empty = savedInstanceState.getFloat("k30e");
            k50empty = savedInstanceState.getFloat("k50e");
            showAtherInfo(salesDay);
            Date date = new Date();
            try {
                date = dateFormat.parse(archeuli_dge);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.setTime(date);
            showXarji();
        } else {
            archeuli_dge = dateFormat.format(calendar.getTime());
            getsales(archeuli_dge, distr_id);
        }

        btn_setDate.setText(archeuli_dge);

        sp_distr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int sel_pos, long l) {
                if (sel_pos > 0) {
                    for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
                        if (Constantebi.USERsLIST.get(i).getName().equals(distributors.get(sel_pos))) {
                            distr_id = String.valueOf(Constantebi.USERsLIST.get(i).getId());
                        }
                    }
                } else {
                    distr_id = "0";
                }
                getsales(archeuli_dge, distr_id);
                startPos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                distr_id = "0";
            }
        });

        btn_setDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DaySaleActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        View.OnClickListener btnBackFF = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_day_back:
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                    case R.id.btn_day_forward:
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        break;
                }
                archeuli_dge = dateFormat.format(calendar.getTime());
                btn_setDate.setText(archeuli_dge);
                getsales(archeuli_dge, distr_id);
                startPos();
            }
        };

        btn_back.setOnClickListener(btnBackFF);
        btn_fwd.setOnClickListener(btnBackFF);

        Button btn_xarjebi = findViewById(R.id.btn_xarjebi);
        btn_xarjebi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        btnExpXarj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xarjListExpanded = !xarjListExpanded;
                showXarjList(xarjListExpanded);
                btnExpXarj.setImageDrawable(getDrawable(xarjListExpanded ? R.drawable.ic_arrow_up_24dp : R.drawable.ic_arrow_down_24dp));
            }
        });

        tXarjiSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showXelze(MyUtil.totalXarji(Constantebi.XARJI_LIST));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showXarjList(Boolean expanded) {
        linearXarjConteiner.removeAllViews();
        boolean canDel = false;
        if (Constantebi.USER_TYPE.equals(Constantebi.USER_TYPE_admin) || archeuli_dge.equals(dateFormat.format(new Date()))) {
            canDel = true;
        }
        if (expanded) {
            for (Xarji xarji : Constantebi.XARJI_LIST) {
                XarjiRow row = new XarjiRow(mContext, xarji, linearXarjConteiner, Constantebi.XARJI_LIST, tXarjiSum, canDel);
                linearXarjConteiner.addView(row);
            }
            final ScrollView scrolMain = findViewById(R.id.scroll_main);
            scrolMain.post(new Runnable() {
                @Override
                public void run() {
                    scrolMain.smoothScrollTo(0, scrolMain.getBottom());
                }
            });
        }
    }

    private void openDialog() {
        XarjebiDialog xarjebiDialog = new XarjebiDialog();
        xarjebiDialog.show(getSupportFragmentManager(), "xarjDialog");
    }

    private void getsales(final String tarigi, final String distrid) {


        JsonArrayRequest request_fasebi = new JsonArrayRequest(Constantebi.URL_GET_SALEDAY + "?tarigi=" + tarigi + "&distrid=" + distrid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                salesDay.clear();
                Log.d("pp", tarigi + " " + distrid);
                if (response.length() > 0) {
                    try {

                        for (int i = 0; i < response.length() - 3; i++) {
                            String dasaxeleba = response.getJSONObject(i).getString("dasaxeleba");
                            float pr = (float) response.getJSONObject(i).getDouble("pr");
                            int lt = response.getJSONObject(i).getInt("lt");
                            float k30 = (float) response.getJSONObject(i).getDouble("k30");
                            float k50 = (float) response.getJSONObject(i).getDouble("k50");

                            salesDay.add(new SaleInfo(dasaxeleba, pr, lt, k30, k50));
                        }

                        takeMoney = (float) response.getJSONObject(response.length() - 3).getDouble("money");

                        k30empty = (float) response.getJSONObject(response.length() - 2).getDouble("k30");
                        k50empty = (float) response.getJSONObject(response.length() - 2).getDouble("k50");

                        JSONArray jXarjArray = response.getJSONArray(response.length() - 1);
                        Log.d("js", "" + jXarjArray.length() + response.length() + response.toString());
                        Constantebi.XARJI_LIST.clear();
                        for (int i = 0; i < jXarjArray.length(); i++) {
                            String comm = jXarjArray.getJSONObject(i).getString("comment");
                            float am = (float) jXarjArray.getJSONObject(i).getDouble("tanxa");
                            String distrID = jXarjArray.getJSONObject(i).getString("distributor_id");
                            String id = jXarjArray.getJSONObject(i).getString("id");
                            Constantebi.XARJI_LIST.add(new Xarji(comm, distrID, id, am));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DaySaleActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }

                salesAdapter = new DaySalesAdapter(getApplicationContext(), salesDay);
                nonScrolSaleslistView.setAdapter(salesAdapter);

                showXarji();

                if (!(progressDialog == null)) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                showAtherInfo(salesDay);
                requestInProgres = false;
                setRequestedOrientation(screenDefOrientation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRequestedOrientation(screenDefOrientation);
                Toast.makeText(DaySaleActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                requestInProgres = false;
            }
        });

        if (!requestInProgres && requestNeeded) {
            progressDialog = ProgressDialog.show(this, "იტვირთება!", "დაელოდეთ!");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            queue.add(request_fasebi);
            requestInProgres = true;
        }
        requestNeeded = true;
    }

    private void showAtherInfo(ArrayList<SaleInfo> salesDay) {
        float k3 = 0, k5 = 0;
        float pr = 0;
        for (int i = 0; i < salesDay.size(); i++) {
            k3 += salesDay.get(i).getK30();
            k5 += salesDay.get(i).getK50();
            pr += salesDay.get(i).getPr();
        }

        t_k30count.setText(String.format("%s\n%s", MyUtil.floatToSmartStr(k3), MyUtil.floatToSmartStr(k30empty)));
        t_k50count.setText(String.format("%s\n%s", MyUtil.floatToSmartStr(k5), MyUtil.floatToSmartStr(k50empty)));
        DecimalFormat df_pr = new DecimalFormat("#0");
        t_laricount.setText(df_pr.format(pr));
        DecimalFormat df = new DecimalFormat("#0.00");
        t_takeMoney.setText(df.format(takeMoney));
    }

    @Override
    public void applayChanges(String comment, Float amount) {
//        tXarjiSum.setText(MyUtil.floatToSmartStr(amount));
//        tXelze.setText(comment);

        GlobalServise globalServise = new GlobalServise(getApplicationContext());
        globalServise.setChangeListener(this);
        globalServise.insertXarjebi(Constantebi.USER_ID, amount, comment);
    }

    @Override
    public void onChange() {
        showXarji();
        startPos();
    }

    void showXarji() {
        Log.d("a", "" + Constantebi.XARJI_LIST.size());
        float sum = MyUtil.totalXarji(Constantebi.XARJI_LIST);

        tXarjiSum.setText(MyUtil.floatToSmartStr(sum) + mContext.getString(R.string.lari));
        showXelze(sum);
    }

    void showXelze(float xarji) {
        tXelze.setText(MyUtil.floatToSmartStr(takeMoney - xarji) + mContext.getString(R.string.lari));
    }

    void startPos() {
        xarjListExpanded = false;
        showXarjList(xarjListExpanded);
        btnExpXarj.setImageDrawable(getDrawable(R.drawable.ic_arrow_down_24dp));
    }
}
