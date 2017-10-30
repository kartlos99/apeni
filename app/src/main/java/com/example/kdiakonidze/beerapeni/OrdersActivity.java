package com.example.kdiakonidze.beerapeni;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.ShekvetebiAdapter;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class OrdersActivity extends AppCompatActivity {

    Button btn_setDate;
    TextView t_Tarigi;
    ListView listView_shekvetebi;
    ProgressDialog progressDialog;
    ArrayList<Shekvetebi> shekvetebiArrayList;
    ShekvetebiAdapter shekvetebiAdapter;


    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            i1++;
            btn_setDate.setText(i+"-"+i1+"-" + i2);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        listView_shekvetebi = (ListView) findViewById(R.id.list_shekvetebi);
        t_Tarigi = (TextView) findViewById(R.id.t_tarigi);
        btn_setDate = (Button) findViewById(R.id.btn_setdate);

        t_Tarigi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Date curtime = Calendar.getInstance().getTime();
                t_Tarigi.setText(curtime.toString());
            }
        });


        btn_setDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.HOUR,4);
//                Date curtime = Calendar.getInstance().getTime();
                t_Tarigi.setText(c.getTime().toString());


                btn_setDate.setText(c.get(Calendar.YEAR)+"/"+ c.get(Calendar.MONTH)+"/"+ c.get(Calendar.DAY_OF_MONTH)+"/"+ c.get(Calendar.HOUR_OF_DAY)+"/"+ c.get(Calendar.MINUTE));

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrdersActivity.this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) );
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        shekvetebis_chamotvirtva();


    }

    private void shekvetebis_chamotvirtva() {
        progressDialog = ProgressDialog.show(this,"იტვირთება!", "loading!");
        shekvetebiArrayList = new ArrayList<Shekvetebi>();
        String url = Constantebi.URL_GET_ORDERLIST;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest requestObieqtebi = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis obieqtebis chamonatvali


                if(response.length() > 0){
                    shekvetebiArrayList.clear();
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
