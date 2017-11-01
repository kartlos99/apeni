package com.example.kdiakonidze.beerapeni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kdiakonidze.beerapeni.adapters.ObjListAdapter;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

public class ObjListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj_list);

        ObjListAdapter objListAdapter = new ObjListAdapter(this, Constantebi.OBIEQTEBI);

        ListView objlistView = (ListView) findViewById(R.id.objListView);
        objlistView.setAdapter(objListAdapter);

        objlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AddOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("obieqti", Constantebi.OBIEQTEBI.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
