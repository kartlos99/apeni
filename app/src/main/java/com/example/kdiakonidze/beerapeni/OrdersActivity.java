package com.example.kdiakonidze.beerapeni;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.text.SimpleDateFormat;
//import android.icu.util.Calendar;
//import android.icu.util.TimeZone;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.ShekvetebiAdapter;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class OrdersActivity extends AppCompatActivity {

    Button btn_setDate, btn_addOrder;
    TextView t_Tarigi;
    CheckBox chBox_orderGroup;
    ListView listView_shekvetebi;
    ProgressDialog progressDialog;

    ArrayList<Shekvetebi> shekvetebiArrayList;
    ShekvetebiAdapter shekvetebiAdapter;
    Calendar calendar;
    String archeuli_dge;
    Boolean chamosatvirtia = true;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int screenDefOrientation;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);
            calendar.add(Calendar.HOUR, 4);
            archeuli_dge = dateFormat.format(calendar.getTime());
            btn_setDate.setText(archeuli_dge);
            shekvetebis_chamotvirtva(archeuli_dge);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tarigi", archeuli_dge);
        outState.putSerializable("order_list", shekvetebiArrayList);
        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        chBox_orderGroup = (CheckBox) findViewById(R.id.checkbox_order_group);
        listView_shekvetebi = (ListView) findViewById(R.id.list_shekvetebi);
        t_Tarigi = (TextView) findViewById(R.id.t_tarigi);
        btn_setDate = (Button) findViewById(R.id.btn_setdate);
        btn_addOrder = (Button) findViewById(R.id.btn_addOrder);

        shekvetebiArrayList = new ArrayList<>();
        screenDefOrientation = getRequestedOrientation();

        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 4);

        if (savedInstanceState != null) {
            archeuli_dge = savedInstanceState.getString("tarigi");
            shekvetebiArrayList.clear();
            shekvetebiArrayList = (ArrayList<Shekvetebi>) savedInstanceState.getSerializable("order_list");
            if (chBox_orderGroup.isChecked()) {
                shekvetebiAdapter = new ShekvetebiAdapter(getApplicationContext(), groupOrders(shekvetebiArrayList), true);
            } else {
                shekvetebiAdapter = new ShekvetebiAdapter(getApplicationContext(), shekvetebiArrayList, false);
            }
            listView_shekvetebi.setAdapter(shekvetebiAdapter);
            chamosatvirtia = false;
        } else {
            archeuli_dge = dateFormat.format(calendar.getTime());
            chamosatvirtia = true;
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
                intent.putExtra("mdebareoba", Constantebi.MDEBAREOBA_SHEKVETA);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        chBox_orderGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    shekvetebiAdapter.reNewData(groupOrders(shekvetebiArrayList), b);
                } else {
                    shekvetebiAdapter.reNewData(shekvetebiArrayList, b);
                }
            }
        });

        this.registerForContextMenu(listView_shekvetebi);
    }

    private ArrayList<Shekvetebi> groupOrders(ArrayList<Shekvetebi> shekvetebiList) {
        ArrayList<Shekvetebi> groupedOrderList = new ArrayList<>();
        String objName = "", beerName = "";
        int k30w = 0, k50w = 0, k30in = 0, k50in = 0;

        if (shekvetebiList.size() > 0) {
            Shekvetebi shekveta = shekvetebiList.get(0);
            objName = shekveta.getObieqti();
            beerName = shekveta.getLudi();
        }

        for (int i = 0; i < shekvetebiList.size(); i++) {
            if (shekvetebiList.get(i).getObieqti().equals(objName) && shekvetebiList.get(i).getLudi().equals(beerName)) {
                k30w += shekvetebiList.get(i).getK30wont();
                k50w += shekvetebiList.get(i).getK50wont();
                k30in += shekvetebiList.get(i).getK30in();
                k50in += shekvetebiList.get(i).getK50in();
            } else {
                Shekvetebi newGrOrder = new Shekvetebi(objName, beerName, k30in, k50in, k30w, k50w);
                groupedOrderList.add(newGrOrder);
                Shekvetebi shekveta = shekvetebiList.get(i);
                objName = shekveta.getObieqti();
                beerName = shekveta.getLudi();
                k30w = shekvetebiList.get(i).getK30wont();
                k50w = shekvetebiList.get(i).getK50wont();
                k30in = shekvetebiList.get(i).getK30in();
                k50in = shekvetebiList.get(i).getK50in();
            }
        }

        if (!objName.equals("")) {
            Shekvetebi newGrOrder = new Shekvetebi(objName, beerName, k30in, k50in, k30w, k50w);
            groupedOrderList.add(newGrOrder);
        }

        return groupedOrderList;
    }

    @Override
    protected void onPause() {
        super.onPause();
        chamosatvirtia = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (chamosatvirtia) {
            shekvetebis_chamotvirtva(archeuli_dge);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Shekvetebi currOrder = (Shekvetebi) shekvetebiAdapter.getItem(info.position);

        if (chBox_orderGroup.isChecked()) {
            Toast.makeText(getApplicationContext(), "მოხსენით დაჯგუფება", Toast.LENGTH_SHORT).show();
        } else {
            this.getMenuInflater().inflate(R.menu.context_menu_order, menu);
            menu.setHeaderTitle(currOrder.getComment());
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Shekvetebi currOrder = (Shekvetebi) shekvetebiAdapter.getItem(info.position);
        switch (item.getItemId()) {

            case R.id.cm_order_edit:
                boolean exist = false;
                for (int i = 0; i < Constantebi.OBIEQTEBI.size(); i++) {
                    if (Constantebi.OBIEQTEBI.get(i).getDasaxeleba().equals(currOrder.getObieqti())) {
                        exist = true;
                    }
                }
                if (exist) {
                    Intent intent_editOrder;
                    if ((currOrder.getK30wont() == 0) && (currOrder.getK50wont() == 0)) { // mitana
                        intent_editOrder = new Intent(getApplicationContext(), AddDeliveryActivity.class);
                        intent_editOrder.putExtra("id", currOrder.getOrder_id());
                        intent_editOrder.putExtra("operacia", Constantebi.MITANA);
                    } else { // shekveta
                        intent_editOrder = new Intent(getApplicationContext(), AddOrderActivity.class);
                        intent_editOrder.putExtra("obj", currOrder);
                    }
                    intent_editOrder.putExtra(Constantebi.REASON, Constantebi.EDIT);
                    startActivity(intent_editOrder);
                }else {
                    Toast.makeText(getApplicationContext(), "ჩანაწერს ვერ დაარედაქტირებთ,\nობიექტი '"+ currOrder.getObieqti() +"' წაშლილია!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cm_order_del:
                if ((currOrder.getK30wont() == 0) && (currOrder.getK50wont() == 0)) {
                    removeRecord(currOrder.getOrder_id(), Constantebi.MITANA, info.position);
                } else {
                    removeRecord(currOrder.getOrder_id(), Constantebi.ORDER, info.position);
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void removeRecord(final int id, final String table, final int listPosition) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request_DelOrder = new StringRequest(Request.Method.POST, Constantebi.URL_DEL_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response + " +", Toast.LENGTH_SHORT).show();
                if (response.equals("Removed!")) {

//                    for (int i = 0; i < shekvetebiArrayList.size(); i++) {
//                        if (shekvetebiArrayList.get(i).getOrder_id() == id) {
//                            shekvetebiArrayList.remove(i);
//                        }
//                    }
                    shekvetebiArrayList.remove(listPosition);
                    shekvetebiAdapter.reNewData(shekvetebiArrayList, false);
                }
                setRequestedOrientation(screenDefOrientation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRequestedOrientation(screenDefOrientation);
                Toast.makeText(getApplicationContext(), error.getMessage() + " -", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("table", table);
                params.toString();
                return params;
            }
        };

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request_DelOrder);
    }

    private void shekvetebis_chamotvirtva(String currentDay) {
        progressDialog = ProgressDialog.show(this, "იტვირთება!", "loading!");

        String url = Constantebi.URL_GET_ORDERLIST + "?tarigi=" + currentDay;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest requestObieqtebi = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis obieqtebis chamonatvali

                shekvetebiArrayList.clear();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Shekvetebi shekveta = new Shekvetebi(
                                    response.getJSONObject(i).getString("obieqti"),
                                    response.getJSONObject(i).getString("dasaxeleba"),
                                    response.getJSONObject(i).getInt("in_30"),
                                    response.getJSONObject(i).getInt("in_50"),
                                    response.getJSONObject(i).getInt("wont_30"),
                                    response.getJSONObject(i).getInt("wont_50")
                            );
                            shekveta.setChk(response.getJSONObject(i).getString("chk"));
                            shekveta.setDistrib_Name(response.getJSONObject(i).getString("name"));
                            shekveta.setDistrib_id(response.getJSONObject(i).getInt("distributor_id"));
                            shekveta.setOrder_id(response.getJSONObject(i).getInt("order_id"));
                            shekveta.setComment(response.getJSONObject(i).getString("comment"));

                            shekvetebiArrayList.add(shekveta);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                }

                if (chBox_orderGroup.isChecked()) {
                    shekvetebiAdapter = new ShekvetebiAdapter(getApplicationContext(), groupOrders(shekvetebiArrayList), chBox_orderGroup.isChecked());
                } else {
                    shekvetebiAdapter = new ShekvetebiAdapter(getApplicationContext(), shekvetebiArrayList, chBox_orderGroup.isChecked());
                }
                listView_shekvetebi.setAdapter(shekvetebiAdapter);

                progressDialog.dismiss();
                setRequestedOrientation(screenDefOrientation);
//                expAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrdersActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                setRequestedOrientation(screenDefOrientation);
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(requestObieqtebi);

    }
}
