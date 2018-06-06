package revenue_express.ziamfood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.dao.ReviewDataDao;
import revenue_express.ziamfood.model.IDShop;
import revenue_express.ziamfood.model.User;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class ReviewsShopActivity extends AppCompatActivity {
    private ArrayList<ReviewDataDao> MainLastReviewDataDaoArrayList;
    private ArrayList<String> photo_img = new ArrayList<String>();
    private ReviewsShopTestAdapter mAdapter;
    private LinearLayout layout_write_review;
    private SwipeRefreshLayout swipeContainer;

//    private ArrayList<BlogTypeMedia> mediaList = new ArrayList();


    String URL_reviews;
    String access_token,access_user_key;
    String ShopID;
    RecyclerView reviews_recycler_view;
    ImageView imgBack,imgLoad;
    public static Context context;

//    String review_title,review_desc,review_score,review_create_date,writer_id,writer_name,writer_photo,review_id;
    String shopname;

    Realm realm;


    //Get review
    String post_id,post_type,post_ref_id,post_title,post_stick_top,post_highlight,review_score,review_price_rate,author_name,author_url,author_domain,created_timeago,created_date,modified_date,post_img_cover,
            shop_id,shop_name,shop_address,city,state_code,rating,shop_img_logo,shop_img_logo_thumb,
            writer_id,writer_name,writer_photo,writer_photo_thumb;

    JSONObject jsonObject = null;
    JSONArray jsonArray = null;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;
    static Integer current = 1;
    static Integer total = 0 ;

    static String loadmoreItem = "false";
    static Integer lastItem = 0;
    private Integer status = 0;

    TextView tv_toolbar;
    TextView tv_write_review;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_shop);

        sendNamePageGoogleAnalytics("ReviewsShopActivity");

        Typeface myTypeface = Typeface.createFromAsset(ReviewsShopActivity.this.getAssets(),getResources().getString( R.string.FontText));
        tv_toolbar = (TextView)findViewById(R.id.tv_toolbar);
        tv_write_review = (TextView)findViewById(R.id.tv_write_review);
        tv_toolbar.setTypeface(myTypeface);
        tv_write_review.setTypeface(myTypeface);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        Intent intent = getIntent();
        shopname = intent.getStringExtra("name");

        URL_reviews = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_timeline);
        access_token = getResources().getString(R.string.access_token);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

