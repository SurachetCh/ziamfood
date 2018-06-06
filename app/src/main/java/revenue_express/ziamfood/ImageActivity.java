package revenue_express.ziamfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class ImageActivity extends AppCompatActivity {
    String img;
    ImageView imgImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        sendNamePageGoogleAnalytics("ImageActivity");

        Intent intent = getIntent();

        imgImage = (ImageView)findViewById(R.id.img);
        img = intent.getStringExtra("img");

        Glide.with(ImageActivity.this).load(img).into(imgImage);
    }
}
