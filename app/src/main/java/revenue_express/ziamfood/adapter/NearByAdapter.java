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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import revenue_express.ziamfood.NearByActivity;
import revenue_express.ziamfood.R;
import revenue_express.ziamfood.ShopActivity;
import revenue_express.ziamfood.dao.NearByDataDao;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.ViewHolder> {

    ArrayList<NearByDataDao> list;
    Context context;

    public NearByAdapter(Context context, ArrayList<NearByDataDao> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_card_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final NearByDataDao mDataset = list.get(position);
        holder.txtTitle.setText(mDataset.getName());
        holder.txtAddress.setText(mDataset.getAddress());
        holder.txtTel.setText(mDataset.getPhone());
        holder.txtDistance.setText(mDataset.getDistance()+"  mile");

        Glide.with(context).load(mDataset.getImg_logo())
                .placeholder(R.drawable.downloads_gif)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(holder.iv_icon);

        holder.layout_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(mDataset.getId());
                Intent intent = new Intent(v.getContext(), ShopActivity.class);
                intent.putExtra("id", id);
                v.getContext().startActivity(intent);
            }
        });

        if (position == list.size()-1){
            String loadmore ="true";
            NearByActivity loadItem = new NearByActivity();
            loadItem.loaditem(loadmore,position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView txtTitle,txtAddress,txtTel,txtDistance;
        public ImageView iv_icon;
        public RelativeLayout layout_card;

        public ViewHolder(View view) {
            super(view);

            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            txtAddress = (TextView)itemView.findViewById(R.id.txtAddress);
            txtTel = (TextView)itemView.findViewById(R.id.txtTel);
            txtDistance = (TextView)itemView.findViewById(R.id.txtDistance);
            iv_icon = (ImageView)itemView.findViewById(R.id.iv_icon);
            layout_card = (RelativeLayout)itemView.findViewById(R.id.layout_card);
//            itemView.setOnClickListener(this);

//            layout_card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    String id = String.valueOf(list.get(position).getId());
//                    Intent intent = new Intent(context, ShopActivity.class);
//                    intent.putExtra("id", id);
//                    v.getContext().startActivity(intent);
//                }
//            });
        }

//        @Override
//        public void onClick(View v) {
//            mCallback.onItemClick(getAdapterPosition(),v);
//        }
    }

}
