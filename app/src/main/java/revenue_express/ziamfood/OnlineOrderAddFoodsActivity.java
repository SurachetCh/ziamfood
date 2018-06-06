package revenue_express.ziamfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import revenue_express.ziamfood.manager.Contextor;
import revenue_express.ziamfood.model.OrderList;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;


public class OnlineOrderAddFoodsActivity extends AppCompatActivity {
    Integer amount = 1 ;
    TextView tvAmount,tvPrice,tvTitle;
    public static TextView tvOrder;
    ImageView imgPlus,imgDelete,imgPhoto,imgSumOrder,imgBack;
    EditText edtAddaition,edtNote;
    LinearLayout btnAdd;
    String title,photo,addition,note;
    Integer ranks = 0;
    int id;
    double price;
    double temptotal;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_order_add_foods);

        sendNamePageGoogleAnalytics("OnlineOrderAddFoodsActivity");

        tvAmount = (TextView)findViewById(R.id.tvAmount);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        imgDelete = (ImageView)findViewById(R.id.imgDelete);
        imgPlus = (ImageView)findViewById(R.id.imgPlus);
        imgPhoto = (ImageView)findViewById(R.id.imgPhoto);
        btnAdd = (LinearLayout)findViewById(R.id.btnAdd);
        edtAddaition = (EditText)findViewById(R.id.edtAddaition);
        edtNote = (EditText)findViewById(R.id.edtNote);

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        title = getIntent().getStringExtra("title");
        price = Double.parseDouble(getIntent().getStringExtra("price"));
        photo = getIntent().getStringExtra("photo");

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        realm.beginTransaction();
        RealmResults<OrderList> result = realm.where(OrderList.class).findAll();

        for (int i = 0; i < result.size(); i++){
            ranks = ranks+result.get(i).getAmount();
        }
        realm.commitTransaction();

        tvOrder = (TextView)findViewById(R.id.tvOrder);
        if (ranks == null){
            tvOrder.setText("0");
        }else {
            tvOrder.setText(String.valueOf(ranks));
        }

        Picasso.with(Contextor.getInstance().getContext())
                .load(photo)
                .placeholder(R.drawable.download)
                .into(imgPhoto);

        RealmResults<OrderList> check =  realm.where(OrderList.class).equalTo("id_menu", id).findAll();
        if (check.size() == 1) {
            tvTitle.setText(title);
            temptotal = check.get(0).getTotal();
            price = check.get(0).getPrice();
            amount = check.get(0).getAmount();
            edtAddaition.setText(check.get(0).getAddition());
            tvPrice.setText("$ "+String.valueOf(temptotal) );
        }else {
            tvPrice.setText("$ "+String.valueOf(price) );
        }
            tvTitle.setText(title);
            tvAmount.setText(String.valueOf(amount));
            temptotal = Double.parseDouble(new DecimalFormat("##.00").format(price*amount));
        imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(amount == 1){
                        amount =1;
                        tvAmount.setText(String.valueOf(amount));
                        temptotal = Double.parseDouble(new DecimalFormat("##.00").format(price*amount));
                        tvPrice.setText("$ "+String.valueOf(temptotal) );
                    }else {
                        amount = amount -1;
                    }
                    tvAmount.setText(String.valueOf(amount));
                    temptotal = Double.parseDouble(new DecimalFormat("##.00").format(price*amount));
                    tvPrice.setText("$ "+String.valueOf(temptotal) );
                }
            });

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = amount +1;
                tvAmount.setText(String.valueOf(amount));
                temptotal = Double.parseDouble(new DecimalFormat("##.00").format(price*amount));
                tvPrice.setText("$ "+String.valueOf(temptotal) );
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = edtNote.getText().toString();
                addition = edtAddaition.getText().toString();

                RealmResults<OrderList> check =  realm.where(OrderList.class).equalTo("id_menu", id).findAll();

                if (check.size() == 1) {

                    realm.beginTransaction();
                    Integer key = check.get(0).getId();
                    check.get(0).setId(key);
                    check.get(0).setId_menu(id);
                    check.get(0).setTitle(title);
                    check.get(0).setPrice(price);
                    check.get(0).setAmount(amount);
                    check.get(0).setAddition(addition);

                    realm.copyToRealmOrUpdate(check);
                    realm.commitTransaction();
                }else{
                        executeRealmTransaction();
                }

                Intent intent = new Intent(getApplicationContext(), SumOrderActivity.class);
                startActivity(intent);
            }
        });
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgSumOrder = (ImageView)findViewById(R.id.imgSumOrder);
        imgSumOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {if (tvOrder.getText().equals("0")){

                AlertDialog.Builder builder = new AlertDialog.Builder(OnlineOrderAddFoodsActivity.this);
                builder.setCancelable(false);
                builder.setMessage(getResources().getString(R.string.text_null_order));
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }else {
                Intent intent = new Intent(getApplicationContext(), SumOrderActivity.class);
                startActivity(intent);
            }
            }
        });

    }

    public static void SumOnlineOrderFoods(int total){

        String total_last = String.valueOf(total);
        tvOrder.setText(total_last);
    }
    private void executeRealmTransaction() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                OrderList orderList = realm.createObject(OrderList.class);

                    Number num = realm.where(OrderList.class).max("id");
                    int nextID;
                    if(num == null) {
                        nextID = 1;
                    } else {
                        nextID = num.intValue() + 1;
                    }
                    orderList.setId(nextID);
                    orderList.setId_menu(id);
                    orderList.setTitle(title);
                    orderList.setPrice(price);
                    orderList.setTotal(temptotal);
                    orderList.setAmount(amount);
                    orderList.setAddition(addition);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(OnlineOrderAddFoodsActivity.this,"Create Complete",Toast.LENGTH_SHORT).show();
//                RealmQuery<OrderList> query = realm.where(OrderList.class);
//                String firstName = String.valueOf(query.equalTo("firstname",String.valueOf(edtFirstname.getText())));
//                String lastName = String.valueOf(query.equalTo("firstname",String.valueOf(edtLasttname.getText())));
//                Log.e("Results ",firstName+" "+lastName);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(OnlineOrderAddFoodsActivity.this,"Create Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
