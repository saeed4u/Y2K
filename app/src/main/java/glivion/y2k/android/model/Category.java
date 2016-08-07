package glivion.y2k.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saeed on 02/08/2016.
 */
public class Category implements Parcelable{

    private int mCatId;
    private String mCatName;
    private int mCatColor;
    private int mCatType;

    private CategoryType mCategoryType;

    public Category(int mCatId, String mCatName, int mCatType, int mCatColor) {
        this.mCatId = mCatId;
        this.mCatName = mCatName;
        this.mCatColor = mCatColor;
        this.mCatType = mCatType;
    }

    protected Category(Parcel in) {
        mCatId = in.readInt();
        mCatName = in.readString();
        mCatColor = in.readInt();
        mCatType = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getmCatType() {
        return mCatType;
    }

    public CategoryType getmCategoryType() {
        return mCategoryType;
    }

    public Category setmCategoryType(CategoryType mCategoryType) {
        this.mCategoryType = mCategoryType;
        return this;
    }

    public int getmCatId() {
        return mCatId;
    }

    public String getmCatName() {
        return mCatName;
    }

    public int getmCatColor() {
        return mCatColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCatId);
        dest.writeString(mCatName);
        dest.writeInt(mCatColor);
        dest.writeInt(mCatType);
    }
}
