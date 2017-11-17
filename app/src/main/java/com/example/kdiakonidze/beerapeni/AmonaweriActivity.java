package com.example.kdiakonidze.beerapeni;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private String archeuli_dge;
    private Button btn_setDate;
    private Obieqti currObieqti;
    private TextView t_objInfo, t_davalianebaM, t_davalianebaK;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    FragmentManager fragmentManager;
    MyPagesAdapter pagerAdapter;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year,month,day);
            archeuli_dge = dateFormat.format(calendar.getTime());
            btn_setDate.setText(archeuli_dge);

            AmonaweriPageFr fragmentM = (AmonaweriPageFr) pagerAdapter.getFragmentM();

            fragmentM.setNewData(archeuli_dge, currObieqti.getId());

            t_objInfo.setText(currObieqti.getDasaxeleba()+"\nთარიღი "+archeuli_dge);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amonaweri);

        t_objInfo = (TextView) findViewById(R.id.t_p4_objInfo);
        btn_setDate = (Button) findViewById(R.id.btn_p4_tarigi);
        viewPager = (ViewPager) findViewById(R.id.viewpager_amonaweri);
        tabLayout = (TabLayout) findViewById(R.id.tabs_amonaweri);


        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,4);
        archeuli_dge = dateFormat.format(calendar.getTime());

        Intent i = getIntent();
        Bundle importedBundle = i.getExtras();
        currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");

        t_objInfo.setText(currObieqti.getDasaxeleba()+"\nთარიღი "+archeuli_dge);

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

        get_davalianeba();
    }

    private void get_davalianeba() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constantebi.URL_GET_DAVALIANEBA;

        JsonArrayRequest request_davalianeba = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis davalianebis chamonaTvali yvela obieqtistvis

                if(response.length() > 0){
                    Integer davalianebaM = 0, davalianebaK = 0;

                    for (int i=0; i<response.length(); i++){
                        try {

                            if(response.getJSONObject(i).getInt("obj_id") == currObieqti.getId()) {
                                davalianebaM = response.getJSONObject(i).getInt("pr") - response.getJSONObject(i).getInt("pay");
                                davalianebaK = response.getJSONObject(i).getInt("k30in") - response.getJSONObject(i).getInt("k30out")
                                        + response.getJSONObject(i).getInt("k50in") - response.getJSONObject(i).getInt("k50out");


                                String titles[] = {"", ""};
                                titles[0] = "დავალიანება\n" + davalianebaM;
                                titles[1] = "კასრი\n" + davalianebaK;
                                pagerAdapter.setTitles(titles);
                                tabLayout.getTabAt(0).setText("დავალიანება\n" + davalianebaM);
                                tabLayout.getTabAt(1).setText("კასრი\n" + davalianebaK);
                            }
                        }catch (JSONException excep){
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
