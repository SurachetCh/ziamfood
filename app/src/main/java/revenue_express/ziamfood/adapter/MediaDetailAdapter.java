package revenue_express.ziamfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import revenue_express.ziamfood.ImageActivity;
import revenue_express.ziamfood.PlayVideoActivity;
import revenue_express.ziamfood.R;
import revenue_express.ziamfood.dao.MediaDetailDataDao;
import revenue_express.ziamfood.model.TypeMedia;

import static android.view.Gravity.CENTER;


public class MediaDetailAdapter extends RecyclerView.Adapter<MediaDetailAdapter.ViewHolder> {

    Context context;
    public String title,detail,alert;
    private MyClickListener mCallback;
    private List<MediaDetailDataDao> mDataset;
    private WebChromeClient chromeClient;
    private ArrayList<TypeMedia> mediaList = new ArrayList<>();

    String url_video;
    int ll_image_food_width,ll_image_food_height,image_play_height,image_play_width;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_detail_card_layout, parent, false);
        ViewHolder dataObjHolder = new ViewHolder(view);
        return dataObjHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString( R.string.FontText));
        holder.tvUser.setTypeface(myTypeface);
        holder.tvPostdate.setTypeface(myTypeface);
        holder.tvTitle.setTypeface(myTypeface);
        holder.tvDetail.setTypeface(myTypeface);

        if(position == 0){

            Glide.with(context)
                        .load(mDataset.get(position).getWriter_photo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.male)
                        .into(holder.imgUser);

            holder.tvUser.setText(mDataset.get(position).getWriter_name());
            holder.tvPostdate.setText(mDataset.get(position).getCreated_date_social());
            holder.tvTitle.setText(mDataset.get(position).getTitle_social());
            holder.tvDetail.setText(mDataset.get(position).getDescription_social());

            holder.ll_header_media_detail.setVisibility(View.VISIBLE);
            loadImage(position , holder );
        }else {
            holder.ll_header_media_detail.setVisibility(View.GONE);
            loadImage(position, holder);
        }
    }

    private void loadImage(final int position , ViewHolder holder) {
        CheckSizeDisplay();
        ImageView iv1 = new ImageView(context);
        ImageView img_play1 = new ImageView(context);
        LinearLayout layout_play1 = new LinearLayout(context);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ll_image_food_width, ll_image_food_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ll_image_food_width, LinearLayout.LayoutParams.WRAP_CONTENT);

//        params.setMargins(0, 0, 0, 0);

        iv1.setImageResource(0);
        img_play1.setImageResource(0);

//        holder.rr_image_food.setLayoutParams(params);
        holder.rr_image_food.removeAllViews();

        iv1.setLayoutParams(params);

        if(mediaList.get(position).getTypeMedia().equals("video")){
//            holder.iv_image_food.setImageResource(0);

//            holder.iv_image_food.setLayoutParams(params);
            image_play_height=100;
            image_play_width=100;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(image_play_width, image_play_height);
//            layoutParams.setMargins((ll_image_food_width - image_play_width) / 2, (params.height - image_play_height) / 2, 0, 0);
//            layoutParams.setMargins((layoutParams.MATCH_PARENT - image_play_width) / 2, (layoutParams.WRAP_CONTENT - image_play_height) / 2, 0, 0);
            img_play1.setLayoutParams(layoutParams);

            Glide.with(context)
                    .load(mediaList.get(position).getUrlImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.load_image)
                    .into(iv1);

            img_play1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
            holder.rr_image_food.addView(iv1);

            img_play1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    url_video = mediaList.get(position).getUrlVideo();
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra("url_video", url_video);
                    v.getContext().startActivity(intent);
                }
            });

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ll_image_food_height/2)+image_play_height);
            layout_play1.setLayoutParams(params1);
            layout_play1.setGravity(CENTER);
            layout_play1.addView(img_play1);
            holder.rr_image_food.addView(layout_play1);

        }else {

            Glide.with(context)
                    .load(mediaList.get(position).getUrlImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.load_image)
                    .into(iv1);

            holder.rr_image_food.addView(iv1);

//            holder.iv_image_food.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ImageActivity.class);
//                    intent.putExtra("img", mediaList.get(position).getUrlImage());
//                    v.getContext().startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
       return mediaList.size();
    }

    public void setOnItemClickListener(MyClickListener mCallback){
        this.mCallback = mCallback;
    }

    public MediaDetailAdapter(Context context, List<MediaDetailDataDao> myDataset, ArrayList<TypeMedia> typeMedia){
        mDataset = myDataset;
        this.context = context;
        this.mediaList = typeMedia;
    }

    public interface MyClickListener{
        public void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Check type shop
        public LinearLayout ll_header_media_detail;

        //Timeline
        public TextView tvTitle, tvDetail,tvUser,tvReadmore,tvPostdate;
        public CircleImageView imgUser;
        public LinearLayout rl_star;
        public RelativeLayout rr_image_food;
//        public ImageView iv_image_food;

        public ViewHolder(View itemView) {
            super(itemView);

            //Check type data
            ll_header_media_detail = (LinearLayout) itemView.findViewById(R.id.ll_header_media_detail);

            //Timeline
            imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDetail = (TextView) itemView.findViewById(R.id.tvDetail);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            tvPostdate = (TextView) itemView.findViewById(R.id.tvPostdate);
            tvReadmore = (TextView) itemView.findViewById(R.id.tvReadmore);

            rr_image_food = (RelativeLayout)itemView.findViewById(R.id.rr_image_food);
//            iv_image_food = (ImageView) itemView.findViewById(R.id.iv_image_food);
        }

        @Override
        public void onClick(View v) {
            mCallback.onItemClick(getAdapterPosition(),v);
        }
    }

    public void CheckSizeDisplay(){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("width-height: ", String.valueOf(width) + "-" + String.valueOf(height));

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dp_width = Math.round(width);
        float dp_height = Math.round(height);
        Log.i("dp_width-dp_height: ", String.valueOf(dp_width) + "-" + String.valueOf(dp_height));

        ll_image_food_width = Math.round(dp_width)-20;
        ll_image_food_height = (ll_image_food_width * 80) / 100;
        Log.i("ll_image_food_width: ", String.valueOf(ll_image_food_width));

    }
}