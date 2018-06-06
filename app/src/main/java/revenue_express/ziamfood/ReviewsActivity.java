package revenue_express.ziamfood;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import revenue_express.ziamfood.model.User;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class ReviewsActivity extends AppCompatActivity {
    RecyclerView reviews_recycler_view;
    ImageView imgBack,imgLoad;
    private ArrayList<ReviewDataDao> MainLastReviewDataDaoArrayList;
    private ArrayList<String> photo_img = new ArrayList<String>();
    private ReviewsAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;

    String serverUrl;
    String access_token,access_user_key,shopname;
    Realm realm;

    //Get review
    String post_id,post_type,post_ref_id,post_title,post_stick_top,post_highlight,review_score,review_price_rate,author_name,author_url,author_domain,created_timeago,created_date,modified_date,post_img_cover,
            shop_id,shop_name,shop_address,city,state_code,rating,shop_img_logo,shop_img_logo_thumb,
            writer_id,writer_name,writer_photo,writer_photo_thumb;

    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    JSONArray media = null;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;
    static Integer current = 1;
    static Integer total = 0 ;

    static String loadmoreItem = "false";
    static Integer lastItem = 0;
    private Integer status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        sendNamePageGoogleAnalytics("ReviewsActivity");

        serverUrl = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_timeline);
        access_token = getResources().getString(R.string.access_token);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        realm.beginTransaction();
        final RealmResults<User> user_all = realm.where(User.class).findAll();
        if(user_all.size() == 0 ){
            access_user_key = "";
        }else{

            access_user_key = user_all.get(0).getAccess_user_key();
        }
        realm.commitTransaction();

        imgBack =(ImageView)findViewById(R.id.imgBack);
        imgLoad =(ImageView)findViewById(R.id.imgLoad);

        Glide.with(ReviewsActivity.this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(imgLoad);

        imgLoad.setVisibility(View.INVISIBLE);

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

        // request authentication with remote server4
        callSyncGet(serverUrl);

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
                callSyncGet(serverUrl);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void callSyncGet(final String url) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
                formBuilder.add("page", String.valueOf(current));
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

                                            mAdapter = new ReviewsAdapter(ReviewsActivity.this,MainLastReviewDataDaoArrayList);
                                            reviews_recycler_view.setItemViewCacheSize(0);
                                            reviews_recycler_view.setAdapter(mAdapter);

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

                callSyncGet(serverUrl);

            }else {

            }
        }else {

        }
    }

    public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

        Context context;
        public String title,desc,username;
        //        private ReviewsShopAdapter.MyClickListener mCallback;
        private List<ReviewDataDao> mDataset;
//        private ArrayList<String> imageList = new ArrayList<>();
//        private ArrayList<String> imageCaption = new ArrayList<>();

        public ReviewsAdapter(Context context, ArrayList<ReviewDataDao> list) {
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
