package glivion.y2k.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import glivion.y2k.R;

/**
 * Created by saeedissah on 5/17/16.
 */
public class DashBoard extends AppCompatActivity {

    AHBottomNavigation mBottomNavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.income_, R.drawable.ic_income, R.color.colorPrimaryIncome);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.expenses, R.drawable.ic_expenditure, R.color.colorPrimaryExpenditure);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.loans, R.drawable.ic_loan, R.color.colorPrimaryLoans);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.budget, R.drawable.ic_budget, R.color.colorPrimaryBudget);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tips, R.drawable.ic_tip, R.color.colorPrimarySettings);

        if (mBottomNavigation != null) {
            mBottomNavigation.addItem(item1);
            mBottomNavigation.addItem(item2);
            mBottomNavigation.addItem(item3);
            mBottomNavigation.addItem(item4);
            mBottomNavigation.addItem(item5);
            mBottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDashBoard));
            mBottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.colorAccentIncome));
            mBottomNavigation.setForceTint(true);
            mBottomNavigation.setForceTitlesDisplay(true);
            mBottomNavigation.setColored(true);
            mBottomNavigation.setNotificationBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryIncome));
            mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position, boolean wasSelected) {
                    if (position == 2) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(DashBoard.this, LoanActivity.class));
                            }
                        }, 500);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBottomNavigation.setCurrentItem(-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }


}
