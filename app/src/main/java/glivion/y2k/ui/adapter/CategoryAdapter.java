package glivion.y2k.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.ui.constants.Constants;
import glivion.y2k.ui.model.Category;
import glivion.y2k.ui.ui.AddCategory;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by saeed on 02/08/2016.
 */
public class CategoryAdapter extends ArrayAdapter<Category> implements StickyListHeadersAdapter {

    private ArrayList<Category> mCategories;

    public CategoryAdapter(Context context, ArrayList<Category> objects) {
        super(context, R.layout.category_item, objects);
        mCategories = objects;
        modifyList();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        viewHolder.catName.setText(WordUtils.capitalize(mCategories.get(position).getmCatName()));
        int color = mCategories.get(position).getmCatColor();
        if (color == 0) {
            viewHolder.catColor.setVisibility(View.GONE);
            viewHolder.catName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int type = mCategories.get(position).getmCatType();
                    Log.v("Type", String.valueOf(type));
                    Intent intent = new Intent(view.getContext(), AddCategory.class);
                    intent.putExtra(Constants.CAT_TYPE, type);
                    view.getContext().startActivity(intent);
                }
            });
        } else {
            viewHolder.catColor.setBackgroundColor(color);
            viewHolder.catColor.setVisibility(View.VISIBLE);
        }
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

        header.textView.setText(mCategories.get(position).getmCategoryType().getmTypeName());
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

    private void modifyList() {
        ArrayList<Category> expenditure = new ArrayList<>();
        ArrayList<Category> income = new ArrayList<>();
        for (Category category : mCategories) {
            if (category.getmCatType() == 0) {
                income.add(category);
            } else {
                expenditure.add(category);
            }
        }
        mCategories.clear();
        mCategories.addAll(income);
        mCategories.addAll(expenditure);
    }
}
