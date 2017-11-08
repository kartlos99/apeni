package com.example.kdiakonidze.beerapeni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kdiakonidze.beerapeni.adapters.ObjListAdapter;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

public class ObjListActivity extends AppCompatActivity {

    Integer mdebareoba = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj_list);

        Intent i = getIntent();
        mdebareoba = i.getIntExtra("mdebareoba", 0);

        ObjListAdapter objListAdapter = new ObjListAdapter(this, Constantebi.OBIEQTEBI);

        ListView objlistView = (ListView) findViewById(R.id.objListView);
        objlistView.setAdapter(objListAdapter);

        objlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mdebareoba.equals(Constantebi.MDEBAREOBA_SHEKVETA)) {
                    Intent intent = new Intent(getApplicationContext(), AddOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("obieqti", Constantebi.OBIEQTEBI.get(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                if(mdebareoba.equals(Constantebi.MDEBAREOBA_MITANA)){
                    Intent intent = new Intent(getApplicationContext(), AddDeliveryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("obieqti", Constantebi.OBIEQTEBI.get(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
}
