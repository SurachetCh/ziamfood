package revenue_express.ziamfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.dao.PostDataDao;

import static android.view.Gravity.CENTER;
import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class PostActivity extends AppCompatActivity {

    private String URL_blog;
    private String access_token;

    private SwipeRefreshLayout swipeContainer;

    ImageView imgBack, imgLoad;

    private static ArrayList<PostDataDao> postDataDao;
    RecyclerView post_recycler_view;
    private static PostAdapter mAdapter;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;
    static Integer current = 1;
    static Integer total = 0;

    static String loadmoreItem = "false";
    static Integer lastItem = 0;
    private Integer status = 0;

    String url_image_food,url_video_food,video_last,image_last,check_type,video_image,url_video;
    Integer ll_image_food_width,ll_image_food_height;
    int image_play_height;
    int image_play_width;

    JSONObject urlmedia;
    JSONArray media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        sendNamePageGoogleAnalytics("PostActivity");

        Typeface myTypeface = Typeface.createFromAsset(PostActivity.this.getAssets(),getResources().getString( R.string.FontText));
        TextView tv_toolbar = (TextView)findViewById(R.id.tv_toolbar);
        tv_toolbar.setTypeface(myTypeface);

        URL_blog = getResources().getString(R.string.Base_URL) + getResources().getString(R.string.URL_timeline);
        access_token = getResources().getString(R.string.access_token);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        postDataDao = new ArrayList<PostDataDao>();

        post_recycler_view = (RecyclerView) findViewById(R.id.post_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        post_recycler_view.setLayoutManager(layoutManager);
        post_recycler_view.setItemAnimator(new DefaultItemAnimator());
        post_recycler_view.setHasFixedSize(true);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgLoad = (ImageView) findViewById(R.id.imgLoad);

        Glide.with(PostActivity.this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(imgLoad);

        imgLoad.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        callSyncGet(URL_blog, current);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                current = 1;
                total = 0;
                status = 1;
                postDataDao.clear();
                callSyncGet(URL_blog, current);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void callSyncGet(final String url, final int present) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (status == 1) {
                    imgLoad.setVisibility(View.INVISIBLE);
                } else {
                    imgLoad.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:" + url.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("page", String.valueOf(current));
                formBuilder.add("page_limit", "10");
                formBuilder.add("posts_type", "posts");

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
                                    Log.i("Response:", response.toString());
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

                                                String ref_id = post.getString("ref_id");
                                                String created_timeago = post.getString("created_timeago");
                                                String writer_name = writer.getString("name");
                                                String highlight = post.getString("highlight");
                                                String writer_photo = writer.getString("photo");
                                                String shop_name = shop.getString("name");

                                                postDataDao.add(new PostDataDao(object,ref_id,created_timeago,writer_name,highlight,media,writer_photo,shop_name));

                                                Log.i("Post array: ", post.toString());
                                                Log.i("Shop array: ", shop.toString());
                                                Log.i("Writer array: ", writer.toString());

                                            }

                                            mAdapter = new PostAdapter(PostActivity.this, postDataDao);
                                            if (loadmoreItem.equals("true")) {
                                                post_recycler_view.scrollToPosition(lastItem);
                                            }
                                            loadmoreItem = "false";
                                            post_recycler_view.setAdapter(mAdapter);
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

    public void loaditem(String loadmore, Integer last) {
        loadmoreItem = loadmore;
        if (loadmoreItem.equals("true")) {
            lastItem = last;
            current = current + 1;
            if (current <= total) {

                callSyncGet(URL_blog, current);

            } else {

            }
        } else {

        }
    }


    public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

        ArrayList<PostDataDao> list;
        Context context;

        public PostAdapter(Context context, ArrayList<PostDataDao> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_post_layout, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            Typeface myTypeface = Typeface.createFromAsset(PostActivity.this.getAssets(),getResources().getString( R.string.FontText));
            holder.tvUser.setTypeface(myTypeface);
            holder.tvPostdate.setTypeface(myTypeface);
            holder.tvTitle.setTypeface(myTypeface);
            holder.tvDetail.setTypeface(myTypeface);

            ImageView iv1 = new ImageView(context);
            ImageView iv2 = new ImageView(context);
            ImageView iv3 = new ImageView(context);
            ImageView iv4 = new ImageView(context);
            ImageView iv5 = new ImageView(context);

            LinearLayout layout_iv1 = new LinearLayout(context);
            LinearLayout layout_iv2 = new LinearLayout(context);
            LinearLayout layout_iv3 = new LinearLayout(context);
            LinearLayout layout_iv4 = new LinearLayout(context);
            LinearLayout layout_iv5 = new LinearLayout(context);

            ImageView img_play1 = new ImageView(context);
            ImageView img_play2 = new ImageView(context);
            ImageView img_play3 = new ImageView(context);
            ImageView img_play4 = new ImageView(context);
            ImageView img_play5 = new ImageView(context);

            ImageView iv_blog = new ImageView(context);

            Button positive_symbol = new Button(context);

            final PostDataDao mDataset = list.get(position);
            holder.tvUser.setText(mDataset.getWriter_name());
            holder.tvPostdate.setText(mDataset.getCreated_timeago());
            holder.tvTitle.setText(mDataset.getShop_name());
            holder.tvDetail.setText(mDataset.getHighlight());

            Glide.with(context).load(mDataset.getWriter_photo())
                    .placeholder(R.drawable.male).into(holder.imgUser);

            holder.post_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PostDataDao m_expert = list.get(position);
                    JSONObject media_data = m_expert.getJsonObjectTimeline();

                    String id = String.valueOf(mDataset.getRef_id());
                    Intent intent = new Intent(v.getContext(), MediaDetailActivity.class);
                    intent.putExtra("media_data", media_data.toString());
                    intent.putExtra("ref_id", id);
                    v.getContext().startActivity(intent);
                }
            });

            if (position == list.size() - 1) {
                String loadmore = "true";
                loaditem(loadmore, position);
            }


            CheckSizeDisplay();

            JSONArray media = mDataset.getMedia();
            Log.i("Data Size: ", String.valueOf(media));
            Log.i("All Size: ", String.valueOf(media.length()));

            Integer sumvi = media.length();

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ll_image_food_width, ll_image_food_height);

            holder.rr_image_food.setLayoutParams(lp);
            holder.rr_image_food.removeAllViews();

            iv1.setImageResource(0);
            iv2.setImageResource(0);
            iv3.setImageResource(0);
            iv4.setImageResource(0);
            iv5.setImageResource(0);

            layout_iv1.removeAllViews();
            layout_iv2.removeAllViews();
            layout_iv3.removeAllViews();
            layout_iv4.removeAllViews();
            layout_iv5.removeAllViews();

            img_play1.setImageResource(0);
            img_play2.setImageResource(0);
            img_play3.setImageResource(0);
            img_play4.setImageResource(0);
            img_play5.setImageResource(0);

            iv_blog.setImageResource(0);

            //SetMargins(int left, int top, int right, int bottom)

            String sum_image = String.valueOf(sumvi - 4);
            positive_symbol.setText("+" + sum_image);
            positive_symbol.setTextSize(30);
            positive_symbol.setTextColor(Color.parseColor("#FFFFFF"));
            positive_symbol.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_tranparent_black));

            image_play_height = 100;
            image_play_width = 100;

            if (sumvi != 0) {
                for (int i = 0; i < sumvi; i++) {

                    try {
                        urlmedia = media.getJSONObject(i);

                        if (urlmedia.getString("media_type").equals("video")) {
                            video_last = "<html><head><style>video {width: 100%  !important;height: 100%   !important;} </style></head><body>" +
                                    " <video controls='false' poster='http://vignette2.wikia.nocookie.net/le-miiverse-resource/images/4/42/Loading.gif'>" +
                                    "<source src='" + urlmedia.getString("file_url") + "' webkit-playsinline type='video/mp4'></video>" +
                                    "</body></html>";

                            video_image = urlmedia.getString("img_url");
                            url_video = urlmedia.getString("file_url");
                        }
                        if (urlmedia.getString("media_type").equals("photo")) {
                            image_last = urlmedia.getString("file_url");
                        }
                    } catch (JSONException e) {
                        e.getMessage();
                    }

                    try {
                        check_type = urlmedia.getString("media_type");

                    } catch (JSONException e) {
                        e.getMessage();
                    }

                    url_image_food = image_last;
                    url_video_food = video_image;

//                        Log.i("url_image_food: ",String.valueOf(url_image_food));
//                        Log.i("url_video_food: ",String.valueOf(url_video_food));

                    if (sumvi == 1) {
//                            Log.i("Size = 1", String.valueOf(ll_image_food_width) + "/" + ll_image_food_height);
//                            Log.i("url_image_food: ", url_image_food);

                        if (i == 0) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width, ll_image_food_height);
                            params.setMargins(0, 0, 0, 0);
                            iv1.setLayoutParams(params);
                            layout_iv1.setLayoutParams(params);
                            layout_iv1.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play1.setLayoutParams(layoutParams);
                            img_play1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv1.addView(img_play1);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                                holder.rr_image_food.addView(layout_iv1);
                            }

//                            img_play1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });

                        }
                    }
                    /////////////////////////////
                    if (sumvi == 2) {
//                            Log.i("Size = 2", String.valueOf(ll_image_food_width) + "/" + ll_image_food_height);
//                            Log.i("url_image_food: ", url_image_food);

                        if (i == 0) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height);
                            params.setMargins(0, 0, 0, 0);
                            iv1.setLayoutParams(params);
                            layout_iv1.setLayoutParams(params);
                            layout_iv1.setGravity(CENTER);

                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(image_play_width, image_play_height);
                            img_play1.setLayoutParams(layoutParams);
                            img_play1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv1.addView(img_play1);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                                holder.rr_image_food.addView(layout_iv1);
                            }
