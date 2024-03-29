package glivion.y2k.android.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.lang.WordUtils;
import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import glivion.y2k.R;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.model.Budget;
import glivion.y2k.android.model.Category;
import glivion.y2k.android.model.IncomeExpenditure;
import glivion.y2k.android.model.Loan;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by blanka on 8/6/2016.
 */
public class DashBoardFragment extends Fragment implements OnChartValueSelectedListener {

    private MultiStateToggleButton mMultiStateToggleButton;
    private MainActivity mMainActivity;
    private TextView mQueryType;
    private TextView mTotalIncome;
    private TextView mTotalExpenditure;
    private TextView mSurplus;
    private TextView mSurplusText;
    private TextView mAmountDashboardText;
    private TextView mAmountDashboard;
    private Y2KStateManager mStateManager;
    private Y2KDatabase mY2KDatabase;

    private double mTotalIncomeDouble;
    private double mTotalExpenditureDouble;
    private PieChart mChart;

    private LinearLayout mBudgetDashboard;
    private LinearLayout mLoansDashboard;
    private View mNoIncomeExpense;
    private View mAmountLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mStateManager = new Y2KStateManager(context);
        mY2KDatabase = new Y2KDatabase(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        mMultiStateToggleButton = (MultiStateToggleButton) view.findViewById(R.id.income_or_expenditure);
        if (mMultiStateToggleButton != null) {
            mMultiStateToggleButton.setColorRes(R.color.white, R.color.colorPrimaryDashBoard);
            mMultiStateToggleButton.setValue(0);
            mMultiStateToggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
                @Override
                public void onValueChanged(int value) {
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");

                    fetchData();
                    if (value == 0) {
                        mAmountDashboardText.setText(R.string.total_income_);
                        mAmountDashboard.setText(mStateManager.getCurrency() + decimalFormat.format(mTotalIncomeDouble));
                    } else {
                        mAmountDashboardText.setText(R.string.total_expenditure);
                        mAmountDashboard.setText(mStateManager.getCurrency() + decimalFormat.format(mTotalExpenditureDouble));
                    }
                    try {
                        getExpiringBudgets();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        mNoIncomeExpense = view.findViewById(R.id.no_income_ex);
        mAmountLayout = view.findViewById(R.id.amount_layout);

        mChart = (PieChart) view.findViewById(R.id.piechart);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(50f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);

        mQueryType = (TextView) view.findViewById(R.id.query_type);
        mTotalExpenditure = (TextView) view.findViewById(R.id.total_expenditure);
        mTotalIncome = (TextView) view.findViewById(R.id.total_income);
        mSurplusText = (TextView) view.findViewById(R.id.surplus_deficit_text);
        mSurplus = (TextView) view.findViewById(R.id.surplus_deficit);
        mAmountDashboard = (TextView) view.findViewById(R.id.amount_dashboard);

        mBudgetDashboard = (LinearLayout) view.findViewById(R.id.budget_dashboard);
        mLoansDashboard = (LinearLayout) view.findViewById(R.id.loan_dashboard);
        mAmountDashboardText = (TextView) view.findViewById(R.id.amount_dashboard_text);
        mAmountDashboardText.setText(R.string.total_income_);
        return view;
    }

    private void fetchData() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        final boolean weekly = mStateManager.getQueryType().equalsIgnoreCase("weekly");
        mY2KDatabase = new Y2KDatabase(mMainActivity);
        ArrayList<Category> categories = mY2KDatabase.getCategoriesForDashboard(mMultiStateToggleButton.getValue() == 0 ? Constants.INCOME_CAT : Constants.EXPENDITURE_CAT);
        mChart.setVisibility(View.VISIBLE);
        mAmountDashboardText.setVisibility(View.VISIBLE);
        mNoIncomeExpense.setVisibility(View.GONE);
        HashMap<Category, ArrayList<IncomeExpenditure>> categoryArrayListHashMap = new HashMap<>();
        for (Category category : categories) {
            Log.v("Category Name", "" + category.getmCatId());
            mY2KDatabase = new Y2KDatabase(mMainActivity);
            if (mMultiStateToggleButton.getValue() == 0) {
                Log.v("Category Name", "" + category.getmCatId());
                categoryArrayListHashMap.put(category, weekly ? mY2KDatabase.getTotalWeekly(Constants.IS_INCOME, week, year, category.getmCatId())
                        : mY2KDatabase.getTotalMonthly(Constants.IS_INCOME, month, year, category.getmCatId()));
            } else {
                categoryArrayListHashMap.put(category, weekly ? mY2KDatabase.getTotalWeekly(Constants.IS_EXPENDITURE, week, year, category.getmCatId())
                        : mY2KDatabase.getTotalMonthly(Constants.IS_EXPENDITURE, month, year, category.getmCatId()));
            }
        }
        setData(categoryArrayListHashMap);

    }

    private boolean isEmpty = true;

    private double getCategoryTotal(ArrayList<IncomeExpenditure> incomeExpenditures) {
        double total = 0.0;
        for (IncomeExpenditure incomeExpenditure : incomeExpenditures) {
            total += incomeExpenditure.getmAmount();
            isEmpty = false;
        }
        return total;
    }

    private void setData(HashMap<Category, ArrayList<IncomeExpenditure>> inCategoryArrayListHashMap) {

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        Set<Category> categories = inCategoryArrayListHashMap.keySet();
        for (Category category : categories) {
            Log.v("Category category", "" + category.getmCatId());
            Log.v("Category Value", "Value " + getCategoryTotal(inCategoryArrayListHashMap.get(category)));
            entries.add(new PieEntry((float) getCategoryTotal(inCategoryArrayListHashMap.get(category)), WordUtils.capitalize(category.getmCatName())));
            colors.add(category.getmCatColor());
        }

        if (isEmpty) {
            mChart.setVisibility(View.GONE);
            mAmountDashboardText.setVisibility(View.GONE);
            mNoIncomeExpense.setVisibility(View.VISIBLE);
        } else {
            mChart.setVisibility(View.VISIBLE);
            mAmountDashboardText.setVisibility(View.VISIBLE);
            mNoIncomeExpense.setVisibility(View.GONE);
        }

        PieDataSet dataSet = new PieDataSet(entries, mMultiStateToggleButton.getValue() == 0 ? "Income" : "Expenditure");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Y2K\nDeveloped by Glivion");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 3, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 3, s.length() - 4, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 4, s.length() - 4, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 4, s.length() - 4, 0);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mMainActivity, R.color.colorAccentDashBoard)), s.length() - 7, s.length(), 0);
        return s;
    }

    private void getExpiringBudgets() throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        mY2KDatabase = new Y2KDatabase(mMainActivity);
        String date = simpleDateFormat.format(new Date());
        Log.v("Date = ", date);
        ArrayList<Budget> budgets = mY2KDatabase.getBudgets(mMultiStateToggleButton.getValue(), date);
        Log.v("Budget Size", "" + budgets.size());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


        mBudgetDashboard.removeViews(1, mBudgetDashboard.getChildCount() - 1);

        for (Budget budget : budgets) {
            View view = mMainActivity.getLayoutInflater().inflate(R.layout.budget_item_layout, null);
            view.setTag(budget);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mMainActivity, BudgetDetailActivity.class);
                    intent.putExtra("budget", (Budget) view.getTag());
                    startActivity(intent);
                }
            });
            Date dateCreated = format.parse(budget.getmDueDate());
            String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString();
            TextView budgetName = (TextView) view.findViewById(R.id.budget_item_name);
            TextView budgetAmount = (TextView) view.findViewById(R.id.budget_item_amount);
            budgetName.setText(budget.getmBudgetTitle() + " (" + mStateManager.getCurrency() + decimalFormat.format(budget.getmBudgetTotal()) + ")");
            budgetAmount.setText(relativeTime);
            mBudgetDashboard.addView(view);
        }
        mBudgetDashboard.setVisibility(!budgets.isEmpty() ? View.VISIBLE : View.GONE);

    }


    private void getExpiringLoans() throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        mY2KDatabase = new Y2KDatabase(mMainActivity);
        String date = simpleDateFormat.format(new Date());
        Log.v("Date = ", date);
        ArrayList<Loan> loans = mY2KDatabase.getExpiringLoans(date);
        Log.v("Budget Size", "" + loans.size());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


        mLoansDashboard.removeViews(1, mLoansDashboard.getChildCount() - 1);

        for (Loan loan : loans) {
            View view = mMainActivity.getLayoutInflater().inflate(R.layout.budget_item_layout, null);
            view.setTag(loan);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mMainActivity, LoanDetailActivity.class);
                    intent.putExtra("loan", (Loan) view.getTag());
                    startActivity(intent);
                }
            });
            Date dateCreated = format.parse(loan.getmDueDate());
            String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString();
            TextView budgetName = (TextView) view.findViewById(R.id.budget_item_name);
            TextView budgetAmount = (TextView) view.findViewById(R.id.budget_item_amount);
            budgetName.setText(loan.getmLoanTitle() + " (" + mStateManager.getCurrency() + decimalFormat.format(loan.getmLoanAmount()) + ")");
            budgetAmount.setText(relativeTime);
            mLoansDashboard.addView(view);
        }
        mLoansDashboard.setVisibility(!loans.isEmpty() ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        final boolean weekly = mStateManager.getQueryType().equalsIgnoreCase("weekly");
        mQueryType.setText(weekly ? " This Week" : "This Month");

        fetchData();
        try {
            getExpiringLoans();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            getExpiringBudgets();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                Calendar calendar = Calendar.getInstance();
                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                final double surplus;

                if (weekly) {
                    mY2KDatabase = new Y2KDatabase(mMainActivity);
                    mTotalExpenditureDouble = mY2KDatabase.getTotalWeekly(Constants.IS_EXPENDITURE, week, year);
                    mY2KDatabase = new Y2KDatabase(mMainActivity);
                    mTotalIncomeDouble = mY2KDatabase.getTotalWeekly(Constants.IS_INCOME, week, year);
                    surplus = mTotalIncomeDouble - mTotalExpenditureDouble;
                } else {
                    mY2KDatabase = new Y2KDatabase(mMainActivity);
                    mTotalExpenditureDouble = mY2KDatabase.getTotalMonthly(Constants.IS_EXPENDITURE, month, year);
                    mY2KDatabase = new Y2KDatabase(mMainActivity);
                    mTotalIncomeDouble = mY2KDatabase.getTotalMonthly(Constants.IS_INCOME, month, year);
                    surplus = mTotalIncomeDouble - mTotalExpenditureDouble;
                }

                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        mTotalIncome.setText(mStateManager.getCurrency() + decimalFormat.format(mTotalIncomeDouble));
                        mTotalExpenditure.setText(mStateManager.getCurrency() + decimalFormat.format(mTotalExpenditureDouble));
                        if (surplus < 0) {
                            mSurplusText.setText("Deficit");
                            mSurplus.setTextColor(ContextCompat.getColor(mMainActivity, R.color.owingColor));
                        } else {
                            mSurplusText.setText("Surplus");
                            mSurplus.setTextColor(ContextCompat.getColor(mMainActivity, R.color.colorAccentDashBoard));
                        }

                        mSurplus.setText(mStateManager.getCurrency() + decimalFormat.format(surplus));

                        int value = mMultiStateToggleButton.getValue();
                        if (value == 0) {
                            mAmountDashboard.setText(mStateManager.getCurrency() + decimalFormat.format(mTotalIncomeDouble));
                        } else {
                            mAmountDashboard.setText(mStateManager.getCurrency() + decimalFormat.format(mTotalExpenditureDouble));
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


}
