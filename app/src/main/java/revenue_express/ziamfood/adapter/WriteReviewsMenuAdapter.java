package revenue_express.ziamfood.adapter;
/**
 * Created by surachet on 12/26/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import revenue_express.ziamfood.R;
import revenue_express.ziamfood.WriteReviewsActivity;
import revenue_express.ziamfood.dao.WriteReviewsMenuDataDao;

public class WriteReviewsMenuAdapter extends RecyclerView.Adapter<WriteReviewsMenuAdapter.MyViewHolder> {

    List<WriteReviewsMenuDataDao> list;
    Context context;
    WriteReviewsMenuDataDao m_expert;
    String check;

    public WriteReviewsMenuAdapter(Context context, List<WriteReviewsMenuDataDao> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.write_reviews_menu_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        m_expert = list.get(position);
        try {

            Glide.with(context).load(m_expert.getImg_menu()).centerCrop().into(holder.iv_image_recommend_menu);

            holder.name_menu.setText(m_expert.getMenu_name());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,list.size());

                    if(list.size() == 0){
                        check = "false";
                    }else{
                        check = "true";
                    }

                    WriteReviewsActivity.removeIdMenu(m_expert.getId(),check);

                }
            });

        } catch (Exception e) {
        }
    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_menu;
        public ImageView delete,iv_image_recommend_menu;

        public MyViewHolder(View view) {
            super(view);

            name_menu = (TextView) view.findViewById(R.id.tv_name_menu);
            delete = (ImageView) view.findViewById(R.id.iv_delete);
            iv_image_recommend_menu = (ImageView)view.findViewById(R.id.iv_image_recommend_menu);

        }
    }
}
