package com.example.kdiakonidze.beerapeni;

import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText e_username, e_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        e_username = (EditText) findViewById(R.id.e_login_username);
        e_password = (EditText) findViewById(R.id.e_input_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avtorizacia();


//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void avtorizacia() {
        String user = e_username.getText().toString();
        String pass = e_password.getText().toString();

        chack_user(user, pass);
    }

    private void chack_user(final String username, final String password) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constantebi.URL_LOGIN;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String name = "";

                if (response.equals("uaryofa")) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject json = (new JSONArray(response).getJSONObject(0));
                        Constantebi.USER_ID = json.getString("id");
                        Constantebi.USER_USERNAME = json.getString("username");
                        Constantebi.USER_NAME = json.getString("name");
                        Constantebi.USER_TYPE = json.getString("type");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Constantebi.loged_in = true;
                    onBackPressed();
                }

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

                params.put("username", username);
                params.put("password", password);

                params.toString();
                return params;
            }
        };

        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        if (Constantebi.loged_in) {
            super.onBackPressed();
        } else {
            moveTaskToBack(true);
        }
    }
}
