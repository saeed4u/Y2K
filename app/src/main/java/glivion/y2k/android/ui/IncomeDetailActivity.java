package glivion.y2k.android.ui;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.commons.lang.WordUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.model.IncomeExpenditure;
import glivion.y2k.android.statemanager.Y2KStateManager;

/**
 * Created by blanka on 8/7/2016.
 */
public class IncomeDetailActivity extends ItemDetailActivity {

    private Toolbar mToolbar;
    private IncomeExpenditure mIncomeExpenditure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        mIncomeExpenditure = getIntent().getParcelableExtra("income");
        mToolbar.setTitle(mIncomeExpenditure.getmTitle());
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(WordUtils.capitalize(mIncomeExpenditure.getmTitle()));
        }
        TextView incomeTitle = (TextView) findViewById(R.id.income_title);
        TextView incomeAmount = (TextView) findViewById(R.id.income_amount);
        TextView incomePayDate = (TextView) findViewById(R.id.date_created);
        TextView incomeDetail = (TextView) findViewById(R.id.income_details);

        Y2KStateManager stateManager = new Y2KStateManager(this);

        if (incomeAmount != null) {
            incomeAmount.setText(stateManager.getCurrency() + decimalFormat.format(mIncomeExpenditure.getmAmount()));
        }
        if (incomeTitle != null) {
            incomeTitle.setText(mIncomeExpenditure.getmTitle());
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateCreated = null;
        try {
            dateCreated = format.parse(mIncomeExpenditure.getmPayDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated != null ? dateCreated.getTime() : 0, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();

        if (incomePayDate != null) {
            incomePayDate.setText(relativeTime);
        }

        if (incomeDetail != null) {
            incomeDetail.setText(mIncomeExpenditure.getmDetails());
        }
        View view = findViewById(R.id.income_detail_layout);
        if (view != null) {
            view.setVisibility(mIncomeExpenditure.getmDetails().isEmpty() ? View.GONE : View.VISIBLE);
        }


        getColors();
    }

    private void getColors() {
        mToolbar.setBackgroundColor(getColor(210, mIncomeExpenditure.getmCategory().getmCatColor()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(255, mIncomeExpenditure.getmCategory().getmCatColor()));
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
                                Y2KDatabase y2KDatabase = new Y2KDatabase(IncomeDetailActivity.this);
                                if (y2KDatabase.deleteItem(Constants.IN_EX_TABLE, Constants.IN_EX_ID, mIncomeExpenditure.getmId())) {
                                    finish();
                                }
                            }
                        }).show();

                break;
        }
        return true;
    }
}
