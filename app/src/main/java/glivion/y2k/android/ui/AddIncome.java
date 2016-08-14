package glivion.y2k.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kennyc.bottomsheet.BottomSheet;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.commons.lang.WordUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.adapter.CategoryBottomSheetAdapter;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.listener.RecyclerItemClickListener;
import glivion.y2k.android.model.Category;
import glivion.y2k.android.utilities.Utilities;

/**
 * Created by blanka on 8/6/2016.
 */
public class AddIncome extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText mName;
    private EditText mDetail;
    private EditText mAmount;
    private String mPayDate = "";
    private int mCatId = 1;
    private TextView mSelectedDate;
    private ArrayList<Category> mCategories;
    private Y2KDatabase mY2KDatabase;
    private boolean isFromIncome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_income);
        mCategories = new ArrayList<>();
        mY2KDatabase = new Y2KDatabase(this);
        isFromIncome = getIntent().getBooleanExtra("is_income", true);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mCategories = mY2KDatabase.getCategory(isFromIncome ? Constants.INCOME_CAT : Constants.EXPENDITURE_CAT);
            }
        });
        thread.start();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(isFromIncome ? "New Income" : "New Expenditure");
        }

        Button addButton = (Button) findViewById(R.id.add_income);
        if (addButton != null) {
            addButton.setText(isFromIncome ? "Add Income" : "Add Expenditure");
        }

        mName = (EditText) findViewById(R.id.income_name);
        mDetail = (EditText) findViewById(R.id.income_detail);
        mAmount = (EditText) findViewById(R.id.income_amount);
        mSelectedDate = (TextView) findViewById(R.id.due_date);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String loanDate = day + Utilities.getDay(day) + " " + Utilities.getMonthName(month) + ", " + year + ".";
        mSelectedDate.setText(loanDate);
    }

    public void showCategories(View view) {
        if (mCategories.size() == Constants.NUMBER_OF_DEFAULT) {
            Toast.makeText(this, getString(R.string.you_have_not_added, isFromIncome ? "income" : "expenditure"), Toast.LENGTH_LONG).show();
        } else {
            final BottomSheet bottomSheet;
            View v = getLayoutInflater().inflate(R.layout.category_bottom_sheet, null);
            final RecyclerView categories = (RecyclerView) v.findViewById(R.id.categories);
            categories.setAdapter(new CategoryBottomSheetAdapter(mCategories));
            bottomSheet = new BottomSheet.Builder(this)
                    .setView(v)
                    .setTitle("Choose Category")
                    .grid()
                    .create();
            bottomSheet.show();
            categories.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    bottomSheet.dismiss();
                    Category category = mCategories.get(position);
                    mCatId = category.getmCatId();
                    TextView textView = (TextView) findViewById(R.id.category);
                    if (textView != null) {
                        textView.setBackgroundColor(category.getmCatColor());
                        textView.setText(category.getmCatName());
                    }
                }
            }));
        }
    }

    public void saveIncome(View view) {
        String incomeName = mName.getText().toString().trim();
        String detail = mDetail.getText().toString().trim();
        String amount = mAmount.getText().toString().trim();

        Calendar calendar = Calendar.getInstance();
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        int month = calendar.get(Calendar.MONTH);

        if (incomeName.isEmpty()) {
            Utilities.shakeView(mName);
            Snackbar snackbar = Snackbar.make(mName, R.string.enter_income_name, Snackbar.LENGTH_LONG);
            ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
        } else if (amount.isEmpty()) {
            Utilities.shakeView(mAmount);
            Snackbar snackbar = Snackbar.make(mAmount, R.string.enter_income_amount, Snackbar.LENGTH_LONG);
            ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
        } else if (mPayDate.isEmpty()) {
            Utilities.shakeView(findViewById(R.id.due_date));
            Snackbar snackbar = Snackbar.make(mSelectedDate, R.string.select_income_date, Snackbar.LENGTH_LONG);
            ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
        } else {
            if (mY2KDatabase.addIncomeOrExpenditure(WordUtils.capitalize(incomeName), detail, isFromIncome, Double.parseDouble(amount), mPayDate,
                    mPayDate, mCatId, weekNumber, month)) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    }

    public void showDatePicker(View view) {
        showDatePicker();

    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddIncome.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
        dpd.setMaxDate(calendar);

        dpd.setTitle("Set " + (isFromIncome ? "Income " : "Expenditure ") + "Date");
        dpd.show(getFragmentManager(), DatePickerDialog.class.getSimpleName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);

        Log.v("Day now", "" + dayNow);
        Log.v("Day month", "" + dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());
        String date = simpleDateFormat.format(System.currentTimeMillis());
        if (year > yearNow) {
            showError(date);
        } else if (year == yearNow && monthOfYear > monthNow) {
            showError(date);
        } else if (year == yearNow && monthOfYear == monthNow && dayOfMonth > dayNow) {
            showError(date);
        } else {
            date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            mPayDate = date;
            String loanDate = dayOfMonth + Utilities.getDay(dayOfMonth) + " " + Utilities.getMonthName(monthOfYear) + ", " + year + ".";
            mSelectedDate.setText(loanDate);
        }
    }

    private void showError(String date) {
        Toast.makeText(AddIncome.this, "Please date can not be after " + date, Toast.LENGTH_LONG).show();
        showDatePicker();
    }
}
