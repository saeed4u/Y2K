package glivion.y2k.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.model.Currency;

/**
 * Created by saeed on 23/08/2016.
 */
public class CurrencyAdapter extends ArrayAdapter<Currency> {

    private ArrayList<Currency> mCurrencies;

    public CurrencyAdapter(Context context,ArrayList<Currency> objects) {
        super(context, R.layout.currency_adapter, R.id.currency_name, objects);
        this.mCurrencies = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_adapter, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.currency_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mCurrencies.get(position).toString());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_adapter, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.currency_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mCurrencies.get(position).toString());
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
