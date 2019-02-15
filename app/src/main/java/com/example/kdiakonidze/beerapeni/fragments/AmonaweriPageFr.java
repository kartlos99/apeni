package com.example.kdiakonidze.beerapeni.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.kdiakonidze.beerapeni.AddDeliveryActivity;
import com.example.kdiakonidze.beerapeni.AmonaweriActivity;
import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.adapters.AmonaweriAdapter;
import com.example.kdiakonidze.beerapeni.models.Amonaweri;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by k.diakonidze on 11/15/2017.
 */

public class AmonaweriPageFr extends Fragment {

    private TextView t_tarigi, t_in, t_out, t_balance;
    private ListView amonaweriList;
    private ProgressDialog progressDialog;
    private SimpleDateFormat dateFormat;
    private ArrayList<Amonaweri> amonaweriArrayList;
    private String gasagzavni_tarigi;
    private int location = -1, id = -1;
    Boolean chamosatvirtia = true;
    private AmonaweriAdapter amonaweriAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View pageview = inflater.inflate(R.layout.amonaweri_page_view, container, false);
        t_balance = pageview.findViewById(R.id.t_amon_title_balance);
        t_in = pageview.findViewById(R.id.t_amon_title_in);
        t_out = pageview.findViewById(R.id.t_amon_title_out);
        t_tarigi = pageview.findViewById(R.id.t_amon_title_tarigi);
        amonaweriList = pageview.findViewById(R.id.listview_amonaweri);
        registerForContextMenu(amonaweriList);
        return pageview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tarigi", gasagzavni_tarigi);
        outState.putSerializable("amonaweri", amonaweriArrayList);

        super.onSaveInstanceState(outState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dateFormat = new java.text.SimpleDateFormat(getString(R.string.patern_date));

        amonaweriArrayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24 + 4); // anu es dge rom bolomde chaitvalos

        location = getArguments().getInt("location"); // romel gverdze vart
        id = getArguments().getInt("id");  // obieqtis id

