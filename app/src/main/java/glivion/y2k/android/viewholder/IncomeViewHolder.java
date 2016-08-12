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
import glivion.y2k.android.model.IncomeExpenditure;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by blanka on 8/6/2016.
 */
public class IncomeViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;
    private TextView mAmount;
    private TextView mDateCreated;
    private Y2KStateManager mStateManager;
    private View mColor;

    public IncomeViewHolder(View itemView) {
        super(itemView);
        mStateManager = new Y2KStateManager(itemView.getContext());
        mTitle = (TextView) itemView.findViewById(R.id.income_title);
        mAmount = (TextView) itemView.findViewById(R.id.income_amount);
        mDateCreated = (TextView) itemView.findViewById(R.id.date_created);
        mColor = itemView.findViewById(R.id.loan_color);
    }

    public void bindData(IncomeExpenditure incomeExpenditure) throws ParseException {
        String title = incomeExpenditure.getmTitle();
        double amount = incomeExpenditure.getmAmount();
        int color = incomeExpenditure.getmCategory().getmCatColor();
        mColor.setBackgroundColor(color);
        mTitle.setText(title);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        mAmount.setText(mStateManager.getCurrency() + decimalFormat.format(amount));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateCreated = format.parse(incomeExpenditure.getmPayDate());
        String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();
        mDateCreated.setText(relativeTime);
        mDateCreated.setTextColor(color);
    }
}
