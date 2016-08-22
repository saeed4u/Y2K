package glivion.y2k.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import fr.ganfra.materialspinner.MaterialSpinner;
import glivion.y2k.R;

/**
 * Created by saeed on 22/08/2016.
 */
public class CurrencyConverter extends AppCompatActivity {

    private TextInputEditText mFirstAmount;
    private TextInputEditText mSecondAmount;

    private MaterialSpinner mFirstAmountSpinner;
    private MaterialSpinner mSecondAmountSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_converter);
        mFirstAmount = (TextInputEditText) findViewById(R.id.first_amount);
        mSecondAmount = (TextInputEditText) findViewById(R.id.second_amount);
        mFirstAmountSpinner = (MaterialSpinner) findViewById(R.id.first_amount_spinner);
        mSecondAmountSpinner = (MaterialSpinner) findViewById(R.id.second_amount_spinner);
    }
}
