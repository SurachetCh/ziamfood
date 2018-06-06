package revenue_express.ziamfood.adapter;
/**
 * Created by surachet on 12/26/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import revenue_express.ziamfood.R;
import revenue_express.ziamfood.ShopActivity;
import revenue_express.ziamfood.dao.DealDataDao;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.MyViewHolder> {

    ArrayList<DealDataDao> list;
    Context context;

    public DealAdapter(Context context, ArrayList<DealDataDao> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deal_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DealDataDao m_expert = list.get(position);
//        holder.id.setText(m_expert.getBssh_id());

        try {
//            Picasso.with(Contextor.getInstance().getContext())
//                    .load(m_expert.getBssh_imghead())
//                    .placeholder(R.drawable.download)
//                    .into(holder.imageView);
            Glide.with(context)
                    .load(m_expert.getBssh_imghead())
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
//        public TextView id;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);

//            id = (TextView) view.findViewById(R.id.txtID);
            imageView = (ImageView) view.findViewById(R.id.iv_icon);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String id = list.get(position).getBssh_id();
                    Intent intent = new Intent(context, ShopActivity.class);
                    intent.putExtra("id", id);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
