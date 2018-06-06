package revenue_express.ziamfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class NearByDataActivity extends AppCompatActivity {
    String id,title,address,detail,distance,imgfront,imghead,loc1,loc2,maps,phone,score,website;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_data);

        sendNamePageGoogleAnalytics("NearByDataActivity");

//        intent.putExtra("Bssh_id",dao.getData().get(position).getBssh_id());
//        intent.putExtra("Bssh_title",dao.getData().get(position).getBssh_title());
//        intent.putExtra("Bssh_address",dao.getData().get(position).getBssh_address());
//        intent.putExtra("Bssh_detail",dao.getData().get(position).getBssh_detail());
//        intent.putExtra("Bssh_distance",dao.getData().get(position).getBssh_distance());
//        intent.putExtra("Bssh_imgfront",dao.getData().get(position).getBssh_imgfront());
//        intent.putExtra("Bssh_imghead",dao.getData().get(position).getBssh_imghead());
//        intent.putExtra("Bssh_loc1",dao.getData().get(position).getBssh_loc1());
//        intent.putExtra("Bssh_loc2",dao.getData().get(position).getBssh_loc2());
//        intent.putExtra("Bssh_maps",dao.getData().get(position).getBssh_maps());
//        intent.putExtra("Bssh_phone",dao.getData().get(position).getBssh_phone());
//        intent.putExtra("Bssh_score",dao.getData().get(position).getBssh_score());
//        intent.putExtra("Bssh_website",dao.getData().get(position).getBssh_website());
        id = getIntent().getStringExtra("Bssh_id");
        title = getIntent().getStringExtra("Bssh_title");
        address = getIntent().getStringExtra("Bssh_address");
        detail = getIntent().getStringExtra("Bssh_detail");
        distance = getIntent().getStringExtra("Bssh_distance");
        imgfront = getIntent().getStringExtra("Bssh_imgfront");
        imghead = getIntent().getStringExtra("Bssh_imghead");
        loc1 = getIntent().getStringExtra("Bssh_loc1");
        loc2 = getIntent().getStringExtra("Bssh_loc2");
        maps = getIntent().getStringExtra("Bssh_maps");
        phone = getIntent().getStringExtra("Bssh_phone");
        score = getIntent().getStringExtra("Bssh_score");
        website = getIntent().getStringExtra("Bssh_website");

    }
}
