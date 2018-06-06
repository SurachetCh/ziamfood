package revenue_express.ziamfood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import revenue_express.ziamfood.model.IDShop;
import revenue_express.ziamfood.model.OrderList;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class ShopActivity extends AppCompatActivity implements OnMapReadyCallback {
    ImageView iv_image_shop_name,imgBack,img_star1,img_star2,img_star3,img_star4,img_star5,img_review;
//    RatingBar ratingBar;
    Button bt_status_shop;
    TextView tvCoin;
    TextView time,txtAddress,txtTel,txtDetail,txtTimeshop,txt_toolbar;
    // Time open and close shop


    GoogleMap mMap;

    String img_cover,title,detail,map_position,address,phone;
    String img_cover_thumb,day,now_open,day_of_week,openshop,closeshop,opening,closing,alert;
    private static String now_opening;
    String url_business_shop ;
    String access_token;
    String idShop = "";
    Realm realm;
    JSONObject json_work_hour_today;

    protected Context context;

    private ArrayList<String> images = new ArrayList<String>();
    private static ArrayList<String> timeshop = new ArrayList<String>();
    public  static ArrayList<String> day_of_week_now = new ArrayList<String>();

    ViewFlipper simpleViewFlipper;
    int checkOrder = 0;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;

    public static String force_online;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        sendNamePageGoogleAnalytics("ShopActivity");

        Typeface myTypeface = Typeface.createFromAsset(ShopActivity.this.getAssets(),getResources().getString( R.string.FontText));

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
//        DeleteOrderListRealm();
        DeleteIDShopRealm();
        executeRealmTransaction();

        url_business_shop = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_shop);
        access_token = getResources().getString(R.string.access_token);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set variable use
        iv_image_shop_name = (ImageView) findViewById(R.id.iv_image_shop_name);
        txtDetail = (TextView) findViewById(R.id.txtDetail);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtTel = (TextView) findViewById(R.id.txtTel);
        txt_toolbar = (TextView) findViewById(R.id.txt_toolbar);
        time = (TextView) findViewById(R.id.tvTime);
        txtTimeshop = (TextView)findViewById(R.id.txtTimeshop);
//        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        img_star1 = (ImageView) findViewById(R.id.img_star1);
        img_star2 = (ImageView) findViewById(R.id.img_star2);
        img_star3 = (ImageView) findViewById(R.id.img_star3);
        img_star4 = (ImageView) findViewById(R.id.img_star4);
        img_star5 = (ImageView) findViewById(R.id.img_star5);

        img_review = (ImageView)findViewById(R.id.img_review);
        img_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewsShopActivity.class);
                startActivity(intent);
            }
        });

        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtDetail.setTypeface(myTypeface);
        txtAddress.setTypeface(myTypeface);
        txtTel.setTypeface(myTypeface);
        txt_toolbar.setTypeface(myTypeface);
        time.setTypeface(myTypeface);
        txtTimeshop.setTypeface(myTypeface);

        //Get id shop from MainActivity
        idShop = getIntent().getStringExtra("id");

        // request authentication with remote ziamfoods app(lastshop)
            callSyncGet(url_business_shop);

//        DeleteIDShopRealm();
//        executeRealmTransaction();

        // request authentication with remote ziamfoods app(lastshop)
//                ShopActivity.AsyncTimeDataClass asyncRequestObjectShop = new ShopActivity.AsyncTimeDataClass();
//        asyncRequestObjectShop.execute(url_business_shop, access_token , id);
//            callSyncGetTime(url_business_shop);
//        addListenerOnButton();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

