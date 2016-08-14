package glivion.y2k.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.adapter.IncomeAdapter;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.listener.RecyclerItemClickListener;
import glivion.y2k.android.model.IncomeExpenditure;
import glivion.y2k.android.statemanager.Y2KStateManager;
import glivion.y2k.android.utilities.Utilities;

/**
 * Created by blanka on 8/6/2016.
 */

// TODO: 12/08/2016 Work on getting week number
public class IncomeFragment extends Fragment {

    private MainActivity mMainActivity;
    private RecyclerView mIncomes;
    private View mNoIncome;
    private View mLoader;
    private View mAddIncome;
    private TextView mWeekNumber;
    private TextView mWeekDateRange;

    private ArrayList<IncomeExpenditure> mIncomeExpenditures;

    private static final int ADD_INCOME = 100;
    private int mWeekNumberFromCalendar;
    private int mMonthCalendar = 0;

    private TextView mTotalIncomeTv;
    private Y2KStateManager mStateManager;
    Calendar calendar;
    private View mPrevious;
    private View mNext;
    private int mYear;

    private boolean isFromIncome = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mStateManager = new Y2KStateManager(mMainActivity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromIncome = getArguments().getBoolean("is_income");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.income, container, false);
        mIncomes = (RecyclerView) view.findViewById(R.id.income);
        mIncomes.addOnItemTouchListener(new RecyclerItemClickListener(mMainActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IncomeExpenditure incomeExpenditure = mIncomeExpenditures.get(position);
                Intent intent = new Intent(mMainActivity, IncomeDetailActivity.class);
                intent.putExtra("income", incomeExpenditure);
                startActivity(intent);
            }
        }));
        TextView textView = (TextView) view.findViewById(R.id.total_income);
        String total = getString(R.string.total_income, isFromIncome ? "Income" : "Expenditure");
        textView.setText(total);
        mNoIncome = view.findViewById(R.id.no_income);
        mIncomes.setVisibility(View.GONE);
        mNoIncome.setVisibility(View.GONE);
        mLoader = view.findViewById(R.id.loader);
        mAddIncome = view.findViewById(R.id.add_income);
        mAddIncome.setScaleX(0f);
        mAddIncome.setScaleY(0f);
        if (mAddIncome != null) {
            mAddIncome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addLoanActivity = new Intent(mMainActivity, AddIncome.class);
                    addLoanActivity.putExtra("is_income", isFromIncome);
                    startActivityForResult(addLoanActivity, ADD_INCOME);
                }
            });
        }
        TextView noIncome = (TextView) view.findViewById(R.id.no_income_ex);
        noIncome.setText(getString(R.string.no_income, isFromIncome ? "Income" : "Expenditure"));
        calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setMinimalDaysInFirstWeek(7);
        mYear = calendar.get(Calendar.YEAR);
        mWeekNumberFromCalendar = calendar.get(Calendar.WEEK_OF_YEAR);
        mMonthCalendar = calendar.get(Calendar.MONTH);
        mPrevious = view.findViewById(R.id.previous_week);
        mTotalIncomeTv = (TextView) view.findViewById(R.id.total_amount);
        mNext = view.findViewById(R.id.next_week);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = calendar.get(Calendar.YEAR);
                calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31);
                int totalWeekInYear = calendar.get(Calendar.WEEK_OF_YEAR);
                String queryType = mStateManager.getQueryType();
                if (queryType.equalsIgnoreCase("monthly")) {
                    mMonthCalendar++;
                    if (mMonthCalendar == 12) {
                        mMonthCalendar = 0;
                        mYear++;
                        calendar.set(Calendar.YEAR, mYear);
                        Log.v("Year Monthly", String.valueOf(mYear));
                    }
                } else if (queryType.equalsIgnoreCase("weekly")) {
                    mWeekNumberFromCalendar++;
                    if (mWeekNumberFromCalendar == totalWeekInYear + 1) {
                        mWeekNumberFromCalendar = 1;
                        mYear++;
                        calendar.set(Calendar.YEAR, mYear);
                        Log.v("Year weekly", String.valueOf(mYear));
                    }
                }
                getDateType(mYear);
                new GetIncome().execute();
            }
        });
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = calendar.get(Calendar.YEAR);
                calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31);
                String queryType = mStateManager.getQueryType();
                if (queryType.equalsIgnoreCase("monthly")) {
                    mMonthCalendar--;
                    if (mMonthCalendar == -1) {
                        mMonthCalendar = 11;
                        mYear--;
                        calendar.set(Calendar.YEAR, mYear);
                    }
                } else if (queryType.equalsIgnoreCase("weekly")) {
                    mWeekNumberFromCalendar--;
                    if (mWeekNumberFromCalendar == 0) {
                        mWeekNumberFromCalendar = calendar.get(Calendar.WEEK_OF_YEAR);
                        mYear--;
                        calendar.set(Calendar.YEAR, mYear);
                    }
                }
                getDateType(IncomeFragment.this.mYear);
                new GetIncome().execute();
            }
        });
        mWeekDateRange = (TextView) view.findViewById(R.id.week_date);
        mWeekNumber = (TextView) view.findViewById(R.id.week_number);

        return view;
    }

    private String getDateRange(int weekNo, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setMinimalDaysInFirstWeek(0);
        calendar.set(Calendar.WEEK_OF_YEAR, weekNo);


        String format = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());

        Calendar first = (Calendar) calendar.clone();
        first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

        Calendar last = (Calendar) first.clone();
        last.add(Calendar.DAY_OF_YEAR, 6);

        return simpleDateFormat.format(first.getTime()) + " - " + simpleDateFormat.format(last.getTime());
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetIncome().execute();
        mAddIncome.animate().scaleX(1.0F).scaleY(1.0F).setDuration(500).start();
        getDateType(mYear);
    }

    private void getDateType(int year) {
        String queryType = mStateManager.getQueryType();
        if (queryType.equalsIgnoreCase("weekly")) {
            mWeekNumber.setText("Week " + mWeekNumberFromCalendar + ", " + calendar.get(Calendar.YEAR));
            mWeekDateRange.setText(getDateRange(mWeekNumberFromCalendar, year));
            mWeekDateRange.setVisibility(View.VISIBLE);
        } else if (queryType.equalsIgnoreCase("monthly")) {
            mWeekNumber.setText(Utilities.getMonthName(mMonthCalendar) + ", " + calendar.get(Calendar.YEAR));
            mWeekDateRange.setVisibility(View.GONE);
        }
    }

    private class GetIncome extends AsyncTask<Void, Void, ArrayList<IncomeExpenditure>> {

        @Override
        protected ArrayList<IncomeExpenditure> doInBackground(Void... params) {
            Y2KDatabase y2KDatabase = new Y2KDatabase(mMainActivity);
            String queryType = mStateManager.getQueryType();
            return y2KDatabase.getIncomeOrExpenditure(isFromIncome ? Constants.IS_INCOME : Constants.IS_EXPENDITURE, queryType.equalsIgnoreCase("weekly") ? mWeekNumberFromCalendar : mMonthCalendar, queryType.equalsIgnoreCase("weekly"));
        }

        @Override
        protected void onPostExecute(ArrayList<IncomeExpenditure> incomeExpenditures) {
            super.onPostExecute(incomeExpenditures);
            double mTotalIncome = 0.0;
            mLoader.setVisibility(View.GONE);
            mIncomeExpenditures = incomeExpenditures;
            if (incomeExpenditures.isEmpty()) {
                mNoIncome.setVisibility(View.VISIBLE);
                mIncomes.setVisibility(View.GONE);
            } else {
                for (IncomeExpenditure incomeExpenditure : incomeExpenditures) {
                    mTotalIncome += incomeExpenditure.getmAmount();
                }
                mIncomes.setAdapter(new IncomeAdapter(incomeExpenditures));
                mIncomes.setVisibility(View.VISIBLE);
                mNoIncome.setVisibility(View.GONE);
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String amount = decimalFormat.format(mTotalIncome);
            mTotalIncomeTv.setText(mStateManager.getCurrency() + amount);
        }
    }
}