//                            img_play1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 1) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height);
                            params.setMargins(ll_image_food_width / 2, 0, 0, 0);
                            iv2.setLayoutParams(params);
                            layout_iv2.setLayoutParams(params);
                            layout_iv2.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play2.setLayoutParams(layoutParams);
                            img_play2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv2.addView(img_play2);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                                holder.rr_image_food.addView(layout_iv2);
                            }
//                            img_play2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                    }
                    /////////////////////////////
                    if (sumvi == 3) {
//                            Log.i("Size = 3", String.valueOf(ll_image_food_width) + "/" + ll_image_food_height);
//                            Log.i("url_image_food: ", url_image_food);

                        if (i == 0) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height);
                            params.setMargins(0, 0, 0, 0);
                            iv1.setLayoutParams(params);
                            layout_iv1.setLayoutParams(params);
                            layout_iv1.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play1.setLayoutParams(layoutParams);
                            img_play1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv1.addView(img_play1);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                                holder.rr_image_food.addView(layout_iv1);
                            }
//                            img_play1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 1) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height / 2);
                            params.setMargins(ll_image_food_width / 2, 0, 0, 0);
                            iv2.setLayoutParams(params);
                            layout_iv2.setLayoutParams(params);
                            layout_iv2.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play2.setLayoutParams(layoutParams);
                            img_play2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv2.addView(img_play2);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                                holder.rr_image_food.addView(layout_iv2);
                            }
