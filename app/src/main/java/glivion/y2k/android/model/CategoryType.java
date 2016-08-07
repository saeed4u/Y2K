package glivion.y2k.android.model;

/**
 * Created by blanka on 8/4/2016.
 */
public class CategoryType {

    private int mCatType;
    private String mTypeName;

    public CategoryType(int mCatType, String mTypeName) {
        this.mCatType = mCatType;
        this.mTypeName = mTypeName;
    }

    public int getmCatType() {
        return mCatType;
    }

    public String getmTypeName() {
        return mTypeName;
    }
}
