package glivion.y2k.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.model.Budget;
import glivion.y2k.android.viewholder.BudgetViewHolder;

/**
 * Created by blanka on 8/7/2016.
 */
public class BudgetAdapter extends RecyclerView.Adapter<BudgetViewHolder> {

    private ArrayList<Budget> mBudgets;

    public BudgetAdapter(ArrayList<Budget> mBudgets) {
        this.mBudgets = mBudgets;
    }

    @Override
    public BudgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BudgetViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BudgetViewHolder holder, int position) {
        try {
            holder.bind(mBudgets.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mBudgets.size();
    }
}
