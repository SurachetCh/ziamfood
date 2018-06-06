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

import revenue_express.ziamfood.R;
import revenue_express.ziamfood.ShopActivity;
import revenue_express.ziamfood.dao.RecommendDataDao;

public class NewShopAdapter extends RecyclerView.Adapter<NewShopAdapter.ViewHolder> {

//    private MyClickListener mCallback;
    ArrayList<RecommendDataDao> list;
    Context context;

    public NewShopAdapter(Context context, ArrayList<RecommendDataDao> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reccomend_card_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecommendDataDao mDataset = list.get(position);
        holder.txtTitle.setText(mDataset.getName());
        holder.txtAddress.setText(mDataset.getAddress());
        holder.txtTel.setText(mDataset.getPhone());

        try {
//            Picasso.with(Contextor.getInstance().getContext())
//                    .load(mDataset.getImg_logo())
//                    .placeholder(R.drawable.download)
//                    .into(holder.iv_icon);
            Glide.with(context)
                    .load(mDataset.getImg_logo())
                    .placeholder(R.drawable.download)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(holder.iv_icon);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    public void setOnItemClickListener(MyClickListener mCallback){
//        this.mCallback = mCallback;
//    }
//
//    public NearByAdapter(NearByActivity NearByActivity, List<NearByDataDao> myDataset){
//        mDataset = myDataset;
//
//    }
//
//    public interface MyClickListener{
//        public void onItemClick(int position,View v);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView txtTitle,txtAddress,txtTel;
        public ImageView iv_icon;
        public RelativeLayout layout_card;

        public ViewHolder(View view) {
            super(view);

            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            txtAddress = (TextView)itemView.findViewById(R.id.txtAddress);
            txtTel = (TextView)itemView.findViewById(R.id.txtTel);
            iv_icon = (ImageView)itemView.findViewById(R.id.iv_icon);
            layout_card = (RelativeLayout)itemView.findViewById(R.id.layout_card);
//            itemView.setOnClickListener(this);

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

//        @Override
//        public void onClick(View v) {
//            mCallback.onItemClick(getAdapterPosition(),v);
//        }
    }
}
