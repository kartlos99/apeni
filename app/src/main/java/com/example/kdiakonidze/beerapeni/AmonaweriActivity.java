package com.example.kdiakonidze.beerapeni;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.AmonaweriAdapter;
import com.example.kdiakonidze.beerapeni.adapters.MyPagesAdapter;
import com.example.kdiakonidze.beerapeni.fragments.AmonaweriPageFr;
import com.example.kdiakonidze.beerapeni.models.Amonaweri;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AmonaweriActivity extends AppCompatActivity {

    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String archeuli_dge, gasagzavni_tarigi, title_0 = "დავალიანება", title_1 = "კასრი";
    private Button btn_setDate;
    private Obieqti currObieqti;
    private TextView t_objInfo;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    FragmentManager fragmentManager;
    MyPagesAdapter pagerAdapter;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);
            archeuli_dge = dateFormat.format(calendar.getTime());
            btn_setDate.setText(archeuli_dge);

            calendar.add(Calendar.HOUR, 24);
            gasagzavni_tarigi = dateFormat.format(calendar.getTime());

            AmonaweriPageFr fragmentM = (AmonaweriPageFr) pagerAdapter.getFragmentM();
            AmonaweriPageFr fragmentK = (AmonaweriPageFr) pagerAdapter.getFragmentK();
            fragmentM.setNewData(gasagzavni_tarigi, currObieqti.getId());
            fragmentK.setNewData(gasagzavni_tarigi, currObieqti.getId());

            t_objInfo.setText(currObieqti.getDasaxeleba() + "\nთარიღი " + archeuli_dge);
        }
    };

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tarigi", archeuli_dge);
        outState.putString("vali_m", title_0);
        outState.putString("vali_k", title_1);

        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amonaweri);

        t_objInfo = (TextView) findViewById(R.id.t_p4_objInfo);
        btn_setDate = (Button) findViewById(R.id.btn_p4_tarigi);
        viewPager = (ViewPager) findViewById(R.id.viewpager_amonaweri);
        tabLayout = (TabLayout) findViewById(R.id.tabs_amonaweri);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_amonaw);


//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });


        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 4);

        Intent i = getIntent();
        Bundle importedBundle = i.getExtras();
        currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");
        toolbar.setTitle(currObieqti.getDasaxeleba());

        toolbar.inflateMenu(R.menu.nav_menu);
        setSupportActionBar(toolbar);


        if (savedInstanceState != null) {
            archeuli_dge = savedInstanceState.getString("tarigi");
            title_0 = savedInstanceState.getString("vali_m");
            title_1 = savedInstanceState.getString("vali_k");
        } else {
            archeuli_dge = dateFormat.format(calendar.getTime());
        }

        t_objInfo.setText(currObieqti.getDasaxeleba() + "\nთარიღი " + archeuli_dge);

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

        tabLayout.getTabAt(0).setText(title_0);
        tabLayout.getTabAt(1).setText(title_1);

        get_davalianeba();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detail_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_call) {
            Toast.makeText(this, currObieqti.getTel(), Toast.LENGTH_LONG).show();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + currObieqti.getTel()));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return true;
            }
            startActivity(callIntent);
        }

        if (item.getItemId() == R.id.m_edit_obj) {
            Intent intent_editObj = new Intent(getApplicationContext(), AddEditObject.class);
            intent_editObj.putExtra(Constantebi.REASON, Constantebi.EDIT);
            intent_editObj.putExtra("obj", currObieqti);
            startActivity(intent_editObj);
        }

        return super.onOptionsItemSelected(item);
    }

    private void get_davalianeba() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constantebi.URL_GET_DAVALIANEBA;

        JsonArrayRequest request_davalianeba = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis davalianebis chamonaTvali yvela obieqtistvis

                if (response.length() > 0) {
                    Integer davalianebaM = 0, davalianebaK = 0;

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            if (response.getJSONObject(i).getInt("obj_id") == currObieqti.getId()) {
                                davalianebaM = response.getJSONObject(i).getInt("pr") - response.getJSONObject(i).getInt("pay");
                                davalianebaK = response.getJSONObject(i).getInt("k30in") - response.getJSONObject(i).getInt("k30out")
                                        + response.getJSONObject(i).getInt("k50in") - response.getJSONObject(i).getInt("k50out");

                                title_0 = "დავალიანება\n" + davalianebaM;
                                title_1 = "კასრი\n" + davalianebaK;
                                tabLayout.getTabAt(0).setText(title_0);
                                tabLayout.getTabAt(1).setText(title_1);
                            }
                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request_davalianeba);
    }

}
