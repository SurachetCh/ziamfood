package revenue_express.ziamfood;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import revenue_express.ziamfood.adapter.MediaDetailAdapter;
import revenue_express.ziamfood.dao.MediaDetailDataDao;
import revenue_express.ziamfood.model.TypeMedia;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class MediaDetailActivity extends AppCompatActivity {

    RecyclerView media_detail_recycler_view;
    List<MediaDetailDataDao> MediaDetailList;
    MediaDetailAdapter MediaDetailadapter;

    JSONObject data_media_choice;

    //Get timeline
    String writer_id,writer_name,writer_photo;
    String type_social,title_social,description_social,score_social,created_date_social;
    int id_social;
    float rating;
    JSONObject jsonData;
    JSONArray media;
    private ArrayList<TypeMedia> mediaList = new ArrayList();
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        sendNamePageGoogleAnalytics("MediaDetailActivity");
        Typeface myTypeface = Typeface.createFromAsset(MediaDetailActivity.this.getAssets(),getResources().getString( R.string.FontText));
        TextView tv_toolbar = (TextView)findViewById(R.id.tv_toolbar);
        tv_toolbar.setTypeface(myTypeface);

        MediaDetailList = new ArrayList<MediaDetailDataDao>();

        Intent intent = getIntent();
        String media_data = intent.getStringExtra("media_data");

        media_detail_recycler_view = (RecyclerView) findViewById(R.id.media_detail_recycler_view);
        LinearLayoutManager layoutManager_review = new LinearLayoutManager(this);
        layoutManager_review.setOrientation(LinearLayoutManager.VERTICAL);
        media_detail_recycler_view.setLayoutManager(layoutManager_review);
        media_detail_recycler_view.setItemAnimator(new DefaultItemAnimator());
        media_detail_recycler_view.setHasFixedSize(true);

        try {
            data_media_choice = new JSONObject(media_data);
            Log.i("Data Media Json: ", data_media_choice.toString());

            //Get writer
            JSONObject post = data_media_choice.getJSONObject("post");
            id_social = post.getInt("id");
            type_social = post.getString("type");
            title_social = post.getString("title");
            if(title_social.equals("null")){
                title_social = "";
            }else{
                title_social = post.getString("title");
            }
            description_social = post.getString("highlight");
            score_social = post.getString("review_score");

            created_date_social = post.getString("created_date");

            //Get data writer
            JSONObject writer = data_media_choice.getJSONObject("writer");
            writer_id = writer.getString("id");
            writer_name = writer.getString("name");
            writer_photo = writer.getString("photo_thumb");

            media = data_media_choice.getJSONArray("media");

        } catch (JSONException e) {
            e.getMessage();
        }

        MediaDetailList.add(new MediaDetailDataDao(id_social, type_social, rating, title_social, description_social, score_social, created_date_social, writer_id, writer_name, writer_photo,media));
        Log.i("Media Detail List: ", String.valueOf(MediaDetailList));
        MediaDetailadapter = new MediaDetailAdapter(MediaDetailActivity.this, MediaDetailList,loadImage());
        media_detail_recycler_view.setItemViewCacheSize(0);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        media_detail_recycler_view.setLayoutManager(mLayoutManager);
        media_detail_recycler_view.setItemAnimator(new DefaultItemAnimator());
        media_detail_recycler_view.setAdapter(MediaDetailadapter);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private ArrayList<TypeMedia> loadImage() {

        for (int i = 0; i < media.length(); i++){
            try {
                JSONObject mediaObject = media.getJSONObject(i);
                if (mediaObject.getString("media_type").equals("video")) {
                    TypeMedia typeMedia = new TypeMedia();
                    typeMedia.setUrlImage(mediaObject.getString("img_url"));
                    typeMedia.setUrlVideo(mediaObject.getString("file_url"));
                    typeMedia.setTypeMedia("video");
                    mediaList.add(typeMedia);
                }else if(mediaObject.getString("media_type").equals("photo")) {
                    TypeMedia typeMedia = new TypeMedia();
                    typeMedia.setUrlImage(mediaObject.getString("file_url"));
                    typeMedia.setUrlVideo(null);
                    typeMedia.setTypeMedia("photo");
                    mediaList.add(typeMedia);
                }
            } catch (JSONException e) {
                e.getMessage();
            }
        }
        return mediaList;
    }
}
