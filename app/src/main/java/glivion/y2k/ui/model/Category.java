package glivion.y2k.ui.model;

/**
 * Created by saeed on 02/08/2016.
 */
public class Category {

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

}
