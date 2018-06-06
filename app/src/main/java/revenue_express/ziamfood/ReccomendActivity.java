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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.dao.RecommendDataDao;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class ReccomendActivity extends AppCompatActivity {
    private ArrayList<RecommendDataDao> categoery;
    RecyclerView reccomend_recycler_view;
    private SwipeRefreshLayout swipeContainer;

    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imgBack,imgLoad;
    private ReccomendTestAdapter mAdapter;
    String serverUrl;
    String access_token;
    String map_position;
    String name, img_logo, address,phone,distance;

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
        setContentView(R.layout.activity_reccomend);

        sendNamePageGoogleAnalytics("ReccomendActivity");

        serverUrl = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_nearby);
        access_token = getResources().getString(R.string.access_token);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        categoery = new ArrayList<RecommendDataDao>();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        reccomend_recycler_view = (RecyclerView) findViewById(R.id.recommend_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        reccomend_recycler_view.setLayoutManager(layoutManager);
        reccomend_recycler_view.setItemAnimator(new DefaultItemAnimator());
        reccomend_recycler_view.setHasFixedSize(true);

        imgBack =(ImageView)findViewById(R.id.imgBack);
        imgLoad =(ImageView)findViewById(R.id.imgLoad);

        Glide.with(ReccomendActivity.this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .into(imgLoad);

        imgLoad.setVisibility(View.INVISIBLE);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        map_position = getIntent().getStringExtra("POS");

        // request authentication with remote server4
            callSyncGet(serverUrl,current);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                current = 1;
                total = 0 ;
                status = 1;
                categoery.clear();
                callSyncGet(serverUrl,current);
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
                formBuilder.add("page", String.valueOf(present));
                formBuilder.add("page_limit", "10");
                formBuilder.add("sort_by", "desc");

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

                                            JSONObject page = jsonDataObject.getJSONObject("page");
                                            current = Integer.parseInt(page.getString("current"));
                                            total = Integer.parseInt(page.getString("total"));


                                            int count = jsonArrayData.length();
                                            for (int i = 0; i < count; i++) {
                                                JSONObject jsonObject2 = jsonArrayData.getJSONObject(i);
                                                Integer id = Integer.parseInt(jsonObject2.getString("id"));
                                                String name = jsonObject2.getString("name");
                                                String phone = jsonObject2.getString("phone");
                                                String website = jsonObject2.getString("website");
                                                String state_code = jsonObject2.getString("state_code");
                                                String address = jsonObject2.getString("address");
                                                String zipcode = jsonObject2.getString("zipcode");
                                                String map_position = jsonObject2.getString("map_position");
                                                String img_cover = jsonObject2.getString("img_cover");
                                                String img_cover_thumb = jsonObject2.getString("img_cover_thumb");
                                                String img_logo = jsonObject2.getString("img_logo");
                                                String img_logo_thumb = jsonObject2.getString("img_logo_thumb");
                                                Double distance = Double.parseDouble(jsonObject2.getString("distance"));
                                                Double score_rate = Double.parseDouble(jsonObject2.getString("score_rate"));
                                                Double price_rate = Double.parseDouble(jsonObject2.getString("price_rate"));

                                                categoery.add(new RecommendDataDao(id,name, phone, website,state_code,address,zipcode,map_position,img_cover,img_cover_thumb,img_logo,img_logo_thumb,distance,score_rate,price_rate));
                                            }

                                            mAdapter = new ReccomendTestAdapter(ReccomendActivity.this,categoery);
                                            reccomend_recycler_view.setAdapter(mAdapter);
                                            if (loadmoreItem.equals("true")){
                                                reccomend_recycler_view.scrollToPosition(lastItem);
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

                callSyncGet(serverUrl, current);

            }else {

            }
        }else {

        }
    }

    public class ReccomendTestAdapter extends RecyclerView.Adapter<ReccomendTestAdapter.ViewHolder> {

        ArrayList<RecommendDataDao> list;
        Context context;

        public ReccomendTestAdapter(Context context, ArrayList<RecommendDataDao> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reccomend_card_layout, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final RecommendDataDao mDataset = list.get(position);
            holder.txtTitle.setText(mDataset.getName());
            holder.txtAddress.setText(mDataset.getAddress());
            holder.txtTel.setText(mDataset.getPhone());

            holder.layout_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(mDataset.getId());
                    String map = String.valueOf(mDataset.getMap_position());
                    String title = String.valueOf(mDataset.getName());
                    Intent intent = new Intent(context, ShopActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("map", map);
                    intent.putExtra("title", title);
                    v.getContext().startActivity(intent);
                }
            });

            try {

                Glide.with(context)
                        .load(mDataset.getImg_logo())
                        .placeholder(R.drawable.download)
                        .into(holder.iv_icon);
            } catch (Exception e) {
            }

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
            public TextView txtTitle,txtAddress,txtTel;
            public ImageView iv_icon;
            public RelativeLayout layout_card;

            public ViewHolder(View view) {
                super(view);

                txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
                txtAddress = (TextView)itemView.findViewById(R.id.txtAddress);
                txtTel = (TextView)itemView.findViewById(R.id.txtTel);
                iv_icon = (ImageView)itemView.findViewById(R.id.iv_icon);
                layout_card = (RelativeLayout)itemView.findViewById(R.id.layout_card);
            }
        }
    }
}
