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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import revenue_express.ziamfood.R;
import revenue_express.ziamfood.ShopActivity;
import revenue_express.ziamfood.dao.InfoDataDao;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {

    ArrayList<InfoDataDao> list;
    Context context;

    public InfoAdapter(Context context, ArrayList<InfoDataDao> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        InfoDataDao m_expert = list.get(position);
//        holder.id.setText(m_expert.getBssh_id());
        holder.title.setText(m_expert.getName());
        holder.address.setText(m_expert.getAddress());
        holder.loc1.setText(m_expert.getState_code()+","+m_expert.getZipcode());

        try {
//            Picasso.with(Contextor.getInstance().getContext())
//                    .load(m_expert.getImg_logo())
//                    .placeholder(R.drawable.download)
//                    .into(holder.imageView);
            Glide.with(context)
                    .load(m_expert.getImg_logo())
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
        public TextView title,address,loc1;
        public ImageView imageView;
        public LinearLayout layout_card;

        public MyViewHolder(View view) {
            super(view);

//            id = (TextView)itemView.findViewById(R.id.txtID);
            title = (TextView)itemView.findViewById(R.id.txtTitle);
            address = (TextView)itemView.findViewById(R.id.txtAddress);
            loc1 = (TextView)itemView.findViewById(R.id.txtLoc1);
            imageView = (ImageView)itemView.findViewById(R.id.iv_icon);
            layout_card = (LinearLayout) view.findViewById(R.id.layout_card);

            layout_card.setOnClickListener(new View.OnClickListener() {
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

