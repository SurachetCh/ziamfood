package revenue_express.ziamfood;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.adapter.WriteReviewsMenuAdapter;
import revenue_express.ziamfood.adapter.WriteReviewsSetImageAdapter;
import revenue_express.ziamfood.dao.WriteReviewsMenuDataDao;
import revenue_express.ziamfood.dao.WriteReviewsSetImageDataDao;
import revenue_express.ziamfood.model.IDShop;
import revenue_express.ziamfood.model.User;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class WriteReviewsActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    private static final String TAG = WriteReviewsActivity.class.getSimpleName();
    static String access_user_key,access_token,shop;
    String URL_write_reviews,URL_menu_reviews,URL_reviews_upload_image,title,des,captionImage,msg,return_json = "1";
    Integer score,price;
    EditText edtTitle,edtDes,edtCaption;
    ImageView imgBack,iv_upload_image;
    TextView tv_title_head,tvShopName,tv_display_title,tv_display_des,tv_display_score,tv_display_price,tv_display_recommend_menu,tv_display_upload_image,tv_write_review;
    Button btnSend;
    Spinner spnRecommend;
    JSONObject json = null;
    private List<WriteReviewsMenuDataDao> categoery;
    private WriteReviewsMenuAdapter mAdapter;
    RecyclerView writereviews_recycler_view,image_upload_recycler_view;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;
    public static Context context;
    String path;

    //rating bar
    RatingBar ratingBar,ratingBarPrice;
    Float ratestar = null ,ratePrice = null;

    //spinner show data image type
    ArrayAdapter<String> dataAdapter;
    Spinner spnImageType;
    private  static ArrayList<String> id_menu_recommend = new ArrayList<String>();
    JSONArray json_menu;

    //set image upload
    private List<WriteReviewsSetImageDataDao> setImage;
    private WriteReviewsSetImageAdapter mAdapterSetImage;

    //remove id image
    private static  ArrayList<String> id_image = new ArrayList<String>();
    public  static  String URL_writereview_remove_image;
    Realm realm;
    ImageView imgProgress;

    //Ok http3
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    String jsonData;

//    Integer ranks = 0;
//    TextView tvOrder;

    //Set type fonts
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_reviews);

        sendNamePageGoogleAnalytics("WriteReviewsActivity");

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        tf = Typeface.createFromAsset(WriteReviewsActivity.this.getAssets(),
                getResources().getString(R.string.FontText));

        realm.beginTransaction();
        RealmResults<User> user = realm.where(User.class).findAll();
        if(user.size() != 0 ) {
            access_user_key = user.get(0).getAccess_user_key();

            RealmResults<IDShop> idShops = realm.where(IDShop.class).findAll();
            shop = String.valueOf(idShops.get(0).getIdshop());
            realm.commitTransaction();

//        shop = getResources().getString(R.string.shop_id_ref);

            //Requesting storage permission
            requestStoragePermission();

            URL_reviews_upload_image = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_reviews_upload_image);
            URL_write_reviews = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_write_reviews);
            URL_menu_reviews = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_menu_item_reviews);
            URL_writereview_remove_image = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_writereview_remove_image);
            access_token = getResources().getString(R.string.access_token);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            imgProgress = (ImageView)findViewById(R.id.imgProgress);

            Glide.with(this)
                    .load(R.drawable.downloads_gif)
                    .asGif()
                    .into(imgProgress);

            imgBack =(ImageView)findViewById(R.id.imgBack);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            edtTitle = (EditText)findViewById(R.id.edtTitle);
            edtDes = (EditText)findViewById(R.id.edtDes);

            tv_title_head = (TextView)findViewById(R.id.tv_title_head);
            tv_display_title = (TextView)findViewById(R.id.tv_display_title);
            tv_display_des = (TextView)findViewById(R.id.tv_display_des);
            tv_display_score = (TextView)findViewById(R.id.tv_display_score);
            tv_display_price = (TextView)findViewById(R.id.tv_display_price);
            tv_display_recommend_menu = (TextView)findViewById(R.id.tv_display_recommend_menu);
            tv_display_upload_image = (TextView)findViewById(R.id.tv_display_upload_image);