//                            img_play2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 2) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height / 2);
                            params.setMargins(ll_image_food_width / 2, ll_image_food_height / 2, 0, 0);
                            iv3.setLayoutParams(params);
                            layout_iv3.setLayoutParams(params);
                            layout_iv3.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play3.setLayoutParams(layoutParams);
                            img_play3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv3.addView(img_play3);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv3);
                                holder.rr_image_food.addView(iv3);

                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv3);
                                holder.rr_image_food.addView(iv3);
                                holder.rr_image_food.addView(layout_iv3);
                            }
//                            img_play3.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                    }
                    /////////////////////////////
                    if (sumvi == 4) {
//                            Log.i("Size = 4", String.valueOf(ll_image_food_width) + "/" + ll_image_food_height);
//                            Log.i("url_image_food: ", url_image_food);

                        if (i == 0) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height / 2);
                            params.setMargins(0, 0, 0, 0);
                            iv1.setLayoutParams(params);
                            layout_iv1.setLayoutParams(params);
                            layout_iv1.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play1.setLayoutParams(layoutParams);
                            img_play1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv1.addView(img_play1);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                                holder.rr_image_food.addView(layout_iv1);
                            }
//                            img_play1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 1) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height / 2);
                            params.setMargins(ll_image_food_width / 2, 0, 0, 0);
                            iv2.setLayoutParams(params);
                            layout_iv2.setLayoutParams(params);
                            layout_iv2.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play2.setLayoutParams(layoutParams);
                            img_play2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv2.addView(img_play2);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                                holder.rr_image_food.addView(layout_iv2);
                            }
//                            img_play2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 2) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height / 2);
                            params.setMargins(0, ll_image_food_height / 2, 0, 0);
                            iv3.setLayoutParams(params);
                            layout_iv3.setLayoutParams(params);
                            layout_iv3.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play3.setLayoutParams(layoutParams);
                            img_play3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv3.addView(img_play3);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv3);
                                holder.rr_image_food.addView(iv3);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv3);
                                holder.rr_image_food.addView(iv3);
                                holder.rr_image_food.addView(layout_iv3);
                            }
