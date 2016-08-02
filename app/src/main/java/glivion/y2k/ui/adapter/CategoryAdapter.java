package glivion.y2k.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.ui.model.Category;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by saeed on 02/08/2016.
 */
public class CategoryAdapter extends ArrayAdapter<Category> implements StickyListHeadersAdapter {

    private ArrayList<Category> mCategories;
    
    public CategoryAdapter(Context context, ArrayList<Category> objects) {
        super(context, R.layout.category_item, objects);
        mCategories = objects;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        return null;
    }

    @Override
    public long getHeaderId(int position) {
        return mCategories.get(position);
    }

    class Header {
        TextView textView;
    }

    class ViewHolder {
        View catColor;
        TextView catName;
    }
}
