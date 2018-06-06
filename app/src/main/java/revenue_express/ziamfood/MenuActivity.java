package revenue_express.ziamfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sendNamePageGoogleAnalytics("MenuActivity");
    }
}