        if (savedInstanceState != null) {
            gasagzavni_tarigi = savedInstanceState.getString("tarigi");
            amonaweriArrayList.clear();
            Object listObj = savedInstanceState.getSerializable("amonaweri");
            if (listObj instanceof List) {
                for (int i = 0; i < ((List) listObj).size(); i++) {
                    Object item = ((List) listObj).get(i);
                    if (item instanceof Amonaweri) {
                        amonaweriArrayList.add((Amonaweri) item);
                    }
                }
            }
            if (AmonaweriActivity.grouped) {
                amonaweriAdapter = new AmonaweriAdapter(getContext(), groupAmonaweri(amonaweriArrayList), location, true);
            } else {
                amonaweriAdapter = new AmonaweriAdapter(getContext(), amonaweriArrayList, location, false);
            }
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

        amonaweriList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!AmonaweriActivity.grouped) {
                    TextView t_comment = view.findViewById(R.id.t_amonaweri_row_comment);
                    Amonaweri amonaweri = (Amonaweri) amonaweriAdapter.getItem(i);
                    if (!amonaweri.getComment().isEmpty()) {
                        if (t_comment.getVisibility() == View.VISIBLE) {
                            t_comment.setVisibility(View.GONE);
                        } else {
                            t_comment.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if(AmonaweriActivity.grouped){
            Toast.makeText(getContext(), "მოხსენით დაჯგუფება!", Toast.LENGTH_SHORT).show();
        }else {
            if (location == 0) {
                getActivity().getMenuInflater().inflate(R.menu.context_menu_amonaw_m, menu);
                menu.setHeaderTitle("--  თანხები  --");
            }
            if (location == 1) {
                getActivity().getMenuInflater().inflate(R.menu.context_menu_amonaw_k, menu);
                menu.setHeaderTitle("--  კასრები  --");
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


        switch (item.getItemId()) {
            case R.id.cm_amonaw_m_edit:
                if (location == 0) {
                    Amonaweri amonaweriRow = (Amonaweri) amonaweriAdapter.getItem(info.position);
//                    Toast.makeText(getContext(), amonaweriRow.toString() + " a1", Toast.LENGTH_LONG).show();
                    if (amonaweriRow.getPay() != 0) { // tanxis agebis redaqtireba
                        Intent intent_editMout = new Intent(getContext(), AddDeliveryActivity.class);
                        intent_editMout.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        intent_editMout.putExtra("operacia", Constantebi.M_OUT);
                        intent_editMout.putExtra("id", amonaweriRow.getId());
                        intent_editMout.putExtra("tanxa", amonaweriRow.getPay());
                        intent_editMout.putExtra("objid", id);
                        intent_editMout.putExtra("tarigi", amonaweriRow.getTarigi());
                        startActivity(intent_editMout);
                    } else {
                        Intent intent_editMitana = new Intent(getContext(), AddDeliveryActivity.class);
                        intent_editMitana.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        intent_editMitana.putExtra("operacia", Constantebi.MITANA);
                        intent_editMitana.putExtra("id", amonaweriRow.getId());
                        intent_editMitana.putExtra("tarigi", amonaweriRow.getTarigi());
                        startActivity(intent_editMitana);
                    }
                }
                break;
            case R.id.cm_amonaw_m_del:
                if (location == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setMessage(Constantebi.MSG_DEL).setTitle("** * * * * **");
                    builder.setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Amonaweri amonaweriRow = (Amonaweri) amonaweriAdapter.getItem(info.position);
                            String table = "mitana";
                            if (amonaweriRow.getPay() != 0) {
                                table = "mout";
                            }
                            delRecord(amonaweriRow.getId(), table);
                        }
                    }).setNegativeButton("არა", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.cm_amonaw_k_edit:
                if (location == 1) {
                    Amonaweri amonaweriRow = (Amonaweri) amonaweriAdapter.getItem(info.position);
//                    Toast.makeText(getContext(), amonaweriRow.toString() + " a2", Toast.LENGTH_LONG).show();
                    if (amonaweriRow.getK_out() != 0) { // kasris aRebas vakoreqtireb
                        Intent intent_editKout = new Intent(getContext(), AddDeliveryActivity.class);
                        intent_editKout.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        intent_editKout.putExtra("operacia", Constantebi.K_OUT);
                        intent_editKout.putExtra("id", amonaweriRow.getId());
                        intent_editKout.putExtra("tarigi", amonaweriRow.getTarigi());
                        startActivity(intent_editKout);
                    } else {
                        Intent intent_editMitana = new Intent(getContext(), AddDeliveryActivity.class);
                        intent_editMitana.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        intent_editMitana.putExtra("operacia", Constantebi.MITANA);
                        intent_editMitana.putExtra("id", amonaweriRow.getId());
                        intent_editMitana.putExtra("tarigi", amonaweriRow.getTarigi());
                        startActivity(intent_editMitana);
                    }
                }

                break;
            case R.id.cm_amonaw_k_del:
                if (location == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setMessage(Constantebi.MSG_DEL).setTitle("** * * * * **");
                    builder.setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Amonaweri amonaweriRow = (Amonaweri) amonaweriAdapter.getItem(info.position);
                            String table = "mitana";
                            if (amonaweriRow.getK_out() != 0) {
                                table = "kout";
                            }
                            delRecord(amonaweriRow.getId(), table);
                        }
                    }).setNegativeButton("არა", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
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
                getActivity().setRequestedOrientation(Constantebi.screenDefOrientation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage() + " -", Toast.LENGTH_SHORT).show();
                getActivity().setRequestedOrientation(Constantebi.screenDefOrientation);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("table", table);
                params.put("userid", Constantebi.USER_ID);
                return params;
            }
        };

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request_DelOrder);
    }

