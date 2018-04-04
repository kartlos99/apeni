package com.example.kdiakonidze.beerapeni;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
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
import com.example.kdiakonidze.beerapeni.models.SaleInfo;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DaySaleActivity extends AppCompatActivity {

    private int screenDefOrientation, k30empty = 0, k50empty = 0;
    private ArrayList<SaleInfo> salesDay;
    private Double takeMoney = 0.0;
    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String archeuli_dge;
    private Button btn_setDate, btn_back, btn_fwd;
    private TextView tTarigi, t_k30count, t_k50count, t_laricount, t_takeMoney;
    //    private ListView saleslistView;
    private DaySalesAdapter salesAdapter;
    private ProgressDialog progressDialog;
    private Spinner sp_distr;
    private String distr_id = "0";
    private RequestQueue queue;
    private Boolean requestInProgres = false, requestNeeded = true;
    private NonScrollListView nonScrolSaleslistView;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);
            archeuli_dge = dateFormat.format(calendar.getTime());
            btn_setDate.setText(archeuli_dge);
            getsales(archeuli_dge, distr_id);
//            tTarigi.setText("დღიური რეალიზაცია\nთარიღი " + archeuli_dge);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tarigi", archeuli_dge);
        outState.putSerializable("sales_list", salesDay);
        outState.putBoolean("progress", requestInProgres);
        outState.putInt("distrId", sp_distr.getSelectedItemPosition());
        outState.putDouble("takemoney", takeMoney);
        outState.putInt("k30e",k30empty);
        outState.putInt("k50e",k50empty);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_sale);
        screenDefOrientation = getRequestedOrientation();

        queue = Volley.newRequestQueue(this);

        btn_setDate = (Button) findViewById(R.id.btn_tarigi);
        btn_back = (Button) findViewById(R.id.btn_day_back);
        btn_fwd = (Button) findViewById(R.id.btn_day_forward);
        tTarigi = (TextView) findViewById(R.id.t_tarigi);
        t_k30count = (TextView) findViewById(R.id.t_p3_k30_count);
        t_k50count = (TextView) findViewById(R.id.t_p3_k50_count);
        t_laricount = (TextView) findViewById(R.id.t_p3_lari_count);
        t_takeMoney = (TextView) findViewById(R.id.t_agebuli_tanxa);
        sp_distr = (Spinner) findViewById(R.id.sp_distributori);
        nonScrolSaleslistView = (NonScrollListView) findViewById(R.id.sales_list1);

        salesDay = new ArrayList<>();

        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 4);

        final ArrayList<String> distributors = new ArrayList<>();
        distributors.add("ყველა");
        for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
            distributors.add(Constantebi.USERsLIST.get(i).getName());
        }
        SpinnerAdapter spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, distributors);
        sp_distr.setAdapter(spAdapter);

        if (Constantebi.USER_TYPE.equals(Constantebi.USER_TYPE_user)){
            int index = 0;
            for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
                if(Constantebi.USERsLIST.get(i).getUsername().equals(Constantebi.USER_USERNAME)){
                    index = i;
                    distr_id = String.valueOf(Constantebi.USERsLIST.get(i).getId());
                }
            }
            sp_distr.setSelection(index+1);
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
            salesDay = (ArrayList<SaleInfo>) savedInstanceState.getSerializable("sales_list");
            salesAdapter = new DaySalesAdapter(getApplicationContext(), salesDay);
            nonScrolSaleslistView.setAdapter(salesAdapter);
            takeMoney = savedInstanceState.getDouble("takemoney");
            k30empty = savedInstanceState.getInt("k30e");
            k50empty = savedInstanceState.getInt("k50e");
            showAtherInfo(salesDay);
            Date date = new Date();
            try {
                date = dateFormat.parse(archeuli_dge);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.setTime(date);
        } else {
            archeuli_dge = dateFormat.format(calendar.getTime());
            getsales(archeuli_dge, distr_id);
        }

        btn_setDate.setText(archeuli_dge);
        tTarigi.setText("დღიური რეალიზაცია");//\nთარიღი " + archeuli_dge);

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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                archeuli_dge = dateFormat.format(calendar.getTime());
                btn_setDate.setText(archeuli_dge);
                getsales(archeuli_dge, distr_id);
            }
        });

        btn_fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                archeuli_dge = dateFormat.format(calendar.getTime());
                btn_setDate.setText(archeuli_dge);
                getsales(archeuli_dge, distr_id);
            }
        });

    }

    private void getsales(String tarigi, String distrid) {


        JsonArrayRequest request_fasebi = new JsonArrayRequest(Constantebi.URL_GET_SALEDAY + "?tarigi=" + tarigi + "&distrid=" + distrid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                salesDay.clear();

                if (response.length() > 0) {
                    try {

                        for (int i = 0; i < response.length() - 2; i++) {
                            String dasaxeleba = response.getJSONObject(i).getString("dasaxeleba");
                            Double pr = response.getJSONObject(i).getDouble("pr");
                            int lt = response.getJSONObject(i).getInt("lt");
                            int k30 = response.getJSONObject(i).getInt("k30");
                            int k50 = response.getJSONObject(i).getInt("k50");

                            salesDay.add(new SaleInfo(dasaxeleba, pr, lt, k30, k50));
                        }

                        takeMoney = response.getJSONObject(response.length() - 2).getDouble("money");

                        k30empty = response.getJSONObject(response.length() - 1).getInt("k30");
                        k50empty = response.getJSONObject(response.length() - 1).getInt("k50");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DaySaleActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }

                salesAdapter = new DaySalesAdapter(getApplicationContext(), salesDay);
                nonScrolSaleslistView.setAdapter(salesAdapter);

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
        int k3 = 0, k5 = 0;
        double pr = 0;
        for (int i = 0; i < salesDay.size(); i++) {
            k3 += salesDay.get(i).getK30();
            k5 += salesDay.get(i).getK50();
            pr += salesDay.get(i).getPr();
        }

        t_k30count.setText(String.valueOf(k3)+"\n"+String.valueOf(k30empty));
        t_k50count.setText(String.valueOf(k5)+"\n"+String.valueOf(k50empty));
        t_laricount.setText(String.valueOf(pr));

        t_takeMoney.setText(String.valueOf(takeMoney));
    }


}
