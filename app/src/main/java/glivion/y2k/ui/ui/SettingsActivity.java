package glivion.y2k.ui.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.ui.adapter.CategoryAdapter;
import glivion.y2k.ui.database.Y2KDatabase;
import glivion.y2k.ui.model.Category;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by saeed on 02/08/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    private StickyListHeadersListView mAddCategory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        mAddCategory = (StickyListHeadersListView) findViewById(R.id.add_category);
    }

    private void getCategories() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Y2KDatabase y2KDatabase = new Y2KDatabase(SettingsActivity.this);
                ArrayList<Category> categories = y2KDatabase.getCategories();
                CategoryAdapter categoryAdapter = new CategoryAdapter(SettingsActivity.this, categories);
                mAddCategory.setAdapter(categoryAdapter);
            }
        });
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategories();
    }
}
