package glivion.y2k.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.commons.lang.WordUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.model.BudgetItem;
import glivion.y2k.android.statemanager.Y2KStateManager;
import glivion.y2k.android.utilities.Utilities;

/**
 * Created by saeed on 10/08/2016.
 */
public class AddBudgetActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText mBudgetName;
    private TextView mDate;
    private int mBudgetType;
    private String mBudgetDate;

    private static final int ADD_BUDGET_ITEM = 100;

    private ArrayList<BudgetItem> mBudgetItems;
    private LinearLayout mBudgetItemsLayout;
    private TextView mBudgetItemTotal;
    private Y2KStateManager mStateManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_budget);
        mBudgetItems = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mStateManager = new Y2KStateManager(this);
        mBudgetName = (EditText) findViewById(R.id.budget_name);
        mBudgetItemsLayout = (LinearLayout) findViewById(R.id.budget_items);
        mBudgetItemTotal = (TextView) findViewById(R.id.budget_item_total);
        mBudgetItemTotal.setText(mStateManager.getCurrency() + "0");
        mDate = (TextView) findViewById(R.id.budget_date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reShowDatePicker();

            }
        });
        RadioButton mIncomeBudget = (RadioButton) findViewById(R.id.income_budget);
        RadioButton mExpenditureBudget = (RadioButton) findViewById(R.id.expenditure_budget);
        if (mIncomeBudget != null) {
            mIncomeBudget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        mBudgetType = Constants.IS_INCOME;
                    }
                }
            });
        }

        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        mBudgetDate = dateFormatForMonth.format(new Date());
        Log.v("Budget Date", String.valueOf(mBudgetDate));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String loanDate = day + Utilities.getDay(day) + " " + Utilities.getMonthName(month) + ", " + year + ".";

        mDate.setText(loanDate);

        if (mExpenditureBudget != null) {
            mExpenditureBudget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        mBudgetType = Constants.IS_EXPENDITURE;
                    }
                }
            });
        }
        final ColorSeekBar colorSeekBar = (ColorSeekBar) findViewById(R.id.color_picker);
        final View colorView = findViewById(R.id.color_view);
        colorView.setBackgroundColor(colorSeekBar.getColor());
        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int i, int i1, int i2) {
                colorView.setBackgroundColor(i2);
            }
        });
        View addBudget = findViewById(R.id.add_budget);
        if (addBudget != null) {
            addBudget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String budgetName = mBudgetName.getText().toString().trim();

                    if (budgetName.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(mBudgetName, R.string.enter_income_name, Snackbar.LENGTH_LONG);
                        Utilities.shakeView(mBudgetName);
                        ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
                    } else if (mBudgetItems.isEmpty() || mBudgetItems.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(mBudgetName, R.string.add_at_least_budget_item, Snackbar.LENGTH_LONG);
                        ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
                    } else {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Y2KDatabase y2KDatabase = new Y2KDatabase(AddBudgetActivity.this);
                                long rowId = y2KDatabase.addBudget(WordUtils.capitalize(budgetName), getAmount(), mBudgetType == Constants.IS_INCOME, mBudgetDate, false, colorSeekBar.getColor());
                                if (rowId != -1) {
                                    if (addBudgetItems((int) rowId)) {
                                        setResult(RESULT_OK);
                                    } else {
                                        setResult(RESULT_CANCELED);
                                    }
                                } else {
                                    setResult(RESULT_CANCELED);
                                }


                                finish();
                            }
                        }).start();
                    }
                }
            });
        }

    }

    private double getAmount() {
        double amount = 0.0;
        for (BudgetItem budgetItem : mBudgetItems) {
            amount += budgetItem.getmBudgetAmount();
        }
        return amount;
    }

    private boolean addBudgetItems(int budgetId) {
        boolean successful = false;
        for (BudgetItem budgetItem : mBudgetItems) {
            Y2KDatabase y2KDatabase = new Y2KDatabase(this);
            successful = y2KDatabase.addBudgetItem(budgetItem.getmItemName(), budgetItem.getmBudgetAmount(), budgetId);
        }
        return successful;
    }

    public void addBudgetItem(BudgetItem budgetItem) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        View view = getLayoutInflater().inflate(R.layout.budget_item_layout, null);
        TextView budgetItemName = (TextView) view.findViewById(R.id.budget_item_name);
        TextView budgetItemAmount = (TextView) view.findViewById(R.id.budget_item_amount);
        budgetItemAmount.setText(mStateManager.getCurrency() + decimalFormat.format(budgetItem.getmBudgetAmount()));
        budgetItemName.setText(budgetItem.getmItemName());
        mBudgetItemsLayout.addView(view);
        mBudgetItemTotal.setText(mStateManager.getCurrency() + decimalFormat.format(getAmount()));
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
        if (requestCode == ADD_BUDGET_ITEM && resultCode == RESULT_OK) {
            BudgetItem budgetItem = data.getParcelableExtra(Constants.BUDGET_ITEM);
            mBudgetItems.add(budgetItem);
            addBudgetItem(budgetItem);
        }
    }

    private void reShowDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddBudgetActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setTitle("Set Budget Date");
        dpd.setMinDate(Calendar.getInstance());
        dpd.show(getFragmentManager(), DatePickerDialog.class.getSimpleName());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String loanDate = dayOfMonth + Utilities.getDay(dayOfMonth) + " " + Utilities.getMonthName(monthOfYear) + ", " + year + ".";
        mDate.setText(loanDate);
        mBudgetDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
    }

    public void budgetItemsFragment(View view) {
        Intent intent = new Intent(this, AddBudgetItemActivity.class);
        startActivityForResult(intent, ADD_BUDGET_ITEM);
    }
}
