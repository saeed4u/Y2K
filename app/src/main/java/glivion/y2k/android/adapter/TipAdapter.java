package glivion.y2k.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.model.NewsItem;
import glivion.y2k.android.viewholder.TipViewHolder;

/**
 * Created by blanka on 8/6/2016.
 */
public class TipAdapter extends RecyclerView.Adapter<TipViewHolder> {

    private ArrayList<NewsItem> mNewsItem;

    public TipAdapter(ArrayList<NewsItem> mNewsItem) {
        this.mNewsItem = mNewsItem;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TipViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TipViewHolder holder, int position) {

        holder.bind(mNewsItem.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsItem.size();
    }
}
