package glivion.y2k.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import glivion.y2k.R;

/**
 * Created by saeedissah on 5/17/16.
 */
public class DashBoard extends AppCompatActivity {

    private AHBottomNavigation mBottomNavigation;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.income_, R.drawable.ic_income, R.color.colorAccentDashBoard);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.expenses, R.drawable.ic_expenditure, R.color.colorAccentDashBoard);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.loans, R.drawable.ic_loan, R.color.colorAccentDashBoard);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.budget, R.drawable.ic_budget, R.color.colorAccentDashBoard);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tips, R.drawable.ic_tip, R.color.colorAccentDashBoard);

        if (mBottomNavigation != null) {
            mBottomNavigation.addItem(item1);
            mBottomNavigation.addItem(item2);
            mBottomNavigation.addItem(item3);
            mBottomNavigation.addItem(item4);
            mBottomNavigation.addItem(item5);
            mBottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentDashBoard));
            mBottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.colorAccentIncome));
            mBottomNavigation.setForceTint(true);
            mBottomNavigation.setForceTitlesDisplay(true);
            mBottomNavigation.setColored(true);
            mBottomNavigation.setNotificationBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentDashBoard));
            mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position, boolean wasSelected) {
                    if (position == 2) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, new LoanFragment(), LoanFragment.class.getSimpleName()).commit();
                        mToolbar.setTitle(R.string.loans);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                showSettingsActivity();
                break;
        }
        return true;
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
