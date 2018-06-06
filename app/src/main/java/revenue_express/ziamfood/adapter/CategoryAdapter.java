package revenue_express.ziamfood.adapter;

/**
 * Created by surachet on 12/25/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import revenue_express.ziamfood.R;
import revenue_express.ziamfood.dao.CategoryDataDao;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    ArrayList<CategoryDataDao> list;
    Context context;;

    public CategoryAdapter(Context context, ArrayList<CategoryDataDao> list)  {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryDataDao m_expert = list.get(position);

        holder.title.setText(m_expert.getName());
    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout layout_card;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.txtTitle);
            layout_card = (RelativeLayout)view.findViewById(R.id.layout_card);

        }
    }
}
