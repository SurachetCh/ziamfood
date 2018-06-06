package revenue_express.ziamfood;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class BlogDetailActivity extends AppCompatActivity {

    String URL_blog_detail,access_token,blog;
    TextView txt_toolbar,tvUser,tvPostdate,tv_author_name,tv_author_domain;
    ImageView imgBack;

    //Okhttp3
    private final OkHttpClient client = new OkHttpClient();
    String jsonData;
    WebView wv_blog_detail;
    JSONObject jsonObject = null;
    JSONObject data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        sendNamePageGoogleAnalytics("BlogDetailActivity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        URL_blog_detail = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_blog_detail);
        access_token = getResources().getString(R.string.access_token);

        wv_blog_detail = (WebView) findViewById(R.id.wv_blog_detail);
        txt_toolbar = (TextView) findViewById(R.id.txt_toolbar);

        tvUser = (TextView) findViewById(R.id.tvUser);
        tvPostdate = (TextView) findViewById(R.id.tvPostdate);
        tv_author_name = (TextView) findViewById(R.id.tv_author_name);
        tv_author_domain = (TextView) findViewById(R.id.tv_author_domain);

        Typeface myTypeface = Typeface.createFromAsset(BlogDetailActivity.this.getAssets(),getResources().getString( R.string.FontText));
        txt_toolbar.setTypeface(myTypeface);
        tvUser.setTypeface(myTypeface);
        tvPostdate.setTypeface(myTypeface);
        tv_author_name.setTypeface(myTypeface);
        tv_author_domain.setTypeface(myTypeface);

        blog = getIntent().getStringExtra("ref_id");
        callSyncGetBlogData(URL_blog_detail);

        imgBack =(ImageView)findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void callSyncGetBlogData(final String url) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Blog Data:"+url.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("blog", blog);

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
                                    Log.d("Data social: ",jsonData);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try{
                                            jsonObject = new JSONObject(jsonData);
                                            data = jsonObject.getJSONObject("data");

                                            String content = data.getString("content");
                                            String title = data.getString("title");

                                            String c_uname = data.getString("c_uname");
                                            String c_date = data.getString("c_date");

                                            String author_name = data.getString("author_name");
                                            String author_domain = data.getString("author_domain");

                                            txt_toolbar.setText(title);
                                            tvUser.setText(c_uname);
                                            tvPostdate.setText(c_date);
                                            tv_author_name.setText(author_name);
                                            tv_author_domain.setText(author_domain);

//                                                wv_blog_detail.setPadding(0, 0, 0, 0);
//                                                wv_blog_detail.setInitialScale(100);

                                            String content_last ="<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\"><style type=\"text/css\">img,iframe {max-width:100% !important; } img {height:auto !important;}</style></head><body>" +
                                                    "" + content + "" +
                                                    "</body></html>";

                                            WebSettings webSettings = wv_blog_detail.getSettings();
                                            webSettings.setJavaScriptEnabled(true);

                                            wv_blog_detail.getSettings().setBuiltInZoomControls(true);
                                            wv_blog_detail.getSettings().setSupportZoom(true);
                                            wv_blog_detail.loadData(content_last, "text/html; charset=utf-8", null);

                                        }catch (JSONException e){
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
