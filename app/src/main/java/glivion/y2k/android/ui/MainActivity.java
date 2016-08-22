package glivion.y2k.android.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.Calendar;

import glivion.y2k.R;
import glivion.y2k.android.model.IncomeExpenditure;
import glivion.y2k.android.service.NotificationService;

/**
 * Created by saeedissah on 5/17/16.
 */
public class MainActivity extends AppCompatActivity {

    private AHBottomNavigation mBottomNavigation;
    private Toolbar mToolbar;

    private static final int REQUEST_WRITE_PERMISSION = 100;
    private int mSelectedPosition = 0;

    private void startNotificationService() {
        Log.v("Called", "Called Start notification");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        startNotificationService();


        mToolbar.setTitle(R.string.dashboard);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new DashBoardFragment(), DashBoardFragment.class.getSimpleName()).commit();

        mBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem dashboard = new AHBottomNavigationItem(R.string.dashboard, R.drawable.ic_dashboard, R.color.colorAccentDashBoard);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.income_, R.drawable.ic_income, R.color.colorAccentDashBoard);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.expenses, R.drawable.ic_expenditure, R.color.colorAccentDashBoard);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.loans, R.drawable.ic_loan, R.color.colorAccentDashBoard);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.budget, R.drawable.ic_budget, R.color.colorAccentDashBoard);

        if (mBottomNavigation != null) {
            mBottomNavigation.addItem(dashboard);
            mBottomNavigation.addItem(item1);
            mBottomNavigation.addItem(item2);
            mBottomNavigation.addItem(item4);
            mBottomNavigation.addItem(item3);
            mBottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentDashBoard));
            mBottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.colorAccentIncome));
            mBottomNavigation.setForceTint(true);
            mBottomNavigation.setForceTitlesDisplay(true);
            mBottomNavigation.setColored(true);
            mBottomNavigation.setNotificationBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentDashBoard));
            mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position, boolean wasSelected) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if (mSelectedPosition != position) {
                        switch (position) {
                            case 0:
                                mToolbar.setTitle(R.string.dashboard);
                                fragmentTransaction.replace(R.id.fragment, new DashBoardFragment(), DashBoardFragment.class.getSimpleName()).commit();
                                break;
                            case 1:
                                mToolbar.setTitle(R.string.income_);
                                IncomeFragment incomeFragment = new IncomeFragment();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("is_income", true);
                                incomeFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragment, incomeFragment, IncomeExpenditure.class.getSimpleName()).commit();
                                break;
                            case 2:
                                mToolbar.setTitle(R.string.expenses);
                                IncomeFragment expenditureFragment = new IncomeFragment();
                                Bundle expenditureBundle = new Bundle();
                                expenditureBundle.putBoolean("is_income", false);
                                expenditureFragment.setArguments(expenditureBundle);
                                fragmentTransaction.replace(R.id.fragment, expenditureFragment, IncomeExpenditure.class.getSimpleName()).commit();

                                break;
                            case 3:
                                mToolbar.setTitle(R.string.budget);
                                fragmentTransaction.replace(R.id.fragment, new BudgetFragment(), IncomeExpenditure.class.getSimpleName()).commit();
                                break;
                            case 4:
                                fragmentTransaction.replace(R.id.fragment, new LoanFragment(), LoanFragment.class.getSimpleName()).commit();
                                mToolbar.setTitle(R.string.loans);
                                break;
                        }
                        mSelectedPosition = position;
                    }
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestForPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkForPermission() {
        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestForPermission() {
        if (!checkForPermission()) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }
    }

    private static boolean isAlreadyAsked = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                new MaterialDialog.Builder(this).title("Y2K Permissions")
                        .content("Y2K requires access to your files in order to make it serve you better with caching of what you like. Would you like to grant Y2K the permission to access your files?")
                        .negativeText("No")
                        .positiveText("Yes")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (!isAlreadyAsked) {
                                    requestForPermission();
                                    isAlreadyAsked = true;
                                }
                            }
                        }).show();

            }
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
            case R.id.tips:
                showTips();
                break;
            case R.id.currency_converter:
                showCurrencyConverter();
                break;
        }
        return true;
    }


    private void showTips() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkForPermission()) {
                Intent intent = new Intent(this, TipsActivity.class);
                startActivity(intent);
            } else {
                new MaterialDialog.Builder(this).title(R.string.app_name).content("File permission required to access this feature. Will you like to grant Y2K file permission?")
                        .positiveText(R.string.yes)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                requestForPermission();
                            }
                        })
                        .negativeText(R.string.no)
                        .show();
            }
        } else {
            Intent intent = new Intent(this, TipsActivity.class);
            startActivity(intent);
        }
    }

    private void showCurrencyConverter() {
        Intent intent = new Intent(this, CurrencyConverter.class);
        startActivity(intent);
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
