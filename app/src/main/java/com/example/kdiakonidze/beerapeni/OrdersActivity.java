package com.example.kdiakonidze.beerapeni;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.ShekvetebiAdapter;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class OrdersActivity extends AppCompatActivity {

    String archeuli_dge;
    Button btn_setDate, btn_addOrder;
    TextView t_Tarigi;
    ListView listView_shekvetebi;
    ProgressDialog progressDialog;
    ArrayList<Shekvetebi> shekvetebiArrayList;
    ShekvetebiAdapter shekvetebiAdapter;
    Calendar calendar;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year,month,day);
            archeuli_dge = dateFormat.format(calendar.getTime());
            btn_setDate.setText(archeuli_dge);
            shekvetebis_chamotvirtva(archeuli_dge);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState){

        outState.putString("tarigi", archeuli_dge);
        outState.putSerializable("order_list",shekvetebiArrayList);

        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        listView_shekvetebi = (ListView) findViewById(R.id.list_shekvetebi);
        t_Tarigi = (TextView) findViewById(R.id.t_tarigi);
        btn_setDate = (Button) findViewById(R.id.btn_setdate);
        btn_addOrder = (Button) findViewById(R.id.btn_addOrder);

        shekvetebiArrayList = new ArrayList<>();

        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,4);

        if(savedInstanceState!=null){
            archeuli_dge = savedInstanceState.getString("tarigi");
            shekvetebiArrayList.clear();
            shekvetebiArrayList = (ArrayList<Shekvetebi>) savedInstanceState.getSerializable("order_list");
            shekvetebiAdapter = new ShekvetebiAdapter(getApplicationContext(),shekvetebiArrayList);
            listView_shekvetebi.setAdapter(shekvetebiAdapter);
        }else {
            archeuli_dge = dateFormat.format(calendar.getTime());
            shekvetebis_chamotvirtva(archeuli_dge);
        }

        btn_setDate.setText(archeuli_dge);

        btn_setDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrdersActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        btn_addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ObjListActivity.class);
                intent.putExtra("mdebareoba",Constantebi.MDEBAREOBA_SHEKVETA);
                startActivity(intent);
            }
        });



    }

    private void shekvetebis_chamotvirtva(String currentDay) {
        progressDialog = ProgressDialog.show(this,"იტვირთება!", "loading!");

        String url = Constantebi.URL_GET_ORDERLIST +"?tarigi="+currentDay;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest requestObieqtebi = new JsonArrayRequest( url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis obieqtebis chamonatvali

                shekvetebiArrayList.clear();
                if(response.length() > 0){
                    for (int i=0; i<response.length(); i++){
                        try {
                            Shekvetebi shekveta= new Shekvetebi(
                                    response.getJSONObject(i).getString("obieqti"),
                                    response.getJSONObject(i).getString("dasaxeleba"),
                                    response.getJSONObject(i).getInt("in_30"),
                                    response.getJSONObject(i).getInt("in_50"),
                                    response.getJSONObject(i).getInt("wont_30"),
                                    response.getJSONObject(i).getInt("wont_50")
                            );

                            shekvetebiArrayList.add(shekveta);

                        }catch (JSONException excep){
                            excep.printStackTrace();
                        }
                    }
                }

                shekvetebiAdapter = new ShekvetebiAdapter(getApplicationContext(),shekvetebiArrayList);
                listView_shekvetebi.setAdapter(shekvetebiAdapter);

                progressDialog.dismiss();
//                expAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrdersActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        queue.add(requestObieqtebi);

    }
}
