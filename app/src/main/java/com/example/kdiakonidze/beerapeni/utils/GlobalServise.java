package com.example.kdiakonidze.beerapeni.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.beerapeni.MainActivity;
import com.example.kdiakonidze.beerapeni.models.BeerModel;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.OrderCommentRowModel;
import com.example.kdiakonidze.beerapeni.models.PeerObjPrice;
import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.Useri;
import com.example.kdiakonidze.beerapeni.models.Xarji;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by k.diakonidze on 12/21/2017.
 */

public class GlobalServise {
    private Context context;
    private RequestQueue queue;

    public GlobalServise(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void get_Obieqts() {

        JsonArrayRequest requestObieqtebi = new JsonArrayRequest(Constantebi.URL_GET_OBIEQTS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // aq modis obieqtebis chamonatvali
//                Toast.makeText(context, "obiectebi ok", Toast.LENGTH_LONG).show();
                Constantebi.OBIEQTEBI.clear();
                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Obieqti axaliObieqti = new Obieqti(response.getJSONObject(i).getString("dasaxeleba"));
                            axaliObieqti.setId(response.getJSONObject(i).getInt("id"));
                            axaliObieqti.setAdress(response.getJSONObject(i).getString("adress"));
                            axaliObieqti.setTel(response.getJSONObject(i).getString("tel"));
                            axaliObieqti.setComment(response.getJSONObject(i).getString("comment"));
                            axaliObieqti.setSk(response.getJSONObject(i).getString("sk"));
                            axaliObieqti.setSakpiri(response.getJSONObject(i).getString("sakpiri"));
                            axaliObieqti.setChek(response.getJSONObject(i).getString("chek"));

                            Constantebi.OBIEQTEBI.add(axaliObieqti);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                }
//                progressDialog.dismiss();
                MainActivity.requestCount++;
                if (MainActivity.requestCount == 4) {
                    activityOrientationNormal();
                    attachPricesToObj();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.requestCount++;
                if (MainActivity.requestCount == 4) {
                    activityOrientationNormal();
                    attachPricesToObj();
                }
                Toast.makeText(context, error.toString() + " | eror no obieqtebi -", Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
            }
        });

        queue.add(requestObieqtebi);
    }

    public void get_BeerList() {
        // ludis saxeobebis sia mogvaqvs
        JsonArrayRequest requestLudiList = new JsonArrayRequest(Constantebi.URL_GET_LUDILIST, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Constantebi.ludiList.clear();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            BeerModel newBeer = new BeerModel();
                            newBeer.setId(response.getJSONObject(i).getInt("id"));
                            newBeer.setDasaxeleba(response.getJSONObject(i).getString("dasaxeleba"));
                            newBeer.setFasi(response.getJSONObject(i).getDouble("fasi"));
                            newBeer.setDisplayColor(response.getJSONObject(i).getString("color"));

                            Constantebi.ludiList.add(newBeer);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "ლუდის სახეოების მონაცემებია შესაყვანი!", Toast.LENGTH_LONG).show();
                }

