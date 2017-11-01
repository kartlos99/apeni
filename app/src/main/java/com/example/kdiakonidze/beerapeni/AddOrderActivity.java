package com.example.kdiakonidze.beerapeni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.models.Obieqti;

public class AddOrderActivity extends AppCompatActivity {

    TextView t_OrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        Intent i = getIntent();
        Bundle importedBundle = i.getExtras();

        Obieqti currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");


        t_OrderInfo = (TextView) findViewById(R.id.t_orderInfo);
        t_OrderInfo.setText(currObieqti.getDasaxeleba());
    }
}