//        realm.beginTransaction();
//        RealmResults<User> user = realm.where(User.class).findAll();
////        access_user_key = user.get(0).getAccess_user_key();
//        RealmResults<IDShop> idshop = realm.where(IDShop.class).findAll();
//        shop = String.valueOf(idshop.get(0).getIdshop());
//        realm.commitTransaction();

        realm.beginTransaction();
        final RealmResults<User> user_all = realm.where(User.class).findAll();
        if(user_all.size() == 0 ){
            access_user_key = "";
        }else{

            access_user_key = user_all.get(0).getAccess_user_key();
        }
        RealmResults<IDShop> IDShop = realm.where(IDShop.class).findAll();
        ShopID = String.valueOf(IDShop.get(0).getIdshop());
        realm.commitTransaction();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imgBack =(ImageView)findViewById(R.id.imgBack);

        MainLastReviewDataDaoArrayList = new ArrayList<ReviewDataDao>();

        reviews_recycler_view = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        reviews_recycler_view.setLayoutManager(layoutManager);
        reviews_recycler_view.setItemAnimator(new DefaultItemAnimator());
        reviews_recycler_view.setHasFixedSize(true);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgLoad =(ImageView)findViewById(R.id.imgLoad);

        Glide.with(ReviewsShopActivity.this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .into(imgLoad);

        imgLoad.setVisibility(View.INVISIBLE);

        layout_write_review = (LinearLayout) findViewById(R.id.layout_write_review);
        layout_write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realm.beginTransaction();
                RealmResults<User> user = realm.where(User.class).findAll();
                int check = user.size();
                realm.commitTransaction();

                if (check != 0){
                    Intent intent = new Intent(getApplicationContext(), WriteReviewsActivity.class);
//                    intent.putExtra("name", shopname);
                    startActivity(intent);
                }else {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(ReviewsShopActivity.this);
                    builder.setMessage(getResources().getString(R.string.please_login_user));
                    builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }

            }
        });

        callSyncGet(URL_reviews);
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
                // request authentication with remote server4
                callSyncGet(URL_reviews);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }
    @Override
    public void onBackPressed(){
        current = 1;
        total = 0 ;
        loadmoreItem = "false";
        lastItem = 0;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume(){
        callSyncGet(URL_reviews);
        super.onResume();
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
                formBuilder.add("limit", "10");
                formBuilder.add("page", String.valueOf(current));
                formBuilder.add("shop", String.valueOf(ShopID));
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

                                            JSONObject jsonObject = null;
                                            JSONArray jsonArray = null;

                                            jsonObject = new JSONObject(jsonData);

                                            JSONObject jsonDataObject = jsonObject.getJSONObject("data");
                                            JSONArray jsonArrayData = jsonDataObject.getJSONArray("data");

                                            JSONObject jsonObjectPage = jsonDataObject.getJSONObject("page");

                                            current = jsonObjectPage.getInt("current");
                                            total = jsonObjectPage.getInt("total");

                                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                                JSONObject object = jsonArrayData.getJSONObject(i);

                                                JSONObject post = object.getJSONObject("post");
                                                JSONObject shop = object.getJSONObject("shop");
                                                JSONObject writer = object.getJSONObject("writer");
                                                JSONArray media = object.getJSONArray("media");
                                                //Get data review
                                                post_id = post.getString("id");
                                                post_ref_id = post.getString("ref_id");
                                                post_type = post.getString("type");
                                                post_title = post.getString("title");
                                                if(post_title.equals("null")){
                                                    post_title = "";
                                                }else{
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

                                                MainLastReviewDataDaoArrayList.add(new ReviewDataDao(post_id,post_type,post_ref_id,post_title,post_stick_top,post_highlight,review_score,review_price_rate,author_name,author_url,author_domain,created_timeago,created_date,modified_date,post_img_cover,
                                                        shop_id,shop_name,shop_address,city,state_code,rating,shop_img_logo,shop_img_logo_thumb,
                                                        writer_id,writer_name,writer_photo,writer_photo_thumb));
                                            }

                                            mAdapter = new ReviewsShopTestAdapter(ReviewsShopActivity.this,MainLastReviewDataDaoArrayList);
                                            if (loadmoreItem.equals("true")) {
                                                reviews_recycler_view.scrollToPosition(lastItem);
                                            }
                                            loadmoreItem = "false";
                                            reviews_recycler_view.setAdapter(mAdapter);
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

                callSyncGet(URL_reviews);

            }else {

            }
        }else {

        }
    }

    public class ReviewsShopTestAdapter extends RecyclerView.Adapter<ReviewsShopTestAdapter.ViewHolder> {

        Context context;
        public String title,desc,username;
        private List<ReviewDataDao> mDataset;

        public ReviewsShopTestAdapter(Context context, ArrayList<ReviewDataDao> list) {
            this.mDataset = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reviews_shop_card_layout, parent, false);
            return new ViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            Typeface myTypeface = Typeface.createFromAsset(ReviewsShopActivity.this.getAssets(),getResources().getString( R.string.FontText));
            holder.tvUser.setTypeface(myTypeface);
            holder.tvPostdate.setTypeface(myTypeface);
            holder.tvTitle.setTypeface(myTypeface);
            holder.tvDetail1.setTypeface(myTypeface);
            holder.tvDetail2.setTypeface(myTypeface);

            Glide.with(context).load(mDataset.get(position).getWriter_photo()).centerCrop().placeholder(R.drawable.male).into(holder.imgUser);
            holder.tvUser.setText(mDataset.get(position).getWriter_name());
            holder.tvPostdate.setText("Post date: "+mDataset.get(position).getCreated_timeago());
            holder.tvTitle.setText(mDataset.get(position).getShop_name());
            holder.tvDetail1.setText(mDataset.get(position).getPost_title());
            holder.tvDetail2.setText(mDataset.get(position).getPost_highlight());

            Integer star = Integer.valueOf(mDataset.get(position).getReview_score());
            holder.img_star1.setImageResource(0);
            holder.img_star2.setImageResource(0);
            holder.img_star3.setImageResource(0);
            holder.img_star4.setImageResource(0);
            holder.img_star5.setImageResource(0);
            if (star==0){
                holder.img_star1.setImageResource(R.drawable.star_emty);
                holder.img_star2.setImageResource(R.drawable.star_emty);
                holder.img_star3.setImageResource(R.drawable.star_emty);
                holder.img_star4.setImageResource(R.drawable.star_emty);
                holder.img_star5.setImageResource(R.drawable.star_emty);

            }else if (star==1){
                holder.img_star1.setImageResource(R.drawable.star);
                holder.img_star2.setImageResource(R.drawable.star_emty);
                holder.img_star3.setImageResource(R.drawable.star_emty);
                holder.img_star4.setImageResource(R.drawable.star_emty);
                holder.img_star5.setImageResource(R.drawable.star_emty);
            }else if (star==2){
                holder.img_star1.setImageResource(R.drawable.star);
                holder.img_star2.setImageResource(R.drawable.star);
                holder.img_star3.setImageResource(R.drawable.star_emty);
                holder.img_star4.setImageResource(R.drawable.star_emty);
                holder.img_star5.setImageResource(R.drawable.star_emty);
            }else if (star==3){
                holder.img_star1.setImageResource(R.drawable.star);
                holder.img_star2.setImageResource(R.drawable.star);
                holder.img_star3.setImageResource(R.drawable.star);
                holder.img_star4.setImageResource(R.drawable.star_emty);
                holder.img_star5.setImageResource(R.drawable.star_emty);
            }else if (star==4){
                holder.img_star1.setImageResource(R.drawable.star);
                holder.img_star2.setImageResource(R.drawable.star);
                holder.img_star3.setImageResource(R.drawable.star);
                holder.img_star4.setImageResource(R.drawable.star);
                holder.img_star5.setImageResource(R.drawable.star_emty);
            }else {
                holder.img_star1.setImageResource(R.drawable.star);
                holder.img_star2.setImageResource(R.drawable.star);
                holder.img_star3.setImageResource(R.drawable.star);
                holder.img_star4.setImageResource(R.drawable.star);
                holder.img_star5.setImageResource(R.drawable.star);
            }
            holder.ll_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReviewDetailActivity.class);
                    intent.putExtra("review_id", mDataset.get(position).getPost_ref_id());
                    v.getContext().startActivity(intent);
                }
            });

            if (mDataset.size() <= 10){
                if (position == mDataset.size() - 1) {
                    String loadmore = "true";
                    loaditem(loadmore, position);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CircleImageView imageView;
            public LinearLayout ll_review;

            public CircleImageView imgUser;
            public TextView tvUser,tvPostdate,tvTitle,tvDetail1,tvDetail2;
            public ImageView img_star1,img_star2,img_star3,img_star4,img_star5;

            public ViewHolder(View view) {
                super(view);

                //Last Review
                imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
                tvUser = (TextView) itemView.findViewById(R.id.tvUser);
                tvPostdate  = (TextView) itemView.findViewById(R.id.tvPostdate);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDetail1 = (TextView) itemView.findViewById(R.id.tvDetail1);
                tvDetail2 = (TextView) itemView.findViewById(R.id.tvDetail2);
                img_star1 = (ImageView) itemView.findViewById(R.id.img_star1);
                img_star2 = (ImageView) itemView.findViewById(R.id.img_star2);
                img_star3 = (ImageView) itemView.findViewById(R.id.img_star3);
                img_star4 = (ImageView) itemView.findViewById(R.id.img_star4);
                img_star5 = (ImageView) itemView.findViewById(R.id.img_star5);
                ll_review = (LinearLayout) itemView.findViewById(R.id.ll_review);
            }
        }
    }
}
