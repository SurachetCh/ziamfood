package revenue_express.ziamfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.adapter.SumOrderAdapter;
import revenue_express.ziamfood.model.IDShop;
import revenue_express.ziamfood.model.OrderList;
import revenue_express.ziamfood.model.OrderListDAO;
import revenue_express.ziamfood.model.User;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class SumOrderActivity extends AppCompatActivity {
    Realm realm;

    RecyclerView sum_order_recycler_view;
    LinearLayout btnAdd,layoutName,layoutEmail,layoutPhone,layoutZipcode,layoutAddress;
    Button btnPickup,btnDelivery;
    EditText edtName,edtEmail,edtPhone,edtZipcode,edtAddress;
    SumOrderAdapter mAdapter;
    ImageView imgBack;
    Double total = 0.00;
    public static TextView tvTotal;
    private ArrayList<OrderListDAO> OnlineOrderDataListDaoArrayList1;

    ArrayList<Integer> item_id = new ArrayList<Integer>();
    ArrayList<Double> item_price = new ArrayList<Double>();
    ArrayList<String> item_name = new ArrayList<String>();
    ArrayList<String> item_instruction = new ArrayList<String>();
    ArrayList<Integer> item_qty = new ArrayList<Integer>();
    ArrayList<Double> item_total = new ArrayList<Double>();
    String shop;
    int SizeOrder;
    String access_user_key,access_token,url_online_order,type,c_name,c_email,c_tel,c_address,c_zipcode;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum_order);

        sendNamePageGoogleAnalytics("SumOrderActivity");
        type = "pickup";
//        delivery = 0;

        btnAdd = (LinearLayout)findViewById(R.id.btnAdd);
        tvTotal = (TextView)findViewById(R.id.tvTotal);
        layoutName = (LinearLayout)findViewById(R.id.layoutName);
        layoutEmail = (LinearLayout)findViewById(R.id.layoutEmail);
        layoutPhone = (LinearLayout)findViewById(R.id.layoutPhone);
        layoutZipcode = (LinearLayout)findViewById(R.id.layoutZipcode);
        layoutAddress = (LinearLayout)findViewById(R.id.layoutAddress);
        btnPickup = (Button)findViewById(R.id.btnPickup);
        btnDelivery = (Button)findViewById(R.id.btnDelivery);
        edtName = (EditText)findViewById(R.id.edtName);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtZipcode = (EditText)findViewById(R.id.edtZipcode);
        edtAddress = (EditText)findViewById(R.id.edtAddress);

        url_online_order = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_online_order);
        access_token = getResources().getString(R.string.access_token);

        layoutZipcode.setVisibility(View.GONE);
        layoutAddress.setVisibility(View.GONE);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        OnlineOrderDataListDaoArrayList1 = new ArrayList<OrderListDAO>();

        sum_order_recycler_view = (RecyclerView) findViewById(R.id.sum_order_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        sum_order_recycler_view.setLayoutManager(layoutManager);
        sum_order_recycler_view.setItemAnimator(new DefaultItemAnimator());
        sum_order_recycler_view.setHasFixedSize(true);

        // add row
//        executeRealmTransaction();
       new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {

               }
       };

//        QueryRealm();

        realm.beginTransaction();
        RealmResults<User> user = realm.where(User.class).findAll();
        access_user_key = user.get(0).getAccess_user_key();
        c_email = user.get(0).getEmail();
        c_name = user.get(0).getName();

        edtName.setText(c_name);
        edtEmail.setText(c_email);

        RealmResults<OrderList> results = realm.where(OrderList.class).findAll();
        Log.e("Results",String.valueOf(results));

        if(results == null || results.size() == 0){
            Log.e("Results",String.valueOf(results));
        }else{
//            OrderList orderList = results.get(0);

            List<OrderList> copied = realm.copyFromRealm(results);

            for (int i = 0; i < copied.size(); i++) {

                Integer id_menu = copied.get(i).getId_menu();
                Integer id = copied.get(i).getId();
                String title = copied.get(i).getTitle();
                Double price = copied.get(i).getPrice();
                Double total = copied.get(i).getTotal();
                Integer amount = copied.get(i).getAmount();
                String addition = copied.get(i).getAddition();
                String note = copied.get(i).getNote();


                item_id.add(copied.get(i).getId_menu());
                item_price.add(copied.get(i).getPrice());
                item_name.add(copied.get(i).getTitle());
                item_instruction.add(copied.get(i).getAddition());
                item_qty.add(copied.get(i).getAmount());
                item_total.add(copied.get(i).getTotal());

                OnlineOrderDataListDaoArrayList1.add(new OrderListDAO(id, id_menu, title, price,total, amount,addition,note));
            }

            RealmResults<IDShop> idshop = realm.where(IDShop.class).findAll();
            shop = String.valueOf(idshop.get(0).getIdshop());

            mAdapter = new SumOrderAdapter(SumOrderActivity.this,OnlineOrderDataListDaoArrayList1);
            sum_order_recycler_view.setAdapter(mAdapter);

            total = Double.valueOf(new DecimalFormat("0.00000").format(realm.where(OrderList.class).sum("total")));;
            tvTotal.setText(total.toString());

            realm.commitTransaction();
        }

        btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutZipcode.setVisibility(View.GONE);
                layoutAddress.setVisibility(View.GONE);
                type = "pickup";
