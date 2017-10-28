package com.example.kdiakonidze.beerapeni;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class OrdersActivity extends AppCompatActivity {

    Button btn_setDate;



    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            btn_setDate.setText(i+"/"+i1+"/" + i2);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);




        btn_setDate = (Button) findViewById(R.id.btn_setdate);
        btn_setDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                btn_setDate.setText(c.get(Calendar.YEAR)+"/"+ c.get(Calendar.MONTH)+"/"+ c.get(Calendar.DAY_OF_MONTH));

//                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(), dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) );
//                datePickerDialog.setCancelable(false);
//                datePickerDialog.show();
            }
        });
    }
}
