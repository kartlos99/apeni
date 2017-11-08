package com.example.kdiakonidze.beerapeni;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

public class AddDeliveryActivity extends AppCompatActivity implements View.OnClickListener{

    TextView t_OrderInfo, t_beerType;
    EditText eK30Count, eK50Count, eK30Count_Kout, eK50Count_Kout, eTakeMoney;
    Obieqti currObieqti;
    Integer beertype = 0;
    Button btn_Done, btnBeerLeft, btnBeerRight, btnK30dec, btnK30inc, btnK50dec, btnK50inc, btnK30dec_Kout, btnK30inc_Kout, btnK50dec_Kout, btnK50inc_Kout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);

        Intent i = getIntent();
        Bundle importedBundle = i.getExtras();
        currObieqti = (Obieqti) importedBundle.getSerializable("obieqti");

        btnBeerLeft = (Button) findViewById(R.id.btn_beerleft);
        btnBeerRight = (Button) findViewById(R.id.btn_beerright);

        btnK30dec = (Button) findViewById(R.id.btn_k30_dec);
        btnK30inc = (Button) findViewById(R.id.btn_k30_inc);
        btnK50dec = (Button) findViewById(R.id.btn_k50_dec);
        btnK50inc = (Button) findViewById(R.id.btn_k50_inc);
        btnK30dec_Kout = (Button) findViewById(R.id.btn_k30_dec_KasriOut);
        btnK30inc_Kout = (Button) findViewById(R.id.btn_k30_inc_KasriOut);
        btnK50dec_Kout = (Button) findViewById(R.id.btn_k50_dec_KasriOut);
        btnK50inc_Kout = (Button) findViewById(R.id.btn_k50_inc_KasriOut);

        eK30Count = (EditText) findViewById(R.id.edit_K30Count);
        eK50Count = (EditText) findViewById(R.id.edit_K50Count);
        eK30Count_Kout = (EditText) findViewById(R.id.edit_K30Count_KasriOut);
        eK50Count_Kout = (EditText) findViewById(R.id.edit_K50Count_KasriOut);
        eTakeMoney = (EditText) findViewById(R.id.e_TakeMoney);

        btnK30dec.setOnClickListener(this);
        btnK30inc.setOnClickListener(this);
        btnK50dec.setOnClickListener(this);
        btnK50inc.setOnClickListener(this);
        btnK30dec_Kout.setOnClickListener(this);
        btnK30inc_Kout.setOnClickListener(this);
        btnK50dec_Kout.setOnClickListener(this);
        btnK50inc_Kout.setOnClickListener(this);

        t_beerType = (TextView) findViewById(R.id.t_ludisDasaxeleba);
        t_beerType.setText(Constantebi.ludiList.get(beertype));

        btnBeerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beertype--;
                if (beertype < 0) {
                    beertype = beertype + Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beertype));
            }
        });

        btnBeerRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beertype++;
                if (beertype == Constantebi.ludiList.size()) {
                    beertype = beertype - Constantebi.ludiList.size();
                }
                t_beerType.setText(Constantebi.ludiList.get(beertype));
            }
        });


    }

    private String pliusMinusText(String stringNaomber, boolean oper) {
        // oper   (true = +) (false = -)
        if (stringNaomber.equals("")){
            stringNaomber="0";
        }
        int ii = Integer.valueOf(stringNaomber);

        if(oper){
            ii++;
        }else{
            if (ii > 0){
                ii--;
            }
        }
        return String.valueOf(ii);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_k30_dec:
                eK30Count.setText(pliusMinusText(eK30Count.getText().toString(),false));
                break;
            case R.id.btn_k30_inc:
                eK30Count.setText(pliusMinusText(eK30Count.getText().toString(),true));
                break;
            case R.id.btn_k50_dec:
                eK50Count.setText(pliusMinusText(eK50Count.getText().toString(),false));
                break;
            case R.id.btn_k50_inc:
                eK50Count.setText(pliusMinusText(eK50Count.getText().toString(),true));
                break;
            case R.id.btn_k30_dec_KasriOut:
                eK30Count_Kout.setText(pliusMinusText(eK30Count_Kout.getText().toString(),false));
                break;
            case R.id.btn_k30_inc_KasriOut:
                eK30Count_Kout.setText(pliusMinusText(eK30Count_Kout.getText().toString(),true));
                break;
            case R.id.btn_k50_dec_KasriOut:
                eK50Count_Kout.setText(pliusMinusText(eK50Count_Kout.getText().toString(),false));
                break;
            case R.id.btn_k50_inc_KasriOut:
                eK50Count_Kout.setText(pliusMinusText(eK50Count_Kout.getText().toString(),true));
                break;
        }
    }
}
