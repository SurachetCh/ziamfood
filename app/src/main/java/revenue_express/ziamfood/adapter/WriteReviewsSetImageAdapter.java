package revenue_express.ziamfood.adapter;
/**
 * Created by chaleamsuk on 12/26/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import revenue_express.ziamfood.R;
import revenue_express.ziamfood.WriteReviewsActivity;
import revenue_express.ziamfood.dao.WriteReviewsSetImageDataDao;

public class WriteReviewsSetImageAdapter extends RecyclerView.Adapter<WriteReviewsSetImageAdapter.MyViewHolder> {

    List<WriteReviewsSetImageDataDao> list;
    Context context;

    public WriteReviewsSetImageAdapter(Context context, List<WriteReviewsSetImageDataDao> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.write_reviews_setimage_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WriteReviewsSetImageDataDao m_expert = list.get(position);
        try {

            Glide.with(context)
                    .load(m_expert.getName_image())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.load_image)
                    .into(holder.iv_uploadimage);

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,list.size());

                    WriteReviewsActivity removeId = new WriteReviewsActivity();
                    removeId.removeListImage(m_expert.getId_image());

                }
            });

        } catch (Exception e) {
        }
    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_uploadimage,iv_delete;

        public MyViewHolder(View view) {
            super(view);

            iv_uploadimage = (ImageView) view.findViewById(R.id.iv_uploadimage);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

        }
    }
}
