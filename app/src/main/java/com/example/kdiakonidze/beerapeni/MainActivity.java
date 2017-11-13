package com.example.kdiakonidze.beerapeni;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.PeerObjPrice;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_shekvetebi, btn_mitana, btn_dayRealiz, btn_objRealiz;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_shekvetebi = (Button) findViewById(R.id.btn_shekvetebi);
        btn_mitana = (Button) findViewById(R.id.btn_mitana);
        btn_dayRealiz = (Button) findViewById(R.id.btn_realiz_dge);
        btn_objRealiz = (Button) findViewById(R.id.btn_realiz_obj);

        btn_shekvetebi.setOnClickListener(MainActivity.this);
        btn_mitana.setOnClickListener(MainActivity.this);
        btn_dayRealiz.setOnClickListener(MainActivity.this);
        btn_objRealiz.setOnClickListener(MainActivity.this);

        get_BaseUnits();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_shekvetebi:
                Intent addOrderPage = new Intent();
                addOrderPage.setClass(getApplicationContext(), OrdersActivity.class);
                startActivity(addOrderPage);
                break;
            case R.id.btn_mitana:
                Intent intent = new Intent(getApplicationContext(), ObjListActivity.class);
                intent.putExtra("mdebareoba", Constantebi.MDEBAREOBA_MITANA);
                startActivity(intent);
                break;
            case R.id.btn_realiz_dge:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_realiz_obj:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "uups", Toast.LENGTH_SHORT).show();
        }
    }

    private void get_BaseUnits() {
        RequestQueue queue = Volley.newRequestQueue(this);

        progressDialog = ProgressDialog.show(this, "იტვირთება!", "loading!");

        String url = Constantebi.URL_GET_OBIEQTS;

        JsonArrayRequest requestObieqtebi = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis obieqtebis chamonatvali
                Toast.makeText(MainActivity.this, "wamoigo", Toast.LENGTH_LONG).show();
                Constantebi.OBIEQTEBI.clear();
                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Obieqti axaliObieqti = new Obieqti(response.getJSONObject(i).getString("dasaxeleba"));
                            axaliObieqti.setAdress(response.getJSONObject(i).getString("adress"));
                            axaliObieqti.setTel(response.getJSONObject(i).getString("tel"));
                            axaliObieqti.setComment(response.getJSONObject(i).getString("comment"));
                            axaliObieqti.setId(response.getJSONObject(i).getInt("id"));

                            Constantebi.OBIEQTEBI.add(axaliObieqti);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }

                }

                progressDialog.dismiss();
//                expAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        // ludis saxeobebis sia mogvaqvs
        JsonArrayRequest requestLudiList = new JsonArrayRequest(Constantebi.URL_GET_LUDILIST, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Constantebi.ludiList.clear();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Constantebi.ludiList.add(response.getJSONObject(i).getString("dasaxeleba"));
                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "ლუდის სახეოების მონაცემებია შესაყვანი!", Toast.LENGTH_LONG).show();
                    Constantebi.ludiList.add("N.A.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // ludis fasebi titoeuli obieqtistvis
        JsonArrayRequest request_fasebi = new JsonArrayRequest(Constantebi.URL_GET_FASEBI, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Constantebi.FASEBI.clear();
                Integer objid = 0;
                if (response.length() > 0) {
                    try {
                        objid = response.getJSONObject(0).getInt("obj_id");

                        PeerObjPrice objPrice = new PeerObjPrice(objid);

                        for (int i = 0; i < response.length(); i++) {
                            if(objid == response.getJSONObject(i).getInt("obj_id")){
                                objPrice.getFasebi().add(response.getJSONObject(i).getDouble("fasi"));
                            }else{
                                Constantebi.FASEBI.add(objPrice);

                                objid = response.getJSONObject(i).getInt("obj_id");
                                objPrice = new PeerObjPrice(objid);
                                objPrice.getFasebi().add(response.getJSONObject(i).getDouble("fasi"));
                            }
                        }
                        Constantebi.FASEBI.add(objPrice);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "ლუდის სახეოების მონაცემებია შესაყვანი!", Toast.LENGTH_LONG).show();
                    Constantebi.ludiList.add("N.A.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request_fasebi);
        queue.add(requestObieqtebi);
        queue.add(requestLudiList);
    }
}
