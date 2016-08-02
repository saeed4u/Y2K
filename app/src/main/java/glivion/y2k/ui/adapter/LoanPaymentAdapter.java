package glivion.y2k.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.ui.model.LoanPayment;
import glivion.y2k.ui.viewholder.LoanPaymentViewHolder;

/**
 * Created by saeedissah on 5/19/16.
 */
public class LoanPaymentAdapter extends RecyclerView.Adapter<LoanPaymentViewHolder> {

    private ArrayList<LoanPayment> mLoanPayments;

    public LoanPaymentAdapter(ArrayList<LoanPayment> mLoanPayments) {
        this.mLoanPayments = mLoanPayments;
    }

    @Override
    public LoanPaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LoanPaymentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_payment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LoanPaymentViewHolder holder, int position) {
        try {
            holder.bind(mLoanPayments.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mLoanPayments.size();
    }
}
