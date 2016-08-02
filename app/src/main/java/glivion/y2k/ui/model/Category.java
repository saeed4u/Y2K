package glivion.y2k.ui.model;

/**
 * Created by saeed on 02/08/2016.
 */
public class Category {

    private int mCatId;
    private String mCatName;
    private int mCatColor;

    public Category(int mCatId, String mCatName, int mCatColor) {
        this.mCatId = mCatId;
        this.mCatName = mCatName;
        this.mCatColor = mCatColor;
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
}
