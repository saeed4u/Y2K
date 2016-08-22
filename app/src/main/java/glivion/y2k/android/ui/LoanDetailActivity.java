package glivion.y2k.android.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
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

import org.apache.commons.lang.WordUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.adapter.LoanPaymentAdapter;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.model.Loan;
import glivion.y2k.android.model.LoanPayment;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by saeedissah on 5/18/16.
 */
public class LoanDetailActivity extends ItemDetailActivity {

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
    private Toolbar mToolbar;
    private View mLoanDueDateLayout;

    private double mAmountEntered;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mLoan = getIntent().getParcelableExtra("loan");

        Log.v("Amount Left Loan ", "" + mLoan.getmAmountLeft());

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(mLoan.getmLoanId());

        mToolbar.setTitle(mLoan.getmLoanTitle());
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(WordUtils.capitalize(mLoan.getmLoanTitle()));
        }
        getColors();


        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        mDatabase = new Y2KDatabase(this);
        mAddPayment = findViewById(R.id.add_payment);
        if (mAddPayment != null) {
            mAddPayment.setScaleX(0.0F);
            mAddPayment.setScaleY(0.0F);
        }

        if (mAddPayment != null) {
            mAddPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mLoan.isPaid()) {
                        Snackbar snackbar = Snackbar.make(mAddPayment, "This loan has been cleared", Snackbar.LENGTH_LONG);
                        ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
                        return;
                    }

                    MaterialDialog dialog = new MaterialDialog.Builder(LoanDetailActivity.this)
                            .customView(R.layout.add_payment, false)
                            .title("Add Payment for \"" + mLoan.getmLoanTitle() + "\"")
                            .positiveText("Add")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    String dateCreated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                                    mDatabase = new Y2KDatabase(LoanDetailActivity.this);

                                    BigDecimal bigDecimal = new BigDecimal(mAmountEntered);
                                    bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_FLOOR);

                                    if (mDatabase.addPayment(mLoan.getmLoanId(), bigDecimal.doubleValue(), dateCreated)) {
                                        LoanPayment loanPayment = new LoanPayment(-1, dateCreated, bigDecimal.doubleValue(), dateCreated);
                                        mLoan.addPayment(loanPayment);
                                        //mLoanPaymentArrayList.add(loanPayment);
                                        mLoanPaymentAdapter.notifyDataSetChanged();
                                        updateLoanView(mLoan);
                                        showOrHideView();
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
                                try {
                                    double amountLeft = -1;
                                    mTextInputLayout.setErrorEnabled(true);
                                    if (!s.toString().isEmpty()) {


                                        String amountLeftString = String.format(Locale.getDefault(), "%.2f", mLoan.getAmountOwing());

                                        Log.v("Amount Left = ", amountLeftString);
                                        amountLeft = Double.parseDouble(amountLeftString) - Double.parseDouble(s.toString());

                                        Log.v("Amount Left Entered ", "" + amountLeft);
                                        Log.v("Amount Entered = ", s.toString());

                                        if (amountLeft < 0) {
                                            mTextInputLayout.setError(getString(R.string.amount_entered_exceeds, mStateManager.getCurrency(), mLoan.getAmountOwing()));
                                            mTextInputLayout.setErrorEnabled(true);
                                        } else {
                                            mAmountEntered = Double.parseDouble(s.toString());
                                            mTextInputLayout.setErrorEnabled(false);
                                        }
                                    }
                                    okayButton.setEnabled(!s.toString().isEmpty() && amountLeft >= 0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
        mLoanDueDateLayout = findViewById(R.id.date_created_layout);
        mAmountPaid = (TextView) findViewById(R.id.amount_paid);
        TextView amountToPay = (TextView) findViewById(R.id.amount_to_pay);
        mAmountOwing = (TextView) findViewById(R.id.amount_owing);
        TextView loanTitle = (TextView) findViewById(R.id.loan_title);
        TextView loanDetail = (TextView) findViewById(R.id.loan_details);
        TextView borrowOrLent = (TextView) findViewById(R.id.amount_borrow_lent);
        if (borrowOrLent != null) {
            borrowOrLent.setText(mLoan.isBorrowed() ? R.string.amount_borrowed : R.string.amount_lent);
        }

        View view = findViewById(R.id.loan_detail);
        String detail = mLoan.getmLoanDetail();
        if (loanDetail != null) {
            loanDetail.setText(detail);
        }
        if (mLoan.getmLoanDetail().trim().isEmpty()) {
            Log.v("Detail ", "Detail " + detail);
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }


        mStateManager = new Y2KStateManager(this);
        if (loanTitle != null) {
            loanTitle.setText(mLoan.getmLoanTitle());
        }
        if (loanAmount != null) {
            loanAmount.setText(mStateManager.getCurrency() + mLoan.getmLoanAmount());
        }
        if (mAmountPaid != null) {
            mAmountPaid.setText(mStateManager.getCurrency() + decimalFormat.format(mLoan.getmAmountPaid()));
        }
        if (loanInterestAmount != null) {
            loanInterestAmount.setText(mStateManager.getCurrency() + decimalFormat.format(mLoan.getmLoanInterest()));
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
            amountToPay.setText(mStateManager.getCurrency() + decimalFormat.format(amountWithInterest));
        }
        if (mLoanDueDate != null) {
            mLoanDueDate.setText(relativeTime);
        }
        if (mAmountOwing != null) {
            if (!mLoan.isPaid()) {
                mAmountOwing.setText(mStateManager.getCurrency() + decimalFormat.format((amountWithInterest - mLoan.getmAmountPaid())));
            } else {
                mAmountOwing.setText(R.string.cleared);
                mLoanDueDateLayout.setVisibility(View.GONE);
            }
        }
        mLoanPaymentArrayList = mLoan.getmLoanPayments();
        Log.v("Well", "" + mLoanPaymentArrayList.size());
        mLoanPaymentAdapter = new LoanPaymentAdapter(mLoanPaymentArrayList);
        mLoanPayments = (RecyclerView) findViewById(R.id.loan_payments);
        if (mLoanPayments != null) {
            Log.v("Adapter", "Adapter");
            mLoanPayments.setAdapter(mLoanPaymentAdapter);
        }
        showOrHideView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLoan.isPaid()) {
            mAddPayment.animate().scaleX(1.0F).scaleY(1.0F).setDuration(1000).start();
        }
    }

    private void showOrHideView() {
        if (mLoanPaymentArrayList.isEmpty()) {
            mLoanPayments.setVisibility(View.GONE);
        } else {
            mLoanPayments.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateLoanView(Loan loan) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (mAmountPaid != null) {
            mAmountPaid.setText(mStateManager.getCurrency() + decimalFormat.format(loan.getmAmountPaid()));
        }
        if (mAmountOwing != null) {
            if (!loan.isPaid()) {
                mAmountOwing.setText(mStateManager.getCurrency() + decimalFormat.format(loan.getmAmountLeft()));
            } else {
                mAmountOwing.setText(R.string.cleared);
                mLoanDueDateLayout.setVisibility(View.GONE);
                mAddPayment.animate().scaleX(0.0F).scaleY(0.0F).start();
            }
        }
    }


    private void getColors() {
        mToolbar.setBackgroundColor(getColor(210, mLoan.getmLoanColor()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(255, mLoan.getmLoanColor()));
        }
        supportStartPostponedEnterTransition();

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
            case R.id.action_delete:
                new MaterialDialog.Builder(this).title("Delete?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Y2KDatabase y2KDatabase = new Y2KDatabase(LoanDetailActivity.this);
                                if (y2KDatabase.deleteItem(Constants.LOAN_TABLE, Constants.LOAN_ID, mLoan.getmLoanId())) {
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    notificationManager.cancel(mLoan.getmLoanId());
                                    finish();
                                }
                            }
                        }).show();

                break;
        }
        return true;
    }
}
