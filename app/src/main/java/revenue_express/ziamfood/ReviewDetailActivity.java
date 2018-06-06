package revenue_express.ziamfood;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.adapter.RecommendMenuReviewAdapter;
import revenue_express.ziamfood.adapter.ReviewDetailAdapter;
import revenue_express.ziamfood.dao.RecommendMenuReviewDataDao;
import revenue_express.ziamfood.dao.ReviewDetailDataDao;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class ReviewDetailActivity extends AppCompatActivity {

    TextView tvRecommendMenu,tvSharePhoto,title,desc,tvUser,tvPostdate;
    CircleImageView imgUser;
    ImageView img_star1,img_star2,img_star3,img_star4,img_star5;
    String URL_reviews_show,access_token,reviewID;
    String review_title,review_desc,review_score,review_create_date,writer_id,writer_name,writer_photo;
    RecyclerView review_detail_recycler_view,recommend_menu_review_recycler_view;
    ImageView btnBack,imgSumOrder;
    RecyclerView.LayoutManager layoutManager;
    private List<ReviewDetailDataDao> reviewShow;
    private ArrayList<RecommendMenuReviewDataDao> RecommendMenuReview;
    private ReviewDetailAdapter mAdapter;
    private RecommendMenuReviewAdapter rAdapter;

    //Ok http3
    private final OkHttpClient client = new OkHttpClient();
    String jsonData;

    Integer ranks = 0;
    TextView tvOrder;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        sendNamePageGoogleAnalytics("ReviewDetailActivity");

        Typeface myTypeface = Typeface.createFromAsset(ReviewDetailActivity.this.getAssets(),getResources().getString( R.string.FontText));

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        URL_reviews_show = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_reviews_show);
        access_token = getResources().getString(R.string.access_token);

        tvUser = (TextView)findViewById(R.id.tvUser);
        tvRecommendMenu = (TextView)findViewById(R.id.tvRecommendMenu);
        tvSharePhoto = (TextView)findViewById(R.id.tvSharePhoto);
        title = (TextView)findViewById(R.id.tvTitle);
        desc = (TextView)findViewById(R.id.tvDesc);
        tvPostdate = (TextView)findViewById(R.id.tvPostdate);
        img_star1 = (ImageView)findViewById(R.id.img_star1);
        img_star2 = (ImageView)findViewById(R.id.img_star2);
        img_star3 = (ImageView)findViewById(R.id.img_star3);
        img_star4 = (ImageView)findViewById(R.id.img_star4);
        img_star5 = (ImageView)findViewById(R.id.img_star5);
        imgUser = (CircleImageView)findViewById(R.id.imgUser);

        TextView tv_toolbar = (TextView)findViewById(R.id.tv_toolbar);
        tv_toolbar.setTypeface(myTypeface);

        tvUser.setTypeface(myTypeface);
        tvSharePhoto.setTypeface(myTypeface);
        title.setTypeface(myTypeface);
        desc.setTypeface(myTypeface);
        tvPostdate.setTypeface(myTypeface);
        tvRecommendMenu.setTypeface(myTypeface);

        btnBack =(ImageView) findViewById(R.id.imgBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reviewShow = new ArrayList<ReviewDetailDataDao>();
        RecommendMenuReview = new ArrayList<RecommendMenuReviewDataDao>();

        reviewID = getIntent().getStringExtra("review_id");

        review_detail_recycler_view = (RecyclerView) findViewById(R.id.review_detail_recycler_view);
        recommend_menu_review_recycler_view = (RecyclerView) findViewById(R.id.recommend_menu_review_recycler_view);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        } else {
            layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        }

        review_detail_recycler_view.setLayoutManager(layoutManager);
        review_detail_recycler_view.setItemAnimator(new DefaultItemAnimator());
        review_detail_recycler_view.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recommend_menu_review_recycler_view.setLayoutManager(layoutManager1);
        recommend_menu_review_recycler_view.setItemAnimator(new DefaultItemAnimator());
        recommend_menu_review_recycler_view.setHasFixedSize(true);
        recommend_menu_review_recycler_view.setNestedScrollingEnabled(false);

        callSyncReviewShow(URL_reviews_show, access_token , reviewID);

//        tvOrder =(TextView)findViewById(R.id.tvOrder);
//        imgSumOrder = (ImageView)findViewById(R.id.imgSumOrder);
//        imgSumOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (tvOrder.getText().equals("0")){
//
//                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ReviewDetailActivity.this);
//                    builder.setCancelable(false);
//                    builder.setMessage(getResources().getString(R.string.text_null_order));
//                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//
//                    android.support.v7.app.AlertDialog alert = builder.create();
//                    alert.show();
//                }else {
//                    Intent intent = new Intent(getApplicationContext(), SumOrderActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public void onResume(){
        super.onResume();
        // Check Shop list
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

    }

    private void callSyncReviewShow(final String URL_reviews_show,final String access_token,final String reviewID) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url รีวิวข้อมูลทั้งหมด: "+URL_reviews_show.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("review", reviewID);

                MediaType.parse("application/json; charset=utf-8");

                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(URL_reviews_show)
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
                                            JSONObject jsonArray = null;

                                            Log.d("Data Review: ",jsonData.toString());

                                            jsonObject = new JSONObject(jsonData);
                                            jsonArray = jsonObject.getJSONObject("data");

                                            JSONObject review = jsonArray.getJSONObject("review");

                                            JSONObject writer = jsonArray.getJSONObject("writer");

                                            review_title = review.getString("title");
                                            review_desc = review.getString("description");
                                            review_score = review.getString("score");
                                            review_create_date = review.getString("created_date");

                                            writer_id = writer.getString("id");
                                            writer_name = writer.getString("name");
                                            writer_photo = writer.getString("photo_thumb");

                                            tvUser.setText(writer_name);
                                            title.setText(review_title);
                                            desc.setText(review_desc);
                                            tvPostdate.setText(review.getString("created_timeago"));

                                            Glide.with(ReviewDetailActivity.this).load(writer.getString("photo")).centerCrop().placeholder(R.drawable.male).into(imgUser);

                                            Integer star = review.getInt("score");
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

