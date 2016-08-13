package glivion.y2k.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saeed on 10/08/2016.
 */
public class BudgetItem implements Parcelable{

    private int mItemId;
    private String mItemName;
    private double mBudgetAmount;

    public BudgetItem(int mItemId, String mItemName, double mBudgetAmount) {
        this.mItemId = mItemId;
        this.mItemName = mItemName;
        this.mBudgetAmount = mBudgetAmount;
    }

    protected BudgetItem(Parcel in) {
        mItemId = in.readInt();
        mItemName = in.readString();
        mBudgetAmount = in.readDouble();
    }

    public static final Creator<BudgetItem> CREATOR = new Creator<BudgetItem>() {
        @Override
        public BudgetItem createFromParcel(Parcel in) {
            return new BudgetItem(in);
        }

        @Override
        public BudgetItem[] newArray(int size) {
            return new BudgetItem[size];
        }
    };

    public int getmItemId() {
        return mItemId;
    }

    public String getmItemName() {
        return mItemName;
    }

    public double getmBudgetAmount() {
        return mBudgetAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mItemId);
        parcel.writeString(mItemName);
        parcel.writeDouble(mBudgetAmount);
    }
}
