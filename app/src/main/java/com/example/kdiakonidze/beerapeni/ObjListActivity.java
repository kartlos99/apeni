package com.example.kdiakonidze.beerapeni;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.GlobalServise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjListActivity extends AppCompatActivity {

    Integer mdebareoba = 0;
    ObjListAdapter objListAdapter;
    ListView objlistView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj_list);

        final Intent passed_i = getIntent();
        mdebareoba = passed_i.getIntExtra("mdebareoba", 0);

        searchView = (SearchView) findViewById(R.id.searchV_objlist);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query + "!!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                objListAdapter.filter(newText);
                return false;
            }
        });

        objListAdapter = new ObjListAdapter(this, Constantebi.OBIEQTEBI);

        objlistView = (ListView) findViewById(R.id.objListView);
        objlistView.setAdapter(objListAdapter);

        objlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mdebareoba.equals(Constantebi.MDEBAREOBA_SHEKVETA)) {

                    Bundle bundleO = passed_i.getExtras().getBundle("objINorder");
                    ArrayList<Shekvetebi> shekvetebiArrayList = (ArrayList<Shekvetebi>) bundleO.getSerializable("data");


                        Intent intent = new Intent(getApplicationContext(), AddOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("obieqti", (Serializable) objListAdapter.getItem(i));
                        bundle.putSerializable("orderArray", shekvetebiArrayList);
                        intent.putExtra(Constantebi.REASON, Constantebi.NEW_ORDER);
                        intent.putExtras(bundle);
                        startActivity(intent);


                }
                if (mdebareoba.equals(Constantebi.MDEBAREOBA_MITANA)) {
                    Intent intent = new Intent(getApplicationContext(), AddDeliveryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("obieqti", (Serializable) objListAdapter.getItem(i));
                    intent.putExtra(Constantebi.REASON, Constantebi.CREATE);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                if (mdebareoba.equals(Constantebi.MDEBAREOBA_AMONAWERI)) {
                    Intent intent = new Intent(getApplicationContext(), AmonaweriActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("obieqti", (Serializable) objListAdapter.getItem(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
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
        switch (menuId) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View infoView = getLayoutInflater().inflate(R.layout.obj_info_dialog, null);
                TextView tDasaxeleba = (TextView) infoView.findViewById(R.id.t_d_dasaxeleba);
                TextView tSakPiri = (TextView) infoView.findViewById(R.id.t_d_sakpiri);
                TextView tComment = (TextView) infoView.findViewById(R.id.t_d_comment);

                tDasaxeleba.setText("ობიექტი: "
                        + obieqti.getDasaxeleba() + "\n    "
                        + obieqti.getSk() + "\n    "
                        + obieqti.getAdress() );
                tSakPiri.setText("საკ.პირი: "
                        + obieqti.getSakpiri() + "\n    "
                        + obieqti.getTel()
                );
                tComment.setText(obieqti.getComment());

                builder.setView(infoView)
                        .setTitle("ინფორმაცია")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
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
                if (response.equals("Removed!")) {
                    GlobalServise globalServise = new GlobalServise(getApplicationContext());
                    globalServise.get_Obieqts();
                }
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
