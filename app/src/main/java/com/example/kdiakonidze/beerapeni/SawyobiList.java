package com.example.kdiakonidze.beerapeni;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.SawyobiDetailAdapter;
import com.example.kdiakonidze.beerapeni.models.SawyobiDetailRow;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SawyobiList extends AppCompatActivity {

    static Boolean chamosatvirtia = false;
    private ListView listView;
    private ArrayList<SawyobiDetailRow> sawyobiDetailData;
    private SawyobiDetailAdapter adapter;

    private ProgressDialog progressDialog;
//    private Boolean requestInProgres = false, requestNeeded = true;
    private RequestQueue queue;

    private String archeuli_tarigi, chek_state;

    public void showtext(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        if(chamosatvirtia){
            getDataList(archeuli_tarigi, chek_state);
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sawyobi_list);

        sawyobiDetailData = new ArrayList<>();
        queue = Volley.newRequestQueue(this);

        listView = findViewById(R.id.list_sawyobi_detaluri);

        archeuli_tarigi = getIntent().getStringExtra("tarigi");
        chek_state = getIntent().getStringExtra("chek");

        getDataList(archeuli_tarigi, chek_state);

        this.registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t_comment = view.findViewById(R.id.t_sawylist_row_comment);
                SawyobiDetailRow row = (SawyobiDetailRow) adapter.getItem(i);
                if (!row.getComment().isEmpty()) {
                    if (t_comment.getVisibility() == View.VISIBLE) {
                        t_comment.setVisibility(View.GONE);
                    } else {
                        t_comment.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void getDataList(String tildate, final String chekState) {

        //Toast.makeText(SawyobiPage.this, "?tarigi="+tildate, Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constantebi.URL_GET_SAWY_DETAIL + "?tarigi=" + tildate, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                sawyobiDetailData.clear();

                if (response.length() > 0) {
                    try {

                        for (int i = 0; i < response.length(); i++) {
                            String tarigi = response.getJSONObject(i).getString("tarigi");
                            String name = response.getJSONObject(i).getString("name");
                            String ludi = response.getJSONObject(i).getString("ludi");
                            int k30 = response.getJSONObject(i).getInt("kasri30");
                            int k50 = response.getJSONObject(i).getInt("kasri50");
                            String comment = response.getJSONObject(i).getString("comment");
                            String chek = response.getJSONObject(i).getString("chek");
                            String recID = response.getJSONObject(i).getString("id");
                            int ludis_id = response.getJSONObject(i).getInt("ludis_id");

                            if (chekState.equals("1")) {
                                if (chek.equals("1")) {
                                    sawyobiDetailData.add(new SawyobiDetailRow(recID, tarigi, name, ludi, ludis_id, comment, k30, k50, chek));
                                }
                            } else {
                                sawyobiDetailData.add(new SawyobiDetailRow(recID, tarigi, name, ludi, ludis_id, comment, k30, k50, chek));
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SawyobiList.this, "ragac veraa rigze sawyobshi", Toast.LENGTH_SHORT).show();
                }

                adapter = new SawyobiDetailAdapter(getApplicationContext(), sawyobiDetailData);
                listView.setAdapter(adapter);

                dasasruli();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SawyobiList.this, error.toString(), Toast.LENGTH_SHORT).show();
                dasasruli();
                error.printStackTrace();
            }
        });

        progressDialog = ProgressDialog.show(this, "იტვირთება!", "დაელოდეთ!");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(jsonArrayRequest);
    }

    private void dasasruli() {
        if (!(progressDialog == null)) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
//        requestInProgres = false;
        chamosatvirtia = false;
        setRequestedOrientation(Constantebi.screenDefOrientation);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        SawyobiDetailRow row = (SawyobiDetailRow) adapter.getItem(info.position);

        this.getMenuInflater().inflate(R.menu.context_menu_order, menu);
        menu.setHeaderTitle(row.getComment());

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final SawyobiDetailRow row = (SawyobiDetailRow) adapter.getItem(info.position);
        switch (item.getItemId()) {

            case R.id.cm_order_edit:

                Intent intent_edit = new Intent(getApplicationContext(), AddDeliveryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("edit_row", row);
                intent_edit.putExtra("data", bundle);
                intent_edit.putExtra(Constantebi.REASON, Constantebi.EDIT);
                if (row.getLudi().equals("-")) {
                    intent_edit.putExtra("operacia", Constantebi.SAWYOBIDAN_GATANA);
                } else {
                    intent_edit.putExtra("operacia", Constantebi.SAWYOBSHI_SHETANA);
                }

                startActivity(intent_edit);
                break;
            case R.id.cm_order_del:
                //showtext(row.toString());

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setMessage(Constantebi.MSG_DEL).setTitle("** * * * * **");
                builder.setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (row.getLudi().equals("-")) {
                            removeRecord(row.getId(), Constantebi.TABLE_SAWY_OUT, info.position);
                        } else {
                            removeRecord(row.getId(), Constantebi.TABLE_SAWY_IN, info.position);
                        }
                    }
                }).setNegativeButton("არა", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void removeRecord(final String id, final String table, final int listPosition) {
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
                    sawyobiDetailData.remove(listPosition);
                    adapter.notifyDataSetChanged();
                    //shekvetebiAdapter.reNewData(shekvetebiArrayList, false);
                }
                setRequestedOrientation(Constantebi.screenDefOrientation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRequestedOrientation(Constantebi.screenDefOrientation);
                Toast.makeText(getApplicationContext(), error.getMessage() + " -", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("table", table);
                return params;
            }
        };

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request_DelOrder);
    }
}