//                delivery = 0;
            }
        });
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layoutZipcode.setVisibility(View.VISIBLE);
//                layoutAddress.setVisibility(View.VISIBLE);
//                type = "delivery";
//                delivery = 1;
                showUnderConstructionDialog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                realm.where(OrderList.class).findAll().clear();
//                DeleteRealm();
//                QueryRealm();
                c_name = String.valueOf(edtName.getText());
                c_email = String.valueOf(edtEmail.getText());
                c_tel = String.valueOf(edtPhone.getText());
                c_address = String.valueOf(edtAddress.getText());
                c_zipcode = String.valueOf(edtZipcode.getText());

                if (c_name.equals("") || c_email.equals("") || c_tel.equals("")){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_deficient), Toast.LENGTH_SHORT).show();
                }else {
                    callSyncGet(url_online_order);
                }
            }
        });
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<OrderList> resultSize = realm.where(OrderList.class).findAll();
                SizeOrder = resultSize.size();
//                SumOnlineOrder(SizeOrder);
//                SumOnlineOrderFoods(SizeOrder);
                onBackPressed();
            }
        });

    }
    @Override
    public void onBackPressed(){
        RealmResults<OrderList> resultSize = realm.where(OrderList.class).findAll();
        for (int i = 0; i < resultSize.size(); i++){
            SizeOrder = SizeOrder+resultSize.get(i).getAmount();
        }
//        SizeOrder = resultSize.size();
//        SumOnlineOrder(SizeOrder);
//        SumOnlineOrderFoods(SizeOrder);
        Intent intent = new Intent(getApplicationContext(), OnlineOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
    public static void SumOrder(double total){

        String total_last = String.valueOf(total);
        tvTotal.setText(total_last);
    }
    private void QueryRealm() {
        RealmQuery<OrderList> query = realm.where(OrderList.class);
        String count = String.valueOf(query.findAll());
        Log.e("Results",String.valueOf(count));
    }
    private  void DeleteOrderListRealm(){

        realm.beginTransaction();
        RealmResults<OrderList> result = realm.where(OrderList.class).findAll();
        result.deleteLastFromRealm();
        realm.commitTransaction();
    }

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
                formBuilder.add("access_user_key",access_user_key);
                formBuilder.add("shop", shop);
                formBuilder.add("type",type);
                formBuilder.add("c_name", c_name);
                formBuilder.add("c_email", c_email);
                formBuilder.add("c_tel", c_tel);
                //                formBuilder.add("c_address", c_address);
//                formBuilder.add("c_zipcode",c_zipcode);
                for (int i = 0; i < item_id.size(); i++) {
                    formBuilder.add("item_id[]", String.valueOf(item_id.get(i)));
                    formBuilder.add("item_name[]", String.valueOf(item_name.get(i)));
                    formBuilder.add("item_instruction[]", String.valueOf(item_instruction.get(i)));
                    formBuilder.add("item_price[]", String.valueOf(item_price.get(i)));
                    formBuilder.add("item_qty[]", String.valueOf(item_qty.get(i)));
                }

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
                                        Log.i("Result shop name", jsonData);
                                        if (jsonData.toLowerCase().contains("success")){
                                            showStatustionDialog("Order Online Status", String.valueOf(R.string.order_success),"success");
                                            DeleteOrderListRealm();
                                        }else {
                                            showStatustionDialog("Order Online Status", String.valueOf(R.string.order_fail),"fail");
                                        }

                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    //-----Show dialog box-----//
    private void showStatustionDialog(String title , String msg, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SumOrderActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (status.equals("success")){
                            Intent intent = new Intent(getApplicationContext(),OnlineOrderActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void showUnderConstructionDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(SumOrderActivity.this);
        View promptView = layoutInflater.inflate(R.layout.under_construction_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SumOrderActivity.this);
        alertDialogBuilder.setView(promptView);

        final ImageView Img = (ImageView) promptView.findViewById(R.id.img);
        Img.setImageResource(R.drawable.under_construction);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(getResources().getText(R.string.close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
