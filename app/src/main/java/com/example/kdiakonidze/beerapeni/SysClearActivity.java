package com.example.kdiakonidze.beerapeni;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.SysCleanAdapter;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.SysClean;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SysClearActivity extends AppCompatActivity {

    Button btn_chawera, btn_sysclear_add;
    Spinner sp_sysclean;
    ListView list_sysclean;
    EditText e_comment;

    SysCleanAdapter adapter;
    ArrayList<SysClean> cleaningList = new ArrayList<>();
    private int screenDefOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_clear);

        btn_chawera = (Button) findViewById(R.id.btn_add_sysclean);
        btn_sysclear_add = (Button) findViewById(R.id.btn_sysclear);
        sp_sysclean = (Spinner) findViewById(R.id.spinner_sysclean);
        list_sysclean = (ListView) findViewById(R.id.list_sys_clean);
        e_comment = (EditText) findViewById(R.id.e_cleaning_comment);
        registerForContextMenu(list_sysclean);
        screenDefOrientation = getRequestedOrientation();

        final SpinnerAdapter spAdapter = new ArrayAdapter<Obieqti>(this, android.R.layout.simple_list_item_1, Constantebi.OBIEQTEBI);
        sp_sysclean.setAdapter(spAdapter);

        get_sysCleanData();

        btn_chawera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_chawera.setEnabled(false);
                Obieqti obieqti = (Obieqti) spAdapter.getItem(sp_sysclean.getSelectedItemPosition());
                insertNewCleaningInfo(obieqti.getId(), 0);
            }
        });

        btn_sysclear_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_chawera.getVisibility() == View.VISIBLE) {
                    sp_sysclean.setVisibility(View.GONE);
                    btn_chawera.setVisibility(View.GONE);
                    e_comment.setVisibility(View.GONE);
                    btn_sysclear_add.setText("+");
                } else {
                    sp_sysclean.setVisibility(View.VISIBLE);
                    btn_chawera.setVisibility(View.VISIBLE);
                    e_comment.setVisibility(View.VISIBLE);
                    btn_sysclear_add.setText("-");
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        SysClean currItem = (SysClean) adapter.getItem(info.position);
        this.getMenuInflater().inflate(R.menu.context_menu_sysclear, menu);
        menu.setHeaderTitle(currItem.getDasaxeleba());
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        SysClean currItem = (SysClean) adapter.getItem(info.position);

        switch (item.getItemId()) {
            case R.id.cm_sysclear_del:
                insertNewCleaningInfo(0, currItem.getId());
        }
        return super.onContextItemSelected(item);
    }

    private void insertNewCleaningInfo(final int objID, final int sysClId) {

        StringRequest ins_cleaning = new StringRequest(Request.Method.POST, Constantebi.URL_INS_SYSCLEAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("ჩაწერილია!")) {

                    String id = response.substring(10);

                    SysClean newClear = new SysClean();
                    newClear.setId(Integer.valueOf(id));
                    newClear.setDasaxeleba(sp_sysclean.getSelectedItem().toString());
                    newClear.setComment(e_comment.getText().toString());
                    newClear.setDistr_id(Integer.valueOf(Constantebi.USER_ID));
                    newClear.setDge(0);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy:MM:dd");

                    newClear.setTarigi(mdformat.format(calendar.getTime()));

                    cleaningList.add(newClear);
                    adapter.notifyDataSetChanged();

                }
                if (response.equals("წაშლილია!")) {
                    for (int i = 0; i < cleaningList.size(); i++) {
                        if (cleaningList.get(i).getId() == sysClId) {
                            cleaningList.remove(i);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                setRequestedOrientation(screenDefOrientation);
                onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                btn_chawera.setEnabled(true);
                setRequestedOrientation(screenDefOrientation);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id", String.valueOf(sysClId));
                params.put("distrib_id", Constantebi.USER_ID);
                params.put("comment", e_comment.getText().toString());
                params.put("objID", String.valueOf(String.valueOf(objID)));

                params.toString();
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(ins_cleaning);
    }

    public void get_sysCleanData() {

        JsonArrayRequest request_cleaningData = new JsonArrayRequest(Constantebi.URL_GET_SYSCLEAN, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis obieqtebis systemebis gawmendis info
//                Toast.makeText(this, "wamoigo", Toast.LENGTH_LONG).show();

                if (response.length() > 0) {
                    cleaningList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            SysClean sysClean = new SysClean();
                            sysClean.setId(response.getJSONObject(i).getInt("id"));
                            sysClean.setDge(response.getJSONObject(i).getInt("dge"));
                            sysClean.setTarigi(response.getJSONObject(i).getString("tarigi"));
                            sysClean.setDasaxeleba(response.getJSONObject(i).getString("dasaxeleba"));
                            sysClean.setComment(response.getJSONObject(i).getString("comment"));
                            cleaningList.add(sysClean);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                }

                adapter = new SysCleanAdapter(getApplicationContext(), cleaningList);
                list_sysclean.setAdapter(adapter);
//                progressDialog.dismiss();
//                expAdapter.notifyDataSetChanged();
                setRequestedOrientation(screenDefOrientation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "eror: no cleaning data!", Toast.LENGTH_LONG).show();
                setRequestedOrientation(screenDefOrientation);
//                progressDialog.dismiss();
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request_cleaningData);
    }
}
