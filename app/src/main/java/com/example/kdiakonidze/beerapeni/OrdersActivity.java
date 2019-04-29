package com.example.kdiakonidze.beerapeni;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.ExpShekvetebiAdapter;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.ShekvetebiGR;
import com.example.kdiakonidze.beerapeni.models.ShekvetebiSum;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.GlobalServise;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrdersActivity extends AppCompatActivity implements GlobalServise.vListener {

    Button btn_setDate, btn_addOrder;
    TextView t_Tarigi;
    CheckBox chBox_orderGroup;
    ExpandableListView listView_shekvetebi;
    ProgressDialog progressDialog;

    ArrayList<Shekvetebi> shekvetebiArrayList;
    ArrayList<ShekvetebiGR> shekvetebiArrayListGR;
    ExpShekvetebiAdapter shekvetebiAdapter;
    Calendar calendar;
    String archeuli_dge;
    static Boolean chamosatvirtia = false;
    SimpleDateFormat dateFormat;
    private int screenDefOrientation;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);
//            calendar.add(Calendar.HOUR, 4);
            archeuli_dge = dateFormat.format(calendar.getTime());
            btn_setDate.setText(String.format("თარიღი: %s ", archeuli_dge));
            shekvetebis_chamotvirtva(archeuli_dge);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tarigi", archeuli_dge);
        outState.putSerializable("order_list", shekvetebiArrayList);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        dateFormat = new SimpleDateFormat(getString(R.string.patern_date));

        chBox_orderGroup = findViewById(R.id.checkbox_order_group);
        listView_shekvetebi = findViewById(R.id.list_shekvetebi);
        t_Tarigi = findViewById(R.id.t_tarigi);
        btn_setDate = findViewById(R.id.btn_setdate);
        btn_addOrder = findViewById(R.id.btn_addOrder);

        shekvetebiArrayList = new ArrayList<>();
        shekvetebiArrayListGR = new ArrayList<>();
        screenDefOrientation = getRequestedOrientation();

        calendar = Calendar.getInstance();
