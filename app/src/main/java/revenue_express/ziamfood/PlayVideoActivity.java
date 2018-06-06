package revenue_express.ziamfood;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;
import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class PlayVideoActivity extends AppCompatActivity {

    VideoView play_video;
    int current = 0;
    String url_video;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_video);
        play_video = (VideoView)findViewById(R.id.play_video);
        play_video.setZOrderOnTop(true);

        sendNamePageGoogleAnalytics("PlayVideoActivity");

        url_video = getIntent().getStringExtra("url_video");
        Log.i("URL Video:",url_video.toString());

        play_video.setVideoPath(url_video);
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(play_video);
        play_video.setMediaController(mediaController);

        play_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                current = play_video.getCurrentPosition();
                Log.e("current: ",current+"");
            }
        });

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            play_video.setVideoURI(Uri.parse(url_video));
            play_video.requestFocus();
            play_video.seekTo(current);
            play_video.start();

        } else {

            play_video.setVideoURI(Uri.parse(url_video));
            play_video.requestFocus();
            play_video.seekTo(current);
            play_video.start();

        }
    }
    public void onPrepared(MediaPlayer mp) {

        play_video.requestFocus();
        play_video.seekTo(position);
        play_video.start();
    }
}
