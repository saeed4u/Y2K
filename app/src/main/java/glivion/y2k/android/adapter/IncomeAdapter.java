package glivion.y2k.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.model.IncomeExpenditure;
import glivion.y2k.android.viewholder.IncomeViewHolder;

/**
 * Created by blanka on 8/6/2016.
 */
public class IncomeAdapter extends RecyclerView.Adapter<IncomeViewHolder> {

    private ArrayList<IncomeExpenditure> mIncomeExpenditures;

    public IncomeAdapter(ArrayList<IncomeExpenditure> mIncomeExpenditures) {
        this.mIncomeExpenditures = mIncomeExpenditures;
    }

    @Override
    public IncomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IncomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IncomeViewHolder holder, int position) {
        try {
            holder.bindData(mIncomeExpenditures.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mIncomeExpenditures.size();
    }
}
