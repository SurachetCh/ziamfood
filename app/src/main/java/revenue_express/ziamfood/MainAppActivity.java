package revenue_express.ziamfood;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
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
import revenue_express.ziamfood.adapter.LastReviewAdapter;
import revenue_express.ziamfood.dao.ReviewDataDao;
import revenue_express.ziamfood.model.BlogTypeMedia;
import revenue_express.ziamfood.model.User;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class MainAppActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener ,GoogleApiClient.OnConnectionFailedListener

{

    private static final String TAG = MainAppActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    CallbackManager callbackManager;
    String text = "Log In";
    CircleImageView iv_user;
    TextView tv_user_name,tv_user_email,tv_login,tv_total_rec,tv_total_new_shop,txt_toolbar;
    public static GoogleApiClient mGoogleApiClient;


    //Get review
    String post_id,post_type,post_ref_id,post_title,post_stick_top,post_highlight,review_score,review_price_rate,author_name,author_url,author_domain,created_timeago,created_date,modified_date,post_img_cover,
            shop_id,shop_name,shop_address,city,state_code,rating,shop_img_logo,shop_img_logo_thumb,
            writer_id,writer_name,writer_photo,writer_photo_thumb;

    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    JSONArray media = null;
//    String video_last = null;
    JSONObject jsnobject;
    JSONObject jsonDataShop;

    private ArrayList<ReviewDataDao> MainLastReviewDataDaoArrayList;
    String display_name = "User Name";
    String email = "Email";
    RecyclerView last_review_recycler_view;
    private SwipeRefreshLayout swipeContainer;
    private LastReviewAdapter mAdapter;
	
	String picture,bundleStr;
    Button btnDeal,btnNearBy;

    String URL_timeline;
    String access_token;

	ArrayList<String> actorsList = new ArrayList<String>();

    ViewFlipper simpleViewFlipper;
 	protected LocationManager locationManager;
    protected Context context;
    protected String latitude, longitude;
    ImageView btnSearch,imgLoad;
    Realm realm;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;
    static Integer current = 1;
    static Integer total = 0 ;


    private ArrayList<BlogTypeMedia> mediaList = new ArrayList();
    private JSONObject post;
    private JSONObject writer;
    private JSONObject shop;

    static String loadmoreItem = "false";
    static Integer lastItem = 0;
    private Integer status = 0;

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    LinearLayout title_main;
    ViewFlipper viewFlipper;
    ImageView img_category1,img_category2,img_category3,img_category4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);


        sendNamePageGoogleAnalytics("MainAppActivity");

        checkinternetconnect();

        Typeface myTypeface = Typeface.createFromAsset(MainAppActivity.this.getAssets(),getResources().getString( R.string.FontText));

        URL_timeline =  getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_timeline);
        access_token = getResources().getString(R.string.access_token);

        MainLastReviewDataDaoArrayList = new ArrayList<ReviewDataDao>();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        debugHashKey();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        //////Header Imageslide /////////

        title_main = (LinearLayout)findViewById(R.id.title_main);
        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        img_category1 = (ImageView)findViewById(R.id.img_category1);
        img_category2 = (ImageView)findViewById(R.id.img_category2);
        img_category3 = (ImageView)findViewById(R.id.img_category3);
        img_category4 = (ImageView)findViewById(R.id.img_category4);

        ArrayList<String> imgslide = new ArrayList<String>(5);
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H01.jpg");
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H02.jpg");
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H03.jpg");
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H04.jpg");
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H05.jpg");

        for(int i=0;i<imgslide.size();i++){
            Log.i("Set Flipper Called", imgslide.get(i).toString()+"");
            ImageView imageviewlide = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(imgslide.get(i).toString()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(imageviewlide);
            viewFlipper.addView(imageviewlide);
        }

        ////// End Header Imageslide /////////

        //start detail user of menu bar
        //start detail user of menu bar
        iv_user = (CircleImageView)header.findViewById(R.id.imgUser);
        tv_user_name = (TextView)header.findViewById(R.id.tv_user_name);
        tv_user_email = (TextView)header.findViewById(R.id.tv_user_email);
        tv_login = (TextView)header.findViewById(R.id.tv_login);

        txt_toolbar = (TextView)findViewById(R.id.txt_toolbar);
//        tv_total_rec = (TextView) findViewById(R.id.tv_total_rec);
//        tv_total_new_shop = (TextView)findViewById(R.id.tv_total_new_shop);
        btnDeal  =(Button)findViewById(R.id.btnDeal);
        btnNearBy = (Button)findViewById(R.id.btnNearBy);
        btnSearch =(ImageView)findViewById(R.id.imgShope) ;

        tv_user_name.setTypeface(myTypeface);
        tv_user_email.setTypeface(myTypeface);
        tv_login.setTypeface(myTypeface);
        btnDeal.setTypeface(myTypeface);
        btnNearBy.setTypeface(myTypeface);
        txt_toolbar.setTypeface(myTypeface);

        addListenerOnButton();
        last_review_recycler_view = (RecyclerView) findViewById(R.id.last_review_recycler_view);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        imgLoad =(ImageView)findViewById(R.id.imgLoad);

        Glide.with(MainAppActivity.this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .into(imgLoad);

        imgLoad.setVisibility(View.INVISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        last_review_recycler_view.setLayoutManager(layoutManager);
        last_review_recycler_view.setItemAnimator(new DefaultItemAnimator());
        last_review_recycler_view.setHasFixedSize(true);

        realm.beginTransaction();
        RealmResults<User> result = realm.where(User.class).findAll();
        int a = result.size();

        if (a != 0){
            display_name = result.get(0).getName();
            email = result.get(0).getEmail();
            Glide.with(this)
                    .load(result.get(0).getPhoto_thumb())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_user);
            text = getResources().getString(R.string.logout);
        }else {
            display_name = getResources().getString(R.string.username);
            email = getResources().getString(R.string.email);
            text = getResources().getString(R.string.login);
            Glide.with(this)
                    .load(R.drawable.male)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_user);
        }

        realm.commitTransaction();
        tv_login.setText(text);
        tv_user_name.setText(display_name);
        tv_user_email.setText(email);
        //end detail user of menu bar

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        ////end detail user of menu bar

        // handler to set duration and to upate animation
//        final Handler mHandler = new Handler();
//
//        // Create runnable for posting
//        final Runnable mUpdateResults = new Runnable() {
//            public void run() {
//                AnimateandSlideShow();
//            }
//        };
//
//        int delay = 500;
//        int period = 4000;
//
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//
//            public void run() {
//                mHandler.post(mUpdateResults);
//            }
//        }, delay, period);

        // get The references of ViewFlipper
//        simpleViewFlipper = (ViewFlipper) findViewById(R.id.simpleViewFlipper); // get the reference of ViewFlipper

        // request authentication with remote ziamfoods app(lastshop)
            callSyncGet(URL_timeline);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                current = 1;
                total = 0 ;
                status = 1;
                MainLastReviewDataDaoArrayList.clear();
                callSyncGet(URL_timeline);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // get location //
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
//    }

    public void addListenerOnButton() {


        btnNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NearByActivity.class);
                intent.putExtra("POS",latitude+","+longitude);
                startActivity(intent);
            }
        });
		

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchAppActivity.class);
                startActivity(intent);
            }
        });


        btnDeal .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), DealActivity.class);
