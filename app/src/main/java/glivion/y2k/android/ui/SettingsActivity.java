package glivion.y2k.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

import glivion.y2k.R;
import glivion.y2k.android.adapter.CategoryAdapter;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.model.Category;
import glivion.y2k.android.statemanager.Y2KStateManager;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by saeed on 02/08/2016.
 */
public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private StickyListHeadersListView mAddCategory;
    private DrawerLayout mDrawerLayout;
    private Y2KStateManager mStateManager;
    private String[] mFinanceStructures = {"Monthly", "Weekly"};
    private static HashMap<Integer, String> mCurrencies;
    private TextView mCurrency;
    private MultiStateToggleButton mMultiStateToggleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrencies = new HashMap<>();
        mCurrencies.put(R.id.cedis, "GHC");
        mCurrencies.put(R.id.dollar, "$");
        mCurrencies.put(R.id.pound_sterling, "£");
        mCurrencies.put(R.id.euro, "€");
        mCurrencies.put(R.id.naira, "₦");
        mCurrencies.put(R.id.south_africa, "R");
        mCurrencies.put(R.id.cfa, "FCFA");
        setContentView(R.layout.settings_layout);
        mStateManager = new Y2KStateManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mCurrency = (TextView) findViewById(R.id.currency);
        mCurrency.setText(mStateManager.getCurrency());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if (mNavigationView != null) {
            mNavigationView.setCheckedItem(mStateManager.getCurrencyId());
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        setSupportActionBar(toolbar);
        mAddCategory = (StickyListHeadersListView) findViewById(R.id.add_category);
        mAddCategory.setDrawingListUnderStickyHeader(false);
        int queryType = mStateManager.getQueryType().equals(mFinanceStructures[0]) ? 0 : 1;

        mMultiStateToggleButton = (MultiStateToggleButton) findViewById(R.id.finance_structure);

        if (mMultiStateToggleButton != null) {
            mMultiStateToggleButton.setColorRes(R.color.colorAccentDashBoardDark, R.color.white);
            mMultiStateToggleButton.setValue(queryType);
            mMultiStateToggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
                @Override
                public void onValueChanged(int value) {
                    mStateManager.saveQueryType(mFinanceStructures[value]);
                }
            });
        }

    }

    public void showCurrencies(View view) {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    private void getCategories() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Y2KDatabase y2KDatabase = new Y2KDatabase(SettingsActivity.this);
                final ArrayList<Category> categories = y2KDatabase.getCategories();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CategoryAdapter categoryAdapter = new CategoryAdapter(SettingsActivity.this, categories);
                        mAddCategory.setAdapter(categoryAdapter);
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategories();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mStateManager.saveCurrencyId(item.getItemId());
        mStateManager.saveCurrency(mCurrencies.get(item.getItemId()));
        mDrawerLayout.closeDrawer(GravityCompat.END);
        mCurrency.setText(mCurrencies.get(item.getItemId()));
        return true;
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

    public void selectItem(View view) {
        int queryType = mStateManager.getQueryType().equals(mFinanceStructures[0]) ? 0 : 1;
        if (queryType > 0) {
            queryType = 0;
        } else {
            queryType = 1;
        }
        mMultiStateToggleButton.setValue(queryType);
    }
}
