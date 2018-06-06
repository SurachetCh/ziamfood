package revenue_express.ziamfood.manager;

import com.google.android.gms.analytics.HitBuilders;

/**
 * Created by chaleamsuk on 22/5/2560.
 */

public class googleAnalytics {

    public static void sendNamePageGoogleAnalytics(String namePage) {
        AnalyticsApplication.tracker().setScreenName(namePage+"(Android)");
        AnalyticsApplication.tracker().send(new HitBuilders.ScreenViewBuilder().build());

        AnalyticsApplication.tracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());
    }

}
