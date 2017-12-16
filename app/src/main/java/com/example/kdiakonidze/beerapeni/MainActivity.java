package com.example.kdiakonidze.beerapeni;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.models.BeerModel;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.PeerObjPrice;
import com.example.kdiakonidze.beerapeni.models.Useri;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_shekvetebi, btn_mitana, btn_dayRealiz, btn_objRealiz;
    private ProgressDialog progressDialog;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navig_view);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        btn_shekvetebi = (Button) findViewById(R.id.btn_shekvetebi);
        btn_mitana = (Button) findViewById(R.id.btn_mitana);
        btn_dayRealiz = (Button) findViewById(R.id.btn_realiz_dge);
        btn_objRealiz = (Button) findViewById(R.id.btn_realiz_obj);

        btn_shekvetebi.setOnClickListener(MainActivity.this);
        btn_mitana.setOnClickListener(MainActivity.this);
        btn_dayRealiz.setOnClickListener(MainActivity.this);
        btn_objRealiz.setOnClickListener(MainActivity.this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView t_name = (TextView) findViewById(R.id.t_nav_name);
                TextView t_username = (TextView) findViewById(R.id.t_nav_username);

                t_name.setText(Constantebi.USER_NAME);
                if (Constantebi.USER_TYPE.equals("2")) {
                    t_username.setText(Constantebi.USER_USERNAME + " (admin)");
                } else {
                    t_username.setText(Constantebi.USER_USERNAME + " (user)");
                }
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.m_addobj:
                        Intent intent = new Intent(getApplicationContext(), AddEditObject.class);
                        intent.putExtra(Constantebi.REASON, Constantebi.CREATE);
                        startActivity(intent);
                        return true;

                    case R.id.m_adduser:
                        Intent intent_newuser = new Intent(getApplicationContext(), AddEditUser.class);
                        intent_newuser.putExtra(Constantebi.REASON, Constantebi.CREATE_USER);
                        startActivity(intent_newuser);
                        return true;

                    case R.id.m_users_list:
                        Intent intent_userlist = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(intent_userlist);
                    return true;

                    case R.id.m_addbeer:

                        return true;

                    case R.id.m_logout:
                        Constantebi.loged_in = false;
                        Intent loginpage = new Intent(getApplicationContext(), LoginActivity.class);
//                        loginpage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(loginpage);
                        return true;
                }


                return false;
            }
        });

        if (Constantebi.OBIEQTEBI.size() == 0) {
            get_BaseUnits();
        }

    }

    @Override
    protected void onStart() {
        if (!Constantebi.loged_in) {
            Intent loginpage = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginpage);
        } else {


        }
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_shekvetebi:
                Intent addOrderPage = new Intent();
                addOrderPage.setClass(getApplicationContext(), OrdersActivity.class);
//                addOrderPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(addOrderPage);
                break;
            case R.id.btn_mitana:
                Intent intent = new Intent(getApplicationContext(), ObjListActivity.class);
                intent.putExtra("mdebareoba", Constantebi.MDEBAREOBA_MITANA);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.btn_realiz_dge:
                Intent salesIntent = new Intent(getApplicationContext(), DaySaleActivity.class);
                startActivity(salesIntent);
                break;
            case R.id.btn_realiz_obj:
                Intent amonaweriIintent = new Intent(getApplicationContext(), ObjListActivity.class);
                amonaweriIintent.putExtra("mdebareoba", Constantebi.MDEBAREOBA_AMONAWERI);
                startActivity(amonaweriIintent);
                break;
            default:
                Toast.makeText(this, "uups", Toast.LENGTH_SHORT).show();
        }
    }

    public void get_BaseUnits() {
        RequestQueue queue = Volley.newRequestQueue(this);
        progressDialog = ProgressDialog.show(this, "იტვირთება!", "loading!");

        JsonArrayRequest requestObieqtebi = new JsonArrayRequest(Constantebi.URL_GET_OBIEQTS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis obieqtebis chamonatvali
                Toast.makeText(MainActivity.this, "wamoigo", Toast.LENGTH_LONG).show();
                Constantebi.OBIEQTEBI.clear();
                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Obieqti axaliObieqti = new Obieqti(response.getJSONObject(i).getString("dasaxeleba"));
                            axaliObieqti.setId(response.getJSONObject(i).getInt("id"));
                            axaliObieqti.setAdress(response.getJSONObject(i).getString("adress"));
                            axaliObieqti.setTel(response.getJSONObject(i).getString("tel"));
                            axaliObieqti.setComment(response.getJSONObject(i).getString("comment"));
                            axaliObieqti.setSk(response.getJSONObject(i).getString("sk"));
                            axaliObieqti.setSakpiri(response.getJSONObject(i).getString("sakpiri"));

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
                Toast.makeText(MainActivity.this, "eror no obieqtebi", Toast.LENGTH_LONG).show();
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
                            BeerModel newBeer = new BeerModel();
                            newBeer.setId(response.getJSONObject(i).getInt("id"));
                            newBeer.setDasaxeleba(response.getJSONObject(i).getString("dasaxeleba"));
                            newBeer.setFasi(response.getJSONObject(i).getDouble("fasi"));

                            Constantebi.ludiList.add(newBeer);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "ლუდის სახეოების მონაცემებია შესაყვანი!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "ludi sadaaa!", Toast.LENGTH_LONG).show();
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
                            if (objid == response.getJSONObject(i).getInt("obj_id")) {
                                objPrice.getFasebi().add(response.getJSONObject(i).getDouble("fasi"));
                            } else {
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

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "error fasebi", Toast.LENGTH_LONG).show();
            }
        });

        // --- momxmareblebis Camonatvali ---
        JsonArrayRequest request_users = new JsonArrayRequest(Constantebi.URL_GET_USERS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Constantebi.USERsLIST.clear();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Useri newUser = new Useri(response.getJSONObject(i).getInt("id"), response.getJSONObject(i).getString("username"));

                            newUser.setName(response.getJSONObject(i).getString("name"));
                            newUser.setType(response.getJSONObject(i).getInt("type"));
                            newUser.setTel(response.getJSONObject(i).getString("tel"));
                            newUser.setAdress(response.getJSONObject(i).getString("adress"));
                            newUser.setMaker(response.getJSONObject(i).getString("maker"));
                            newUser.setComment(response.getJSONObject(i).getString("comment"));

                            Constantebi.USERsLIST.add(newUser);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "no users!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "error users load", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(requestObieqtebi);
        queue.add(requestLudiList);
        queue.add(request_users);
        queue.add(request_fasebi);
    }
}
