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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import revenue_express.ziamfood.dao.InfoDataDao;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class SearchAppActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    String serverUrl,StateServerUrl;
    String access_token;
    private ListView LV_Search;
    Button btnSearch;
    String q="";
    EditText edtSearch;
    JSONArray jsonData;
    Spinner spnState;

    private ArrayList<InfoDataDao> categoery = new ArrayList<InfoDataDao>();
    RecyclerView searchshop_recycler_view;
    private ProgressBar progressBar;

    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imgBack,imgLoad;
    private SearchShopAdapter mAdapter;
    String name,state;
    Integer id;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonDataSearch;
    static Integer current = 1;
    static Integer total = 0 ;

    static String loadmoreItem = "false";
    static Integer lastItem = 0;

    static String Status = "Q";
    TextView txt_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_app);

        sendNamePageGoogleAnalytics("SearchAppActivity");

        Typeface myTypeface = Typeface.createFromAsset(SearchAppActivity.this.getAssets(),getResources().getString( R.string.FontText));

        serverUrl = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_search);
        StateServerUrl = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_search_state);
        access_token = getResources().getString(R.string.access_token);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        searchshop_recycler_view = (RecyclerView) findViewById(R.id.searchshop_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        searchshop_recycler_view.setLayoutManager(layoutManager);
        searchshop_recycler_view.setItemAnimator(new DefaultItemAnimator());
        searchshop_recycler_view.setHasFixedSize(true);

        txt_toolbar = (TextView)findViewById(R.id.txt_toolbar);
        LV_Search = (ListView)findViewById(R.id.LV_Search);
        imgBack =(ImageView)findViewById(R.id.imgBack);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        edtSearch = (EditText)findViewById(R.id.edtSearch);
        spnState = (Spinner)findViewById(R.id.spnState);

        imgLoad =(ImageView)findViewById(R.id.imgLoad);

        txt_toolbar.setTypeface(myTypeface);
        edtSearch.setTypeface(myTypeface);

        Glide.with(SearchAppActivity.this)
                .load(R.drawable.downloads_gif)
                .asGif()
                .into(imgLoad);

        imgLoad.setVisibility(View.INVISIBLE);

        callSyncGetSearch(serverUrl,q,current);

        edtSearch.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            current=1;
                            q = edtSearch.getText().toString();
                            System.out.print(q);
                            categoery.clear();
                            Status = "Q";
                            callSyncGetSearch(serverUrl,q,current);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = edtSearch.getText().toString();
                System.out.print(q);
                categoery.clear();
                Status = "Q";
                current = 1;

                    callSyncGetSearch(serverUrl,q,current);


            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        callSyncGetStete(StateServerUrl);
        spnState.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        state = (getSName(position).toString());
        name = (getState(position).toString());

        if (position == 0){
            System.out.println("Not found data");
        }
        else {

            categoery.clear();
            // request authentication with remote server
                Status = "State";
            current = 1;
                callSyncGetDataByState(serverUrl,current);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Doing the same with this method as we did with getName()
    private String getSName(int position){
        String sname="";
        try {
            JSONObject json = jsonData.getJSONObject(position);
            sname = json.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sname;
    }

    //Doing the same with this method as we did with getState()
    private String getState(int position){
        String state="";
        try {
            JSONObject json = jsonData.getJSONObject(position);
            state = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }


    private void callSyncGetSearch(final String url,final  String search_key,final  int page_data) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                imgLoad.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+url.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("q", search_key);
                formBuilder.add("page", String.valueOf(page_data));
                formBuilder.add("page_limit", "10");

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
                                imgLoad.setVisibility(View.INVISIBLE);

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
                                    jsonDataSearch = response.body().string();
                                    Log.i("Json Value: ",jsonDataSearch.toString());
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = null;
                                            JSONArray jsonArray = null;

                                            jsonObject = new JSONObject(jsonDataSearch);

                                            if(jsonDataSearch.contains("error")){
                                                loadmoreItem = "false";
                                                imgLoad.setVisibility(View.INVISIBLE);
                                                Toast.makeText(SearchAppActivity.this,"No Data",
                                                        Toast.LENGTH_SHORT).show();
                                            }else {

                                                JSONObject jsonDataObject = jsonObject.getJSONObject("data");
                                                jsonArray = jsonDataObject.getJSONArray("data");

                                                JSONObject page = jsonDataObject.getJSONObject("page");
                                                current = Integer.parseInt(page.getString("current"));
                                                total = Integer.parseInt(page.getString("total"));

                                                int count = jsonArray.length();
                                                for (int i = 0; i < count; i++) {
                                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
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

                                                    categoery.add(new InfoDataDao(id, name, phone, website, state_code, address, zipcode, map_position, img_cover, img_cover_thumb, img_logo, img_logo_thumb, distance, score_rate, price_rate));
                                                }

                                                mAdapter = new SearchShopAdapter(SearchAppActivity.this, categoery);
                                                searchshop_recycler_view.setAdapter(mAdapter);
                                                if (loadmoreItem.equals("true")) {
                                                    searchshop_recycler_view.scrollToPosition(lastItem);
                                                }
                                                loadmoreItem = "false";
                                                imgLoad.setVisibility(View.INVISIBLE);
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

    private void callSyncGetDataByState(final String url,final Integer page_data) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                imgLoad.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+url.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("state", state);
                formBuilder.add("page", String.valueOf(page_data));
                formBuilder.add("page_limit", "10");

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
                                imgLoad.setVisibility(View.INVISIBLE);

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
                                    jsonDataSearch = response.body().string();
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = null;
                                            JSONArray jsonArray = null;

                                            jsonObject = new JSONObject(jsonDataSearch);

                                            JSONObject jsonDataObject = jsonObject.getJSONObject("data");
                                            jsonArray = jsonDataObject.getJSONArray("data");

                                            JSONObject page = jsonDataObject.getJSONObject("page");
                                            current = Integer.parseInt(page.getString("current"));
                                            total = Integer.parseInt(page.getString("total"));

                                            int count = jsonArray.length();
                                            for (int i = 0; i < count; i++) {
                                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
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

                                                categoery.add(new InfoDataDao(id,name, phone, website,state_code,address,zipcode,map_position,img_cover,img_cover_thumb,img_logo,img_logo_thumb,distance,score_rate,price_rate));
                                            }

                                            mAdapter = new SearchShopAdapter(SearchAppActivity.this,categoery);
                                            searchshop_recycler_view.setAdapter(mAdapter);
                                            if (loadmoreItem.equals("true")){
                                                searchshop_recycler_view.scrollToPosition(lastItem);
                                            }
                                            loadmoreItem = "false";
                                            imgLoad.setVisibility(View.INVISIBLE);

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



//    private StringBuilder inputStreamToString(InputStream is) {
//
//        String rLine = "";
//        StringBuilder answer = new StringBuilder();
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        try {
//
//            while ((rLine = br.readLine()) != null) {
//                answer.append(rLine);
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return answer;
//    }



    private void callSyncGetStete(final String url) {
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
                                    jsonDataSearch = response.body().string();
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                            showStateData(jsonDataSearch);

                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    private void showStateData(String result) {

        ArrayList<String> items = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            jsonData = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject object = jsonData.getJSONObject(i);

                String name = String.valueOf(object.getString("name"));
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
        spnState.setAdapter(dataAdapter);
    }

    public void loaditem(String loadmore,Integer last){
        loadmoreItem = loadmore;
        if (loadmoreItem.equals("true")){
            lastItem = last;
            current = current+1;
            if (current <= total) {

                if (Status.equals("Q")){
                    callSyncGetSearch(serverUrl,q,current);
                }else if (Status.equals("State")){
                    callSyncGetDataByState(serverUrl, current);
                }

            }else {

            }
        }else {

        }
    }


    ////Adapter of Search /////
    public class SearchShopAdapter extends RecyclerView.Adapter<SearchShopAdapter.ViewHolder> {

        ArrayList<InfoDataDao> list;
        Context context;

        public SearchShopAdapter(Context context, ArrayList<InfoDataDao> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_layout, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Typeface myTypeface = Typeface.createFromAsset(SearchAppActivity.this.getAssets(),getResources().getString( R.string.FontText));
            holder.title.setTypeface(myTypeface);
            holder.address.setTypeface(myTypeface);
            holder.loc1.setTypeface(myTypeface);
            InfoDataDao m_expert = list.get(position);
//        holder.id.setText(m_expert.getBssh_id());
            holder.title.setText(m_expert.getName());
            holder.address.setText(m_expert.getAddress());
            holder.loc1.setText(m_expert.getState_code()+","+m_expert.getZipcode());

            try {
                Glide.with(context)
                        .load(m_expert.getImg_logo_thumb())
                        .placeholder(R.drawable.download)
                        .into(holder.imageView);
            } catch (Exception e) {
            }

            if (list.size() <= 10){
                if (position == list.size() - 1) {
                    String loadmore = "true";
                    loaditem(loadmore, position);
                }
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title,address,loc1;
            public ImageView imageView;
            public LinearLayout layout_card;

            public ViewHolder(View view) {
                super(view);

                title = (TextView)itemView.findViewById(R.id.txtTitle);
                address = (TextView)itemView.findViewById(R.id.txtAddress);
                loc1 = (TextView)itemView.findViewById(R.id.txtLoc1);
                imageView = (ImageView)itemView.findViewById(R.id.iv_icon);
                layout_card = (LinearLayout) view.findViewById(R.id.layout_card);

                layout_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        String id = String.valueOf(list.get(position).getId());
                        String map = String.valueOf(list.get(position).getMap_position());
                        String title = String.valueOf(list.get(position).getName());
                        Intent intent = new Intent(context, ShopActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("map", map);
                        intent.putExtra("title", title);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

}
