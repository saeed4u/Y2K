package glivion.y2k.android.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import glivion.y2k.R;
import glivion.y2k.android.model.Category;

/**
 * Created by blanka on 8/6/2016.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private View mCategoryColor;
    private TextView mCategoryName;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        mCategoryColor = itemView.findViewById(R.id.category_color);
        mCategoryName = (TextView) itemView.findViewById(R.id.category_name);
    }

    public void category(Category category) {
        mCategoryColor.setBackgroundColor(category.getmCatColor());
        mCategoryName.setText(category.getmCatName());
    }
}