//                            img_play3.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 3) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height / 2);
                            params.setMargins(ll_image_food_width / 2, ll_image_food_height / 2, 0, 0);
                            iv4.setLayoutParams(params);
                            layout_iv4.setLayoutParams(params);
                            layout_iv4.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play4.setLayoutParams(layoutParams);
                            img_play4.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv4.addView(img_play4);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv4);
                                holder.rr_image_food.addView(iv4);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv4);
                                holder.rr_image_food.addView(iv4);
                                holder.rr_image_food.addView(layout_iv4);
                            }
//                            img_play4.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                    }
                    /////////////////////////////
                    if (sumvi == 5) {
//                            Log.i("Size = 5", String.valueOf(ll_image_food_width) + "/" + ll_image_food_height);
//                            Log.i("url_image_food: ", url_image_food);

                        if (i == 0) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height/2);
                            params.setMargins(0, 0, 0, 0);
                            iv1.setLayoutParams(params);
                            layout_iv1.setLayoutParams(params);
                            layout_iv1.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play1.setLayoutParams(layoutParams);
                            img_play1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv1.addView(img_play1);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                                holder.rr_image_food.addView(layout_iv1);
                            }
//                            img_play1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 1) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height / 2);
                            params.setMargins(ll_image_food_width / 2, 0, 0, 0);
                            iv2.setLayoutParams(params);
                            layout_iv2.setLayoutParams(params);
                            layout_iv2.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play2.setLayoutParams(layoutParams);
                            img_play2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv2.addView(img_play2);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                                holder.rr_image_food.addView(layout_iv2);
                            }
//                            img_play2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 2) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 3, ll_image_food_height / 2);
                            params.setMargins(0, ll_image_food_height / 2, 0, 0);
                            iv3.setLayoutParams(params);
                            layout_iv3.setLayoutParams(params);
                            layout_iv3.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play3.setLayoutParams(layoutParams);
                            img_play3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv3.addView(img_play3);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv3);
                                holder.rr_image_food.addView(iv3);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv3);
                                holder.rr_image_food.addView(iv3);
                                holder.rr_image_food.addView(layout_iv3);
                            }
//                            img_play3.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 3) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 3, ll_image_food_height / 2);
                            params.setMargins(ll_image_food_width / 3, ll_image_food_height / 2, 0, 0);
                            iv4.setLayoutParams(params);
                            layout_iv4.setLayoutParams(params);
                            layout_iv4.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play4.setLayoutParams(layoutParams);
                            img_play4.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv4.addView(img_play4);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv4);
                                holder.rr_image_food.addView(iv4);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv4);
                                holder.rr_image_food.addView(iv4);
                                holder.rr_image_food.addView(layout_iv4);
                            }
//                            img_play4.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 4) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 3, ll_image_food_height / 2);
                            params.setMargins((ll_image_food_width * 2) / 3, ll_image_food_height / 2, 0, 0);
                            iv5.setLayoutParams(params);
                            layout_iv5.setLayoutParams(params);
                            layout_iv5.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play5.setLayoutParams(layoutParams);
                            img_play5.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv5.addView(img_play5);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv5);
                                holder.rr_image_food.addView(iv5);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv5);
                                holder.rr_image_food.addView(iv5);
                                holder.rr_image_food.addView(layout_iv5);
                            }
//                            img_play5.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                    }

                    ///////////////////////////
                    if (sumvi > 5) {
//                            Log.i("Size > 5", String.valueOf(ll_image_food_width) + "/" + String.valueOf(ll_image_food_height));
//                            Log.i("url_image_food: ", url_image_food);

                        if (i == 0) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height/2);
                            params.setMargins(0, 0, 0, 0);
                            iv1.setLayoutParams(params);
                            layout_iv1.setLayoutParams(params);
                            layout_iv1.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play1.setLayoutParams(layoutParams);
                            img_play1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv1.addView(img_play1);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv1);
                                holder.rr_image_food.addView(iv1);
                                holder.rr_image_food.addView(layout_iv1);
                            }