//            tv_write_review = (TextView)findViewById(R.id.tv_write_review);

            btnSend = (Button)findViewById(R.id.btnSend);

            //Set type fonts
            edtTitle.setTypeface(tf);
            edtDes.setTypeface(tf);
//            tv_write_review.setTypeface(tf);
            tv_title_head.setTypeface(tf);

            tv_display_title.setTypeface(tf);
            tv_display_des.setTypeface(tf);
            tv_display_score.setTypeface(tf);
            tv_display_price.setTypeface(tf);
            tv_display_recommend_menu.setTypeface(tf);
            tv_display_upload_image.setTypeface(tf);

            btnSend.setTypeface(tf);

            btnSend.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    title = edtTitle.getText().toString();
                    des = edtDes.getText().toString();

                    Log.i("Ratestar",ratestar.toString());
                    Log.i("RatePrice",ratePrice.toString());

                    if(ratestar.isNaN()){
                        score = 1;
                    }else{
                        score = ratestar.intValue();
                    }

                    if(ratePrice.isNaN()){
                        price = 1;
                    }else{
                        price = ratePrice.intValue();
                    }

                    if (title.equals("")) {
                        Toast.makeText(WriteReviewsActivity.this, getResources().getString(R.string.title_null), Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(des.equals("")){
                        Toast.makeText(WriteReviewsActivity.this, getResources().getString(R.string.description_null), Toast.LENGTH_LONG).show();
                        return;
                    }else {

                        callSyncSaveReviewData(URL_write_reviews, access_token , access_user_key, shop, title, des, String.valueOf(score), String.valueOf(price));
                        Log.i("Write Reviews : ","Success");
                    }
                }

            });

            //Upload image
            iv_upload_image = (ImageView)findViewById(R.id.iv_upload_image);
            iv_upload_image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    showFileChooser();

                }

            });

            categoery = new ArrayList<WriteReviewsMenuDataDao>();
            writereviews_recycler_view = (RecyclerView) findViewById(R.id.writereviews_recycler_view);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
            layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            writereviews_recycler_view.setLayoutManager(layoutManager1);
            writereviews_recycler_view.setItemAnimator(new DefaultItemAnimator());
            writereviews_recycler_view.setHasFixedSize(true);
            writereviews_recycler_view.setNestedScrollingEnabled(false);

            setImage = new ArrayList<WriteReviewsSetImageDataDao>();
            image_upload_recycler_view = (RecyclerView) findViewById(R.id.image_upload_recycler_view);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
            layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            image_upload_recycler_view.setLayoutManager(layoutManager2);
            image_upload_recycler_view.setItemAnimator(new DefaultItemAnimator());
            image_upload_recycler_view.setHasFixedSize(true);
            image_upload_recycler_view.setNestedScrollingEnabled(false);

            //Call menu item
            callSyncMenuData(URL_menu_reviews, access_token , shop);
            Log.i("Menu Reviews : ","Success");

            //get star rating point
            addListenerOnRatingBar();

            //clear id_menu_recommend
            id_menu_recommend.clear();

            // create menu foods dropdown
            spnRecommend = (Spinner)findViewById(R.id.spnRecommend);
            spnRecommend.setOnItemSelectedListener(this);
        }else{
            realm.commitTransaction();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }



    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        // Check Shop list
