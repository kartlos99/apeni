package com.example.kdiakonidze.beerapeni;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.services.NotificationService;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.PrivateKey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

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

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText e_username, e_password;
    CheckBox chk_remember;

    public static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btn_login = findViewById(R.id.btn_login);
        e_username = findViewById(R.id.e_login_username);
        e_password = findViewById(R.id.e_input_password);
        chk_remember = findViewById(R.id.chk_damimaxsovre);

        File file = new File(getFilesDir(), Constantebi.USER_FILENAME);
        if (file.exists()) {
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
        mAuth = FirebaseAuth.getInstance();
    }

    private void avtorizacia() {
        String user = e_username.getText().toString();
        String pass = e_password.getText().toString();

        chack_user(user, pass);
    }

    private void chack_user(final String username, final String password) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constantebi.URL_LOGIN;
        Log.d(TAG, "chack_user");
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("uaryofa")) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    btn_login.setEnabled(true);
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

                    loginToFirebase(username);

                    if (chk_remember.isChecked()) {
                        try {
                            rememberUser(username, password);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "chack_user:Eror");
                btn_login.setEnabled(true);
                error.printStackTrace();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Toast.makeText(getApplicationContext(), error.toString() + " შეცდომა login -ზე", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        queue.add(request);
    }

    private void registerInFirebase(final String username) {

        mAuth.createUserWithEmailAndPassword(username, PrivateKey.FIREBASE_PASS)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
                            onLoginSucsses();
                        } else {
                            Log.d(TAG, username);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void loginToFirebase(String name) {

        final String username = name + "@apeni.ge";

        mAuth.signInWithEmailAndPassword(username, PrivateKey.FIREBASE_PASS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        registerInFirebase(username);
                        Log.d(TAG, "exp_MEssage: " + task.getException().getMessage());
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "SignIN:success");
                    onLoginSucsses();
                }
            }
        });
    }

    private void onLoginSucsses(){
        Constantebi.loged_in = true;
        btn_login.setEnabled(true);
        startService(new Intent(getBaseContext(), NotificationService.class));
        onBackPressed();
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
        BufferedReader reader;
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
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.msg_noUserFile, Toast.LENGTH_SHORT).show();
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
