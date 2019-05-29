package com.example.kdiakonidze.beerapeni;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.GlobalServise;

import java.util.HashMap;
import java.util.Map;

public class AddEditObject extends AppCompatActivity {
    EditText e_name, e_adress, e_tel, e_comment, e_sk, e_sakpiri;
    String fasebi = "", id_ebi;
    LinearLayout linearLayout_fasebi;
    private Obieqti editedObieqti;
    private String reason;
    private Button btn_done;
    private CheckBox obj_chek;

    private RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_object);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        e_name = ((TextInputLayout) findViewById(R.id.e_addobj_name)).getEditText();
        e_adress = ((TextInputLayout) findViewById(R.id.e_addobj_adress)).getEditText();
        e_tel = ((TextInputLayout) findViewById(R.id.e_addobj_tel)).getEditText();
        e_comment = ((TextInputLayout) findViewById(R.id.e_addobj_comment)).getEditText();
        e_sk = ((TextInputLayout) findViewById(R.id.e_addobj_sk)).getEditText();
        e_sakpiri = ((TextInputLayout) findViewById(R.id.e_addobj_sakpiri)).getEditText();
        linearLayout_fasebi = findViewById(R.id.linear_addobj_prices);
        btn_done = findViewById(R.id.btn_addobj_done);
        obj_chek = findViewById(R.id.chk_obj_chek);

        reason = getIntent().getStringExtra(Constantebi.REASON);
        if (reason.equals(Constantebi.EDIT)) {
            editedObieqti = (Obieqti) getIntent().getSerializableExtra("obj");
            if (editedObieqti != null) {
                e_name.setText(editedObieqti.getDasaxeleba());
                e_adress.setText(editedObieqti.getAdress());
                e_tel.setText(editedObieqti.getTel());
                e_comment.setText(editedObieqti.getComment());
                e_sk.setText(editedObieqti.getSk());
                e_sakpiri.setText(editedObieqti.getSakpiri());
                if (editedObieqti.getChek().equals("1")){
                    obj_chek.setChecked(true);
                }else {
                    obj_chek.setChecked(false);
                }
            }
        }
        toolbar.setTitle(reason);
        setSupportActionBar(toolbar);


        for (int i = 0; i < Constantebi.ludiList.size(); i++) {
            LinearLayout h_Layout = new LinearLayout(this);
            h_Layout.setOrientation(LinearLayout.HORIZONTAL);
            h_Layout.setGravity(Gravity.END);
            EditText editText = new EditText(this);
            editText.setId(i);
            editText.setGravity(Gravity.CENTER);
            editText.setWidth(350);
            editText.setHint(Constantebi.ludiList.get(i).getFasi().toString());
            editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            TextView textView = new TextView(this);
            textView.setId(100 + i);
            textView.setText(Constantebi.ludiList.get(i).getDasaxeleba());

            if (reason.equals(Constantebi.EDIT)) {
                editText.setText(String.format("%s", editedObieqti.getFasebi().get(i)));
            }
            h_Layout.addView(textView, 0);
            h_Layout.addView(editText, 1);
            linearLayout_fasebi.addView(h_Layout, i);
        }

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sbPR = new StringBuilder();
                StringBuilder sbID = new StringBuilder();

                for (int i = 0; i < linearLayout_fasebi.getChildCount(); i++) {
                    View element = linearLayout_fasebi.getChildAt(i);
                    LinearLayout h_row = (LinearLayout) element;
                    View element2 = h_row.getChildAt(1);
                    if (element2 instanceof EditText) {
                        EditText editText = (EditText) element2;
                        if (editText.getText().toString().equals("")) {
                            sbPR.append(editText.getHint());
                        } else {
                            sbPR.append(editText.getText());
                        }
                        sbID.append(Constantebi.ludiList.get(i).getId());
                        if (i < linearLayout_fasebi.getChildCount() - 1) {
                            sbPR.append("|");
                            sbID.append("|");
                        }
                    }
                }
                fasebi = sbPR.toString();
                id_ebi = sbID.toString();

                btn_done.setEnabled(false);
                sendToDB(reason);
            }
        });


    }

    private void sendToDB(final String moqmedeba) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constantebi.URL_INS_AXALI_OBIEQTI;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                if (response.equals("ჩაწერილია!") || response.equals("განახლებულია!")){
                    updateLocalInfo();
                    onBackPressed();
                }
                btn_done.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                btn_done.setEnabled(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (moqmedeba.equals(Constantebi.EDIT)) {
                    params.put("obj_id", editedObieqti.getId().toString());
                }
                params.put("moqmedeba", moqmedeba);
                params.put("name", e_name.getText().toString());
                params.put("adress", e_adress.getText().toString());
                params.put("tel", e_tel.getText().toString());
                params.put("comment", e_comment.getText().toString());
                params.put("sk", e_sk.getText().toString());
                params.put("sakpiri", e_sakpiri.getText().toString());
                params.put("fasebi", fasebi);
                params.put("id_ebi", id_ebi);
                params.put("chek", (obj_chek.isChecked() ? "1" : "0"));
                return params;
            }
        };

        request.setRetryPolicy(mRetryPolicy);
        queue.add(request);
    }

    private void updateLocalInfo() {

        MainActivity.requestCount = 2; // 2 requests vamateb da 4 mde rom ava fasebs miabams obieqtebs!!!
        GlobalServise globalServise = new GlobalServise(AddEditObject.this);
        globalServise.get_Obieqts();
        globalServise.get_Prises();
    }
}