//                            img_play1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 1) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 2, ll_image_food_height / 2);
                            params.setMargins(ll_image_food_width / 2, 0, 0, 0);
                            iv2.setLayoutParams(params);
                            layout_iv2.setLayoutParams(params);
                            layout_iv2.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play2.setLayoutParams(layoutParams);
                            img_play2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv2.addView(img_play2);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv2);
                                holder.rr_image_food.addView(iv2);
                                holder.rr_image_food.addView(layout_iv2);
                            }
//                            img_play2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 2) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 3, ll_image_food_height / 2);
                            params.setMargins(0, ll_image_food_height / 2, 0, 0);
                            iv3.setLayoutParams(params);
                            layout_iv3.setLayoutParams(params);
                            layout_iv3.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play3.setLayoutParams(layoutParams);
                            img_play3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv3.addView(img_play3);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv3);
                                holder.rr_image_food.addView(iv3);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv3);
                                holder.rr_image_food.addView(iv3);
                                holder.rr_image_food.addView(layout_iv3);
                            }
//                            img_play3.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 3) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 3, ll_image_food_height / 2);
                            params.setMargins(ll_image_food_width / 3, ll_image_food_height / 2, 0, 0);
                            iv4.setLayoutParams(params);
                            layout_iv4.setLayoutParams(params);
                            layout_iv4.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
                            img_play4.setLayoutParams(layoutParams);
                            img_play4.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv4.addView(img_play4);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv4);
                                holder.rr_image_food.addView(iv4);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv4);
                                holder.rr_image_food.addView(iv4);
                                holder.rr_image_food.addView(layout_iv4);
                            }
//                            img_play4.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                                    intent.putExtra("url_video", url_video);
//                                    v.getContext().startActivity(intent);
//                                }
//                            });
                        }
                        if (i == 4) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ll_image_food_width / 3, ll_image_food_height / 2);
                            params.setMargins((ll_image_food_width * 2) / 3, ll_image_food_height / 2, 0, 0);
                            iv5.setLayoutParams(params);
                            layout_iv5.setLayoutParams(params);
                            layout_iv5.setGravity(CENTER);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
//                            layoutParams.setMargins((ll_image_food_width - image_play_width) / 2, (ll_image_food_height - image_play_height) / 2, 0, 0);
                            img_play5.setLayoutParams(layoutParams);
                            img_play5.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            layout_iv5.addView(img_play5);

                            positive_symbol.setLayoutParams(params);

                            if (check_type.equals("photo")) {
                                Glide.with(context)
                                        .load(url_image_food)
                                        .centerCrop()
                                        .into(iv5);
                                holder.rr_image_food.addView(iv5);
                                holder.rr_image_food.addView(positive_symbol);
                            } else {
                                Glide.with(context)
                                        .load(url_video_food)
                                        .centerCrop()
                                        .into(iv5);
                                holder.rr_image_food.addView(iv5);
                                holder.rr_image_food.addView(layout_iv5);
                                holder.rr_image_food.addView(positive_symbol);
                            }
//                            img_play5.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                holder.rr_image_food.setVisibility(View.GONE);
            }

            holder.post_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostDataDao m_expert = list.get(position);
                    JSONObject media_data = m_expert.getJsonObjectTimeline();
                    Intent intent = new Intent(context, MediaDetailActivity.class);
                    intent.putExtra("media_data", media_data.toString());
                    Log.i("Media Data Json: ", media_data.toString());
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CircleImageView imgUser;
            public TextView tvUser, tvPostdate, tvTitle,tvDetail;
            public LinearLayout post_card;
            public RelativeLayout rr_image_food;

            public ViewHolder(View view) {
                super(view);

                tvUser = (TextView) itemView.findViewById(R.id.tvUser);
                tvPostdate = (TextView) itemView.findViewById(R.id.tvPostdate);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
                imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
                post_card = (LinearLayout) itemView.findViewById(R.id.post_card);
                rr_image_food = (RelativeLayout) itemView.findViewById(R.id.rr_image_food);
            }
        }

    }
    public void CheckSizeDisplay(){

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
//        Log.i("width-height: ", String.valueOf(width) + "-" + String.valueOf(height));

        float dp_width;
        float dp_height;

            dp_width = Math.round(width);
            dp_height = Math.round(height);
//             Log.i("Mobile width-height: ", String.valueOf(dp_width) + "-" + String.valueOf(dp_height));

            ll_image_food_width = Math.round(dp_width)-20;
            ll_image_food_height = (ll_image_food_width * 80) / 100;
//            Log.i("ll_image_food_width: ", String.valueOf(ll_image_food_width));


    }
}