                MainActivity.requestCount++;
                if (MainActivity.requestCount == 4) {
                    activityOrientationNormal();
                    attachPricesToObj();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString() + " | ludi sadaaa! -", Toast.LENGTH_LONG).show();
                MainActivity.requestCount++;
                if (MainActivity.requestCount == 4) {
                    activityOrientationNormal();
                    attachPricesToObj();
                }
            }
        });

        queue.add(requestLudiList);
    }

    public void get_Prises() {
        // ludis fasebi titoeuli obieqtistvis
        JsonArrayRequest request_fasebi = new JsonArrayRequest(Constantebi.URL_GET_FASEBI, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Toast.makeText(context, "fasebi ok", Toast.LENGTH_LONG).show();
                Constantebi.FASEBI.clear();
                int objid;
                if (response.length() > 0) {
                    try {
                        objid = response.getJSONObject(0).getInt("obj_id");

                        PeerObjPrice objPrice = new PeerObjPrice(objid);

                        for (int i = 0; i < response.length(); i++) {
                            if (objid == response.getJSONObject(i).getInt("obj_id")) {
                                objPrice.getFasebi().add( Float.valueOf(response.getJSONObject(i).getString("fasi")) );
                            } else {
                                Constantebi.FASEBI.add(objPrice);

                                objid = response.getJSONObject(i).getInt("obj_id");
                                objPrice = new PeerObjPrice(objid);
                                objPrice.getFasebi().add( Float.valueOf(response.getJSONObject(i).getString("fasi")));
                            }
                        }
                        Constantebi.FASEBI.add(objPrice);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, "ლუდის ფასებია შესაყვანი!", Toast.LENGTH_LONG).show();
                }

                MainActivity.requestCount++;
                if (MainActivity.requestCount == 4) {
                    activityOrientationNormal();
                    attachPricesToObj();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString() + " | error fasebi -", Toast.LENGTH_LONG).show();
                MainActivity.requestCount++;
                if (MainActivity.requestCount == 4) {
                    activityOrientationNormal();
                    attachPricesToObj();
                }
            }
        });

        queue.add(request_fasebi);
    }

    public void get_Users() {
        // --- momxmareblebis Camonatvali ---
        JsonArrayRequest request_users = new JsonArrayRequest(Constantebi.URL_GET_USERS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Constantebi.USERsLIST.clear();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Useri newUser = new Useri(response.getJSONObject(i).getInt("id"), response.getJSONObject(i).getString("username"));

                            newUser.setName(response.getJSONObject(i).getString("name"));
                            newUser.setType(response.getJSONObject(i).getInt("type"));
                            newUser.setTel(response.getJSONObject(i).getString("tel"));
                            newUser.setAdress(response.getJSONObject(i).getString("adress"));
                            newUser.setMaker(response.getJSONObject(i).getString("maker"));
                            newUser.setComment(response.getJSONObject(i).getString("comment"));

                            Constantebi.USERsLIST.add(newUser);

                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "no users!", Toast.LENGTH_LONG).show();
                }
                MainActivity.requestCount++;
                if (MainActivity.requestCount == 4) {
                    activityOrientationNormal();
                    attachPricesToObj();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString() + " | error users load -", Toast.LENGTH_LONG).show();
                MainActivity.requestCount++;
                if (MainActivity.requestCount == 4) {
                    activityOrientationNormal();
                    attachPricesToObj();
                }
            }
        });

        queue.add(request_users);
    }

    private void attachPricesToObj() {  // ludis fasebs vawebeb tavis obieqtebs
        for (int i = 0; i < Constantebi.OBIEQTEBI.size(); i++) {
            int objid = Constantebi.OBIEQTEBI.get(i).getId();

            for (int j = 0; j < Constantebi.FASEBI.size(); j++) {
                if (objid == Constantebi.FASEBI.get(j).getObj_id()) {
                    Constantebi.OBIEQTEBI.get(i).setFasebi(Constantebi.FASEBI.get(j).getFasebi());
                }
            }
        }
        //Toast.makeText(context, "mieba", Toast.LENGTH_LONG).show();
    }

    private void activityOrientationNormal() {
        Activity activity = (Activity) context;
        activity.setRequestedOrientation(Constantebi.screenDefOrientation);
        getOrderComments();
    }

    public void editOrder(final Shekvetebi order, final int distID) {
        // aq vagzavnit monacemebs chasawerad
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request_orderEdit = new StringRequest(Request.Method.POST, Constantebi.URL_INS_SHEKVETA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                if (response.equals("ჩაწერილია!") || response.equals("შეკვეთა დაკორექტირდა!")) {
                    l.onChange();
                }
                activityOrientationNormal();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activityOrientationNormal();
                Toast.makeText(context, error.getMessage() + " -", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("moqmedeba", Constantebi.EDIT);

                params.put("order_id", String.valueOf(order.getOrder_id()));

                params.put("k30", String.valueOf(order.getK30wont()));
                params.put("k50", String.valueOf(order.getK50wont()));
                params.put("beer_id", String.valueOf(findBeerId(order.getLudi())));
                params.put("comment", order.getComment());
                params.put("distributor_id", String.valueOf(distID));

                params.put("chek", order.getChk());
                params.put("user_type", Constantebi.USER_TYPE);

                return params;
            }
        };

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
//        request.setRetryPolicy(mRetryPolicy);
        queue.add(request_orderEdit);
    }

    public void insertXarjebi(final String distrID, final float amount, final String comment){

        StringRequest request = new StringRequest(Request.Method.POST, Constantebi.URL_INSERT_XARJI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("INS_id", response);
                if (!response.equals("0")) {
                    Constantebi.XARJI_LIST.add(new Xarji(comment, distrID, response, amount));
                    l.onChange();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("distrid", distrID);
                params.put("amount", String.valueOf(amount));
                params.put("comment", comment);

                return params;
            }
        };

        queue.add(request);
    }

    public void delXarjebi(final String id){

        StringRequest request = new StringRequest(Request.Method.POST, Constantebi.URL_DEL_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(context, response , Toast.LENGTH_SHORT).show();
                Log.d("INS_id", response);
                if (response.equals("Removed!")) {
//                    Constantebi.XARJI_LIST.add(new Xarji(comment, distrID, response, amount));
                    l.onChange();
                }else {
                    Toast.makeText(context, "can't delete : "+response , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("table", "xarjebi");
                params.put("userid", Constantebi.USER_ID);

                return params;
            }
        };

        queue.add(request);
    }

    public void getOrderComments(){
        JsonArrayRequest request_comments = new JsonArrayRequest(Constantebi.URL_GET_ORDER_COMMENTS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Constantebi.ORDER_COMMENTS = new ArrayList<>();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            OrderCommentRowModel rowModel = new OrderCommentRowModel(
                                    response.getJSONObject(i).getString("dasaxeleba"),
                                    response.getJSONObject(i).getString("comment"),
                                    response.getJSONObject(i).getString("op")
                            );
                            Constantebi.ORDER_COMMENTS.add(rowModel);
                        } catch (JSONException excep) {
                            excep.printStackTrace();
                        }
                    }
                }
                l.onChange();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString() + " | error load Order Comments", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request_comments);
    }


    private Integer findBeerId(String ludi) {
        for (int i = 0; i < Constantebi.ludiList.size(); i++) {
            if (Constantebi.ludiList.get(i).getDasaxeleba().equals(ludi)) {
                return Constantebi.ludiList.get(i).getId();
            }
        }
        return 0;
    }

    public interface vListener {
        void onChange();
    }

    private vListener l = null;

    public void setChangeListener(vListener mListener){
        l = mListener;
    }
}
