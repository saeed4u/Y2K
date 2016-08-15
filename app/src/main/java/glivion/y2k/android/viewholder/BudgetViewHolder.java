package glivion.y2k.android.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.model.Budget;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by blanka on 8/7/2016.
 */
public class BudgetViewHolder extends RecyclerView.ViewHolder {

    private View mBudgetColor;
    private TextView mBudgetTitle;
    private TextView mBudgetDate;
    private TextView mBudgetAmount;
    private Y2KStateManager mStateManager;

    public BudgetViewHolder(View itemView) {
        super(itemView);
        mBudgetColor = itemView.findViewById(R.id.budget_color);
        mBudgetAmount = (TextView) itemView.findViewById(R.id.budget_total);
        mBudgetTitle = (TextView) itemView.findViewById(R.id.budget_title);
        mBudgetDate = (TextView) itemView.findViewById(R.id.budget_date);
        mStateManager = new Y2KStateManager(itemView.getContext());
    }

    public void bind(Budget budget) throws ParseException {
        mBudgetColor.setBackgroundColor(budget.getmColor());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        mBudgetAmount.setText(mStateManager.getCurrency() + decimalFormat.format(budget.getmBudgetTotal()));
        mBudgetTitle.setText(budget.getmBudgetTitle());
        Log.v("Budget Date",budget.getmDueDate());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateCreated = format.parse(budget.getmCreatedAt());
        String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();
        mBudgetDate.setText(relativeTime);
    }
}
