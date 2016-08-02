package glivion.y2k.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by saeedissah on 5/16/16.
 */
public class Loan implements Parcelable {

    private int mLoanId;
    private String mLoanTitle;
    private String mLoanDetail;
    private double mLoanAmount;
    private float mLoanInterest;
    private String mDateCreated;
    private String mDueDate;
    private double mAmountLeft;
    private boolean isBorrowed;
    private boolean isDue;
    private boolean isPaid;
    private double mAmountPaid;
    private int mLoanColor;
    private ArrayList<LoanPayment> mLoanPayments;


    public Loan(int mLoanId, String mLoanTitle, String mLoanDetail, double mLoanAmount, float mLoanInterest, String mDateCreated, String mDueDate, int isBorrowed) {
        this.mLoanId = mLoanId;
        this.mLoanTitle = mLoanTitle;
        this.mLoanDetail = mLoanDetail;
        this.mLoanAmount = mLoanAmount;
        this.mLoanInterest = mLoanInterest;
        this.mDateCreated = mDateCreated;
        this.isBorrowed = isBorrowed == 0;
        this.mDueDate = mDueDate;
    }

    public double getAmountOwing() {
        double amountWithInterest = mLoanInterest + mLoanAmount;
        return amountWithInterest - mAmountPaid;
    }

    protected Loan(Parcel in) {
        mLoanId = in.readInt();
        mLoanTitle = in.readString();
        mLoanDetail = in.readString();
        mLoanAmount = in.readDouble();
        mLoanInterest = in.readFloat();
        mDateCreated = in.readString();
        mDueDate = in.readString();
        mAmountLeft = in.readDouble();
        isBorrowed = in.readByte() != 0;
        isDue = in.readByte() != 0;
        isPaid = in.readByte() != 0;
        mAmountPaid = in.readDouble();
        mLoanColor = in.readInt();
        mLoanPayments = in.createTypedArrayList(LoanPayment.CREATOR);
    }

    public static final Creator<Loan> CREATOR = new Creator<Loan>() {
        @Override
        public Loan createFromParcel(Parcel in) {
            return new Loan(in);
        }

        @Override
        public Loan[] newArray(int size) {
            return new Loan[size];
        }
    };

    private void setIsPaid() {
        mAmountPaid = 0;
        for (LoanPayment loanPayment : mLoanPayments) {
            mAmountPaid += loanPayment.getmAmountPaid();
        }
        isPaid = (mAmountLeft = (mLoanAmount + mLoanInterest) - mAmountPaid) == 0;
    }

    public double getmAmountPaid() {
        return mAmountPaid;
    }

    public int getmLoanId() {
        return mLoanId;
    }

    public String getmLoanTitle() {
        return mLoanTitle;
    }

    public String getmLoanDetail() {
        return mLoanDetail;
    }

    public String getmDueDate() {
        return mDueDate;
    }

    public double getmLoanAmount() {
        return mLoanAmount;
    }

    public float getmLoanInterest() {
        return mLoanInterest;
    }

    public String getmDateCreated() {
        return mDateCreated;
    }

    public double getmAmountLeft() {
        return mAmountLeft;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public boolean isDue() {
        return isDue;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public int getmLoanColor() {
        return mLoanColor;
    }

    public void setmLoanColor(int mLoanColor) {
        this.mLoanColor = mLoanColor;
    }

    public ArrayList<LoanPayment> getmLoanPayments() {
        return mLoanPayments;
    }

    public void setmLoanPayments(ArrayList<LoanPayment> mLoanPayments) {
        this.mLoanPayments = mLoanPayments;
        setIsPaid();
    }

    public void addPayment(LoanPayment payment) {
        mLoanPayments.add(payment);
        setIsPaid();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLoanId);
        dest.writeString(mLoanTitle);
        dest.writeString(mLoanDetail);
        dest.writeDouble(mLoanAmount);
        dest.writeFloat(mLoanInterest);
        dest.writeString(mDateCreated);
        dest.writeString(mDueDate);
        dest.writeDouble(mAmountLeft);
        dest.writeByte((byte) (isBorrowed ? 1 : 0));
        dest.writeByte((byte) (isDue ? 1 : 0));
        dest.writeByte((byte) (isPaid ? 1 : 0));
        dest.writeDouble(mAmountPaid);
        dest.writeInt(mLoanColor);
        dest.writeTypedList(mLoanPayments);

    }
}
