package com.example.kdiakonidze.beerapeni;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.GlobalServise;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_shekvetebi, btn_mitana, btn_dayRealiz, btn_objRealiz;
    private ProgressDialog progressDialog;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    public static int requestCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constantebi.screenDefOrientation = getRequestedOrientation();

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
                        Intent intent_Beerlist = new Intent(getApplicationContext(), AddEditBeer.class);
                        startActivity(intent_Beerlist);
                        return true;

                    case R.id.m_sys_clear:
                        Intent intent_SysClear = new Intent(getApplicationContext(), SysClearActivity.class);
                        startActivity(intent_SysClear);
                        return true;

                    case R.id.m_ch_pass:
                        change_pass_dialog();

                        return true;

                    case R.id.m_logout:
                        Constantebi.loged_in = false;
                        File file = new File(getFilesDir(), Constantebi.USER_FILENAME);
                        if (file.exists()) {
                            file.delete();
                        }
                        Intent loginpage = new Intent(getApplicationContext(), LoginActivity.class);
//                        loginpage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(loginpage);
                        return true;
                }


                return false;
            }
        });


        if (Constantebi.OBIEQTEBI.size() == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            requestCount = 0;
            GlobalServise globalServise = new GlobalServise(this);
            globalServise.get_Obieqts();
            globalServise.get_BeerList();
            globalServise.get_Prises();
            globalServise.get_Users();
        }
    }

    private void change_pass_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View chPassView = getLayoutInflater().inflate(R.layout.change_pass_dialog, null);
        builder
                .setView(chPassView)
                .setTitle("პაროლის შეცვლა\nმომხმარებელი: "+Constantebi.USER_USERNAME)
                .setPositiveButton("შეცვლა", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("გასვლა", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText eOldPass = (EditText) chPassView.findViewById(R.id.e_old_pass);
                EditText eNewPass = (EditText) chPassView.findViewById(R.id.e_new_pass);
                EditText eNewPass2 = (EditText) chPassView.findViewById(R.id.e_new_pass2);
                if (eOldPass.getText().toString().equals(Constantebi.USER_PASS)) {
                    if (eNewPass.getText().toString().length() >= 3) {
                        if (eNewPass.getText().toString().equals(eNewPass2.getText().toString())) {
                            change_password(eNewPass.getText().toString());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "დაადასტურეთ ახალი პაროლი!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "მინიმუმ 3 სიმბოლო!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "პაროლი არასწორია!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void change_password(final String newPass) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Constantebi.URL_CH_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response + " +", Toast.LENGTH_SHORT).show();
                if (response.equals("sheicvala!") ) {
                    Constantebi.USER_PASS = newPass;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Constantebi.USER_ID);
                params.put("new_pass", newPass);

                params.toString();
                return params;
            }
        };

        queue.add(request);
    }

    @Override
    protected void onStart() {
        if (!Constantebi.loged_in) {
            Intent loginpage = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginpage);
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
}