package revenue_express.ziamfood.adapter;

/**
 * Created by surachet on 12/25/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import revenue_express.ziamfood.R;
import revenue_express.ziamfood.ReviewDetailActivity;
import revenue_express.ziamfood.dao.ReviewDataDao;
import revenue_express.ziamfood.model.BlogTypeMedia;

public class LastReviewAdapter extends RecyclerView.Adapter<LastReviewAdapter.ViewHolder> {
    Context context;
    Realm realm;
    public String title,address,map_position,phone;
    private MyClickListener mCallback;
    private List<ReviewDataDao> mDataset;
    private ArrayList<BlogTypeMedia> mediaList = new ArrayList<>();
    private WebChromeClient chromeClient;
    Integer ll_image_food_width,ll_image_food_height;
    ImageView imageView;
    ArrayList<String> imgslide;

//    public ViewFlipper viewFlipper;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_main_last_review_layout, parent, false);
        ViewHolder dataObjHolder = new ViewHolder(view);

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        return dataObjHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ArrayList<String> imgslide = new ArrayList<String>(5);
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H01.jpg");
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H02.jpg");
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H03.jpg");
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H04.jpg");
        imgslide.add("https://www.ziamthai.com/asset/zth-food/homepage_slideshow/H05.jpg");

        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.FontText));
        holder.tvUser.setTypeface(myTypeface);
        holder.tvPostdate.setTypeface(myTypeface);
        holder.tvTitle.setTypeface(myTypeface);
        holder.tvDetail1.setTypeface(myTypeface);
        holder.tvDetail2.setTypeface(myTypeface);
        holder.tv_last_review.setTypeface(myTypeface);

        holder.tv_last_review.setVisibility(View.GONE);
        if (position == 0){
            for(int i=0;i<imgslide.size();i++){
                Log.i("Set Flipper Called", imgslide.get(i).toString()+"");
                ImageView image = new ImageView(context);
                Glide.with(context).load(imgslide.get(i).toString()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(image);
                holder.viewFlipper.addView(image);
            }
            holder.title_main.setVisibility(View.VISIBLE);
            holder.ll_last_review.setVisibility(View.GONE);

//            holder.img_category2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, SearchAppActivity.class);
//                    v.getContext().startActivity(intent);
//                }
//            });
//            holder.img_category3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, SearchAppActivity.class);
//                    v.getContext().startActivity(intent);
//                }
//            });
//
//            holder.img_category4.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ReccomendActivity.class);
//                    v.getContext().startActivity(intent);
//                }
//            });

        }else if (position == 1){
            holder.tv_last_review.setVisibility(View.VISIBLE);
            holder.title_main.setVisibility(View.GONE);
            holder.ll_last_review.setVisibility(View.GONE);
        } else {
            holder.title_main.setVisibility(View.GONE);
            holder.ll_last_review.setVisibility(View.VISIBLE);
            loadImage(position-2 , holder );

        }
    }

    private void loadImage(final int position , ViewHolder holder) {
        Glide.with(context).load(mediaList.get(position).getPhoto_thumb()).centerCrop().placeholder(R.drawable.male).into(holder.imgUser);
        holder.tvUser.setText(mediaList.get(position).getWriter_name());
        holder.tvPostdate.setText(mediaList.get(position).getCreated_timeago());
        holder.tvTitle.setText(mediaList.get(position).getShop_name());
        holder.tvDetail1.setText(mediaList.get(position).getTitle());
        holder.tvDetail2.setText(mediaList.get(position).getPost_highlight());

        Integer star = mediaList.get(position).getReview_score();
        holder.img_star1.setImageResource(0);
        holder.img_star2.setImageResource(0);
        holder.img_star3.setImageResource(0);
        holder.img_star4.setImageResource(0);
        holder.img_star5.setImageResource(0);
        if (star==0){
            holder.img_star1.setImageResource(R.drawable.star_emty);
            holder.img_star2.setImageResource(R.drawable.star_emty);
            holder.img_star3.setImageResource(R.drawable.star_emty);
            holder.img_star4.setImageResource(R.drawable.star_emty);
            holder.img_star5.setImageResource(R.drawable.star_emty);

        }else if (star==1){
            holder.img_star1.setImageResource(R.drawable.star);
            holder.img_star2.setImageResource(R.drawable.star_emty);
            holder.img_star3.setImageResource(R.drawable.star_emty);
            holder.img_star4.setImageResource(R.drawable.star_emty);
            holder.img_star5.setImageResource(R.drawable.star_emty);
        }else if (star==2){
            holder.img_star1.setImageResource(R.drawable.star);
            holder.img_star2.setImageResource(R.drawable.star);
            holder.img_star3.setImageResource(R.drawable.star_emty);
            holder.img_star4.setImageResource(R.drawable.star_emty);
            holder.img_star5.setImageResource(R.drawable.star_emty);
        }else if (star==3){
            holder.img_star1.setImageResource(R.drawable.star);
            holder.img_star2.setImageResource(R.drawable.star);
            holder.img_star3.setImageResource(R.drawable.star);
            holder.img_star4.setImageResource(R.drawable.star_emty);
            holder.img_star5.setImageResource(R.drawable.star_emty);
        }else if (star==4){
            holder.img_star1.setImageResource(R.drawable.star);
            holder.img_star2.setImageResource(R.drawable.star);
            holder.img_star3.setImageResource(R.drawable.star);
            holder.img_star4.setImageResource(R.drawable.star);
            holder.img_star5.setImageResource(R.drawable.star_emty);
        }else {
            holder.img_star1.setImageResource(R.drawable.star);
            holder.img_star2.setImageResource(R.drawable.star);
            holder.img_star3.setImageResource(R.drawable.star);
            holder.img_star4.setImageResource(R.drawable.star);
            holder.img_star5.setImageResource(R.drawable.star);
        }

        holder.ll_last_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewDetailActivity.class);
                intent.putExtra("review_id", mediaList.get(position).getRef_id());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mediaList.size()+2;
    }

    public void setOnItemClickListener(MyClickListener mCallback){
        this.mCallback = mCallback;
    }

    public LastReviewAdapter(Context context, List<ReviewDataDao> myDataset , ArrayList<BlogTypeMedia> typeMedia){
        mDataset = myDataset;
        this.context = context;
        this.mediaList = typeMedia;
    }

    public interface MyClickListener{
        public void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //title_main
        public LinearLayout title_main;
//        public LinearLayout ll_shop_image;
        public ImageView img_category1,img_category2,img_category3,img_category4;


        //Last Review
        public CircleImageView imgUser;
        public TextView tvUser,tvPostdate,tvTitle,tvDetail1,tvDetail2,tv_last_review;
        public LinearLayout ll_last_review;
        public ViewFlipper viewFlipper;
        public ImageView img_star1,img_star2,img_star3,img_star4,img_star5;


        public ViewHolder(View itemView) {
            super(itemView);

            viewFlipper = (ViewFlipper)itemView.findViewById(R.id.viewFlipper);
            //title_main
            title_main = (LinearLayout) itemView.findViewById(R.id.title_main);
//            ll_shop_image = (LinearLayout) itemView.findViewById(R.id.ll_shop_image);
            img_category1 = (ImageView) itemView.findViewById(R.id.img_category1);
            img_category2 = (ImageView) itemView.findViewById(R.id.img_category2);
            img_category3 = (ImageView) itemView.findViewById(R.id.img_category3);
            img_category4 = (ImageView) itemView.findViewById(R.id.img_category4);

            //Last Review
            imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            tvPostdate  = (TextView) itemView.findViewById(R.id.tvPostdate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDetail1 = (TextView) itemView.findViewById(R.id.tvDetail1);
            tvDetail2 = (TextView) itemView.findViewById(R.id.tvDetail2);
            img_star1 = (ImageView) itemView.findViewById(R.id.img_star1);
            img_star2 = (ImageView) itemView.findViewById(R.id.img_star2);
            img_star3 = (ImageView) itemView.findViewById(R.id.img_star3);
            img_star4 = (ImageView) itemView.findViewById(R.id.img_star4);
            img_star5 = (ImageView) itemView.findViewById(R.id.img_star5);
            ll_last_review = (LinearLayout) itemView.findViewById(R.id.ll_last_review);
            tv_last_review = (TextView) itemView.findViewById(R.id.tv_last_review);

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
        float dp_width = Math.round(width * 0.95);
        float dp_height = Math.round(height * 0.95);
        Log.i("dp_width-dp_height: ", String.valueOf(dp_width) + "-" + String.valueOf(dp_height));

        ll_image_food_width = Math.round(dp_width);
        ll_image_food_height = (ll_image_food_width * 80) / 100;
        Log.i("ll_image_food_width: ", String.valueOf(ll_image_food_width));

    }
}