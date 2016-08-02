package glivion.y2k.ui.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.ui.adapter.LoanPaymentAdapter;
import glivion.y2k.ui.database.Y2KDatabase;
import glivion.y2k.ui.model.Loan;
import glivion.y2k.ui.model.LoanPayment;
import glivion.y2k.ui.statemanager.Y2KStateManager;

/**
 * Created by saeedissah on 5/18/16.
 */
public class LoanDetailActivity extends AppCompatActivity {

    private RecyclerView mLoanPayments;
    private ArrayList<LoanPayment> mLoanPaymentArrayList;
    private LoanPaymentAdapter mLoanPaymentAdapter;
    private Y2KDatabase mDatabase;
    private TextView mAmountPaid;
    private TextView mAmountOwing;
    private Y2KStateManager mStateManager;
    private TextInputLayout mTextInputLayout;
    private Loan mLoan;
    private TextView mLoanDueDate;
    private View mAddPayment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_back);
        }
        setSupportActionBar(toolbar);
        mLoan = getIntent().getParcelableExtra("loan");

        final View noPayment = findViewById(R.id.no_payment);
        mDatabase = new Y2KDatabase(this);
        mAddPayment = findViewById(R.id.add_payment);
        mAddPayment.setScaleX(0.0F);
        mAddPayment.setScaleY(0.0F);

        if (mAddPayment != null) {
            mAddPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mLoan.isPaid()) {
                        Snackbar snackbar = Snackbar.make(mAddPayment, "This loan has been cleared", Snackbar.LENGTH_LONG);
                        ColoredSnakBar.get(snackbar, R.color.colorAccentLoans).show();
                        return;
                    }

                    final double[] amount = new double[1];
                    MaterialDialog dialog = new MaterialDialog.Builder(LoanDetailActivity.this)
                            .customView(R.layout.add_payment, false)
                            .title("Add Payment")
                            .positiveText("Add")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    String dateCreated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                                    if (mDatabase.addPayment(mLoan.getmLoanId(), amount[0], dateCreated)) {
                                        LoanPayment loanPayment = new LoanPayment(-1, dateCreated, amount[0], dateCreated);
                                        mLoan.addPayment(loanPayment);
                                        //mLoanPaymentArrayList.add(loanPayment);
                                        mLoanPaymentAdapter.notifyDataSetChanged();
                                        updateLoanView(mLoan);
                                        showOrHideView(noPayment);
                                        Log.v("Called", "Called");
                                    }
                                }
                            })
                            .negativeText("Cancel").build();
                    View view = dialog.getCustomView();
                    final View okayButton = dialog.getActionButton(DialogAction.POSITIVE);
                    okayButton.setEnabled(false);
                    if (view != null) {
                        mTextInputLayout = (TextInputLayout) view.findViewById(R.id.payment_amount_layout);
                        EditText amountPaid = (EditText) view.findViewById(R.id.payment_amount);
                        amountPaid.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                double amountLeft = -1;
                                mTextInputLayout.setErrorEnabled(true);
                                if (!s.toString().isEmpty()) {
                                    amountLeft = mLoan.getAmountOwing() - Double.parseDouble(s.toString());
                                    if (amountLeft < 0) {
                                        mTextInputLayout.setError(getString(R.string.amount_entered_exceeds));
                                    } else {
                                        amount[0] = Double.parseDouble(s.toString());
                                        mTextInputLayout.setErrorEnabled(false);
                                    }
                                }
                                okayButton.setEnabled(!s.toString().isEmpty() && amountLeft >= 0);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }
                    dialog.show();
                }
            });
        }


        TextView loanAmount = (TextView) findViewById(R.id.loan_amount);
        TextView loanInterestAmount = (TextView) findViewById(R.id.loan_amount_interest);
        mLoanDueDate = (TextView) findViewById(R.id.date_created);
        mAmountPaid = (TextView) findViewById(R.id.amount_paid);
        TextView amountToPay = (TextView) findViewById(R.id.amount_to_pay);
        mAmountOwing = (TextView) findViewById(R.id.amount_owing);
        TextView loanTitle = (TextView) findViewById(R.id.loan_title);


        mStateManager = new Y2KStateManager(this);
        if (loanTitle != null) {
            loanTitle.setText(mLoan.getmLoanTitle());
        }
        if (loanAmount != null) {
            loanAmount.setText("Total: " + mStateManager.getCurrency() + "" + mLoan.getmLoanAmount());
        }
        if (mAmountPaid != null) {
            mAmountPaid.setText("Paid: " + mStateManager.getCurrency() + "" + mLoan.getmAmountPaid());
        }
        if (loanInterestAmount != null) {
            loanInterestAmount.setText("Loan Interest: " + mStateManager.getCurrency() + "" + mLoan.getmLoanInterest());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateCreated = null;
        try {
            dateCreated = format.parse(mLoan.getmDueDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated != null ? dateCreated.getTime() : 0, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();
        double amountWithInterest = mLoan.getmLoanInterest() + mLoan.getmLoanAmount();
        if (amountToPay != null) {
            amountToPay.setText("Amount to be Paid: " + mStateManager.getCurrency() + "" + amountWithInterest);
        }
        if (mLoanDueDate != null) {
            mLoanDueDate.setText("Due: " + relativeTime);
        }
        if (mAmountOwing != null) {
            if (!mLoan.isPaid()) {
                mAmountOwing.setText("Owing: " + mStateManager.getCurrency() + (amountWithInterest - mLoan.getmAmountPaid()));
            } else {
                mAmountOwing.setText(R.string.cleared);
                mLoanDueDate.setVisibility(View.GONE);
            }
        }
        mLoanPaymentArrayList = mLoan.getmLoanPayments();
        Log.v("Well", "" + mLoanPaymentArrayList.size());
        mLoanPaymentAdapter = new LoanPaymentAdapter(mLoanPaymentArrayList);
        mLoanPayments = (RecyclerView) findViewById(R.id.loan_payments);
        if (mLoanPayments != null) {
            mLoanPayments.setAdapter(mLoanPaymentAdapter);
        }
        showOrHideView(noPayment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLoan.isPaid()) {
            mAddPayment.animate().scaleX(1.0F).scaleY(1.0F).setDuration(1000).start();
        }
    }

    private void showOrHideView(View noPayment) {
        if (mLoanPaymentArrayList.size() == 0) {
            if (noPayment != null) {
                noPayment.setVisibility(View.VISIBLE);
            }
            mLoanPayments.setVisibility(View.GONE);
        } else {
            if (noPayment != null) {
                noPayment.setVisibility(View.GONE);
            }
            mLoanPayments.setVisibility(View.VISIBLE);
        }
    }

    private void updateLoanView(Loan loan) {
        if (mAmountPaid != null) {
            mAmountPaid.setText("Paid: " + mStateManager.getCurrency() + loan.getmAmountPaid());
        }
        if (mAmountOwing != null) {
            if (!loan.isPaid()) {
                mAmountOwing.setText("Owing: " + mStateManager.getCurrency() + loan.getmAmountLeft());
            } else {
                mAmountOwing.setText(R.string.cleared);
                mLoanDueDate.setVisibility(View.GONE);
                mAddPayment.animate().scaleX(0.0F).scaleY(0.0F).start();
            }
        }
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
