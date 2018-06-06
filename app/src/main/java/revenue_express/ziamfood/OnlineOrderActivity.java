package revenue_express.ziamfood;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.adapter.OnlineOrderListAdapter;
import revenue_express.ziamfood.dao.OnlineOrderDataListDao;
import revenue_express.ziamfood.model.IDShop;
import revenue_express.ziamfood.model.OrderList;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class OnlineOrderActivity extends AppCompatActivity {
    String id;
    ImageView imgBack,imgSumOrder;
    RecyclerView order_recycler_view;
    String URL_last_update ;
    String access_token;
    List<Integer> list = new ArrayList<>();
    Realm realm;
    public static TextView tvOrder;
    Integer ranks = 0;

    public static Context context;


    public static OnlineOrderListAdapter MAdapter;
    public static ArrayList<OnlineOrderDataListDao> OnlineOrderDataListDaoArrayList;
    private ArrayList<Integer>List;
    public static JSONArray jsonArray;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;
    static Integer current = 1;
    static Integer total = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_order);

        sendNamePageGoogleAnalytics("OnlineOrderActivity");
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        realm.beginTransaction();
        RealmResults<OrderList> result = realm.where(OrderList.class).findAll();
        for (int i = 0; i < result.size(); i++){
            ranks = ranks+result.get(i).getAmount();
        }

//        tvOrder = (TextView)findViewById(R.id.tvOrder);
//        tvOrder.setText(String.valueOf(ranks));

        URL_last_update = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_order);
        access_token = getResources().getString(R.string.access_token);

        OnlineOrderDataListDaoArrayList = new ArrayList<OnlineOrderDataListDao>();
        List = new ArrayList<>();
        RealmResults<IDShop> idshop = realm.where(IDShop.class).findAll();
        id = String.valueOf(idshop.get(0).getIdshop());
        realm.commitTransaction();
        order_recycler_view = (RecyclerView) findViewById(R.id.order_recycler_view);

//        OnlineOrderActivity.AsyncDataClass asyncRequestObject = new OnlineOrderActivity.AsyncDataClass();
        callSyncGet(URL_last_update);


        LinearLayoutManager layoutManager_new_shop = new LinearLayoutManager(this);
        layoutManager_new_shop.setOrientation(LinearLayoutManager.VERTICAL);
        order_recycler_view.setLayoutManager(layoutManager_new_shop);
        order_recycler_view.setItemAnimator(new DefaultItemAnimator());
        order_recycler_view.setHasFixedSize(true);

        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        imgSumOrder = (ImageView)findViewById(R.id.imgSumOrder);
//        imgSumOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (tvOrder.getText().equals("0")){
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(OnlineOrderActivity.this);
//                    builder.setCancelable(false);
//                    builder.setMessage(getResources().getString(R.string.text_null_order));
//                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //if user pressed "yes", then he is allowed to exit from application
//
//                        }
//                    });
//
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }else {
//                    Intent intent = new Intent(getApplicationContext(), SumOrderActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

    }

//    public static void SumOnlineOrder(int total){
//
//        String total_last = String.valueOf(total);
//        tvOrder.setText(total_last);
//    }


    private void callSyncGet(final String url) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                imgLoad.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+url.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("shop", String.valueOf(id));
                formBuilder.add("show_item", "1");

                MediaType.parse("application/json; charset=utf-8");
                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                client.newCall(request)
                        .enqueue(new Callback() {

                            @Override
                            public void onFailure(final Call call, IOException e) {
                                // Error

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                if (!response.isSuccessful()) {
                                    throw new IOException("Unexpected code " + response);
                                } else {
                                    Log.i("Response:",response.toString());
                                    jsonData = response.body().string();
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = null;
                                            JSONArray jsonArray = null;

                                            jsonObject = new JSONObject(jsonData);

                                            jsonArray = jsonObject.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); ++i) {
                                                JSONObject jsonObject_list_id = jsonArray.getJSONObject(i);

                                                JSONArray jsonArray_list = jsonObject_list_id.getJSONArray("item");
                                                for (int j = 0; j < jsonArray_list.length(); j++) {
                                                    JSONObject jsonObject_list_detail = jsonArray_list.getJSONObject(j);

                                                    Integer id_menu = jsonObject_list_detail.getInt("id");
                                                    String name_menu = jsonObject_list_detail.getString("name");
                                                    String description = jsonObject_list_detail.getString("description");
                                                    String photo = jsonObject_list_detail.getString("photo");
                                                    String price = jsonObject_list_detail.getString("price");
                                                    String recommend = jsonObject_list_detail.getString("recommend");
                                                    System.out.println("Result Menu list detail: " + name_menu);

                                                    OnlineOrderDataListDaoArrayList.add(new OnlineOrderDataListDao(id_menu, name_menu, description, photo, price, recommend));
                                                }
                                            }

                                            MAdapter = new OnlineOrderListAdapter(OnlineOrderActivity.this,OnlineOrderDataListDaoArrayList);
                                            order_recycler_view.setAdapter(MAdapter);

                                        } catch (JSONException e) {
                                            e.getMessage();
                                        }
                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }
}
