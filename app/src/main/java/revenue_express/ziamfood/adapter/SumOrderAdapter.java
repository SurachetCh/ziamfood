package revenue_express.ziamfood.adapter;

/**
 * Created by surachet on 1/16/2017 AD.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import revenue_express.ziamfood.R;
import revenue_express.ziamfood.manager.Contextor;
import revenue_express.ziamfood.model.OrderList;
import revenue_express.ziamfood.model.OrderListDAO;

import static revenue_express.ziamfood.SumOrderActivity.SumOrder;

public class SumOrderAdapter extends RecyclerView.Adapter<SumOrderAdapter.MyViewHolder> {

    ArrayList<OrderListDAO> list;
    Context context;
    Integer id_order;
    Realm realm;

    public SumOrderAdapter(Context context, ArrayList<OrderListDAO> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sum_order_card_layout, parent, false);

        Realm.init(Contextor.getInstance().getContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        OrderListDAO m_expert = list.get(position);
        holder.title.setText(m_expert.getTitle());
        holder.price.setText(Double.toString(m_expert.getPrice()));
        holder.amount.setText(Integer.toString(m_expert.getAmount()));
        holder.total.setText(Double.toString(m_expert.getTotal()));
        String addition = m_expert.getAddition();
        String note = m_expert.getNote();
        if ((addition == null)){
            addition = " ";
        }
        if (note == null){
            note = "";
        }
        holder.add_note.setText(addition+" "+note);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderListDAO m_expert = list.get(position);

                id_order = m_expert.getId();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.delete1);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        realm.beginTransaction();
                        RealmResults<OrderList> result = realm.where(OrderList.class).equalTo("id",id_order).findAll();
                        result.deleteLastFromRealm();

                        double total = (double) realm.where(OrderList.class).sum("total");
                        SumOrder(total);

                        RealmResults<OrderList> resultSize = realm.where(OrderList.class).findAll();
                        int SizeOrder = resultSize.size();
//                        SumOnlineOrder(SizeOrder);
//                        SumOnlineOrderFoods(SizeOrder);

                        realm.commitTransaction();

                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,list.size());

                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });

    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,price,amount,add_note,total;
        public ImageView imgDelete;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView)itemView.findViewById(R.id.txtTitle);
            price = (TextView)itemView.findViewById(R.id.txtPrice);
            amount = (TextView)itemView.findViewById(R.id.txtAmount);
            total = (TextView)itemView.findViewById(R.id.txtTotal);
            add_note = (TextView)itemView.findViewById(R.id.txtAdd_Note);
            imgDelete = (ImageView)itemView.findViewById(R.id.imgDelete);

        }
    }

}