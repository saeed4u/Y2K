package glivion.y2k.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.ui.model.Loan;
import glivion.y2k.ui.viewholder.LoanViewHolder;

/**
 * Created by saeedissah on 5/18/16.
 */
public class LoanAdapter extends RecyclerView.Adapter<LoanViewHolder> {

    private ArrayList<Loan> mLoans;

    public LoanAdapter(ArrayList<Loan> mLoans) {
        this.mLoans = mLoans;
    }

    @Override
    public LoanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LoanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LoanViewHolder holder, int position) {
        try {
            holder.bind(mLoans.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mLoans.size();
    }
}
