package glivion.y2k.android.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.model.Loan;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by saeedissah on 5/18/16.
 */
public class LoanViewHolder extends RecyclerView.ViewHolder {

    private TextView mLoanTitle;
    private TextView mLoanAmount;
    private TextView mAmountOwing;
    private TextView mDateCreated;
    private Y2KStateManager mStateManager;
    private View mLoanColor;
    private TextView mAmountPaid;

    public LoanViewHolder(View itemView) {
        super(itemView);
        mStateManager = new Y2KStateManager(itemView.getContext());
        mLoanTitle = (TextView) itemView.findViewById(R.id.loan_title);
        mLoanAmount = (TextView) itemView.findViewById(R.id.loan_amount);
        mAmountOwing = (TextView) itemView.findViewById(R.id.amount_owing);
        mDateCreated = (TextView) itemView.findViewById(R.id.date_created);
        mLoanColor = itemView.findViewById(R.id.loan_color);
        mAmountPaid = (TextView) itemView.findViewById(R.id.amount_paid);
    }

    public void bind(Loan loan) throws ParseException {

        mLoanTitle.setText(loan.getmLoanTitle());
        double interest = loan.getmLoanInterest();
        double amount = loan.getmLoanAmount();
        double totalAmount = amount + interest;
        double amountPaid = loan.getmAmountPaid();
        double amountLeft = totalAmount - amountPaid;

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        mLoanAmount.setText(mStateManager.getCurrency() + decimalFormat.format(totalAmount));

        if (loan.isPaid()) {
            mAmountOwing.setText(R.string.cleared);
        } else {
            mAmountOwing.setText("Owing: " + mStateManager.getCurrency() + decimalFormat.format(amountLeft));
        }
        mAmountPaid.setText("Paid: " + mStateManager.getCurrency() + decimalFormat.format(amountPaid));
        int color = loan.getmLoanColor();
        mLoanColor.setBackgroundColor(color);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date dateCreated = format.parse(loan.getmDateCreated());
        String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();
        mDateCreated.setText(relativeTime);
    }
}
