package glivion.y2k.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.model.Category;
import glivion.y2k.android.viewholder.CategoryViewHolder;

/**
 * Created by blanka on 8/6/2016.
 */
public class CategoryBottomSheetAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private ArrayList<Category> mCategories;

    public CategoryBottomSheetAdapter(ArrayList<Category> mCategories) {
        this.mCategories = mCategories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_in_ex, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.category(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
