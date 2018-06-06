package revenue_express.ziamfood;

import android.content.Context;
import android.content.Intent;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.dao.BlogDataDao;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class BlogActivity extends AppCompatActivity {

    private String URL_blog;
    private String access_token;

    private SwipeRefreshLayout swipeContainer;

    ImageView imgBack,imgLoad;

    private static ArrayList<BlogDataDao> blogDataDao;
    RecyclerView blog_recycler_view;
    private static BlogAdapter mAdapter;

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
        setContentView(R.layout.activity_blog);

        sendNamePageGoogleAnalytics("BlogActivity");

        Typeface myTypeface = Typeface.createFromAsset(BlogActivity.this.getAssets(),getResources().getString( R.string.FontText));
        TextView tv_toolbar = (TextView)findViewById(R.id.tv_toolbar);
        tv_toolbar.setTypeface(myTypeface);

        URL_blog =  getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_timeline);
        access_token = getResources().getString(R.string.access_token);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        blogDataDao = new ArrayList<BlogDataDao>();

        blog_recycler_view = (RecyclerView) findViewById(R.id.blog_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        blog_recycler_view.setLayoutManager(layoutManager);
        blog_recycler_view.setItemAnimator(new DefaultItemAnimator());
        blog_recycler_view.setHasFixedSize(true);

        imgBack =(ImageView)findViewById(R.id.imgBack);
        imgLoad =(ImageView)findViewById(R.id.imgLoad);

        Glide.with(BlogActivity.this)
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

        callSyncGet(URL_blog,current);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                current = 1;
                total = 0 ;
                status = 1;
                blogDataDao.clear();
                callSyncGet(URL_blog,current);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void callSyncGet(final String url,final  int present) {
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
                formBuilder.add("page_limit", "10");
                formBuilder.add("posts_type","blog");

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

                                                String ref_id = post.getString("ref_id");
                                                String shop_name = shop.getString("name");
                                                String author_name = post.getString("author_name");
                                                String author_domain = post.getString("author_domain");
                                                String created_timeago = post.getString("created_timeago");
                                                String writer_name = writer.getString("name");
                                                String title = post.getString("title");
                                                String highlight = post.getString("highlight");
                                                String img_cover = post.getString("img_cover");
                                                String writer_photo = writer.getString("photo");

                                                blogDataDao.add(new BlogDataDao(ref_id,shop_name,author_name,author_domain,created_timeago,writer_name,title,highlight,img_cover,writer_photo));

                                                Log.i("Post array: ", post.toString());
                                                Log.i("Shop array: ", shop.toString());
                                                Log.i("Writer array: ", writer.toString());

                                            }

                                            mAdapter = new BlogAdapter(BlogActivity.this,blogDataDao);
                                            if (loadmoreItem.equals("true")){
                                                blog_recycler_view.scrollToPosition(lastItem);
                                            }
                                            loadmoreItem = "false";
                                            blog_recycler_view.setAdapter(mAdapter);
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

                callSyncGet(URL_blog, current);

            }else {

            }
        }else {

        }
    }


    public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

        ArrayList<BlogDataDao> list;
        Context context;

        public BlogAdapter(Context context, ArrayList<BlogDataDao> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_blog_layout, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            Typeface myTypeface = Typeface.createFromAsset(BlogActivity.this.getAssets(),getResources().getString( R.string.FontText));
            holder.tvUser.setTypeface(myTypeface);
            holder.tvPostdate.setTypeface(myTypeface);
            holder.tv_author_name.setTypeface(myTypeface);
            holder.tv_author_domain.setTypeface(myTypeface);
            holder.tvTitle.setTypeface(myTypeface);
            holder.tvDetail.setTypeface(myTypeface);

            final BlogDataDao mDataset = list.get(position);
            holder.tvUser.setText(mDataset.getWriter_name());
            holder.tvPostdate.setText(mDataset.getCreated_timeago());
            holder.tv_author_name.setText(mDataset.getAuthor_name());
            holder.tv_author_domain.setText(mDataset.getAuthor_domain());
            holder.tvTitle.setText(mDataset.getTitle());
            holder.tvDetail.setText(mDataset.getHighlight());

            Glide.with(context).load(mDataset.getImg_cover()).placeholder(R.drawable.download).into(holder.img_cover);

            Glide.with(context).load(mDataset.getWriter_photo()).placeholder(R.drawable.male).into(holder.imgUser);

            holder.blog_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(mDataset.getRef_id());
                    Intent intent = new Intent(v.getContext(), BlogDetailActivity.class);
                    intent.putExtra("ref_id", id);
                    v.getContext().startActivity(intent);
                }
            });

            if (position == list.size() - 1) {
                String loadmore = "true";
                loaditem(loadmore, position);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CircleImageView imgUser;
            public TextView tvUser,tvPostdate,tv_author_name,tv_author_domain,tvTitle,tvDetail;
            public ImageView img_cover;
            public LinearLayout blog_card;

            public ViewHolder(View view) {
                super(view);

                tvUser = (TextView) itemView.findViewById(R.id.tvUser);
                tvPostdate = (TextView) itemView.findViewById(R.id.tvPostdate);
                tv_author_name = (TextView) itemView.findViewById(R.id.tv_author_name);
                tv_author_domain = (TextView) itemView.findViewById(R.id.tv_author_domain);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitleBlog);
                tvDetail = (TextView) itemView.findViewById(R.id.tvDetailBlog);
                imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
                img_cover = (ImageView) itemView.findViewById(R.id.img_cover);
                blog_card = (LinearLayout) itemView.findViewById(R.id.blog_card);
            }
        }
    }
}
