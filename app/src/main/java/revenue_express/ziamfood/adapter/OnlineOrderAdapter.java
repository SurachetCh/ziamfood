package revenue_express.ziamfood.adapter;

/**
 * Created by surachet on 12/25/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import revenue_express.ziamfood.OnlineOrderAddFoodsActivity;
import revenue_express.ziamfood.R;
import revenue_express.ziamfood.dao.OnlineOrderDataDao;

public class OnlineOrderAdapter extends RecyclerView.Adapter<OnlineOrderAdapter.MyViewHolder> {

    ArrayList<OnlineOrderDataDao> list;
    Context context;;

    public OnlineOrderAdapter(Context context, ArrayList<OnlineOrderDataDao> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_order_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OnlineOrderDataDao m_expert = list.get(position);

        holder.title.setText(m_expert.getBsic_name());

    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.txtTitle);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OnlineOrderAddFoodsActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
