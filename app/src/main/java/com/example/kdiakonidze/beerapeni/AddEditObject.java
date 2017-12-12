package com.example.kdiakonidze.beerapeni;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import java.util.HashMap;
import java.util.Map;

public class AddEditObject extends AppCompatActivity {
    TextInputLayout e_name, e_adress, e_tel, e_comment;
    String fasebi = "", id_ebi;
    LinearLayout linearLayout_fasebi;
    private Obieqti editedObieqti;
    private String reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_object);

        e_name = (TextInputLayout) findViewById(R.id.e_addobj_name);
        e_adress = (TextInputLayout) findViewById(R.id.e_addobj_adress);
        e_tel = (TextInputLayout) findViewById(R.id.e_addobj_tel);
        e_comment = (TextInputLayout) findViewById(R.id.e_addobj_comment);
        linearLayout_fasebi = (LinearLayout) findViewById(R.id.linear_addobj_prices);
        Button btn_done = (Button) findViewById(R.id.btn_addobj_done);

        reason = getIntent().getStringExtra(Constantebi.REASON);
        if (reason.equals(Constantebi.EDIT)) {
            editedObieqti = (Obieqti) getIntent().getSerializableExtra("obj");
            if (editedObieqti != null) {
                e_name.getEditText().setText(editedObieqti.getDasaxeleba());
                e_adress.getEditText().setText(editedObieqti.getAdress());
                e_tel.getEditText().setText(editedObieqti.getTel());
                e_comment.getEditText().setText(editedObieqti.getComment());
            }
        }


        for (int i = 0; i < Constantebi.ludiList.size(); i++) {
            LinearLayout h_Layout = new LinearLayout(this);
            h_Layout.setOrientation(LinearLayout.HORIZONTAL);
            h_Layout.setGravity(Gravity.RIGHT);
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
                editText.setText(editedObieqti.getFasebi().get(i).toString());
            }
            h_Layout.addView(textView, 0);
            h_Layout.addView(editText, 1);
            linearLayout_fasebi.addView(h_Layout, i);
        }

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fasebi = "";
                id_ebi = "";

                for (int i = 0; i < linearLayout_fasebi.getChildCount(); i++) {
                    View element = linearLayout_fasebi.getChildAt(i);
                    LinearLayout h_row = (LinearLayout) element;
                    View element2 = h_row.getChildAt(1);
                    if (element2 instanceof EditText) {
                        EditText editText = (EditText) element2;
                        if (editText.getText().toString().equals("")) {
                            fasebi += editText.getHint().toString();
                        } else {
                            fasebi += editText.getText().toString();
                        }
                        id_ebi += Constantebi.ludiList.get(i).getId();
                        if (i < linearLayout_fasebi.getChildCount() - 1) {
                            fasebi += "|";
                            id_ebi += "|";
                        }
                    }
                }

                Toast.makeText(getApplicationContext(), fasebi, Toast.LENGTH_SHORT).show();
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (moqmedeba.equals(Constantebi.EDIT)) {
                    params.put("obj_id", editedObieqti.getId().toString());
                }
                params.put("moqmedeba", moqmedeba);

                params.put("name", e_name.getEditText().getText().toString());
                params.put("adress", e_adress.getEditText().getText().toString());
                params.put("tel", e_tel.getEditText().getText().toString());
                params.put("comment", e_comment.getEditText().getText().toString());
                params.put("fasebi", fasebi);
                params.put("id_ebi", id_ebi);

                params.toString();
                return params;
            }
        };

        queue.add(request);
    }
}
