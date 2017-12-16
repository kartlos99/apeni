package com.example.kdiakonidze.beerapeni;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.models.Useri;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import java.util.HashMap;
import java.util.Map;

public class AddEditUser extends AppCompatActivity {

    private String reason;
    private Toolbar toolbar;
    private Button btn_done;
    private TextInputLayout e_username, e_name, e_pass, e_adress, e_tel, e_comment;
    private EditText e_pass_conf;
    private CheckBox ch_Box_admin, ch_Box_passchange;

    private Useri editedUser;
    private int passCH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        toolbar = (Toolbar) findViewById(R.id.tool_bar_userpage);
        e_username = (TextInputLayout) findViewById(R.id.e_user_username);
        e_name = (TextInputLayout) findViewById(R.id.e_user_name);
        e_adress = (TextInputLayout) findViewById(R.id.e_user_adress);
        e_tel = (TextInputLayout) findViewById(R.id.e_user_tel);
        e_comment = (TextInputLayout) findViewById(R.id.e_user_comment);
        e_pass = (TextInputLayout) findViewById(R.id.e_user_pass);
        e_pass_conf = (EditText) findViewById(R.id.e_user_pass_conf);

        btn_done = (Button) findViewById(R.id.btn_user_reg);
        ch_Box_admin = (CheckBox) findViewById(R.id.checkBox_user_admin);
        ch_Box_passchange = (CheckBox) findViewById(R.id.checkbox_user_chpass);

        reason = getIntent().getStringExtra(Constantebi.REASON);

        if (reason.equals(Constantebi.EDIT)) {
            editedUser = (Useri) getIntent().getSerializableExtra("user");
            if (editedUser != null) {
                e_username.getEditText().setText(editedUser.getUsername());
                e_name.getEditText().setText(editedUser.getName());
                e_pass.getEditText().setText(editedUser.getPass());
                e_pass_conf.setText(editedUser.getPass());
                e_adress.getEditText().setText(editedUser.getAdress());
                e_tel.getEditText().setText(editedUser.getTel());
                e_comment.getEditText().setText(editedUser.getComment());
                if (editedUser.getType() == 2) {
                    ch_Box_admin.setChecked(true);
                } else {
                    ch_Box_admin.setChecked(false);
                }
            }
            ch_Box_passchange.setVisibility(View.VISIBLE);
            e_pass.setEnabled(false);
            e_pass_conf.setEnabled(false);
        }

        ch_Box_passchange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    e_pass.setEnabled(true);
                    e_pass_conf.setEnabled(true);
                    passCH = 1;
                } else {
                    e_pass.setEnabled(false);
                    e_pass_conf.setEnabled(false);
                    passCH = 0;
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkCondition()) {
                    btn_done.setEnabled(false);
                    if (ch_Box_admin.isChecked()) {
                        sendToDB(reason, "2");
                    } else {
                        sendToDB(reason, "1");
                    }
                }
            }
        });

        toolbar.setTitle(reason);
        setSupportActionBar(toolbar);
    }

    private Boolean checkCondition() {
        if (e_username.getEditText().getText().length() < 3) {
            e_username.setError("min 3 symbol");
            return false;
        }else {
            e_username.setError("");
        }

        if (((e_pass.getEditText().getText().length() < 3) && reason.equals(Constantebi.CREATE_USER)) ||
                ((e_pass.getEditText().getText().length() < 3) && reason.equals(Constantebi.EDIT) && (passCH == 1))) {

            e_pass.setError("min 3 symbol");
            return false;
        } else {
            e_pass.setError("");
        }

        if ((e_pass.getEditText().getText().toString().equals(e_pass_conf.getText().toString()) && reason.equals(Constantebi.CREATE_USER)) ||
                (e_pass.getEditText().getText().toString().equals(e_pass_conf.getText().toString()) && reason.equals(Constantebi.EDIT) && (passCH == 1))) {
            e_pass.setError("not confirmed!");
            return false;
        }else {
            e_pass.setError("");
        }

        return true;
    }

    private void sendToDB(final String moqmedeba, final String userType) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constantebi.URL_INS_AXALI_USERI;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                if (response.equals("ჩაწერილია!") || response.equals("განახლებულია!")){
                    onBackPressed();
                }
//                btn_done.setEnabled(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                btn_done.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (moqmedeba.equals(Constantebi.EDIT)) {
                    params.put("obj_id", String.valueOf(editedUser.getId()));
                    params.put("passCH", String.valueOf(passCH));
                }
                params.put("moqmedeba", moqmedeba);

                params.put("username", e_username.getEditText().getText().toString());
                params.put("name", e_name.getEditText().getText().toString());
                params.put("adress", e_adress.getEditText().getText().toString());
                params.put("tel", e_tel.getEditText().getText().toString());
                params.put("comment", e_comment.getEditText().getText().toString());
                params.put("pass", e_pass.getEditText().getText().toString());
                params.put("type", userType);
                params.put("maker", Constantebi.USER_ID);

                params.toString();
                return params;
            }
        };

        queue.add(request);
    }
}