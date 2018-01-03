package com.example.kdiakonidze.beerapeni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SysClearActivity extends AppCompatActivity {

    Button btn_chawera;
    Spinner sp_sysclean;
    ListView list_sysclean;
    EditText e_comment;

    ArrayList<SysClean> cleaningList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_clear);

        btn_chawera = (Button) findViewById(R.id.btn_add_sysclean);
        sp_sysclean = (Spinner) findViewById(R.id.spinner_sysclean);
        list_sysclean = (ListView) findViewById(R.id.list_sys_clean);
        e_comment = (EditText) findViewById(R.id.e_cleaning_comment);

        final SpinnerAdapter spAdapter = new ArrayAdapter<Obieqti>(this, android.R.layout.simple_list_item_1, Constantebi.OBIEQTEBI);
        sp_sysclean.setAdapter(spAdapter);

        get_sysCleanData();

        btn_chawera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_chawera.setEnabled(false);
                Obieqti obieqti = (Obieqti) spAdapter.getItem(sp_sysclean.getSelectedItemPosition());
                insertNewCleaningInfo(obieqti.getId());
            }
        });
    }

    private void insertNewCleaningInfo(final int objID) {

        StringRequest ins_cleaning = new StringRequest(Request.Method.POST, Constantebi.URL_INS_LUDISSHETANA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "მონაცემები ჩაწერილია! " + response, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                btn_chawera.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("distrib_id", Constantebi.USER_ID);
                params.put("comment", e_comment.getText().toString());
                params.put("objID", String.valueOf(String.valueOf(objID)));

                params.toString();
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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

                SysCleanAdapter adapter = new SysCleanAdapter(getApplicationContext(), cleaningList);
                list_sysclean.setAdapter(adapter);
//                progressDialog.dismiss();
//                expAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "eror: no cleaning data!", Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request_cleaningData);
    }
}