//        realm.beginTransaction();
//        RealmResults<OrderList> shop_list = realm.where(OrderList.class).findAll();
//
//        ranks = 0;
//
//        for (int i = 0; i < shop_list.size(); i++){
//            ranks = ranks + shop_list.get(i).getAmount();
//            Log.d("Ranks : ", String.valueOf(ranks));
//        }
//        realm.commitTransaction();
//
//        if (shop_list.size() == 0 ){
//            tvOrder.setText("0");
//        }else {
//            tvOrder.setText(String.valueOf(ranks));
//        }
//
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String select_id_menu = (getIdMenu(position).toString());
        if (position == 0){
            Log.i("Input Menu :" ,"Recommend");
        }else {

            if (id_menu_recommend.contains(select_id_menu)) {
                System.out.println("Add Menu Same!");
                Log.i("Add Id Menu :", String.valueOf(id_menu_recommend));
            } else {

                id_menu_recommend.add(select_id_menu);
                Log.i("Add Id Menu :", String.valueOf(id_menu_recommend));

                categoery.add(new WriteReviewsMenuDataDao(getIdMenu(position).toString(), parent.getItemAtPosition(position).toString(), getPhotoMenu(position).toString()));
                mAdapter = new WriteReviewsMenuAdapter(WriteReviewsActivity.this, categoery);
                writereviews_recycler_view.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //get id menu recommend
    private String getIdMenu(int position){
        String id_menu = "";
        try {
            JSONObject json = json_menu.getJSONObject(position);
            id_menu = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id_menu;
    }

    //get id menu recommend
    private String getPhotoMenu(int position){
        String menu_photo = "";
        try {
            JSONObject json = json_menu.getJSONObject(position);
            menu_photo = json.getString("photo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return menu_photo;
    }

    //remove list image
    public static void removeIdMenu(String id,String check) {

        id_menu_recommend.remove(id);

        if(check.equals("false")){
            id_menu_recommend.clear();
        }

        Log.i("Remove Id Menu :", String.valueOf(id_menu_recommend));
    }


    //-----Save review data-----//
    private void callSyncSaveReviewData(final String URL_write_reviews, final String access_token , final String access_user_key, final String shop, final String title, final String des, final String score, final String price) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+URL_write_reviews.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("access_user_key", access_user_key);
                formBuilder.add("shop", shop);
                formBuilder.add("title", title);
                formBuilder.add("desc", des);
                formBuilder.add("score", score);
                formBuilder.add("price_rate", price);
                for (int i = 0; i < id_image.size(); i++) {
                    formBuilder.add("photo[]", id_image.get(i));
                }
                for (int x = 0; x < id_menu_recommend.size(); x++) {
                    formBuilder.add("recommend_menu[]", id_menu_recommend.get(x));
                }

                MediaType.parse("application/json; charset=utf-8");

                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(URL_write_reviews)
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

                                        Log.i("Result Save review" , jsonData);

                                        try {
                                            json = new JSONObject(jsonData);
                                            msg = json.getString("message");

                                            if(jsonData.toLowerCase().contains("success")){
                                                showSuccessDialog("Write Review",msg);
                                                id_image.clear();
                                                id_menu_recommend.clear();
                                            }else{
                                                showFailDialog("Write Review",msg);
                                                return;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    //-----Add menu item-----//
    private void callSyncMenuData(final  String URL_menu_reviews, final  String access_token , final  String shop) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+URL_menu_reviews.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("shop", shop);

                MediaType.parse("application/json; charset=utf-8");

                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(URL_menu_reviews)
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

                                        Log.i("Result Menu", jsonData);

                                        showData(jsonData);

                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    //-----Upload image review-----//
    private void callSyncUploadDataClass(final String URL_reviews_upload_image, final String access_token , final String access_user_key ,final String shop , final String captionImage , final String path , final String return_json) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                imgProgress.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+URL_reviews_upload_image.toString());

                MultipartBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("photo", "logo-square.png",
                                RequestBody.create(MEDIA_TYPE_PNG, new File(path)))
                        .addFormDataPart("access_token", access_token)
                        .addFormDataPart("access_user_key", access_user_key)
                        .addFormDataPart("shop", shop)
                        .addFormDataPart("caption", captionImage)
                        .addFormDataPart("return_json", return_json)
                        .build();

                MediaType.parse("application/json; charset=utf-8");

                Request request = new Request.Builder()
                        .url(URL_reviews_upload_image)
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

                                        imgProgress.setVisibility(View.INVISIBLE);
                                        Log.i("Result Upload", jsonData);

                                        try {
                                            JSONObject json = new JSONObject(jsonData);
                                            JSONObject dataObject = json.getJSONObject("data");

                                            String id = String.valueOf(dataObject.getString("id"));
                                            String caption = String.valueOf(dataObject.getString("caption"));
                                            String type = String.valueOf(dataObject.getString("type"));
                                            String img = String.valueOf(dataObject.getString("img"));

                                            System.out.println("Show Data Image Upload :"+id+"/"+caption+"/"+type+"/"+img);

                                            id_image.add(id);
                                            Log.i("Add Id Image :", String.valueOf(id_image));

                                            setImage.add(new WriteReviewsSetImageDataDao(id,img));
                                            mAdapterSetImage = new WriteReviewsSetImageAdapter(WriteReviewsActivity.this,setImage);
                                            image_upload_recycler_view.setAdapter(mAdapterSetImage);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    //-----Show Success dialog box-----//
    private void showSuccessDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WriteReviewsActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), ReviewsShopActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //Show Fail dialog box//
    private void showFailDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WriteReviewsActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), ReviewsShopActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Dropdown select menu foods
    private void showData(String result){
        ArrayList<String> items = new ArrayList<>();

        try {

            json_menu = new JSONArray(result);

            items.add("---Menu Recommend---");

            for (int i = 0; i < json_menu.length(); i++) {

                JSONObject object = json_menu.getJSONObject(i);

                String name = object.getString("name");

                items.add(name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnRecommend.setAdapter(dataAdapter);
    }

    //This is the method responsible for image upload
    public void uploadMultipart() {

        //getting the actual path of the image
        path = getPath(filePath);

        //Uploading code
        try {

            callSyncUploadDataClass(URL_reviews_upload_image, access_token , access_user_key ,shop ,captionImage , path , return_json);


        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //remove list image
    public void removeListImage(String id) {

        String access_token_remove = access_token;
        String access_user_key_remove = access_user_key;
        String shop_remove = shop;

        callSyncRemoveIDImageDataClass(URL_writereview_remove_image, access_token_remove , access_user_key_remove ,shop_remove , id);

        id_image.remove(id);
        Log.i("Remove Id Image :", String.valueOf(id_image));
    }

    //-----Remove id image-----//
    private void callSyncRemoveIDImageDataClass(final String URL_writereview_remove_image, final String access_token_remove, final String access_user_key_remove, final String shop_remove, final String id) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+URL_writereview_remove_image.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token_remove);
                formBuilder.add("access_user_key", access_user_key_remove);
                formBuilder.add("shop", shop_remove);
                formBuilder.add("photo", id);

                MediaType.parse("application/json; charset=utf-8");

                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(URL_writereview_remove_image)
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

                                        Log.i("Remove Image :", jsonData);

                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    // show rate star point
    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBarPrice = (RatingBar) findViewById(R.id.ratingBarPrice);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                ratestar = rating;
                ratePrice = rating;

            }
        });
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                // get prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(WriteReviewsActivity.this);
                View promptView = layoutInflater.inflate(R.layout.photo_review_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WriteReviewsActivity.this);
                alertDialogBuilder.setView(promptView);

                alertDialogBuilder.setTitle("Photo Review");

                ImageView imgPhotoReview=(ImageView)promptView.findViewById(R.id.imgPhotoReview);
                imgPhotoReview.setImageBitmap(bitmap);
                //Edit caption image
                edtCaption=(EditText)promptView.findViewById(R.id.edtCaption);

                // Spinner element
                //spnImageType = (Spinner) promptView.findViewById(R.id.spnImageType);

                // Spinner Drop down elements
                //List<String> categories = new ArrayList<String>();
                //categories.add("--Select--");
                //categories.add("Front Store");
                //categories.add("Enviroment");
                //categories.add("Menu");
                //categories.add("Foods & Drink");
                //categories.add("Other");

                // Creating adapter for spinner
                //dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

                // Drop down layout style - list view with radio button
                //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //show image type by spinner
                //spnImageType.setAdapter(dataAdapter);

                alertDialogBuilder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        captionImage = edtCaption.getText().toString();
                        Log.i("Caption image: ",captionImage.toString());
                        uploadMultipart();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        dialog.dismiss();
                    }
                });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
