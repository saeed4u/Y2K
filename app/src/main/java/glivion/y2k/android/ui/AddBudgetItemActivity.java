package glivion.y2k.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.lang.WordUtils;

import glivion.y2k.R;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.model.BudgetItem;
import glivion.y2k.android.utilities.Utilities;

/**
 * Created by saeed on 10/08/2016.
 */
public class AddBudgetItemActivity extends AppCompatActivity {

    private EditText mBudgetItemName;
    private EditText mBudgetItemAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_budget_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.add_budget_item);
        }

        mBudgetItemName = (EditText) findViewById(R.id.budget_item_name);
        mBudgetItemAmount = (EditText) findViewById(R.id.amount);
        View save = findViewById(R.id.add_budget_items);
        if (save != null) {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String budgetItemName = mBudgetItemName.getText().toString().trim();
                    String budgetItemAmount = mBudgetItemAmount.getText().toString().trim();

                    if (budgetItemName.isEmpty()) {
                        Utilities.shakeView(mBudgetItemName);
                        Snackbar snackbar = Snackbar.make(mBudgetItemName, R.string.enter_name_of_budget_item, Snackbar.LENGTH_LONG);
                        ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
                    } else if (budgetItemAmount.isEmpty()  || (budgetItemAmount.length() == 1 && budgetItemAmount.equals("."))) {
                        Utilities.shakeView(mBudgetItemAmount);
                        Snackbar snackbar = Snackbar.make(mBudgetItemAmount, R.string.enter_amount_budget_item, Snackbar.LENGTH_LONG);
                        ColoredSnakBar.get(snackbar, R.color.colorAccentDashBoard).show();
                    } else {
                        BudgetItem budgetItem = new BudgetItem(0, WordUtils.capitalize(budgetItemName), Double.parseDouble(budgetItemAmount));
                        Intent intent = new Intent();
                        intent.putExtra(Constants.BUDGET_ITEM, budgetItem);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
        }
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
}
