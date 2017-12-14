package com.example.kdiakonidze.beerapeni;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.adapters.ObjListAdapter;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import java.util.HashMap;
import java.util.Map;

public class ObjListActivity extends AppCompatActivity {

    Integer mdebareoba = 0;
    ObjListAdapter objListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj_list);

        Intent passed_i = getIntent();
        mdebareoba = passed_i.getIntExtra("mdebareoba", 0);

        for (int i = 0; i < Constantebi.OBIEQTEBI.size(); i++) {
            int objid = Constantebi.OBIEQTEBI.get(i).getId();

            for (int j = 0; j < Constantebi.FASEBI.size(); j++) {
                if (objid == Constantebi.FASEBI.get(j).getObj_id()) {
                    Constantebi.OBIEQTEBI.get(i).setFasebi(Constantebi.FASEBI.get(j).getFasebi());
                }
            }
        }

        objListAdapter = new ObjListAdapter(this, Constantebi.OBIEQTEBI);

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
                if (mdebareoba.equals(Constantebi.MDEBAREOBA_MITANA)) {
                    Intent intent = new Intent(getApplicationContext(), AddDeliveryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("obieqti", Constantebi.OBIEQTEBI.get(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                if (mdebareoba.equals(Constantebi.MDEBAREOBA_AMONAWERI)) {
                    Intent intent = new Intent(getApplicationContext(), AmonaweriActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("obieqti", Constantebi.OBIEQTEBI.get(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        objlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        this.registerForContextMenu(objlistView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        this.getMenuInflater().inflate(R.menu.context_menu_obj, menu);

        Obieqti obieqti = (Obieqti) objListAdapter.getItem(info.position);
        menu.setHeaderTitle(obieqti.getDasaxeleba());

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Obieqti obieqti = (Obieqti) objListAdapter.getItem(info.position);

        int menuId = item.getItemId();
        switch (menuId){
            case R.id.cm_call:
                Toast.makeText(this, obieqti.getTel(), Toast.LENGTH_LONG).show();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + obieqti.getTel()));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return true;
                }
                startActivity(callIntent);
                break;
            case R.id.cm_info:
                Toast.makeText(this, obieqti.toString(), Toast.LENGTH_LONG).show();
                break;
            case R.id.cm_edit_obj:
                Intent intent_editObj = new Intent(getApplicationContext(), AddEditObject.class);
                intent_editObj.putExtra(Constantebi.REASON, Constantebi.EDIT);
                intent_editObj.putExtra("obj", obieqti);
                startActivity(intent_editObj);
                break;
            case R.id.cm_del:
                removeObjFromList(obieqti.getId());
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void removeObjFromList(final Integer id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request_DelObj = new StringRequest(Request.Method.POST, Constantebi.URL_DEL_OBJ, new Response.Listener<String>() {
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

                params.put("obj_id", String.valueOf(id));

                params.toString();
                return params;
            }
        };

        queue.add(request_DelObj);
    }
}