//                                            if (jsonArray.isNull("recommend_menu")){
//                                                tvRecommendMenu.setVisibility(View.GONE);
//                                            }else {
//                                                tvRecommendMenu.setVisibility(View.VISIBLE);
//                                            }
//                                            if(jsonArray.isNull("photo")){
//                                                Log.i("Photo","Photo Is Null");
//                                                tvSharePhoto.setVisibility(View.GONE);
//                                            }else{
//                                                tvSharePhoto.setVisibility(View.VISIBLE);
//                                                JSONArray photo = jsonArray.getJSONArray("photo");
//                                                if (photo.length() != 0) {
//                                                    for (int j = 0; j < photo.length(); j++) {
//                                                        JSONObject urlImage = photo.getJSONObject(j);
//                                                        String imgFoodReview = urlImage.getString("img");
//                                                        String imgCaptionReview = urlImage.getString("caption");
//
//                                                        Log.d("Image | Caption: ", imgFoodReview + "|" + imgCaptionReview);
//                                                        reviewShow.add(new ReviewDetailDataDao(imgFoodReview, imgCaptionReview));
//                                                    }
//                                                }
//                                            }
//
//                                            if(jsonArray.isNull("recommend_menu")){
//                                                Log.i("Recommend Menu","Recommend Menu Is Null");
//                                            }else{
//                                                JSONArray recommend_menu = jsonArray.getJSONArray("recommend_menu");
//                                                if (recommend_menu.length() != 0) {
//                                                    for (int j = 0; j < recommend_menu.length(); j++) {
//                                                        JSONObject recommend_menu_object = recommend_menu.getJSONObject(j);
//                                                        String recommend_menu_name = recommend_menu_object.getString("name");
//                                                        String recommend_menu_photo = recommend_menu_object.getString("photo");
//
//                                                        Log.i("Recommend Menu Name: ",recommend_menu_name.toString());
//                                                        Log.i("Recommend Menu photo: ",recommend_menu_photo.toString());
//
//
//                                                        RecommendMenuReview.add(new RecommendMenuReviewDataDao(recommend_menu_name,recommend_menu_photo));
//                                                    }
//
//                                                    rAdapter = new RecommendMenuReviewAdapter(ReviewDetailActivity.this,RecommendMenuReview);
//                                                    recommend_menu_review_recycler_view.setAdapter(rAdapter);
//                                                }
//                                            }
//
//
//                                            mAdapter = new ReviewDetailAdapter(ReviewDetailActivity.this,reviewShow);
//                                            review_detail_recycler_view.setAdapter(mAdapter);

                                            //TODO set image


                                            if(jsonArray.isNull("photo")) {
                                                tvSharePhoto.setVisibility(View.INVISIBLE);
                                                Log.i("Photo", "Photo Is Null");
                                            }else {
                                                JSONArray photo = jsonArray.getJSONArray("photo");
                                                if (photo.isNull(0)){
                                                    tvSharePhoto.setVisibility(View.INVISIBLE);
                                                    Log.i("Photo","Photo Is Null");
                                                }else {
                                                    for (int j = 0; j < photo.length(); j++) {
                                                        JSONObject urlImage = null;
                                                        try {
                                                            urlImage = photo.getJSONObject(j);
                                                            String imgFoodReview = urlImage.getString("img");
                                                            String imgCaptionReview = urlImage.getString("caption");
                                                            Log.d("Image | Caption: ", imgFoodReview + "|" + imgCaptionReview);
                                                            reviewShow.add(new ReviewDetailDataDao(imgFoodReview, imgCaptionReview));
                                                        } catch (JSONException e1) {
                                                            e1.printStackTrace();
                                                        }

                                                    }
                                                    mAdapter = new ReviewDetailAdapter(ReviewDetailActivity.this,reviewShow);
                                                    review_detail_recycler_view.setAdapter(mAdapter);
                                                }
                                            }

// TODO menu_recommend
                                            if(jsonArray.isNull("recommend_menu")) {
                                                tvRecommendMenu.setVisibility(View.INVISIBLE);
                                                Log.i("Recommend Menu","Recommend Menu Is Null");
                                            }else {
                                                JSONArray recommend_menu = jsonArray.getJSONArray("recommend_menu");
                                                if (recommend_menu.isNull(0)){
                                                    tvRecommendMenu.setVisibility(View.INVISIBLE);
                                                    Log.i("Recommend Menu","Recommend Menu Is Null");
                                                }else {
                                                    if (recommend_menu.length() != 0) {
                                                        for (int j = 0; j < recommend_menu.length(); j++) {
                                                            JSONObject recommend_menu_object = recommend_menu.getJSONObject(j);
                                                            String recommend_menu_name = recommend_menu_object.getString("name");
                                                            String recommend_menu_photo = recommend_menu_object.getString("photo");

                                                            Log.i("Recommend Menu Name: ",recommend_menu_name.toString());
                                                            Log.i("Recommend Menu photo: ",recommend_menu_photo.toString());


                                                            RecommendMenuReview.add(new RecommendMenuReviewDataDao(recommend_menu_name,recommend_menu_photo));
                                                        }

                                                        rAdapter = new RecommendMenuReviewAdapter(ReviewDetailActivity.this,RecommendMenuReview);
                                                        recommend_menu_review_recycler_view.setAdapter(rAdapter);
                                                    }
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
}
