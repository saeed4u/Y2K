package glivion.y2k.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by blanka on 8/7/2016.
 */
public class Budget implements Parcelable {

    private int mBudgetId;
    private String mBudgetTitle;
    private double mBudgetTotal;
    private boolean isIncome;
    private String mCreatedAt;
    private String mDueDate;
    private boolean isCompleted;
    private int mColor;

    public Budget(boolean isCompleted, boolean isIncome, int mBudgetId, String mBudgetTitle, double mBudgetTotal, String mCreatedAt, String mDueDate, int mColor) {
        this.isCompleted = isCompleted;
        this.isIncome = isIncome;
        this.mBudgetId = mBudgetId;
        this.mBudgetTitle = mBudgetTitle;
        this.mBudgetTotal = mBudgetTotal;
        this.mCreatedAt = mCreatedAt;
        this.mDueDate = mDueDate;
        this.mColor = mColor;
    }

    protected Budget(Parcel in) {
        mBudgetId = in.readInt();
        mBudgetTitle = in.readString();
        mBudgetTotal = in.readDouble();
        isIncome = in.readByte() != 0;
        mCreatedAt = in.readString();
        mDueDate = in.readString();
        isCompleted = in.readByte() != 0;
        mColor = in.readInt();
    }

    public static final Creator<Budget> CREATOR = new Creator<Budget>() {
        @Override
        public Budget createFromParcel(Parcel in) {
            return new Budget(in);
        }

        @Override
        public Budget[] newArray(int size) {
            return new Budget[size];
        }
    };

    public int getmColor() {
        return mColor;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public int getmBudgetId() {
        return mBudgetId;
    }

    public String getmBudgetTitle() {
        return mBudgetTitle;
    }

    public double getmBudgetTotal() {
        return mBudgetTotal;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public String getmDueDate() {
        return mDueDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mBudgetId);
        dest.writeString(mBudgetTitle);
        dest.writeDouble(mBudgetTotal);
        dest.writeByte((byte) (isIncome ? 1 : 0));
        dest.writeString(mCreatedAt);
        dest.writeString(mDueDate);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeInt(mColor);
    }
}
