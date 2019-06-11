package com.example.kdiakonidze.beerapeni;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.MyPagesAdapter;
import com.example.kdiakonidze.beerapeni.fragments.AmonaweriPageFr;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//@RequiresApi(api = Build.VERSION_CODES.N)
public class AmonaweriActivity extends AppCompatActivity {

    private static final int CALL_PERMISSION_REQUEST = 101;
    private String phoneNumber;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String archeuli_dge, title_0 = "დავალიანება", title_1 = "კასრი";
    private Button btn_setDate;
    private Obieqti currObieqti;
    private TextView t_objInfo;
    private TabLayout tabLayout;
    FragmentManager fragmentManager;
    MyPagesAdapter pagerAdapter;

    public static int requestCount = 0;
    public static Boolean grouped = true;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);
            archeuli_dge = dateFormat.format(calendar.getTime());
            btn_setDate.setText(archeuli_dge);

            calendar.add(Calendar.HOUR, 24);
            String gasagzavni_tarigi = dateFormat.format(calendar.getTime());

            AmonaweriPageFr fragmentM = (AmonaweriPageFr) pagerAdapter.getFragmentM();
            AmonaweriPageFr fragmentK = (AmonaweriPageFr) pagerAdapter.getFragmentK();
            fragmentM.setNewData(gasagzavni_tarigi, currObieqti.getId());
            fragmentK.setNewData(gasagzavni_tarigi, currObieqti.getId());

            t_objInfo.setText(String.format("%s\nთარიღი %s", currObieqti.getDasaxeleba(), archeuli_dge));
        }
    };

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tarigi", archeuli_dge);
        outState.putString("vali_m", title_0);
        outState.putString("vali_k", title_1);

        super.onSaveInstanceState(outState);
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amonaweri);
        dateFormat = new java.text.SimpleDateFormat(getString(R.string.patern_date));

        t_objInfo = findViewById(R.id.t_p4_objInfo);
        btn_setDate = findViewById(R.id.btn_p4_tarigi);
        tabLayout = findViewById(R.id.tabs_amonaweri);
        ViewPager viewPager = findViewById(R.id.viewpager_amonaweri);
        Toolbar toolbar = findViewById(R.id.tool_bar_amonaw);
        CheckBox chk_gr_amonaweri = findViewById(R.id.chk_gr_amonaweri);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 4);

        Intent i = getIntent();
        Bundle importedBundle = i.getExtras();
        if (importedBundle != null) {
            currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");
            if (currObieqti != null) {
                toolbar.setTitle(currObieqti.getDasaxeleba());
            }
        }

        toolbar.inflateMenu(R.menu.nav_menu);
        setSupportActionBar(toolbar);


        if (savedInstanceState != null) {
            archeuli_dge = savedInstanceState.getString("tarigi");
            title_0 = savedInstanceState.getString("vali_m");
            title_1 = savedInstanceState.getString("vali_k");
        } else {
            archeuli_dge = dateFormat.format(calendar.getTime());
            grouped = true;
        }

        t_objInfo.setText(String.format("%s\nთარიღი %s", currObieqti.getDasaxeleba(), archeuli_dge));

        btn_setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AmonaweriActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });


        fragmentManager = getSupportFragmentManager();
        pagerAdapter = new MyPagesAdapter(fragmentManager, currObieqti.getId());

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setTabsTitle(title_0, title_1);
//        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
//        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
//        if (tab0 != null && tab1 != null) {
//            tab0.setText(title_0);
//            tab1.setText(title_1);
//            tabLayout.getTabAt(0).setText(title_0);
//            tabLayout.getTabAt(1).setText(title_1);
//        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    AmonaweriPageFr fragmentM = (AmonaweriPageFr) pagerAdapter.getFragmentM();
                    fragmentM.dataRefresh();
                } else {
                    AmonaweriPageFr fragmentK = (AmonaweriPageFr) pagerAdapter.getFragmentK();
                    fragmentK.dataRefresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        chk_gr_amonaweri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                grouped = b;

                AmonaweriPageFr fragmentM = (AmonaweriPageFr) pagerAdapter.getFragmentM();
                AmonaweriPageFr fragmentK = (AmonaweriPageFr) pagerAdapter.getFragmentK();
                fragmentM.refreshData(grouped);
                fragmentK.refreshData(grouped);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detail_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_call) {
            phoneNumber = currObieqti.getTel();
            dialTo(phoneNumber);
        }

        if (item.getItemId() == R.id.m_edit_obj) {
            Intent intent_editObj = new Intent(getApplicationContext(), AddEditObject.class);
            intent_editObj.putExtra(Constantebi.REASON, Constantebi.EDIT);
            intent_editObj.putExtra("obj", currObieqti);
            startActivity(intent_editObj);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_davalianeba();
    }

    private void get_davalianeba() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constantebi.URL_GET_DAVALIANEBA;

        JsonArrayRequest request_davalianeba = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis davalianebis chamonaTvali yvela obieqtistvis

                if (response.length() > 0) {
                    int davalianebaM;
                    float davalianebaK;

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            if (response.getJSONObject(i).getInt("obj_id") == currObieqti.getId()) {
                                davalianebaM = response.getJSONObject(i).getInt("pr") - response.getJSONObject(i).getInt("pay");
                                davalianebaK = (float) response.getJSONObject(i).getDouble("k30in") - (float) response.getJSONObject(i).getDouble("k30out")
                                        + (float) response.getJSONObject(i).getDouble("k50in") - (float) response.getJSONObject(i).getDouble("k50out");

                                title_0 = "დავალიანება\n" + davalianebaM + " " + getString(R.string.lari);
                                title_1 = "კასრი\n" + MyUtil.floatToSmartStr(davalianebaK);
                                setTabsTitle(title_0, title_1);
//                                tabLayout.getTabAt(0).setText(title_0);
//                                tabLayout.getTabAt(1).setText(title_1);
                            }
                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }

                }

                requestCount--;
                if (requestCount == 0) {
                    setRequestedOrientation(Constantebi.screenDefOrientation);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestCount = 0;
                setRequestedOrientation(Constantebi.screenDefOrientation);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestCount++;
        queue.add(request_davalianeba);
    }

    private void setTabsTitle(String title_0, String title_1) {
        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        if (tab0 != null && tab1 != null) {
            tab0.setText(title_0);
            tab1.setText(title_1);
//            tabLayout.getTabAt(0).setText(title_0);
//            tabLayout.getTabAt(1).setText(title_1);
        }
    }

    private void dialTo(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AmonaweriActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dialTo(phoneNumber);
            }
        }
    }

}