//        calendar.add(Calendar.HOUR, 4);

        if (savedInstanceState != null) {
            archeuli_dge = savedInstanceState.getString("tarigi");
            shekvetebiArrayList.clear();
            Object ordListObj = savedInstanceState.getSerializable("order_list");
            if (ordListObj instanceof List) {
                for (int i = 0; i < ((List) ordListObj).size(); i++) {
                    Object item = ((List) ordListObj).get(i);
                    if (item instanceof Shekvetebi) {
                        shekvetebiArrayList.add((Shekvetebi) item);
                    }
                }
            }
            shekvetebiAdapter = new ExpShekvetebiAdapter(
                    getApplicationContext(),
                    (chBox_orderGroup.isChecked() ? expandOrders(groupOrders(shekvetebiArrayList)) : expandOrders(shekvetebiArrayList)),
                    chBox_orderGroup.isChecked()
            );
            listView_shekvetebi.setAdapter(shekvetebiAdapter);
        } else {
            archeuli_dge = dateFormat.format(calendar.getTime());
            chamosatvirtia = true;
        }

        btn_setDate.setText(String.format("თარიღი: %s ", archeuli_dge));

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
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", groupOrders(shekvetebiArrayList));
                intent.putExtra("objINorder", bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        chBox_orderGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                shekvetebiAdapter.reNewData((checked ? expandOrders(groupOrders(shekvetebiArrayList)) : expandOrders(shekvetebiArrayList)), checked);
            }
        });

        this.registerForContextMenu(listView_shekvetebi);

        listView_shekvetebi.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (!chBox_orderGroup.isChecked()) {
                    //  Toast.makeText(getApplicationContext(), "მოხსენით დაჯგუფება", Toast.LENGTH_SHORT).show();
                    TextView t_comment = view.findViewById(R.id.t_orderlist_row_comment);
                    Shekvetebi shekvetebi = (Shekvetebi) shekvetebiAdapter.getChild(i, i1);
                    if (!shekvetebi.getComment().isEmpty()) {
                        if (t_comment.getVisibility() == View.VISIBLE) {
                            t_comment.setVisibility(View.GONE);
                        } else {
                            t_comment.setVisibility(View.VISIBLE);
                        }
                    }
                }
                return true;
            }
        });
    }

    private ArrayList<Shekvetebi> groupOrders(ArrayList<Shekvetebi> gadmocemuliShekvetebi) {
        ArrayList<Shekvetebi> groupedOrderList = new ArrayList<>();
        String objName = "", beerName = "", dName = "", beerBeckgroundColor = String.format("#%02X%02X%02X", Color.red(Color.WHITE), Color.green(Color.WHITE), Color.blue(Color.WHITE));
        float k30w = 0, k50w = 0, k30in = 0, k50in = 0;
        boolean chek = false;

        ArrayList<Shekvetebi> shekvetebiList = new ArrayList<>(gadmocemuliShekvetebi);
//        shekvetebiList.addAll(gadmocemuliShekvetebi);

        if (shekvetebiList.size() > 0) {
            Shekvetebi shekveta = shekvetebiList.get(0);
            objName = shekveta.getObieqti();
            beerName = shekveta.getLudi();
            dName = shekveta.getDistrib_Name();
            beerBeckgroundColor = shekveta.getColor();
        }

        for (int i = 0; i < shekvetebiList.size(); i++) {
            if (shekvetebiList.get(i).getObieqti().equals(objName) && shekvetebiList.get(i).getLudi().equals(beerName)) {
                k30w += shekvetebiList.get(i).getK30wont();
                k50w += shekvetebiList.get(i).getK50wont();
                k30in += shekvetebiList.get(i).getK30in();
                k50in += shekvetebiList.get(i).getK50in();
            } else {
                Shekvetebi newGrOrder = new Shekvetebi(objName, beerName, k30in, k50in, k30w, k50w);
                if (chek) {
                    newGrOrder.setChk("1");
                } else {
                    newGrOrder.setChk("0");
                }
                chek = false;
                newGrOrder.setDistrib_Name(dName);
                newGrOrder.setColor(beerBeckgroundColor);
                groupedOrderList.add(newGrOrder);
                Shekvetebi shekveta = shekvetebiList.get(i);
                objName = shekveta.getObieqti();
                beerName = shekveta.getLudi();
                dName = shekveta.getDistrib_Name();
                beerBeckgroundColor = shekveta.getColor();
                k30w = shekvetebiList.get(i).getK30wont();
                k50w = shekvetebiList.get(i).getK50wont();
                k30in = shekvetebiList.get(i).getK30in();
                k50in = shekvetebiList.get(i).getK50in();
            }
            if (shekvetebiList.get(i).getChk().equals("1")) {
                chek = true;
            }
        }

        if (!objName.equals("")) {
            Shekvetebi newGrOrder = new Shekvetebi(objName, beerName, k30in, k50in, k30w, k50w);
            if (chek) {
                newGrOrder.setChk("1");
            } else {
                newGrOrder.setChk("0");
            }
            //chek = false;
            newGrOrder.setDistrib_Name(dName);
            newGrOrder.setColor(beerBeckgroundColor);
            groupedOrderList.add(newGrOrder);
        }

        return groupedOrderList;
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
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int child = ExpandableListView.getPackedPositionChild(info.packedPosition);

        if (type == 1) {
            if (chBox_orderGroup.isChecked()) {
                Toast.makeText(getApplicationContext(), "მოხსენით დაჯგუფება", Toast.LENGTH_SHORT).show();
            } else {
                final Shekvetebi currOrder = (Shekvetebi) shekvetebiAdapter.getChild(group, child);
                this.getMenuInflater().inflate(R.menu.context_menu_order, menu);
                menu.setHeaderTitle(currOrder.getComment());

                MenuItem.OnMenuItemClickListener itemLisener = new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                        GlobalServise gServise = new GlobalServise(OrdersActivity.this);
                        gServise.setChangeListener(OrdersActivity.this);
                        gServise.editOrder(currOrder, item.getItemId());

                        return true;
                    }
                };

                for (int i = 0; i < Constantebi.USERsLIST.size(); i++) {
                    if (Constantebi.USERsLIST.get(i).getId() != currOrder.getDistrib_id()) {
                        menu.add(1, Constantebi.USERsLIST.get(i).getId(), i, "--> " + Constantebi.USERsLIST.get(i).getUsername()).setOnMenuItemClickListener(itemLisener);
                    }
                }

            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();

        int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int child = ExpandableListView.getPackedPositionChild(info.packedPosition);

        final Shekvetebi currOrder = (Shekvetebi) shekvetebiAdapter.getChild(group, child);
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
                    if ((currOrder.getK30in() > 0) || (currOrder.getK50in() > 0)) { // mitana
                        intent_editOrder = new Intent(getApplicationContext(), AddDeliveryActivity.class);
                        intent_editOrder.putExtra("id", currOrder.getOrder_id());
                        intent_editOrder.putExtra("operacia", Constantebi.MITANA);
                        intent_editOrder.putExtra("tarigi", archeuli_dge);
                        intent_editOrder.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        startActivity(intent_editOrder);
                    }
                    if ((currOrder.getK30wont() > 0) || (currOrder.getK50wont() > 0)) { // shekveta
                        intent_editOrder = new Intent(getApplicationContext(), AddOrderActivity.class);
                        intent_editOrder.putExtra("obj", currOrder);
                        intent_editOrder.putExtra(Constantebi.REASON, Constantebi.EDIT);
                        startActivity(intent_editOrder);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ჩანაწერს ვერ დაარედაქტირებთ,\nობიექტი '" + currOrder.getObieqti() + "' წაშლილია!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cm_order_del:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setMessage(Constantebi.MSG_DEL).setTitle("** * * * * **");
                builder.setPositiveButton("დიახ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if ((currOrder.getK30wont() > 0) || (currOrder.getK50wont() > 0))
                            removeRecord(currOrder.getOrder_id(), Constantebi.ORDER);
                        if ((currOrder.getK30in() > 0) || (currOrder.getK50in() > 0))
                            removeRecord(currOrder.getOrder_id(), Constantebi.MITANA);
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

    private void removeRecord(final int id, final String table) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request_DelOrder = new StringRequest(Request.Method.POST, Constantebi.URL_DEL_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response + " +", Toast.LENGTH_SHORT).show();
                if (response.equals("Removed!")) {

                    for (int i = 0; i < shekvetebiArrayList.size(); i++) {
                        if (shekvetebiArrayList.get(i).getOrder_id() == id) {
                            if (table.equals(Constantebi.MITANA) && (shekvetebiArrayList.get(i).getK30wont() + shekvetebiArrayList.get(i).getK50wont() == 0)) {
                                shekvetebiArrayList.remove(i);
                            }
                            if (table.equals(Constantebi.ORDER) && (shekvetebiArrayList.get(i).getK30in() + shekvetebiArrayList.get(i).getK50in() == 0)) {
                                shekvetebiArrayList.remove(i);
                            }
                        }
                    }

                    shekvetebiAdapter.reNewData(expandOrders(shekvetebiArrayList), false);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("table", table);
                params.put("userid", Constantebi.USER_ID);
                return params;
            }
        };

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request_DelOrder);
    }

    public void shekvetebis_chamotvirtva(String currentDay) {
        progressDialog = ProgressDialog.show(this, "იტვირთება!", "loading!");

        String url = Constantebi.URL_GET_ORDERLIST + "?tarigi=" + currentDay;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest requestObieqtebi = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis obieqtebis chamonatvali

                shekvetebiArrayList.clear();
                shekvetebiArrayListGR.clear();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Shekvetebi shekveta = new Shekvetebi(
                                    response.getJSONObject(i).getString("obieqti"),
                                    response.getJSONObject(i).getString("dasaxeleba"),
                                    (float)response.getJSONObject(i).getDouble("in_30"),
                                    (float)response.getJSONObject(i).getDouble("in_50"),
                                    (float)response.getJSONObject(i).getDouble("wont_30"),
                                    (float)response.getJSONObject(i).getDouble("wont_50")
                            );
                            shekveta.setChk(response.getJSONObject(i).getString("chk"));
                            shekveta.setDistrib_Name(response.getJSONObject(i).getString("name"));
                            shekveta.setDistrib_id(response.getJSONObject(i).getInt("distributor_id"));
                            shekveta.setOrder_id(response.getJSONObject(i).getInt("order_id"));
                            shekveta.setComment(response.getJSONObject(i).getString("comment"));
                            shekveta.setColor(response.getJSONObject(i).getString("color"));

                            shekvetebiArrayList.add(shekveta);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                }


                if (chBox_orderGroup.isChecked()) {
                    shekvetebiArrayListGR = expandOrders(groupOrders(shekvetebiArrayList));
                } else {
                    shekvetebiArrayListGR = expandOrders(shekvetebiArrayList);
                }

                shekvetebiAdapter = new ExpShekvetebiAdapter(getApplicationContext(), shekvetebiArrayListGR, chBox_orderGroup.isChecked());
                listView_shekvetebi.setAdapter(shekvetebiAdapter);
                if (shekvetebiArrayListGR.size() > 0) {
                    for (int i = 0; i < shekvetebiArrayListGR.size(); i++) {
                        listView_shekvetebi.expandGroup(i);
                    }
                }

                chamosatvirtia = false;
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

    private ArrayList<ShekvetebiGR> expandOrders(ArrayList<Shekvetebi> shekvetebiArrayList) {
        ArrayList<ShekvetebiGR> shekvetebiArListGR = new ArrayList<>();

        if (shekvetebiArrayList.size() > 0) {
            boolean exists = false;
            int k30w, k50w, k30, k50;

            if (chBox_orderGroup.isChecked()) {

                for (int i = 0; i < shekvetebiArrayList.size(); i++) {

                    for (int j = 0; j < shekvetebiArListGR.size(); j++) {
                        if (shekvetebiArListGR.get(j).getName().equals(shekvetebiArrayList.get(i).getDistrib_Name())) {
                            exists = true;
                            shekvetebiArListGR.get(j).getChilds().add(shekvetebiArrayList.get(i));
                            break;
                        }
                    }

                    if (!exists) {
                        ArrayList<Shekvetebi> newChildList = new ArrayList<>();
                        newChildList.add(shekvetebiArrayList.get(i));
                        shekvetebiArListGR.add(new ShekvetebiGR(shekvetebiArrayList.get(i).getDistrib_Name(), newChildList));
                    }
                    exists = false;
                }

            } else {

                float order;
                ArrayList<Shekvetebi> mitanebi = new ArrayList<>();

                for (int i = 0; i < shekvetebiArrayList.size(); i++) {
                    order = shekvetebiArrayList.get(i).getK30wont() + shekvetebiArrayList.get(i).getK50wont();

                    if (order > Constantebi.ACCURACY) {
                        for (int j = 0; j < shekvetebiArListGR.size(); j++) {
                            if (shekvetebiArListGR.get(j).getName().equals(shekvetebiArrayList.get(i).getDistrib_Name())) {
                                exists = true;
                                shekvetebiArListGR.get(j).getChilds().add(shekvetebiArrayList.get(i));
                                break;
                            }
                        }

                        if (!exists) {
                            ArrayList<Shekvetebi> newChildList = new ArrayList<>();
                            newChildList.add(shekvetebiArrayList.get(i));
                            shekvetebiArListGR.add(new ShekvetebiGR(shekvetebiArrayList.get(i).getDistrib_Name(), newChildList));
                        }
                        exists = false;

                    } else {
                        mitanebi.add(shekvetebiArrayList.get(i));
                    }
                }

                for (int i = 0; i < mitanebi.size(); i++) {

                    for (int j = 0; j < shekvetebiArListGR.size(); j++) {
                        for (int k = 0; k < shekvetebiArListGR.get(j).getChilds().size(); k++) {
                            if (shekvetebiArListGR.get(j).getChilds().get(k).getObieqti().equals(mitanebi.get(i).getObieqti())) {
                                exists = true;
                                shekvetebiArListGR.get(j).getChilds().add(mitanebi.get(i));
                                break;
                            }
                        }
                        if (exists) {
                            break;
                        }
                    }

                    if (!exists) {
                        Boolean b = false;
                        for (int j = 0; j < shekvetebiArListGR.size(); j++) {
                            if (shekvetebiArListGR.get(j).getName().equals(mitanebi.get(i).getDistrib_Name())) {
                                //exists = true;
                                shekvetebiArListGR.get(j).getChilds().add(mitanebi.get(i));
                                b = true;
                                break;
                            }
                        }

                        if (!b) {
                            ArrayList<Shekvetebi> newChildList = new ArrayList<>();
                            newChildList.add(mitanebi.get(i));
                            shekvetebiArListGR.add(new ShekvetebiGR(shekvetebiArrayList.get(i).getDistrib_Name(), newChildList));
                        }
                    }
                    exists = false;
                }


            }

            for (int i = 0; i < shekvetebiArListGR.size(); i++) {
                k30w = 0;
                k50w = 0;
                k30 = 0;
                k50 = 0;
                shekvetebiArListGR.get(i).getGrHeadOrderSum().clear();

                for (int j = 0; j < shekvetebiArListGR.get(i).getChilds().size(); j++) {
                    String ludi = shekvetebiArListGR.get(i).getChilds().get(j).getLudi();
                    exists = false;

                    for (int k = 0; k < shekvetebiArListGR.get(i).getGrHeadOrderSum().size(); k++) {
                        if (shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).getLudi().equals(ludi)) {
// getGrHeadOrderSum -s rom vcvli children -ic icvleba :@@@ ???
                            shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).setK30wont(shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).getK30wont() + shekvetebiArListGR.get(i).getChilds().get(j).getK30wont());
                            shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).setK50wont(shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).getK50wont() + shekvetebiArListGR.get(i).getChilds().get(j).getK50wont());
                            shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).setK30in(shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).getK30in() + shekvetebiArListGR.get(i).getChilds().get(j).getK30in());
                            shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).setK50in(shekvetebiArListGR.get(i).getGrHeadOrderSum().get(k).getK50in() + shekvetebiArListGR.get(i).getChilds().get(j).getK50in());
                            exists = true;
                        }
                    }

                    if (!exists) {
                        ShekvetebiSum newbeerItem = new ShekvetebiSum();
                        newbeerItem.setK30wont(shekvetebiArListGR.get(i).getChilds().get(j).getK30wont());
                        newbeerItem.setK50wont(shekvetebiArListGR.get(i).getChilds().get(j).getK50wont());
                        newbeerItem.setK30in(shekvetebiArListGR.get(i).getChilds().get(j).getK30in());
                        newbeerItem.setK50in(shekvetebiArListGR.get(i).getChilds().get(j).getK50in());
                        newbeerItem.setLudi(ludi);

                        shekvetebiArListGR.get(i).getGrHeadOrderSum().add(newbeerItem);
                    }

                    k30w += shekvetebiArListGR.get(i).getChilds().get(j).getK30wont();
                    k50w += shekvetebiArListGR.get(i).getChilds().get(j).getK50wont();
                    k30 += shekvetebiArListGR.get(i).getChilds().get(j).getK30in();
                    k50 += shekvetebiArListGR.get(i).getChilds().get(j).getK50in();
                }
                shekvetebiArListGR.get(i).setK30w(k30w);
                shekvetebiArListGR.get(i).setK50w(k50w);
                shekvetebiArListGR.get(i).setK30(k30);
                shekvetebiArListGR.get(i).setK50(k50);
            }
        }

        return shekvetebiArListGR;
    }

    @Override
    public void onChange() {
        shekvetebis_chamotvirtva(archeuli_dge);
    }
}
