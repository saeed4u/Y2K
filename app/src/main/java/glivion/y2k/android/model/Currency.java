package glivion.y2k.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by saeed on 23/08/2016.
 */
public class Currency implements Parcelable {

    private String mCurrencyName;
    private String mSellPrice;
    private String mBuyPrice;
    private String mLastUpdate;

    private ArrayList<Currency> mQuotes;

    public Currency(String mCurrencyName) {
        this.mCurrencyName = mCurrencyName;
    }

    protected Currency(Parcel in) {
        mCurrencyName = in.readString();
        mSellPrice = in.readString();
        mBuyPrice = in.readString();
        mLastUpdate = in.readString();
        mQuotes = in.createTypedArrayList(Currency.CREATOR);
    }

    public void setmLastUpdate(String mLastUpdate) {
        this.mLastUpdate = mLastUpdate;
    }

    public void setmSellPrice(String mSellPrice) {
        this.mSellPrice = mSellPrice;
    }

    public void setmBuyPrice(String mBuyPrice) {
        this.mBuyPrice = mBuyPrice;
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
    public String getmCurrencyName() {
        return mCurrencyName;
    }

    public String getmSellPrice() {
        return mSellPrice;
    }

    public String getmBuyPrice() {
        return mBuyPrice;
    }

    public String getmLastUpdate() {
        return mLastUpdate;
    }

    public ArrayList<Currency> getmQuotes() {
        return mQuotes;
    }

    public void setmQuotes(ArrayList<Currency> mQuotes) {
        this.mQuotes = mQuotes;
    }

    public Currency getQuote(String quoteName) {
        Log.v("quoteName",quoteName);
        for (Currency currency : mQuotes) {
            Log.v("Currency",""+currency);
            if(currency.mCurrencyName.equalsIgnoreCase(quoteName)){
                return currency;
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mCurrencyName);
        parcel.writeString(mSellPrice);
        parcel.writeString(mBuyPrice);
        parcel.writeString(mLastUpdate);
        parcel.writeTypedList(mQuotes);
    }

    @Override
    public String toString() {
        return mCurrencyName;
    }
}