//    @Override
//    public void onBackPressed() {
//        realm.beginTransaction();
//        RealmResults<OrderList> result = realm.where(OrderList.class).findAll();
//        checkOrder = result.size();
//        realm.commitTransaction();
//        if (checkOrder != 0 ){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setCancelable(false);
//            builder.setMessage(getResources().getString(R.string.text_delete_order));
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //if user pressed "yes", then he is allowed to exit from application
//                    DeleteOrderListRealm();
//                    ShopActivity.super.onBackPressed();
//                }
//            });
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //if user select "No", just cancel this dialog and continue with app
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//
//        }else {
//            super.onBackPressed();
//        }
//
//    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
         String title_map = intent.getStringExtra("title");
         String map_position = intent.getStringExtra("map");

        String CurrentString = map_position;
        String[] separated = CurrentString.split(",");

        separated[0] = separated[0].trim();
        separated[1] = separated[1].trim();

        String lat = separated[0];
        String lng = separated[1];

        double valueLat = Double.parseDouble(lat);
        double valueLng = Double.parseDouble(lng);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(valueLat,valueLng);

        mMap.addMarker(new MarkerOptions().position(sydney).title(title_map).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    public void addListenerOnButton() {
//
//        btnCall = (Button) findViewById(R.id.btnCall);
//        btnCall.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                callIntent.setData(Uri.parse("tel:"+"+1"+phone));
//                startActivity(callIntent);
//            }
//        });
//
//        btnInfo = (Button) findViewById(R.id.btnInfo);
//        btnInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                txtDetail.setText(detail);
////                txtAddress.setText(address);
////                txtTel.setText(phone);
//
//                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
//                intent.putExtra("title",title);
//                intent.putExtra("address", address);
//                intent.putExtra("phone", phone);
//                intent.putExtra("detail", detail);
//                intent.putExtra("map_position", map_position);
//                startActivity(intent);
////                    showInputDialog();
//            }
//        });
//
//        btnMenu = (Button) findViewById(R.id.btnMenu);
//        btnMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    Intent intent = new Intent(getApplicationContext(), OnlineOrderActivity.class);
//                    intent.putExtra("id", id);
//                    startActivity(intent);
//            }
//        });
//
//        btnReview = (Button) findViewById(R.id.btnReview);
//        btnReview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), ReviewsShopActivity.class);
//                    intent.putExtra("id", id);
//                    intent.putExtra("name", title);
//                    startActivity(intent);
//            }
//        });
//    }

    public static String checkTimeOpen(){
        return now_opening;
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
                formBuilder.add("shop", String.valueOf(idShop));

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
                                    Log.i("Shop Data:",jsonData.toString());
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = null;
//                                    JSONArray jsonArray = null;

                                            jsonObject = new JSONObject(jsonData);

                                            JSONObject jsonData = jsonObject.getJSONObject("data");
                                            JSONObject json_info = jsonData.getJSONObject("info");
//                                            if(jsonData.getString("work_hour_today").equals("null")) {
//                                                 Log.i("work_hour_today ","is null");
//                                            }else{
//                                                json_work_hour_today = jsonData.getJSONObject("work_hour_today");
//                                            }

                                            img_cover = json_info.getString("img_cover");
                                            title = json_info.getString("title");
                                            address = json_info.getString("address");
                                            phone = json_info.getString("phone");
                                            detail = json_info.getString("detail");

                                            map_position = json_info.getString("map_position");

                                            force_online = json_info.getString("force_online");

                                            Glide.with(ShopActivity.this)
                                                    .load(img_cover)
                                                    .placeholder(R.drawable.download)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .skipMemoryCache(true)
                                                    .into(iv_image_shop_name);
                                            Log.i(".........", img_cover);

                                            iv_image_shop_name = (ImageView) findViewById(R.id.iv_image_shop_name);
                                            txtDetail.setText(detail);
                                            txtAddress.setText(address);
                                            txtTel.setText(phone);
                                            txt_toolbar.setText(title);

//                                            JSONArray jsonArray_list = jsonData.getJSONArray("img_menu");
//
//                                            for (int i = 0; i < jsonArray_list.length(); i++) {
//                                                JSONObject jsonObject_menu = jsonArray_list.getJSONObject(i);
//
//                                                String img = jsonObject_menu.getString("img");
//
//                                                images.add(img);
//
//                                            }
//
//                                            // Set slider image menu
////                init(images);
//
//                                            txt_toolbar.setText(title);
//                                            if (images.size() != 0) {
//
//                                            } else {
//                                                simpleViewFlipper.setVisibility(View.GONE);
//                                            }
//
//                                            setFlipperImage(images);

                                            JSONObject jsonratstar = jsonData.getJSONObject("rating_now");
                                            String review_score = jsonratstar.getString("review_score");
//                                            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                                            float rating = Float.parseFloat(review_score);
//                                            ratingBar.setRating(rating);
                                            int star = Math.round(rating);
                                            img_star1.setImageResource(0);
                                            img_star2.setImageResource(0);
                                            img_star3.setImageResource(0);
                                            img_star4.setImageResource(0);
                                            img_star5.setImageResource(0);

                                            if (star==0){
                                                img_star1.setImageResource(R.drawable.star_emty);
                                                img_star2.setImageResource(R.drawable.star_emty);
                                                img_star3.setImageResource(R.drawable.star_emty);
                                                img_star4.setImageResource(R.drawable.star_emty);
                                                img_star5.setImageResource(R.drawable.star_emty);

                                            }else if (star==1){
                                                img_star1.setImageResource(R.drawable.star);
                                                img_star2.setImageResource(R.drawable.star_emty);
                                                img_star3.setImageResource(R.drawable.star_emty);
                                                img_star4.setImageResource(R.drawable.star_emty);
                                                img_star5.setImageResource(R.drawable.star_emty);
                                            }else if (star==2){
                                                img_star1.setImageResource(R.drawable.star);
                                                img_star2.setImageResource(R.drawable.star);
                                                img_star3.setImageResource(R.drawable.star_emty);
                                                img_star4.setImageResource(R.drawable.star_emty);
                                                img_star5.setImageResource(R.drawable.star_emty);
                                            }else if (star==3){
                                                img_star1.setImageResource(R.drawable.star);
                                                img_star2.setImageResource(R.drawable.star);
                                                img_star3.setImageResource(R.drawable.star);
                                                img_star4.setImageResource(R.drawable.star_emty);
                                                img_star5.setImageResource(R.drawable.star_emty);
                                            }else if (star==4){
                                                img_star1.setImageResource(R.drawable.star);
                                                img_star2.setImageResource(R.drawable.star);
                                                img_star3.setImageResource(R.drawable.star);
                                                img_star4.setImageResource(R.drawable.star);
                                                img_star5.setImageResource(R.drawable.star_emty);
                                            }else {
                                                img_star1.setImageResource(R.drawable.star);
                                                img_star2.setImageResource(R.drawable.star);
                                                img_star3.setImageResource(R.drawable.star);
                                                img_star4.setImageResource(R.drawable.star);
                                                img_star5.setImageResource(R.drawable.star);
                                            }

//                                            Log.i("Work houre today>>>>", String.valueOf(jsonData.get("work_hour_today")));
                                            //settimestatus
                                            Boolean shop_status = jsonData.getBoolean("shop_status");

                                            ImageView checkTime = (ImageView) findViewById(R.id.bt_status_shop);
                                            if (shop_status==true){
                                                checkTime.setImageResource(R.drawable.open);
                                            }else {
                                                checkTime.setImageResource(R.drawable.close);
                                            }
                                            //settimeshop
                                            JSONArray json_shop_open = jsonData.getJSONArray("shop_open");
                                            timeshop.clear();
                                            for (int j = 0; j < json_shop_open.length(); j++){
                                                timeshop.add(json_shop_open.get(j).toString());

                                            }
                                            for (int j = 0; j < timeshop.size(); j++){
                                                txtTimeshop.append(timeshop.get(j) + "\n");
                                            }

                                            //setshop_message
                                            String shop_message = jsonData.getString("shop_message");
                                            time.setText(shop_message);


//                                            if(jsonData.isNull("work_hour_today")) {
//                                                now_opening = "";
//                                                Log.e("Opening /Day of week: ",now_opening+"/"+day_of_week_now);
//                                            }else {
//
//                                                JSONObject jsontime = jsonData.getJSONObject("work_hour_today");
//                                                now_opening = jsontime.getString("now_opening");
//                                                day_of_week_now.add(jsontime.getString("day_of_week"));
//                                                Log.e("Opening /Day of week: ", now_opening + "/" + day_of_week_now);
//
//                                                // check time shop open
////                                                ImageView checkTime = (ImageView) findViewById(R.id.bt_status_shop);
//
//                                                    if (now_opening.equals("true")) {
//                                                        now_open = "OPEN";
//                                                        closing = jsontime.getString("upcoming_close");
//                                                        String openclose = formatTime(closing);
//                                                        time.setText("upcoming_close :" + openclose);
////                                                        checkTime.setTextColor(Color.parseColor("#FFFFFF"));
////                                                        checkTime.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
////                                                        checkTime.setText(now_open);
//                                                        checkTime.setImageResource(R.drawable.open);
//                                                        String time_open = json_work_hour_today.getString("upcoming_close");;
//                                                        time.setText("upcoming_close :"+time_open);
//                                                    }
//                                                    if (now_opening.equals("false")) {
//                                                        now_opening = "";
//                                                        now_open = "CLOSE";
//                                                        opening = jsontime.getString("upcoming_open");
//                                                        String next_day_open = jsontime.getString("next_day_open");
//                                                        if (next_day_open == Boolean.toString(true)) {
//                                                            alert = "Opening on Next day ";
//                                                        } else {
//                                                            alert = "Opening on ";
//                                                        }
//                                                        String opentime = formatTime(opening);
//                                                        time.setText(alert + " " + opentime);
////                                                        checkTime.setTextColor(Color.parseColor("#FFFFFF"));
////                                                        checkTime.setBackgroundColor(getResources().getColor(R.color.red));
////                                                        checkTime.setText(now_open);
//                                                        checkTime.setImageResource(R.drawable.close);
//                                                        String time_open = json_work_hour_today.getString("upcoming_open");
//                                                        time.setText("upcoming_open"+time_open);
//                                                    }
//
//                                                // Time all shop
//                                                JSONArray jsonwork_hour_list = jsonData.getJSONArray("work_hour_list");
//                                                for (int i = 0; i < jsonwork_hour_list.length(); i++) {
//
//                                                    JSONObject jsonwork_hour_list_check = jsonwork_hour_list.getJSONObject(i);
//
//                                                    day_of_week = jsonwork_hour_list_check.getString("day_of_week");
//                                                    openshop = jsonwork_hour_list_check.getString("opening");
//                                                    closeshop = jsonwork_hour_list_check.getString("closing");
//
//                                                    if (day_of_week.equals("0")) {
//                                                        day = "Sun";
//                                                    }
//                                                    if (day_of_week.equals("1")) {
//                                                        day = "Mon";
//                                                    }
//                                                    if (day_of_week.equals("2")) {
//                                                        day = "Tue";
//                                                    }
//                                                    if (day_of_week.equals("3")) {
//                                                        day = "Wed";
//                                                    }
//                                                    if (day_of_week.equals("4")) {
//                                                        day = "Thu";
//                                                    }
//                                                    if (day_of_week.equals("5")) {
//                                                        day = "Fri";
//                                                    }
//                                                    if (day_of_week.equals("6")) {
//                                                        day = "Sat";
//                                                    }
//
//                                                    if(i==Integer.valueOf(day_of_week_now.get(0))) {
//                                                        txtTimeshop.append(day + " " + formatTime(openshop) + "-" + formatTime(closeshop)+" (OPEN)" + "\n");
//                                                    }else{
//                                                        txtTimeshop.append(day + " " + formatTime(openshop) + "-" + formatTime(closeshop) + "\n");
//                                                    }
//                                                }
//                                            }

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


    protected void showUnderConstructionDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ShopActivity.this);
        View promptView = layoutInflater.inflate(R.layout.under_construction_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShopActivity.this);
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

    //-----Show dialog box-----//
    private void showStatusOpenDialog(String title ,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private  void DeleteOrderListRealm(){

        realm.beginTransaction();
        RealmResults<OrderList> result = realm.where(OrderList.class).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }
    private  void DeleteIDShopRealm(){

        realm.beginTransaction();
        RealmResults<IDShop> result = realm.where(IDShop.class).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }

    private void executeRealmTransaction() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                IDShop idshop = realm.createObject(IDShop.class);
                idshop.setIdshop(Integer.parseInt(idShop));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(ShopActivity.this,"Create Complete",Toast.LENGTH_SHORT).show();
//                RealmQuery<OrderList> query = realm.where(OrderList.class);
//                String firstName = String.valueOf(query.equalTo("firstname",String.valueOf(edtFirstname.getText())));
//                String lastName = String.valueOf(query.equalTo("firstname",String.valueOf(edtLasttname.getText())));
//                Log.e("Results ",firstName+" "+lastName);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
//                Toast.makeText(ShopActivity.this,"Create Error",Toast.LENGTH_SHORT).show();
                executeRealmTransaction();
            }
        });
    }


    private void callSyncGetTime(final String url) {
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
                formBuilder.add("shop", String.valueOf(idShop));

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
//                                    JSONArray jsonArray = null;

                                            jsonObject = new JSONObject(jsonData);
                                            JSONObject jsonData = jsonObject.getJSONObject("data");

                                            JSONObject jsonArray_list_info = jsonData.getJSONObject("info");

                                            img_cover_thumb = jsonArray_list_info.getString("img_cover_thumb");
                                            address = jsonArray_list_info.getString("address");
                                            detail = jsonArray_list_info.getString("detail");
                                            force_online = jsonArray_list_info.getString("force_online");

                                            Glide.with(ShopActivity.this)
                                                    .load(img_cover_thumb)
                                                    .placeholder(R.drawable.download)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .skipMemoryCache(true)
                                                    .into(iv_image_shop_name);


                                            //get star rating point
                                            JSONObject jsonratstar = jsonData.getJSONObject("rating_now");
                                            String review_score = jsonratstar.getString("review_score");
//                                            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                                            float rating = Float.parseFloat(review_score);
//                                            ratingBar.setRating(rating);
                                            int star = Math.round(rating);

                                            if (star==0){
                                                img_star1.setVisibility(View.GONE);
                                                img_star2.setVisibility(View.GONE);
                                                img_star3.setVisibility(View.GONE);
                                                img_star4.setVisibility(View.GONE);
                                                img_star5.setVisibility(View.GONE);

                                            }else if (star==1){
                                                img_star1.setVisibility(View.VISIBLE);
                                                img_star2.setVisibility(View.GONE);
                                                img_star3.setVisibility(View.GONE);
                                                img_star4.setVisibility(View.GONE);
                                                img_star5.setVisibility(View.GONE);
                                            }else if (star==2){
                                                img_star1.setVisibility(View.VISIBLE);
                                                img_star2.setVisibility(View.VISIBLE);
                                                img_star3.setVisibility(View.GONE);
                                                img_star4.setVisibility(View.GONE);
                                                img_star5.setVisibility(View.GONE);
                                            }else if (star==3){
                                                img_star1.setVisibility(View.VISIBLE);
                                                img_star2.setVisibility(View.VISIBLE);
                                                img_star3.setVisibility(View.VISIBLE);
                                                img_star4.setVisibility(View.GONE);
                                                img_star5.setVisibility(View.GONE);
                                            }else if (star==4){
                                                img_star1.setVisibility(View.VISIBLE);
                                                img_star2.setVisibility(View.VISIBLE);
                                                img_star3.setVisibility(View.VISIBLE);
                                                img_star4.setVisibility(View.VISIBLE);
                                                img_star5.setVisibility(View.GONE);
                                            }else if (star==5){
                                                img_star1.setVisibility(View.VISIBLE);
                                                img_star2.setVisibility(View.VISIBLE);
                                                img_star3.setVisibility(View.VISIBLE);
                                                img_star4.setVisibility(View.VISIBLE);
                                                img_star5.setVisibility(View.VISIBLE);
                                            }else {
                                                img_star1.setVisibility(View.VISIBLE);
                                                img_star2.setVisibility(View.VISIBLE);
                                                img_star3.setVisibility(View.VISIBLE);
                                                img_star4.setVisibility(View.VISIBLE);
                                                img_star5.setVisibility(View.VISIBLE);
                                            }



                                            Log.i("Work houre today>>>>", String.valueOf(jsonData.get("work_hour_today")));

                                            if(jsonData.isNull("work_hour_today")) {
                                                now_opening = "";
                                                Log.e("Opening /Day of week: ",now_opening+"/"+day_of_week_now);
                                            }else{

                                                JSONObject jsontime = jsonData.getJSONObject("work_hour_today");
                                                now_opening = jsontime.getString("now_opening");
                                                day_of_week_now.add(jsontime.getString("day_of_week"));
                                                Log.e("Opening /Day of week: ",now_opening+"/"+day_of_week_now);

                                                // check time shop open
                                                ImageView checkTime = (ImageView) findViewById(R.id.bt_status_shop);

                                                if(force_online.equals("1")){
                                                    now_open = "OPEN";
//                                                    checkTime.setTextColor(Color.parseColor("#FFFFFF"));
//                                                    checkTime.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                                                    checkTime.setText(now_open);
                                                    checkTime.setImageResource(R.drawable.open);
                                                }else{
                                                    if (now_opening.equals("true")) {
                                                        now_open = "OPEN";
                                                        closing = jsontime.getString("upcoming_close");
                                                        String openclose = formatTime(closing);
                                                        time.setText("upcoming_close " + openclose);
//                                                        checkTime.setTextColor(Color.parseColor("#FFFFFF"));
//                                                        checkTime.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                                                        checkTime.setText(now_open);
                                                        checkTime.setImageResource(R.drawable.open);
                                                    }
                                                    if (now_opening.equals("false")) {
                                                        now_opening = "";
                                                        now_open = "CLOSE";
                                                        opening = jsontime.getString("upcoming_open");
                                                        String next_day_open = jsontime.getString("next_day_open");
                                                        if(next_day_open == Boolean.toString(true)){
                                                            alert = "Opening on Next day ";
                                                        }else{
                                                            alert = "Opening on ";
                                                        }
                                                        String opentime = formatTime(opening);
                                                        time.setText(alert + " " + opentime);
//                                                        checkTime.setTextColor(Color.parseColor("#FFFFFF"));
//                                                        checkTime.setBackgroundColor(getResources().getColor(R.color.red));
//                                                        checkTime.setText(now_open);
                                                        checkTime.setImageResource(R.drawable.close);
                                                    }
                                                }

                                                // Time all shop
                                                JSONArray jsonwork_hour_list = jsonData.getJSONArray("work_hour_list");
                                                for (int i = 0; i < jsonwork_hour_list.length(); i++) {

                                                    JSONObject jsonwork_hour_list_check = jsonwork_hour_list.getJSONObject(i);

                                                    day_of_week = jsonwork_hour_list_check.getString("day_of_week");
                                                    openshop = jsonwork_hour_list_check.getString("opening");
                                                    closeshop = jsonwork_hour_list_check.getString("closing");

                                                    if(day_of_week.equals("0")){
                                                        day = "Sun";
                                                    }
                                                    if(day_of_week.equals("1")){
                                                        day = "Mon";
                                                    }
                                                    if(day_of_week.equals("2")){
                                                        day = "Tue";
                                                    }
                                                    if(day_of_week.equals("3")){
                                                        day = "Wed";
                                                    }
                                                    if(day_of_week.equals("4")){
                                                        day = "Thu";
                                                    }
                                                    if(day_of_week.equals("5")){
                                                        day = "Fri";
                                                    }
                                                    if(day_of_week.equals("6")){
                                                        day = "Sat";
                                                    }

                                                    timeshop.add(day+" "+formatTime(openshop)+"-"+formatTime(closeshop));
                                                }
                                            }
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

    public String formatTime(String time){
        String lastTime= "";
        try {
            DateFormat inputFormat = new SimpleDateFormat("HH:mm");
            DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
            lastTime = outputFormat.format(inputFormat.parse(time));

        }catch (java.text.ParseException e){
            e.getMessage();
        }
        return lastTime;
    }

    public static ArrayList<String> getDay_of_week_now(){
        return day_of_week_now;
    }

    public  static ArrayList<String> getTimeShop(){
        return timeshop;
    }

    public static String checkForceOnline(){
        return force_online;
    }


}
