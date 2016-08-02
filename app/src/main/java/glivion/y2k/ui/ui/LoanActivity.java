package glivion.y2k.ui.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.ui.adapter.LoanAdapter;
import glivion.y2k.ui.database.Y2KDatabase;
import glivion.y2k.ui.listener.RecyclerItemClickListener;
import glivion.y2k.ui.model.Loan;

/**
 * Created by saeedissah on 5/17/16.
 */
public class LoanActivity extends AppCompatActivity {

    private static final int ADD_LOAN_REQUEST = 100;
    private RecyclerView mLoanList;
    private View mNoLoans;
    private ArrayList<Loan> mLoans;
    private Y2KDatabase mDatabase;
    private GoogleProgressBar mProgressBar;
    private LoanAdapter mLoanAdapter;
    private MultiStateToggleButton mMultiStateToggleButton;

    private View mAddLoan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_back);
        }
        mAddLoan = findViewById(R.id.add_loan);
        mAddLoan.setScaleX(0f);
        mAddLoan.setScaleY(0f);
        if (mAddLoan != null) {
            mAddLoan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addLoanActivity = new Intent(LoanActivity.this, AddLoanActivity.class);
                    startActivityForResult(addLoanActivity, ADD_LOAN_REQUEST);
                }
            });
        }
        mProgressBar = (GoogleProgressBar) findViewById(R.id.progress_bar);
        mNoLoans = findViewById(R.id.no_loan);
        mLoanList = (RecyclerView) findViewById(R.id.loans);
        mLoanList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Loan loan = mLoans.get(position);
                Log.v("Loan Payment", "" + loan.getmLoanPayments().size());
                Intent intent = new Intent(LoanActivity.this, LoanDetailActivity.class);
                intent.putExtra("loan", loan);
                startActivity(intent);
            }
        }));
        mLoans = new ArrayList<>();
        mLoanAdapter = new LoanAdapter(mLoans);
        mLoanList.setAdapter(mLoanAdapter);
        setSupportActionBar(toolbar);

        mMultiStateToggleButton = (MultiStateToggleButton) this.findViewById(R.id.mstb_multi_id);
        if (mMultiStateToggleButton != null) {
            mMultiStateToggleButton.setColorRes(R.color.colorAccentLoans, R.color.white);
            mMultiStateToggleButton.setValue(0);
            mMultiStateToggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
                @Override
                public void onValueChanged(int value) {
                    Log.v("Value", "" + value);
                    new GetLoans().execute(--value);
                }
            });
        }
    }

    private class GetLoans extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            mDatabase = new Y2KDatabase(LoanActivity.this);
            mLoans.clear();
            mLoans.addAll(mDatabase.getLoans(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
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
    protected void onResume() {
        super.onResume();
        mAddLoan.animate().scaleX(1.0F).scaleY(1.0F).setDuration(1000).start();
        new GetLoans().execute(mMultiStateToggleButton.getValue() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_LOAN_REQUEST && resultCode == RESULT_OK) {
        }
    }
}
