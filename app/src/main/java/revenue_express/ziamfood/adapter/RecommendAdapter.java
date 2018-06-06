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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import revenue_express.ziamfood.R;
import revenue_express.ziamfood.ShopActivity;
import revenue_express.ziamfood.dao.RecommendDataDao;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.MyViewHolder> {

    ArrayList<RecommendDataDao> list;
    Context context;
    RecommendDataDao m_expert;

    public RecommendAdapter(Context context, ArrayList<RecommendDataDao> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommend_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        m_expert = list.get(position);
        holder.title.setText(m_expert.getName());
//        holder.id.setText(m_expert.getBssh_id());
        String img = m_expert.getImg_cover();

        try {
//            Picasso.with(Contextor.getInstance().getContext())
//                    .load(m_expert.getImg_logo())
//                    .placeholder(R.drawable.download)
//                    .into(holder.imageView);
            Glide.with(context)
                    .load(m_expert.getImg_cover())
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
        public TextView title,id;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txtTitle);
//            id = (TextView) itemView.findViewById(R.id.txtID);
            imageView = (ImageView) itemView.findViewById(R.id.iv_icon);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String id = String.valueOf(list.get(position).getId());
                    Intent intent = new Intent(context, ShopActivity.class);
                    intent.putExtra("id", id);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}