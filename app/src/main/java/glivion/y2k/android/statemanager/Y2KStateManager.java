package glivion.y2k.android.statemanager;

import android.content.Context;
import android.content.SharedPreferences;

import glivion.y2k.R;

/**
 * Created by saeedissah on 5/17/16.
 */
public class Y2KStateManager {

    private static final String FIRST_RUN = "first_run";
    private static final String CURRENCY = "currency";
    private static final String QUERY_TYPE = "query_type";
    private static final String CURRENCY_ID = "currency_id";

    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;

    public Y2KStateManager(Context context) {
        mSharedPreference = context.getSharedPreferences("Y2K", Context.MODE_PRIVATE);
    }

    public void saveFirstRun(boolean isFirstRun) {
        mEditor = mSharedPreference.edit();
        mEditor.putBoolean(FIRST_RUN, isFirstRun);
        mEditor.apply();
    }

    public boolean isFirstRun() {
        return mSharedPreference.getBoolean(FIRST_RUN, true);
    }

    public void saveCurrency(String currency) {
        mEditor = mSharedPreference.edit();
        mEditor.putString(CURRENCY, currency);
        mEditor.apply();
    }

    public String getCurrency() {
        return mSharedPreference.getString(CURRENCY, "GHC");
    }

    public void saveQueryType(String queryType) {
        mEditor = mSharedPreference.edit();
        mEditor.putString(QUERY_TYPE, queryType);
        mEditor.apply();
    }

    public String getQueryType() {
        return mSharedPreference.getString(QUERY_TYPE, "weekly");
    }

    public void saveCurrencyId(int id) {
        mEditor = mSharedPreference.edit();
        mEditor.putInt(CURRENCY_ID, id);
        mEditor.apply();
    }

    public int getCurrencyId() {
        return mSharedPreference.getInt(CURRENCY_ID, R.id.cedis);
    }
}
