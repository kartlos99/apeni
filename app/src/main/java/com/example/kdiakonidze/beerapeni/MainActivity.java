package com.example.kdiakonidze.beerapeni;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_shekvetebi, btn_mitana, btn_dayRealiz, btn_objRealiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_shekvetebi = (Button) findViewById(R.id.btn_shekvetebi);
        btn_mitana = (Button) findViewById(R.id.btn_mitana);
        btn_dayRealiz = (Button) findViewById(R.id.btn_realiz_dge);
        btn_objRealiz = (Button) findViewById(R.id.btn_realiz_obj);

        btn_shekvetebi.setOnClickListener(MainActivity.this);
        btn_mitana.setOnClickListener(MainActivity.this);
        btn_dayRealiz.setOnClickListener(MainActivity.this);
        btn_objRealiz.setOnClickListener(MainActivity.this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_shekvetebi:
                Intent addOrderPage = new Intent();
                addOrderPage.setClass(getApplicationContext(), OrdersActivity.class);
                startActivity(addOrderPage);
                break;
            case R.id.btn_mitana:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_realiz_dge:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_realiz_obj:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "uups", Toast.LENGTH_SHORT).show();
        }
    }
}
