package revenue_express.ziamfood.adapter;

/**
 * Created by surachet on 12/25/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import revenue_express.ziamfood.OnlineOrderAddFoodsActivity;
import revenue_express.ziamfood.R;
import revenue_express.ziamfood.SignInActivity;
import revenue_express.ziamfood.dao.OnlineOrderDataListDao;
import revenue_express.ziamfood.model.User;

import static revenue_express.ziamfood.ShopActivity.checkForceOnline;
import static revenue_express.ziamfood.ShopActivity.checkTimeOpen;

public class OnlineOrderListAdapter extends RecyclerView.Adapter<OnlineOrderListAdapter.MyViewHolder> {

    ArrayList<OnlineOrderDataListDao> list;
    Context context;
    Realm realm;

    public OnlineOrderListAdapter(Context context, ArrayList<OnlineOrderDataListDao> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_order_list_card_layout, parent, false);

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OnlineOrderDataListDao m_expert = list.get(position);

        holder.title.setText(m_expert.getName());
        holder.desc.setText(m_expert.getDescription());
        holder.price.setText(m_expert.getPrice());
        try {
//            Picasso.with(Contextor.getInstance().getContext())
//                    .load(m_expert.getPhoto())
//                    .placeholder(R.drawable.download)
//                    .into(holder.imageView);
            Log.d("Url image: ",m_expert.getPhoto().toString());
            Glide.with(context)
                    .load(m_expert.getPhoto())
                    .placeholder(R.drawable.download)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(holder.imageView);
        } catch (Exception e) {
        }
    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,desc,price;
        public ImageView imageView;
        public RelativeLayout layout_card;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.txtTitle);
            desc = (TextView) view.findViewById(R.id.txtDesc);
            price = (TextView) view.findViewById(R.id.txtPrice);
            imageView = (ImageView) view.findViewById(R.id.iv_icon);
            layout_card = (RelativeLayout)view.findViewById(R.id.layout_card);


            layout_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    realm.beginTransaction();
                    RealmResults<User> user = realm.where(User.class).findAll();
                    int check = user.size();
                    realm.commitTransaction();

                    if (check != 0){
                        int position = getAdapterPosition();
                        String id = String.valueOf(list.get(position).getId());
                        String title = list.get(position).getName();
                        String price = list.get(position).getPrice();
                        String desc = list.get(position).getDescription();
                        String photo = list.get(position).getPhoto();

                        //check time open
                        String timeopen =checkTimeOpen();
                        String forceonline = checkForceOnline();
                        if(forceonline.equals("1")){
                            //Check Force Online
                            Intent intent = new Intent(context, OnlineOrderAddFoodsActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("id",id);
                            intent.putExtra("title",title);
                            intent.putExtra("price",price);
                            intent.putExtra("desc",desc);
                            intent.putExtra("photo",photo);
                            v.getContext().startActivity(intent);
                        }else {

                            if (timeopen.equals("true")) {
                                Intent intent = new Intent(context, OnlineOrderAddFoodsActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("id", id);
                                intent.putExtra("title", title);
                                intent.putExtra("price", price);
                                intent.putExtra("desc", desc);
                                intent.putExtra("photo", photo);
                                v.getContext().startActivity(intent);
                            } else {
                                showStatusOpenDialog("Menu online order", "Shop is close,Please wait shop open!");
                            }
                        }

                    }else {
                        new AlertDialog.Builder(context)
                                .setTitle("Please login user?")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with login
                                        Intent intent = new Intent(context, SignInActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        v.getContext().startActivity(intent);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();
                    }
                }
            });
        }
    }

    //-----Show dialog box-----//
    private void showStatusOpenDialog(String title , String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = "OK";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}
