package glivion.y2k.android.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.service.NotificationService;
import glivion.y2k.android.utilities.Utilities;

/**
 * Created by saeedissah on 5/17/16.
 */
public class AddLoanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText mLoanName;
    private EditText mLoanAmount;
    private EditText mLoanInterest;
    private int mLoanMode = 0;
    private String mLoanDate;
    private EditText mLoanDetails;
    private TextView mDueDateTime;
    private int mColor;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_loan);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        RadioButton borrowed = (RadioButton) findViewById(R.id.borrowed);
        if (borrowed != null) {
            borrowed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mLoanMode = 0;
                    }
                }
            });
        }

        ColorSeekBar mColorSeekBar = (ColorSeekBar) findViewById(R.id.color_picker);

        final View colorView = findViewById(R.id.color_view);
        colorView.setBackgroundColor(mColor = mColorSeekBar.getColor());
        mColorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarValue, int alphaBarValue, int color) {
                mColor = color;
                colorView.setBackgroundColor(color);
                //getColors(mColor);
            }
        });
        RadioButton lent = (RadioButton) findViewById(R.id.lent);
        if (lent != null) {
            lent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mLoanMode = 1;
                    }
                }
            });
        }
        mLoanName = (EditText) findViewById(R.id.loan_name);
        mLoanAmount = (EditText) findViewById(R.id.loan_amount);
        mLoanInterest = (EditText) findViewById(R.id.loan_interest);
        mLoanInterest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if (!text.contains("%") && !text.isEmpty()) {
                    mLoanInterest.append("%");
                }
            }
        });
        mLoanDetails = (EditText) findViewById(R.id.loan_details);

        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        mLoanDate = dateFormatForMonth.format(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String loanDate = day + Utilities.getDay(day) + " " + Utilities.getMonthName(month) + "," + year;
        mDueDateTime = (TextView) findViewById(R.id.due_date);
        mDueDateTime.setText(loanDate);
        if (mDueDateTime != null) {
            mDueDateTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reShowDatePicker();

                }
            });
        }
    }

    private void reShowDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddLoanActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(Calendar.getInstance());
        dpd.setTitle("Set Loan Due Date");
        dpd.show(getFragmentManager(), DatePickerDialog.class.getSimpleName());
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

    public void saveLoan(View view) {
        String loanName = mLoanName.getText().toString().trim();
        String loanAmount = mLoanAmount.getText().toString().trim();
        String loanInterest = mLoanInterest.getText().toString().trim();
        String loanDetails = mLoanDetails.getText().toString().trim();

        if (loanName.isEmpty()) {
            Snackbar snackbar = Snackbar.make(mLoanName, R.string.enter_loan_name, Snackbar.LENGTH_LONG);
            ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
            Utilities.shakeView(mLoanName);
            mLoanName.requestFocus();
        } else if (loanAmount.isEmpty() || (loanAmount.length() == 1 && loanAmount.equals("."))) {
            Snackbar snackbar = Snackbar.make(mLoanAmount, R.string.enter_loan_amount, Snackbar.LENGTH_LONG);
            ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
            Utilities.shakeView(mLoanAmount);
            mLoanAmount.requestFocus();
        } else if (loanInterest.isEmpty() || (loanInterest.length() == 1 && loanInterest.equals("."))) {
            Snackbar snackbar = Snackbar.make(mLoanInterest, R.string.enter_loan_interest, Snackbar.LENGTH_LONG);
            ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
            Utilities.shakeView(mLoanInterest);
            mLoanInterest.requestFocus();
        } else if (mLoanDate.isEmpty()) {
            Snackbar snackbar = Snackbar.make(mDueDateTime, R.string.select_loan_due_date, Snackbar.LENGTH_LONG);
            ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
            Utilities.shakeView(mDueDateTime);
        } else {

            BigDecimal loanAmountBig = new BigDecimal(loanAmount);
            loanAmountBig = loanAmountBig.setScale(2, BigDecimal.ROUND_FLOOR);

            BigDecimal loanInterestBig = new BigDecimal(loanInterest);
            loanInterestBig = loanInterestBig.setScale(2, BigDecimal.ROUND_FLOOR);

            new SaveLoan().execute(loanName, loanDetails, ""+loanAmountBig.doubleValue(), ""+loanInterestBig.doubleValue());
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String loanDate = dayOfMonth + Utilities.getDay(dayOfMonth) + " " + Utilities.getMonthName(monthOfYear) + ", " + year + ".";
        mDueDateTime.setText(loanDate);
        String monthYear = String.valueOf((monthOfYear + 1));
        if ((monthOfYear + 1) < 10) {
            monthYear = "0" + monthYear;
        }
        String dayYear = String.valueOf(dayOfMonth);

        if (dayOfMonth < 10) {
            dayYear = "0" + dayYear;
        }
        mLoanDate = year + "-" + monthYear + "-" + dayYear;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = hourOfDay + ":" + minute + ":" + second;
        mLoanDate += time;
        mDueDateTime.setText(mLoanDate);
    }

    private class SaveLoan extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            Y2KDatabase database = new Y2KDatabase(AddLoanActivity.this);
            String dateCreated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Log.v("Date Created", dateCreated);
            return database.addLoan(params[0], params[1], Double.parseDouble(params[2]), Float.parseFloat(params[3]), mLoanMode == 0, mLoanDate, dateCreated, mColor);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                NotificationService.isRunning = false;
                Intent intent = new Intent(AddLoanActivity.this, NotificationService.class);
                startService(intent);
                setResult(RESULT_OK);
                finish();
            } else {
                ColoredSnakBar.get(Snackbar.make(mLoanDetails, R.string.an_error_occured, Snackbar.LENGTH_LONG), R.color.colorAccentDashBoard).show();
            }
        }
    }

    private void getColors(int color) {
        mToolbar.setBackgroundColor(getColor(210, color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(255, color));
        }
        supportStartPostponedEnterTransition();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int getColor(int alpha, int color) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}
