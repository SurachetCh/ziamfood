package revenue_express.ziamfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import revenue_express.ziamfood.adapter.DealAdapter;
import revenue_express.ziamfood.dao.DealDataDao;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class DealActivity extends AppCompatActivity {
    private ArrayList<DealDataDao> categoery;
    RecyclerView deal_recycler_view;
    ImageView imgBack;

    private DealAdapter mAdapter;
    private ProgressBar progressBar;
    String serverUrl= "http://10.252.2.222/ziamthai-com/index.php/api/foods_store/finder/?q=&loc=&pos=&page=2";
    String API_Key;
    String pos ;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;
    static Integer current = 1;
    static Integer total = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        sendNamePageGoogleAnalytics("DealActivity");

        serverUrl = getResources().getString(R.string.URL_deal);
        API_Key = getResources().getString(R.string.access_token);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        categoery = new ArrayList<DealDataDao>();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        deal_recycler_view = (RecyclerView) findViewById(R.id.deal_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        deal_recycler_view.setLayoutManager(layoutManager);
        deal_recycler_view.setItemAnimator(new DefaultItemAnimator());
        deal_recycler_view.setHasFixedSize(true);

        imgBack =(ImageView)findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        pos = getIntent().getStringExtra("POS");

        // request authentication with remote server
//        com.revenue_express.ziamfoods.DealActivity.AsyncDataClass asyncRequestObject = new com.revenue_express.ziamfoods.DealActivity.AsyncDataClass();
//        asyncRequestObject.execute(serverUrl, API_Key,pos);

        try {
            doGetRequest(serverUrl,current);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void doGetRequest(String url, Integer present) throws IOException{
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("api_key", API_Key);
        formBuilder.add("pos", String.valueOf(present));

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
                    public void onResponse(Call call, final Response response) throws IOException {
                        jsonData = response.body().string();
                        current = 0;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = null;
                                    JSONArray jsonArray = null;

                                    jsonObject = new JSONObject(jsonData);
                                    jsonArray = jsonObject.getJSONArray("data");
                                    int count = jsonArray.length();
                                    for (int i = 0; i < count; i++) {
                                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                        String bssh_id = jsonObject2.getString("bssh_id");
                                        String bssh_imghead = jsonObject2.getString("bssh_imghead");

                                        categoery.add(new DealDataDao(bssh_id,bssh_imghead));
                                    }

                                    mAdapter = new DealAdapter(DealActivity.this,categoery);
                                    deal_recycler_view.setAdapter(mAdapter);

                                } catch (JSONException e) {
                                    e.getMessage();
                                }
                            }
                        });

                    }
                });
    }

//    //-----Info Shop-----//
//    public class AsyncDataClass extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            HttpParams httpParameters = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
//            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
//            HttpClient httpClient = new DefaultHttpClient(httpParameters);
//            HttpPost httpPost = new HttpPost(params[0]);
//            String jsonResult = "";
//
//            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
//                nameValuePairs.add(new BasicNameValuePair("api_key", params[1]));
//                nameValuePairs.add(new BasicNameValuePair("pos", params[2]));
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                HttpResponse response = httpClient.execute(httpPost);
//                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
//                JSONObject jsonObject = new JSONObject(jsonResult);
//                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                int count = jsonArray.length();
//                for (int i = 0; i < count; i++) {
//                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                    String bssh_id = jsonObject2.getString("bssh_id");
//                    String bssh_imghead = jsonObject2.getString("bssh_imghead");
//
//                    categoery.add(new DealDataDao(bssh_id,bssh_imghead));
//                }
//
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return jsonResult;
//        }
//
//        @Override
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//            progressBar.setVisibility(View.GONE);
//            System.out.println("Resulted Value: " + result);
//
//            try {
//                mAdapter = new DealAdapter(DealActivity.this,categoery);
//                deal_recycler_view.setAdapter(mAdapter);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
////                swipeRefreshLayout.setRefreshing(false);
//        }
//    }
//
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
}

