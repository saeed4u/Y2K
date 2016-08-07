package glivion.y2k.android.ui;

import android.app.Activity;
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

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.adapter.LoanAdapter;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.listener.RecyclerItemClickListener;
import glivion.y2k.android.model.Loan;

/**
 * Created by saeedissah on 5/17/16.
 */
public class LoanFragment extends Fragment {

    private static final int ADD_LOAN_REQUEST = 100;
    private RecyclerView mLoanList;
    private View mNoLoans;
    private ArrayList<Loan> mLoans;
    private Y2KDatabase mDatabase;
    private LoanAdapter mLoanAdapter;
    private MultiStateToggleButton mMultiStateToggleButton;

    private View mAddLoan;
    private MainActivity mDashBoard;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDashBoard = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loan_activity, container, false);
        mAddLoan = view.findViewById(R.id.add_loan);
        mAddLoan.setScaleX(0f);
        mAddLoan.setScaleY(0f);
        if (mAddLoan != null) {
            mAddLoan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addLoanActivity = new Intent(mDashBoard, AddLoanActivity.class);
                    startActivityForResult(addLoanActivity, ADD_LOAN_REQUEST);
                }
            });
        }
        mNoLoans = view.findViewById(R.id.no_loan);
        mLoanList = (RecyclerView) view.findViewById(R.id.loans);
        mLoanList.addOnItemTouchListener(new RecyclerItemClickListener(mDashBoard, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Loan loan = mLoans.get(position);
                Log.v("Loan Payment", "" + loan.getmLoanPayments().size());
                Intent intent = new Intent(mDashBoard, LoanDetailActivity.class);
                intent.putExtra("loan", loan);
                startActivity(intent);
            }
        }));
        mLoans = new ArrayList<>();
        mLoanAdapter = new LoanAdapter(mLoans);
        mLoanList.setAdapter(mLoanAdapter);

        mMultiStateToggleButton = (MultiStateToggleButton) view.findViewById(R.id.mstb_multi_id);
        if (mMultiStateToggleButton != null) {
            mMultiStateToggleButton.setColorRes(R.color.colorPrimaryDashBoard, R.color.white);
            mMultiStateToggleButton.setValue(0);
            mMultiStateToggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
                @Override
                public void onValueChanged(int value) {
                    Log.v("Value", "" + value);
                    new GetLoans().execute(--value);
                }
            });
        }
        return view;
    }



    private class GetLoans extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            mDatabase = new Y2KDatabase(mDashBoard);
            mLoans.clear();
            mLoans.addAll(mDatabase.getLoans(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mLoans.size() == 0) {
                mLoanList.setVisibility(View.GONE);
                mNoLoans.setVisibility(View.VISIBLE);
            } else {
                mLoanList.setVisibility(View.VISIBLE);
                mNoLoans.setVisibility(View.GONE);
                mLoanAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddLoan.animate().scaleX(1.0F).scaleY(1.0F).setDuration(500).start();
        new GetLoans().execute(mMultiStateToggleButton.getValue() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_LOAN_REQUEST && resultCode == Activity.RESULT_OK) {
        }
    }
}
