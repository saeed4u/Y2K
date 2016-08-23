package glivion.y2k.android.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;
import glivion.y2k.R;
import glivion.y2k.android.adapter.CurrencyAdapter;
import glivion.y2k.android.model.Currency;

/**
 * Created by saeed on 22/08/2016.
 */
public class CurrencyConverter extends AppCompatActivity {

    private TextInputEditText mSelling;
    private TextInputEditText mBuying;
    private TextInputEditText mEnteredAmount;

    private MaterialSpinner mFirstAmountSpinner;
    private MaterialSpinner mSecondAmountSpinner;

    private Currency mFromCurrency;
    private Currency mToCurrency;
    private TextView mLastUpdate;

    private static final String[] CURRENCIES = {"GHS", "USD", "GBP", "EUR", "NGN", "ZAR", "XOF"};
    private String[] CURRENCY_NAME = {"GHC", "$", "£", "€", "₦", "R", "FCFA"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_converter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mSelling = (TextInputEditText) findViewById(R.id.selling_amount);
        mLastUpdate = (TextView) findViewById(R.id.last_update);
        mBuying = (TextInputEditText) findViewById(R.id.buying_amount);
        mEnteredAmount = (TextInputEditText) findViewById(R.id.first_amount);
        mFirstAmountSpinner = (MaterialSpinner) findViewById(R.id.first_amount_spinner);
        mSecondAmountSpinner = (MaterialSpinner) findViewById(R.id.second_amount_spinner);
        mFirstAmountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > -1) {
                    mFromCurrency = (Currency) mFirstAmountSpinner.getSelectedItem();
                    convertAmount(mEnteredAmount.getText().toString());
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date dateCreated = format.parse(mFromCurrency.getmLastUpdate());
                        String relativeTime = DateUtils.getRelativeTimeSpanString(dateCreated.getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString();
                        mLastUpdate.setText("Last Update: "+relativeTime);
                        mLastUpdate.setVisibility(View.VISIBLE);
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                } else {
                    mFromCurrency = null;
                    mSelling.setText("");
                    mBuying.setText("");
                    mLastUpdate.setText("");
                    mLastUpdate.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSecondAmountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > -1) {
                    mToCurrency = (Currency) mSecondAmountSpinner.getSelectedItem();
                    convertAmount(mEnteredAmount.getText().toString());
                } else {
                    mToCurrency = null;
                    mSelling.setText("");
                    mBuying.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
      /*  View to = findViewById(R.id.to_image);
        to.setOnClickListener(new View.OnClickListener() {
            boolean isAnimating;

            @Override
            public void onClick(View v) {
                if (isAnimating)
                    return;
                isAnimating = true;

                View v1 = findViewById(R.id.from_currency);
                View v2 = findViewById(R.id.to_currency);

                float x1, y1, x2, y2;
                x1 = getRelativeX(v1);//Use v1.getX() if v1 & v2 have same parent
                y1 = getRelativeY(v1);//Use v1.getY() if v1 & v2 have same parent
                x2 = getRelativeX(v2);//Use v2.getX() if v1 & v2 have same parent
                y2 = getRelativeY(v2);//Use v2.getY() if v1 & v2 have same parent

                float x_displacement = (x2 - x1);
                float y_displacement = (y2 - y1);

                v1.animate().xBy(x_displacement).yBy(y_displacement);
                v2.animate().xBy(-x_displacement).yBy(-y_displacement);
                long anim_duration = v1.animate().getDuration();

                //Wait till animation is over to set isAnimating to false
                //take 10 ms as buffer time to ensure proper functioning
                //If you remove this timer & isAnimating variable, the animation will function improperly when user rapidly clicks on swap button
                new CountDownTimer(anim_duration + 10, anim_duration + 10) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        isAnimating = false;
                    }
                }.start();

            }

            //returns x-pos relative to root layout
            private float getRelativeX(View myView) {
                if (myView.getParent() == myView.getRootView())
                    return myView.getX();
                else
                    return myView.getX() + getRelativeX((View) myView.getParent());
            }

            //returns y-pos relative to root layout
            private float getRelativeY(View myView) {
                if (myView.getParent() == myView.getRootView())
                    return myView.getY();
                else
                    return myView.getY() + getRelativeY((View) myView.getParent());
            }
        });*/

        mEnteredAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String amount = editable.toString();
                convertAmount(amount);
            }
        });

        new SetUpCurrencies().execute();
    }

    private void convertAmount(String amount) {
        try {
            if (!amount.isEmpty() && mToCurrency != null && mFromCurrency != null) {
                Log.v("Called After ", amount);
                Double amountDouble = Double.parseDouble(amount);
                if (!mToCurrency.getmCurrencyName().equals(mFromCurrency.getmCurrencyName())) {
                    Currency currency = mFromCurrency.getQuote(mToCurrency.getmCurrencyName());
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String sellingAmount = String.format(Locale.getDefault(), "%.2f", Double.parseDouble(decimalFormat.format(amountDouble * Double.parseDouble(currency.getmSellPrice()))));
                    String buyingAmount = String.format(Locale.getDefault(), "%.2f", Double.parseDouble(decimalFormat.format(amountDouble * Double.parseDouble(currency.getmBuyPrice()))));
                    mSelling.setText(currency.getmCurrencyName() + sellingAmount);
                    mBuying.setText(currency.getmCurrencyName() + buyingAmount);
                } else {
                    mSelling.setText(mToCurrency.getmCurrencyName() + mEnteredAmount.getText());
                    mBuying.setText(mToCurrency.getmCurrencyName() + mEnteredAmount.getText());
                }


            } else {
                mSelling.setText("");
                mBuying.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private class SetUpCurrencies extends AsyncTask<Void, Void, ArrayList<Currency>> {
        @Override
        protected ArrayList<Currency> doInBackground(Void... voids) {
            return getCurrencies();
        }

        @Override
        protected void onPostExecute(ArrayList<Currency> currencies) {
            super.onPostExecute(currencies);
            mFirstAmountSpinner.setAdapter(new CurrencyAdapter(CurrencyConverter.this, currencies));
            mSecondAmountSpinner.setAdapter(new CurrencyAdapter(CurrencyConverter.this, currencies));
        }
    }

    private ArrayList<Currency> getCurrencies() {
        ArrayList<Currency> currencies = new ArrayList<>();
        int i = 0;
        for (String currency : CURRENCIES) {
            File directory = new File(Environment.getExternalStorageDirectory() + "/Y2K/Currency");
            if (directory.exists()) {
                try {
                    File currencyFile = new File(directory, currency + ".txt");
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(currencyFile));
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String jsonString = stringBuilder.toString();
                    Log.v("JSON String", jsonString);
                    currencies.add(getJsonFromString(CURRENCY_NAME[i], jsonString, currency));

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        return currencies;
    }

    private Currency getJsonFromString(String currency, String jsonString, String baseCurrency) throws JSONException {
        ArrayList<Currency> currencies = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        String date = jsonObject.getJSONObject("meta").getJSONObject("effective_params").getString("date");
        Currency currency1 = new Currency(currency);
        currency1.setmLastUpdate(date);
        JSONObject quotes = jsonObject.getJSONObject("quotes");
        int i = 0;
        for (String quote : CURRENCIES) {
            if (!quote.equals(baseCurrency)) {
                JSONObject quoteJson = quotes.getJSONObject(quote);
                String ask = quoteJson.getString("ask");
                String bid = quoteJson.getString("bid");
                Currency currency_ = new Currency(CURRENCY_NAME[i]);
                currency_.setmBuyPrice(ask);
                currency_.setmSellPrice(bid);
                currencies.add(currency_);
            }
            i++;
        }
        currency1.setmQuotes(currencies);
        return currency1;
    }
}
