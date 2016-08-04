package glivion.y2k.ui.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.ui.model.Category;
import glivion.y2k.ui.model.CategoryType;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.catColor = convertView.findViewById(R.id.category_color);
            viewHolder.catName = (TextView) convertView.findViewById(R.id.category_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.catName.setText(mCategories.get(position).getmCatName());
        viewHolder.catColor.setBackgroundColor(mCategories.get(position).getmCatColor());
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        Header header;
        if (convertView == null) {
            header = new Header();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_header, parent, false);
            header.textView = (TextView) convertView.findViewById(R.id.category);
            convertView.setTag(header);
        } else {
            header = (Header) convertView.getTag();
        }
        header.textView.setText(mCategories.get(position).getmCatType());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mCategories.get(position).getmCatType();
    }

    class Header {
        TextView textView;
    }

    class ViewHolder {
        View catColor;
        TextView catName;
    }
}
