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
import android.widget.LinearLayout;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.adapter.BudgetAdapter;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.listener.RecyclerItemClickListener;
import glivion.y2k.android.model.Budget;

/**
 * Created by blanka on 8/6/2016.
 */
public class BudgetFragment extends Fragment {

    private static final int ADD_BUDGET_REQUEST = 100;

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
        if (mMultiStateToggleButton != null) {
            mMultiStateToggleButton.setColorRes(R.color.colorPrimaryDashBoard, R.color.white);
            mMultiStateToggleButton.setValue(0);
            mMultiStateToggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
                @Override
                public void onValueChanged(int value) {
                    Log.v("Value", "" + value);
                    new GetBudgets().execute(--value);
                }
            });
        }
        mAddBudget = view.findViewById(R.id.add_budget);
        mAddBudget.setScaleX(0f);
        mAddBudget.setScaleY(0f);
        mAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mDashBoard, AddBudgetActivity.class);
                startActivityForResult(intent, ADD_BUDGET_REQUEST);
            }
        });
        mBudgetList = (RecyclerView) view.findViewById(R.id.budget);
        mBudgetList.addOnItemTouchListener(new RecyclerItemClickListener(mDashBoard, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Budget budget = mBudgets.get(position);
                Intent intent = new Intent(mDashBoard, BudgetDetailActivity.class);
                intent.putExtra("budget", budget);
                startActivity(intent);
            }
        }));
        mNoBudget = view.findViewById(R.id.no_budget);
        mDatabase = new Y2KDatabase(mDashBoard);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddBudget.animate().scaleX(1.0F).scaleY(1.0F).setDuration(500).start();
        mDatabase = new Y2KDatabase(mDashBoard);
        new GetBudgets().execute(-1);
    }

    private class GetBudgets extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            mDatabase = new Y2KDatabase(mDashBoard);
            if (params[0] != -1) {
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
                mBudgetList.setVisibility(View.VISIBLE);
            } else {
                mBudgetList.setVisibility(View.GONE);
                mNoBudget.setVisibility(View.VISIBLE);
            }
        }
    }
}