//                startActivity(intent);
                showUnderConstructionDialog();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.equals(getResources().getString(R.string.logout))){

                    FacebookSdk.sdkInitialize(getApplicationContext());
                    // Logout facebook
                    LoginManager.getInstance().logOut();
                    //Logout google
                    logoutUser();
                }else {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                }
            }
        });

        iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

    }


    private void callSyncGet(final String url) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                imgLoad.setVisibility(View.VISIBLE);
                if(status == 1 ){
                    imgLoad.setVisibility(View.INVISIBLE);
                }else {
                    imgLoad.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+url.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("limit", "10");
                formBuilder.add("posts_type", "review");

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
                                            if(jsonData.contains("{\"error\":true,") ){
//                                                Log.i("Response: error", jsonData.toString());
                                                title_main.setVisibility(View.VISIBLE);
                                                Toast.makeText(MainAppActivity.this,"Not found data",Toast.LENGTH_SHORT).show();
                                            }else {

                                                jsonObject = new JSONObject(jsonData);
                                                jsonArray = jsonObject.getJSONArray("data");

                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    jsnobject = jsonArray.getJSONObject(i);
                                                    Log.d("Data social post: ", jsnobject.toString());
                                                    post = jsnobject.getJSONObject("post");
                                                    writer = jsnobject.getJSONObject("writer");
                                                    shop = jsnobject.getJSONObject("shop");

                                                    //Get data review
                                                    post_id = post.getString("id");
                                                    post_ref_id = post.getString("ref_id");
                                                    post_type = post.getString("type");
                                                    post_title = post.getString("title");
                                                    if (post_title.equals("null")) {
                                                        post_title = "";
                                                    } else {
                                                        post_title = post.getString("title");
                                                    }
                                                    post_highlight = post.getString("highlight");
                                                    review_score = post.getString("review_score");
                                                    review_price_rate = post.getString("review_price_rate");
                                                    author_name = post.getString("author_name");
                                                    author_url = post.getString("author_url");
                                                    author_domain = post.getString("author_domain");
                                                    created_timeago = post.getString("created_timeago");
                                                    created_date = post.getString("created_date");
                                                    modified_date = post.getString("modified_date");
                                                    post_img_cover = post.getString("img_cover");

                                                    //Get data shop
                                                    shop_id = shop.getString("id");
                                                    shop_name = shop.getString("name");
                                                    shop_address = shop.getString("address");
                                                    city = shop.getString("city");
                                                    state_code = shop.getString("state_code");
                                                    rating = shop.getString("rating");
                                                    shop_img_logo = shop.getString("img_logo");
                                                    shop_img_logo_thumb = shop.getString("img_logo_thumb");

                                                    //Get data writer
                                                    writer_id = writer.getString("id");
                                                    writer_name = writer.getString("name");
                                                    writer_photo = writer.getString("photo_thumb");

                                                    MainLastReviewDataDaoArrayList.add(new ReviewDataDao(post_id, post_type, post_ref_id, post_title, post_stick_top, post_highlight, review_score, review_price_rate, author_name, author_url, author_domain, created_timeago, created_date, modified_date, post_img_cover,
                                                            shop_id, shop_name, shop_address, city, state_code, rating, shop_img_logo, shop_img_logo_thumb,
                                                            writer_id, writer_name, writer_photo, writer_photo_thumb));
                                                }

                                                mAdapter = new LastReviewAdapter(MainAppActivity.this, MainLastReviewDataDaoArrayList, loadReview());
                                                last_review_recycler_view.setItemViewCacheSize(0);
                                                last_review_recycler_view.setAdapter(mAdapter);

                                                if (loadmoreItem.equals("true")) {
                                                    last_review_recycler_view.scrollToPosition(lastItem);
                                                }
                                            }


                                                loadmoreItem = "false";
                                                imgLoad.setVisibility(View.INVISIBLE);
                                                swipeContainer.setRefreshing(false);
                                                status = 0;


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

    public void loaditem(String loadmore,Integer last){
        loadmoreItem = loadmore;
        if (loadmoreItem.equals("true")){
            lastItem = last;
            current = current+1;
            if (current <= total) {

                callSyncGet(URL_timeline);

            }else {

            }
        }else {

        }
    }
    //-----End Slide Show last shop-----//

    private ArrayList<BlogTypeMedia> loadReview() {

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                jsnobject = jsonArray.getJSONObject(i);
                Log.d("Data social post: ",jsnobject.toString());
                post = jsnobject.getJSONObject("post");
                writer = jsnobject.getJSONObject("writer");
                shop = jsnobject.getJSONObject("shop");

                BlogTypeMedia typeMedia = new BlogTypeMedia();
                typeMedia.setPhoto_thumb(writer.getString("photo_thumb"));
                typeMedia.setWriter_name(writer.getString("name"));

                typeMedia.setPost_id(post.getString("id"));
                typeMedia.setRef_id(post.getString("ref_id"));
                typeMedia.setCreated_timeago(post.getString("created_timeago"));
                typeMedia.setTitle(post.getString("title"));
                typeMedia.setPost_highlight(post.getString("highlight"));
                typeMedia.setReview_score(post.getInt("review_score"));

                typeMedia.setShop_name(shop.getString("name"));

                mediaList.add(typeMedia);
            } catch (JSONException e) {
                e.getMessage();
            }
        }
        return mediaList;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_review) {
            Intent intent = new Intent(getApplicationContext(), ReviewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_deal) {
//            Intent intent = new Intent(getApplicationContext(), DealActivity.class);
//            startActivity(intent);
            showUnderConstructionDialog();
        } else if (id == R.id.nav_nearby) {
            Intent intent = new Intent(getApplicationContext(), NearByActivity.class);
            intent.putExtra("POS",latitude+","+longitude);
            startActivity(intent);
        } else if (id == R.id.nav_newsfeed) {
            Intent intent = new Intent(getApplicationContext(), NewsFeedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_blog) {
            Intent intent = new Intent(getApplicationContext(), BlogActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_posts) {
            Intent intent = new Intent(getApplicationContext(), PostActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            showUnderConstructionDialog();
        } else if (id ==  R.id.nav_profile) {
            realm.beginTransaction();
            RealmResults<User> user = realm.where(User.class).findAll();
            if(user.size() != 0 ) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(MainAppActivity.this,getResources().getString(R.string.user_profile_login_before),Toast.LENGTH_SHORT).show();
            }
            realm.commitTransaction();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /// get location ///
    @Override
    public void onLocationChanged(Location location) {
        latitude = new DecimalFormat("0.00000").format(location.getLatitude());
        longitude = new DecimalFormat("0.00000").format(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    private void debugHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "revenue_express.ziamfood",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void logoutUser() {
        // Logout google
        realm.beginTransaction();
        RealmResults<User> result = realm.where(User.class).findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
        signOut();
    }

    //-----Login user Google+-----//
    public  void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public
                    void onResult(Status status) {

                        if (mGoogleApiClient.isConnected()) {
                            mGoogleApiClient.disconnect();
                            mGoogleApiClient.connect();

                            System.err.println("LOG OUT GOOGLE SUCCESS");

//                            Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "Status login of google: Success");

            String personName = acct.getDisplayName();


            String personPhotoUrl = String.valueOf(acct.getPhotoUrl());
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " );

            Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
            intent.putExtra("display_name", personName);
            intent.putExtra("email",email);
            intent.putExtra("photo",personPhotoUrl);
        }else{
            Log.e(TAG, "Status login of google: False");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    protected void showUnderConstructionDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainAppActivity.this);
        View promptView = layoutInflater.inflate(R.layout.under_construction_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainAppActivity.this);
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

    public void checkinternetconnect(){
        //Check connection internet mobile or wifi
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            Log.d("สถานะการเชื่อมต่อ","ต่อ internet");

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            Log.d("สถานะการเชื่อมต่อ","ไม่ต่อ internet");
            checkInternet(getResources().getString(R.string.status_connect),getResources().getString(R.string.disconnection));
        }
    }
    //-----Show Success dialog box-----//
    private void checkInternet(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainAppActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