    public void refreshData(Boolean gr) {
        if (gr) {
            amonaweriAdapter = new AmonaweriAdapter(getContext(), groupAmonaweri(amonaweriArrayList), location, true);
        } else {
            amonaweriAdapter = new AmonaweriAdapter(getContext(), amonaweriArrayList, location, false);
        }
        amonaweriList.setAdapter(amonaweriAdapter);
    }

    public void setNewData(String tarigi, int objID) {
        // tarigi aris shemdegi dgis 00:00 saati
        gasagzavni_tarigi = tarigi;
        progressDialog = ProgressDialog.show(getContext(), "იტვირთება!", "დაელოდეთ!");
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url;
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
                            amonaweri.setComment(response.getJSONObject(i).getString("comment"));
                            amonaweriArrayList.add(amonaweri);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "ჩანაწერები არ არის!", Toast.LENGTH_SHORT).show();
                }

                if (AmonaweriActivity.grouped) {
                    amonaweriAdapter = new AmonaweriAdapter(getContext(), groupAmonaweri(amonaweriArrayList), location, true);
                } else {
                    amonaweriAdapter = new AmonaweriAdapter(getContext(), amonaweriArrayList, location, false);
                }
                amonaweriList.setAdapter(amonaweriAdapter);
                progressDialog.dismiss();
                AmonaweriActivity.requestCount--;
                if (AmonaweriActivity.requestCount == 0) {
                    getActivity().setRequestedOrientation(Constantebi.screenDefOrientation);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                AmonaweriActivity.requestCount = 0;
                getActivity().setRequestedOrientation(Constantebi.screenDefOrientation);
            }
        });

        AmonaweriActivity.requestCount++;
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request_amonaweri);
    }

    private ArrayList<Amonaweri> groupAmonaweri(ArrayList<Amonaweri> rowList) {
        ArrayList<Amonaweri> groupedList = new ArrayList<>();
        Date gr_date = new Date();
        Date currRowDate = new Date();

        if (rowList.size() > 0) {

            try {
                gr_date = dateFormat.parse(rowList.get(0).getTarigi());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Double pr = 0.0;
            Double pay = 0.0;
            Double bal = rowList.get(0).getBalance();
            int k_in = 0;
            int k_out = 0;
            int k_bal = rowList.get(0).getK_balance();

            for (int i = 0; i < rowList.size(); i++) {
                try {
                    currRowDate = dateFormat.parse(rowList.get(i).getTarigi());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (currRowDate.equals(gr_date)) {
                    pr += rowList.get(i).getPrice();
                    pay += rowList.get(i).getPay();
                    k_in += rowList.get(i).getK_in();
                    k_out += rowList.get(i).getK_out();

                } else {
                    Amonaweri currGrRow = new Amonaweri();
                    currGrRow.setTarigi(dateFormat.format(gr_date));
                    currGrRow.setPrice(pr);
                    currGrRow.setPay(pay);
                    currGrRow.setBalance(bal);
                    currGrRow.setK_in(k_in);
                    currGrRow.setK_out(k_out);
                    currGrRow.setK_balance(k_bal);
                    groupedList.add(currGrRow);

                    gr_date = currRowDate;
                    pr = rowList.get(i).getPrice();
                    pay = rowList.get(i).getPay();
                    bal = rowList.get(i).getBalance();
                    k_in = rowList.get(i).getK_in();
                    k_out = rowList.get(i).getK_out();
                    k_bal = rowList.get(i).getK_balance();

                }
            }
            Amonaweri currGrRow = new Amonaweri();
            currGrRow.setTarigi(dateFormat.format(gr_date));
            currGrRow.setPrice(pr);
            currGrRow.setPay(pay);
            currGrRow.setBalance(bal);
            currGrRow.setK_in(k_in);
            currGrRow.setK_out(k_out);
            currGrRow.setK_balance(k_bal);
            groupedList.add(currGrRow);

        }
        return groupedList;
    }

    public void dataRefresh() {
        amonaweriAdapter.notifyDataSetChanged();
    }
}
