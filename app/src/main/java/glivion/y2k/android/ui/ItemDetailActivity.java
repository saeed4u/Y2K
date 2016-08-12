package glivion.y2k.android.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import glivion.y2k.R;

/**
 * Created by saeed on 12/08/2016.
 */
public class ItemDetailActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_detail, menu);
        return true;
    }
}
