package glivion.y2k.ui.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.rtugeek.android.colorseekbar.ColorSeekBar;

import glivion.y2k.R;
import glivion.y2k.ui.constants.Constants;
import glivion.y2k.ui.database.Y2KDatabase;
import glivion.y2k.ui.utilities.Utilities;

/**
 * Created by saeed on 04/08/2016.
 */
public class AddCategory extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ColorSeekBar colorSeekBar = (ColorSeekBar) findViewById(R.id.color_picker);
        final EditText catName = (EditText) findViewById(R.id.category_name);
        final int type = getIntent().getIntExtra(Constants.CAT_TYPE, 0);
        View view = findViewById(R.id.save_category);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (catName.getText().toString().trim().isEmpty()) {
                        Utilities.shakeView(catName);
                    } else {
                        Y2KDatabase y2KDatabase = new Y2KDatabase(AddCategory.this);
                        y2KDatabase.addCategory(catName.getText().toString(), colorSeekBar.getColor(), type);
                        setResult(RESULT_OK);
                        finish();
                    }

                }
            });
        }

    }
}
