package com.example.kdiakonidze.beerapeni;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.BeerListAdapter;
import com.example.kdiakonidze.beerapeni.models.BeerModel;
import com.example.kdiakonidze.beerapeni.utils.ChooseColorDialog;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.GlobalServise;

import java.util.HashMap;
import java.util.Map;

public class AddEditBeer extends AppCompatActivity implements ChooseColorDialog.ColorDialogListener {

    private TextView tTitle;
    private Button btn_beerDone, btn_uaryofa;
    private EditText eBeerName, eBeerPr;
    private ImageButton btnColor;

    private BeerListAdapter beerListAdapter;
    private int beerID = 0;
    private int beerColor = Color.rgb(128, 128, 128);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_beer);

        tTitle = findViewById(R.id.t_addedit_beer);
        btn_beerDone = findViewById(R.id.btn_beer_done);
        btn_uaryofa = findViewById(R.id.btn_beer_uaryofa);
        TextInputLayout tiBeerName = findViewById(R.id.e_beer_name);
        TextInputLayout tiBeerPr = findViewById(R.id.e_beer_pr);
        eBeerName = tiBeerName.getEditText();
        eBeerPr = tiBeerPr.getEditText();
        ListView listViewBeer = findViewById(R.id.listview_beer);
        btnColor = findViewById(R.id.btn_color);

        beerListAdapter = new BeerListAdapter(this, Constantebi.ludiList);
        listViewBeer.setAdapter(beerListAdapter);

        btn_uaryofa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beerID = 0;
                eBeerName.setText("");
                eBeerPr.setText("");
                btn_beerDone.setText("+");
                tTitle.setText("ახალი ლუდის დამატება");
                btn_uaryofa.setVisibility(View.INVISIBLE);
            }
        });

        btn_beerDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToDB(beerID, eBeerName.getText().toString(), eBeerPr.getText().toString());
            }
        });

        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseColorDialog colorDialog = new ChooseColorDialog();
                colorDialog.show(getSupportFragmentManager(), String.valueOf(beerColor));
            }
        });

        myColor(beerColor);

        this.registerForContextMenu(listViewBeer);
    }

    private void sendDataToDB(final int beerID, final String dasaxeleba, final String price) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Constantebi.URL_INS_BEER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response + " +", Toast.LENGTH_SHORT).show();
                if (response.equals("ჩაწერილია!") || response.equals("განახლებულია!")) {
                    GlobalServise globalServise = new GlobalServise(getApplicationContext());
                    globalServise.get_Prises();
                    globalServise.get_BeerList();
                    onBackPressed();
                }
                btn_beerDone.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                btn_beerDone.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("dasaxeleba", dasaxeleba);
                params.put("price", price);
                params.put("beerId", String.valueOf(beerID));
                params.put("color", String.format("#%02X%02X%02X", Color.red(beerColor), Color.green(beerColor), Color.blue(beerColor)));

                return params;
            }
        };

        queue.add(request);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        this.getMenuInflater().inflate(R.menu.context_menu_order, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        BeerModel beer = (BeerModel) beerListAdapter.getItem(info.position);
        switch (item.getItemId()) {

            case R.id.cm_order_edit:
                eBeerName.setText(beer.getDasaxeleba());
                eBeerPr.setText(String.valueOf(beer.getFasi()));
                beerID = beer.getId();
                btn_beerDone.setText("რედაქტირება");
                tTitle.setText("რედაქტირება");
                btn_uaryofa.setVisibility(View.VISIBLE);
                beerColor = Color.parseColor(beer.getDisplayColor());
                myColor(beerColor);
                break;
            case R.id.cm_order_del:
                removeBeer(beer.getId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void removeBeer(final int beerId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request_DelBeer = new StringRequest(Request.Method.POST, Constantebi.URL_DEL_BEER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response + " +", Toast.LENGTH_SHORT).show();
                if (response.equals("Removed!")) {

                    for (int i = 0; i < Constantebi.ludiList.size(); i++) {
                        if (Constantebi.ludiList.get(i).getId() == beerId) {
                            Constantebi.ludiList.remove(i);
                        }
                    }
                    beerListAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString() + " -", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("beerId", String.valueOf(beerId));
                return params;
            }
        };

        queue.add(request_DelBeer);
    }

    @Override
    public void myColor(int color) {
        beerColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnColor.setBackgroundTintList(ColorStateList.valueOf(color));
        } else {
            btnColor.setBackgroundColor(color);
        }
    }
}
