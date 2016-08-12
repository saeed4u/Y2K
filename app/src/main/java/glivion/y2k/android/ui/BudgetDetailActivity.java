package glivion.y2k.android.ui;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang.WordUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.model.Budget;
import glivion.y2k.android.model.BudgetItem;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by saeed on 12/08/2016.
 */
public class BudgetDetailActivity extends ItemDetailActivity {

    private Toolbar mToolbar;
    private Budget mBudget;

    private Y2KStateManager mStateManager;

    private LinearLayout mBudgetItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_details);
        mBudget = getIntent().getParcelableExtra("budget");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mStateManager = new Y2KStateManager(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(WordUtils.capitalize(mBudget.getmBudgetTitle()));
        }

        TextView incomeTitle = (TextView) findViewById(R.id.budget_title);
        TextView incomeAmount = (TextView) findViewById(R.id.budget_amount);
        TextView budgetDate = (TextView) findViewById(R.id.date_created);
        TextView budgetType = (TextView) findViewById(R.id.budget_type);
        mBudgetItems = (LinearLayout) findViewById(R.id.budget_items);

        setBudgetItems();

        if (incomeTitle != null) {
            incomeTitle.setText(mBudget.getmBudgetTitle());
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (incomeAmount != null) {
            incomeAmount.setText(mStateManager.getCurrency() + decimalFormat.format(mBudget.getmBudgetTotal()));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateCreated = null;
        try {
            dateCreated = format.parse(mBudget.getmDueDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated != null ? dateCreated.getTime() : 0, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();

        if (budgetDate != null) {
            budgetDate.setText(relativeTime);
        }

        if (budgetType != null) {
            budgetType.setText(mBudget.isIncome() ? "Income Budget" : "Expenditure Budget");
        }

        getColors();

        ArrayList<BudgetItem> budgetItems = mBudget.getmBudgetItems();
        Log.v("Size", "Size = " + budgetItems.size());

    }

    private void getColors() {
        mToolbar.setBackgroundColor(getColor(210, mBudget.getmColor()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(255, mBudget.getmColor()));
        }
        supportStartPostponedEnterTransition();

    }

    private void setBudgetItems() {
        for (BudgetItem budgetItem : mBudget.getmBudgetItems()) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            View view = getLayoutInflater().inflate(R.layout.budget_item_layout, null);
            TextView budgetItemName = (TextView) view.findViewById(R.id.budget_item_name);
            TextView budgetItemAmount = (TextView) view.findViewById(R.id.budget_item_amount);
            budgetItemAmount.setText(mStateManager.getCurrency() + decimalFormat.format(budgetItem.getmBudgetAmount()));
            budgetItemName.setText(budgetItem.getmItemName());
            mBudgetItems.addView(view);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int getColor(int alpha, int color) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
