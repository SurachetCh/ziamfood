package revenue_express.ziamfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class InfoActivity extends AppCompatActivity implements OnMapReadyCallback {
     ImageView imgBack;
     TextView txt_toolbar,txtTel,txtAddress,txtDetail,txtTimeshop;
     GoogleMap mMap;
     ArrayList<String> timeshop;
     ArrayList<String> day_of_week_now;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_info);

            sendNamePageGoogleAnalytics("InfoActivity");

            Intent intent = getIntent();
            String title = intent.getStringExtra("title");
            String address = intent.getStringExtra("address");
            String phone = intent.getStringExtra("phone");
            String detail = intent.getStringExtra("detail");
            String map_position = intent.getStringExtra("map_position");

            txt_toolbar = (TextView) findViewById(R.id.txt_toolbar);
            txtAddress = (TextView) findViewById(R.id.txtAddress);
            txtTel = (TextView) findViewById(R.id.txtTel);
            txtDetail = (TextView) findViewById(R.id.txtDetail);
            imgBack =(ImageView)findViewById(R.id.imgBack);
            txtTimeshop = (TextView)findViewById(R.id.txtTimeshop);

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            txt_toolbar.setText(title);
            txtAddress.setText(address);
            txtTel.setText(phone);
            txtDetail.setText(detail);
            txtTimeshop.setText("");

            ShopActivity shop = new ShopActivity();
            day_of_week_now = shop.getDay_of_week_now();
            timeshop = shop.getTimeShop();

            for (int j = 0; j < timeshop.size(); j++){

                if(j==Integer.valueOf(day_of_week_now.get(0))) {
                    txtTimeshop.append(timeshop.get(j)+" (OPEN)" + "\n");
                }else{
                    txtTimeshop.append(timeshop.get(j) + "\n");
                }
            }

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


     @Override
     public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;

         Intent intent = getIntent();
         String title = intent.getStringExtra("title");
         String map_position = intent.getStringExtra("map_position");

         String CurrentString = map_position;
         String[] separated = CurrentString.split(",");

        separated[0] = separated[0].trim();
        separated[1] = separated[1].trim();

        String lat = separated[0];
        String lng = separated[1];

        double valueLat = Double.parseDouble(lat);
        double valueLng = Double.parseDouble(lng);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(valueLat,valueLng);

         mMap.addMarker(new MarkerOptions().position(sydney).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker)));
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20));

     }
 }

