package glivion.y2k.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by saeedissah on 5/16/16.
 */
public class Loan implements Parcelable {

    private int mLoanId;
    private String mLoanTitle;
    private String mLoanDetail;
    private double mLoanAmount;
    private double mLoanInterest;
    private String mDateCreated;
    private String mDueDate;
    private double mAmountLeft;
    private boolean isBorrowed;
    private boolean isDue;
    private boolean isPaid;
    private double mAmountPaid;
    private int mLoanColor;
    private ArrayList<LoanPayment> mLoanPayments;
    private boolean isNotified = false;


    public Loan(int mLoanId, String mLoanTitle, String mLoanDetail, double mLoanAmount, double mLoanInterest, String mDateCreated, String mDueDate, int isBorrowed) {
        this.mLoanId = mLoanId;
        this.mLoanTitle = mLoanTitle;
        this.mLoanDetail = mLoanDetail;
        this.mLoanAmount = mLoanAmount;
        this.mLoanInterest = mLoanInterest;
        this.mDateCreated = mDateCreated;
        this.isBorrowed = isBorrowed == 0;
        this.mDueDate = mDueDate;
        mAmountLeft = mLoanAmount + mLoanInterest;
    }


    protected Loan(Parcel in) {
        mLoanId = in.readInt();
        mLoanTitle = in.readString();
        mLoanDetail = in.readString();
        mLoanAmount = in.readDouble();
        mLoanInterest = in.readDouble();
        mDateCreated = in.readString();
        mDueDate = in.readString();
        mAmountLeft = in.readDouble();
        isBorrowed = in.readByte() != 0;
        isDue = in.readByte() != 0;
        isPaid = in.readByte() != 0;
        mAmountPaid = in.readDouble();
        mLoanColor = in.readInt();
        mLoanPayments = in.createTypedArrayList(LoanPayment.CREATOR);
        isNotified = in.readByte() != 0;
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

    public double getAmountOwing() {
        double amountWithInterest = mLoanInterest + mLoanAmount;

        BigDecimal bigDecimal = new BigDecimal(amountWithInterest);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.FLOOR);


        BigDecimal bigDecimal1 = new BigDecimal(mAmountPaid);
        bigDecimal1 = bigDecimal1.setScale(2, RoundingMode.FLOOR);

        return bigDecimal.doubleValue() - bigDecimal1.doubleValue();
    }

    public Loan(boolean isNotified) {
        this.isNotified = isNotified;
    }

    private void setIsPaid() {
        mAmountPaid = 0;
        for (LoanPayment loanPayment : mLoanPayments) {
            mAmountPaid += loanPayment.getmAmountPaid();
        }

        BigDecimal bigDecimal = new BigDecimal(mLoanAmount + mLoanInterest);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.FLOOR);


        BigDecimal bigDecimal1 = new BigDecimal(mAmountPaid);
        bigDecimal1 = bigDecimal1.setScale(2, RoundingMode.FLOOR);

        mAmountLeft = bigDecimal.doubleValue() - bigDecimal1.doubleValue();
        isPaid = mAmountLeft == 0;
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

    public double getmLoanInterest() {
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

    public void setPaid(boolean paid) {
        isPaid = paid;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mLoanId);
        parcel.writeString(mLoanTitle);
        parcel.writeString(mLoanDetail);
        parcel.writeDouble(mLoanAmount);
        parcel.writeDouble(mLoanInterest);
        parcel.writeString(mDateCreated);
        parcel.writeString(mDueDate);
        parcel.writeDouble(mAmountLeft);
        parcel.writeByte((byte) (isBorrowed ? 1 : 0));
        parcel.writeByte((byte) (isDue ? 1 : 0));
        parcel.writeByte((byte) (isPaid ? 1 : 0));
        parcel.writeDouble(mAmountPaid);
        parcel.writeInt(mLoanColor);
        parcel.writeTypedList(mLoanPayments);
        parcel.writeByte((byte) (isNotified ? 1 : 0));
    }
}
