package com.example.kdiakonidze.beerapeni;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import java.util.HashMap;
import java.util.Map;

public class AddEditObject extends AppCompatActivity {
    TextInputLayout e_name;
    LinearLayout linearLayout_fasebi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_object);

        e_name = (TextInputLayout) findViewById(R.id.e_addobj_name);
        linearLayout_fasebi = (LinearLayout) findViewById(R.id.linear_addobj_prices);
        Button btn_done = (Button) findViewById(R.id.btn_addobj_done);

        for (int i = 0; i < Constantebi.ludiList.size(); i++) {
            TextInputLayout editText = new TextInputLayout(this);
            editText.setId(i);
//            editText.setText("haimee_"+i);
            editText.setHint(Constantebi.ludiList.get(i).toString());
            linearLayout_fasebi.addView(editText,i);
        }

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

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

                        params.put("obieqtis_id", String.valueOf(currObieqti.getId()));
                        params.put("k30", eK30Count.getText().toString());
                        params.put("k50", eK50Count.getText().toString());
                        params.put("beer_type", String.valueOf(beertype + 1));
                        params.put("comment", "comentaris grafaa dasamatebeli");

                        params.toString();
                        return params;
                    }
                };

                queue.add(request);*/
            }
        });

        for (int i = 0; i < linearLayout_fasebi.getChildCount(); i++) {
            View element = linearLayout_fasebi.getChildAt(i);
            if(element instanceof EditText){
                EditText editText = (EditText) element;
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }

        }

    }
}
