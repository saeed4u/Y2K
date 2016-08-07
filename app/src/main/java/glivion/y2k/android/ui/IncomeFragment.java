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
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.adapter.IncomeAdapter;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.listener.RecyclerItemClickListener;
import glivion.y2k.android.model.IncomeExpenditure;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by blanka on 8/6/2016.
 */
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

    private TextView mTotalIncomeTv;
    private Y2KStateManager mStateManager;
    Calendar calendar = Calendar.getInstance();

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
                Intent intent = new Intent(mMainActivity, IncomeDetail.class);
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
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setMinimalDaysInFirstWeek(7);

        mWeekNumberFromCalendar = calendar.get(Calendar.WEEK_OF_YEAR);
        View mPreviousWeek = view.findViewById(R.id.previous_week);
        mTotalIncomeTv = (TextView) view.findViewById(R.id.total_amount);
        View mNextWeek = view.findViewById(R.id.next_week);
        mPreviousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mWeekNumberFromCalendar -= 1;
                Log.v("Week Number Next", "" + mWeekNumberFromCalendar);
                mWeekNumber.setText("Week " + mWeekNumberFromCalendar + ", " + calendar.get(Calendar.YEAR));
                mWeekDateRange.setText(getDateRange(mWeekNumberFromCalendar));
                new GetIncome().execute();
            }
        });
        mNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mWeekNumberFromCalendar += 1;
                Log.v("Week Number", "" + mWeekNumberFromCalendar);
                mWeekNumber.setText("Week " + mWeekNumberFromCalendar + ", " + calendar.get(Calendar.YEAR));
                mWeekDateRange.setText(getDateRange(mWeekNumberFromCalendar));
                new GetIncome().execute();
            }
        });
        mWeekDateRange = (TextView) view.findViewById(R.id.week_date);
        mWeekNumber = (TextView) view.findViewById(R.id.week_number);

        Calendar calendar = Calendar.getInstance();
        mWeekNumberFromCalendar -= 2;
        mWeekNumber.setText("Week " + mWeekNumberFromCalendar + ", " + calendar.get(Calendar.YEAR));
        mWeekDateRange.setText(getDateRange(mWeekNumberFromCalendar));
        return view;
    }

    private String getDateRange(int weekNo) {
        Calendar calendar = Calendar.getInstance();
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
    }

    private class GetIncome extends AsyncTask<Void, Void, ArrayList<IncomeExpenditure>> {

        @Override
        protected ArrayList<IncomeExpenditure> doInBackground(Void... params) {
            Y2KDatabase y2KDatabase = new Y2KDatabase(mMainActivity);
            int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            return y2KDatabase.getIncomeOrExpenditure(isFromIncome ? Constants.IS_INCOME : Constants.IS_EXPENDITURE, week);
        }

        @Override
        protected void onPostExecute(ArrayList<IncomeExpenditure> incomeExpenditures) {
            super.onPostExecute(incomeExpenditures);
            double mTotalIncome = 0.0;
            mLoader.setVisibility(View.GONE);
            mIncomeExpenditures = incomeExpenditures;
            if (incomeExpenditures.isEmpty()) {
                mNoIncome.setVisibility(View.VISIBLE);
            } else {
                for (IncomeExpenditure incomeExpenditure : incomeExpenditures) {
                    mTotalIncome += incomeExpenditure.getmAmount();
                }
                mIncomes.setAdapter(new IncomeAdapter(incomeExpenditures));
                mIncomes.setVisibility(View.VISIBLE);
                mNoIncome.setVisibility(View.GONE);
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String amount = decimalFormat.format(mTotalIncome);
            mTotalIncomeTv.setText(mStateManager.getCurrency() + amount);
        }
    }
}
