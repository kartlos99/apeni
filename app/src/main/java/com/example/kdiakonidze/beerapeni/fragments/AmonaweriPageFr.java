package com.example.kdiakonidze.beerapeni.fragments;

import android.app.ProgressDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.adapters.AmonaweriAdapter;
import com.example.kdiakonidze.beerapeni.models.Amonaweri;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pageview = inflater.inflate(R.layout.amonaweri_page_view, container, false);
        t_balance = (TextView) pageview.findViewById(R.id.t_amon_title_balance);
        t_in = (TextView) pageview.findViewById(R.id.t_amon_title_in);
        t_out = (TextView) pageview.findViewById(R.id.t_amon_title_out);
        t_tarigi = (TextView) pageview.findViewById(R.id.t_amon_title_tarigi);
        amonaweriList = (ListView) pageview.findViewById(R.id.listview_amonaweri);

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
        calendar.add(Calendar.HOUR,24+4); // anu es dge rom bolomde chaitvalos

        location = getArguments().getInt("location"); // romel gverdze vart
        id = getArguments().getInt("id");

        if (savedInstanceState != null) {
            gasagzavni_tarigi = savedInstanceState.getString("tarigi");
            amonaweriArrayList.clear();
            amonaweriArrayList = (ArrayList<Amonaweri>) savedInstanceState.getSerializable("amonaweri");
            AmonaweriAdapter adapter = new AmonaweriAdapter(getContext(), amonaweriArrayList, location);
            amonaweriList.setAdapter(adapter);

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
        if (location == 1){
            t_in.setText("მიტანილი კასრები");
            t_out.setText("წამოღებული კასრები");
            t_balance.setText("ნაშთი");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(chamosatvirtia) {
            setNewData(gasagzavni_tarigi, id);
        }
    }

    public void setNewData(String tarigi, int objID) {
        // tarigi aris shemdegi dgis 00:00 saati
        gasagzavni_tarigi = tarigi;
        progressDialog = ProgressDialog.show(getContext(), "იტვირთება!", "დაელოდეთ!");
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url="";
        if(location == 0){
            url = Constantebi.URL_GET_AMONAWERI + "?tarigi=" + tarigi + "&objID="+ objID;
        }else {
            url = Constantebi.URL_GET_AMONAWERI_K + "?tarigi=" + tarigi + "&objID="+ objID;
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
                            if(location==0) {
                                amonaweri.setPrice(response.getJSONObject(i).getDouble("pr"));
                                amonaweri.setPay(response.getJSONObject(i).getDouble("pay"));
                                amonaweri.setBalance(response.getJSONObject(i).getDouble("bal"));
                            }else {
                                amonaweri.setK_in(response.getJSONObject(i).getInt("k_in"));
                                amonaweri.setK_out(response.getJSONObject(i).getInt("k_out"));
                                amonaweri.setK_balance(response.getJSONObject(i).getInt("bal"));
                            }
                            amonaweriArrayList.add(amonaweri);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "no activiti!", Toast.LENGTH_SHORT).show();
                }

                AmonaweriAdapter adapter = new AmonaweriAdapter(getContext(), amonaweriArrayList, location);
                amonaweriList.setAdapter(adapter);
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


}
