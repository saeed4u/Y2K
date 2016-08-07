package glivion.y2k.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by blanka on 8/6/2016.
 */
public class IncomeExpenditure implements Parcelable {

    private int mId;
    private String mTitle;
    private String mDetails;
    private double mAmount;
    private boolean isIncome;
    private String mCreatedAt;
    private String mPayDate;
    private int mWeekNumber;

    private Category mCategory;


    public IncomeExpenditure(boolean isIncome, double mAmount, String mCreatedAt, String mDetails, int mId, String mPayDate, String mTitle, int mWeekNumber) {
        this.isIncome = isIncome;
        this.mAmount = mAmount;
        this.mCreatedAt = mCreatedAt;
        this.mDetails = mDetails;
        this.mId = mId;
        this.mPayDate = mPayDate;
        this.mTitle = mTitle;
        this.mWeekNumber = mWeekNumber;
    }

    protected IncomeExpenditure(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mDetails = in.readString();
        mAmount = in.readDouble();
        isIncome = in.readByte() != 0;
        mCreatedAt = in.readString();
        mPayDate = in.readString();
        mWeekNumber = in.readInt();
        mCategory = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<IncomeExpenditure> CREATOR = new Creator<IncomeExpenditure>() {
        @Override
        public IncomeExpenditure createFromParcel(Parcel in) {
            return new IncomeExpenditure(in);
        }

        @Override
        public IncomeExpenditure[] newArray(int size) {
            return new IncomeExpenditure[size];
        }
    };

    public int getmWeekNumber() {
        return mWeekNumber;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public double getmAmount() {
        return mAmount;
    }

    public void setmCategory(Category mCategory) {
        this.mCategory = mCategory;
    }

    public Category getmCategory() {
        return mCategory;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public String getmDetails() {
        return mDetails;
    }

    public int getmId() {
        return mId;
    }

    public String getmPayDate() {
        return mPayDate;
    }

    public String getmTitle() {
        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mDetails);
        dest.writeDouble(mAmount);
        dest.writeByte((byte) (isIncome ? 1 : 0));
        dest.writeString(mCreatedAt);
        dest.writeString(mPayDate);
        dest.writeInt(mWeekNumber);
        dest.writeParcelable(mCategory, flags);
    }
}
