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
import glivion.y2k.android.model.LoanPayment;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by saeedissah on 5/19/16.
 */
public class LoanPaymentViewHolder extends RecyclerView.ViewHolder {

    private TextView mAmountPaid;
    private TextView mDateCreated;

    public LoanPaymentViewHolder(View itemView) {
        super(itemView);

        mAmountPaid = (TextView) itemView.findViewById(R.id.amount_paid);
        mDateCreated = (TextView) itemView.findViewById(R.id.date_paid);

    }

    public void bind(LoanPayment loanPayment) throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Y2KStateManager mStateManager = new Y2KStateManager(mAmountPaid.getContext());
        mAmountPaid.setText(mStateManager.getCurrency() + decimalFormat.format(loanPayment.getmAmountPaid()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date dateCreated = format.parse(loanPayment.getmDatePaid());
        String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();

        mDateCreated.setText(relativeTime);


    }
}
