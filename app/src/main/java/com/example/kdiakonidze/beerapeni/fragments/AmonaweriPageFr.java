package com.example.kdiakonidze.beerapeni.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.AddDeliveryActivity;
import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.adapters.AmonaweriAdapter;
import com.example.kdiakonidze.beerapeni.models.Amonaweri;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by k.diakonidze on 11/15/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class AmonaweriPageFr extends Fragment {

    private View pageview;
    private TextView t_tarigi, t_in, t_out, t_balance;
    private ListView amonaweriList;
    private ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<Amonaweri> amonaweriArrayList;
    private String gasagzavni_tarigi;
    private int location = -1, id = -1;
    Boolean chamosatvirtia = true;
    private AmonaweriAdapter amonaweriAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pageview = inflater.inflate(R.layout.amonaweri_page_view, container, false);
        t_balance = (TextView) pageview.findViewById(R.id.t_amon_title_balance);
        t_in = (TextView) pageview.findViewById(R.id.t_amon_title_in);
        t_out = (TextView) pageview.findViewById(R.id.t_amon_title_out);
        t_tarigi = (TextView) pageview.findViewById(R.id.t_amon_title_tarigi);
        amonaweriList = (ListView) pageview.findViewById(R.id.listview_amonaweri);
        registerForContextMenu(amonaweriList);
        return pageview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tarigi", gasagzavni_tarigi);
        outState.putSerializable("amonaweri", amonaweriArrayList);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        amonaweriArrayList = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24 + 4); // anu es dge rom bolomde chaitvalos

        location = getArguments().getInt("location"); // romel gverdze vart
        id = getArguments().getInt("id");  // obieqtis id

        if (savedInstanceState != null) {
            gasagzavni_tarigi = savedInstanceState.getString("tarigi");
            amonaweriArrayList.clear();
            amonaweriArrayList = (ArrayList<Amonaweri>) savedInstanceState.getSerializable("amonaweri");
            amonaweriAdapter = new AmonaweriAdapter(getContext(), amonaweriArrayList, location);
            amonaweriList.setAdapter(amonaweriAdapter);

            chamosatvirtia = false;
        } else {
            gasagzavni_tarigi = dateFormat.format(calendar.getTime());
            chamosatvirtia = true;
        }

        t_tarigi.setText("თარიღი");
        if (location == 0) {
            t_in.setText("ლუდის ღირებულება");
            t_out.setText("გადახდა");
            t_balance.setText("დავალიანება");
        }
        if (location == 1) {
            t_in.setText("მიტანილი კასრები");
            t_out.setText("წამოღებული კასრები");
            t_balance.setText("ნაშთი");
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (location == 0) {
            getActivity().getMenuInflater().inflate(R.menu.context_menu_amonaw_m, menu);
        }
        if (location == 1) {
            getActivity().getMenuInflater().inflate(R.menu.context_menu_amonaw_k, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Amonaweri amonaweriRow = (Amonaweri) amonaweriAdapter.getItem(info.position);

        switch (item.getItemId()) {
            case R.id.cm_amonaw_m_edit:
                if (location == 0) {
//                    Toast.makeText(getContext(), amonaweriRow.toString(), Toast.LENGTH_LONG).show();
                    if(amonaweriRow.getPay() != 0){ // tanxis agebis redaqtireba
                        Intent intent_editMout = new Intent(getContext(), AddDeliveryActivity.class);
                        intent_editMout.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        intent_editMout.putExtra("operacia", Constantebi.M_OUT);
                        intent_editMout.putExtra("id", amonaweriRow.getId());
                        intent_editMout.putExtra("tanxa", amonaweriRow.getPay());
                        intent_editMout.putExtra("objid",id);
                        startActivity(intent_editMout);
                    }else {
                        Intent intent_editMitana = new Intent(getContext(), AddDeliveryActivity.class);
                        intent_editMitana.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        intent_editMitana.putExtra("operacia", Constantebi.MITANA);
                        intent_editMitana.putExtra("id", amonaweriRow.getId());
                        startActivity(intent_editMitana);
                    }
                }
                break;
            case R.id.cm_amonaw_m_del:
                if (location == 0) {
                    String table = "mitana";
                    if (amonaweriRow.getPay() != 0) {
                        table = "mout";
                    }
                    delRecord(amonaweriRow.getId(), table);
                }
                break;
            case R.id.cm_amonaw_k_edit:
                if (location == 1) {
//                    Toast.makeText(getContext(), amonaweriRow.toString(), Toast.LENGTH_LONG).show();
                    if (amonaweriRow.getK_out() != 0) { // kasris aRebas vakoreqtireb
                        Intent intent_editKout = new Intent(getContext(), AddDeliveryActivity.class);
                        intent_editKout.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        intent_editKout.putExtra("operacia", Constantebi.K_OUT);
                        intent_editKout.putExtra("id", amonaweriRow.getId());
                        startActivity(intent_editKout);
                    }else {
                        Intent intent_editMitana = new Intent(getContext(), AddDeliveryActivity.class);
                        intent_editMitana.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        intent_editMitana.putExtra("operacia", Constantebi.MITANA);
                        intent_editMitana.putExtra("id", amonaweriRow.getId());
                        startActivity(intent_editMitana);
                    }
                }

                break;
            case R.id.cm_amonaw_k_del:
                if (location == 1) {
                    String table = "mitana";
                    if (amonaweriRow.getK_out() != 0) {
                        table = "kout";
                    }
                    delRecord(amonaweriRow.getId(), table);
                }
                break;
        }
        amonaweriAdapter.notifyDataSetChanged();

        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chamosatvirtia) {
            setNewData(gasagzavni_tarigi, id);
        }
    }

    private void delRecord(final int id, final String table) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request_DelOrder = new StringRequest(Request.Method.POST, Constantebi.URL_DEL_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response + " +", Toast.LENGTH_SHORT).show();
                if (response.equals("Removed!")) {

                    for (int i = 0; i < amonaweriArrayList.size(); i++) {
                        if (amonaweriArrayList.get(i).getId() == id) {
                            amonaweriArrayList.remove(i);
                        }
                    }
                    amonaweriAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage() + " -", Toast.LENGTH_SHORT).show();
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

        queue.add(request_DelOrder);
    }

    public void setNewData(String tarigi, int objID) {
        // tarigi aris shemdegi dgis 00:00 saati
        gasagzavni_tarigi = tarigi;
        progressDialog = ProgressDialog.show(getContext(), "იტვირთება!", "დაელოდეთ!");
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "";
        if (location == 0) {
            url = Constantebi.URL_GET_AMONAWERI + "?tarigi=" + tarigi + "&objID=" + objID;
        } else {
            url = Constantebi.URL_GET_AMONAWERI_K + "?tarigi=" + tarigi + "&objID=" + objID;
        }

        JsonArrayRequest request_amonaweri = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                amonaweriArrayList.clear();

                if (response.length() > 0) {
                    try {

                        for (int i = 0; i < response.length(); i++) {
                            Amonaweri amonaweri = new Amonaweri();
                            amonaweri.setTarigi(response.getJSONObject(i).getString("dt"));
                            if (location == 0) {
                                amonaweri.setPrice(response.getJSONObject(i).getDouble("pr"));
                                amonaweri.setPay(response.getJSONObject(i).getDouble("pay"));
                                amonaweri.setBalance(response.getJSONObject(i).getDouble("bal"));
                            } else {
                                amonaweri.setK_in(response.getJSONObject(i).getInt("k_in"));
                                amonaweri.setK_out(response.getJSONObject(i).getInt("k_out"));
                                amonaweri.setK_balance(response.getJSONObject(i).getInt("bal"));
                            }
                            amonaweri.setId(response.getJSONObject(i).getInt("id"));
                            amonaweriArrayList.add(amonaweri);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "no activiti!", Toast.LENGTH_SHORT).show();
                }

                amonaweriAdapter = new AmonaweriAdapter(getContext(), amonaweriArrayList, location);
                amonaweriList.setAdapter(amonaweriAdapter);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        queue.add(request_amonaweri);
    }

    public void dataRefresh() {
        amonaweriAdapter.notifyDataSetChanged();
    }
}
