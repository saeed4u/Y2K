package glivion.y2k.android.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.adapter.BudgetAdapter;
import glivion.y2k.android.adapter.LoanAdapter;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.model.Budget;
import glivion.y2k.android.model.Loan;

/**
 * Created by blanka on 8/6/2016.
 */
public class BudgetFragment extends Fragment {

    private static final int ADD_LOAN_REQUEST = 100;

    private MultiStateToggleButton mMultiStateToggleButton;
    private View mAddBudget;
    private MainActivity mDashBoard;

    private RecyclerView mBudgetList;
    private View mNoBudget;
    private ArrayList<Budget> mBudgets;
    private Y2KDatabase mDatabase;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDashBoard = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.budget_fragment, container, false);
        mMultiStateToggleButton = (MultiStateToggleButton) view.findViewById(R.id.income_ex_selector);
        mAddBudget = view.findViewById(R.id.add_budget);
        mAddBudget.setScaleX(0f);
        mAddBudget.setScaleY(0f);
        mAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBudgetList = (RecyclerView) view.findViewById(R.id.budget);
        mNoBudget = view.findViewById(R.id.no_budget);
        mDatabase = new Y2KDatabase(mDashBoard);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddBudget.animate().scaleX(1.0F).scaleY(1.0F).setDuration(500).start();
        new GetBudgets().execute(mMultiStateToggleButton.getValue() - 1);
    }

    private class GetBudgets extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            if (params[0] != 0) {
                mBudgets = mDatabase.getBudgets(params[0]);
            } else {
                mBudgets = mDatabase.getBudgets();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!mBudgets.isEmpty()) {
                mNoBudget.setVisibility(View.GONE);
                BudgetAdapter budgetAdapter = new BudgetAdapter(mBudgets);
                mBudgetList.setAdapter(budgetAdapter);
            } else {
                mBudgetList.setVisibility(View.GONE);
                mNoBudget.setVisibility(View.VISIBLE);
            }
        }
    }
}
