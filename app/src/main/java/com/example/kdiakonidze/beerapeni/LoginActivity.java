package com.example.kdiakonidze.beerapeni;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText e_username, e_password;
    CheckBox chk_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btn_login = (Button) findViewById(R.id.btn_login);
        e_username = (EditText) findViewById(R.id.e_login_username);
        e_password = (EditText) findViewById(R.id.e_input_password);
        chk_remember = (CheckBox) findViewById(R.id.chk_damimaxsovre);

        File file = new File(getFilesDir(),Constantebi.USER_FILENAME);
        if(file.exists()){
            readFileAsString(Constantebi.USER_FILENAME);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_login.setEnabled(false);
                avtorizacia();
//                try {
//                    rememberUser("me", "123");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Constantebi.loged_in = true;
//                onBackPressed();
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
                btn_login.setEnabled(true);
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
                        Constantebi.USER_PASS = password;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Constantebi.loged_in = true;
                    if (chk_remember.isChecked()) {
                        try {
                            rememberUser(username, password);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    onBackPressed();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_login.setEnabled(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Toast.makeText(getApplicationContext(), error.toString()+" შეცდომა login -ze" , Toast.LENGTH_SHORT).show();
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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request);
    }

    private void rememberUser(String username, String password) throws IOException {
//        String fileName = "userdata";
        File userFile = new File(getFilesDir(), Constantebi.USER_FILENAME);
        FileWriter fileWriter = new FileWriter(userFile);
        fileWriter.write(username);
        fileWriter.write("\n");
        fileWriter.write(password);
        fileWriter.close();
    }

    public void readFileAsString(String fileName) {

//        StringBuilder stringBuilder = new StringBuilder();
//        String line;
        BufferedReader reader = null;
        String username = "";
        String pass = "";

        try {
            reader = new BufferedReader(new FileReader(new File(getFilesDir(), fileName)));
            username = reader.readLine();
            pass = reader.readLine();
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line).append("\n");
//                Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
//            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

        e_username.setText(username);
        e_password.setText(pass);
        btn_login.setEnabled(false);
        chack_user(username, pass);
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
