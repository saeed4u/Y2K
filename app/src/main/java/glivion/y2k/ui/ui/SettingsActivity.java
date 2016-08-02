package glivion.y2k.ui.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import glivion.y2k.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by saeed on 02/08/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    private StickyListHeadersListView mAddIncomeCategory;
    private StickyListHeadersListView mAddExpenditureCategory;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
    }
}
