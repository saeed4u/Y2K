package glivion.y2k.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saeedissah on 5/16/16.
 */
public class LoanPayment implements Parcelable{

    private int mLoanPaymentId;
    private String mDatePaid;
    private double mAmountPaid;
    private String mDateCreated;
    private Loan mLoan;
    private double mTotalAmountPaid = 0;

    public LoanPayment(int mLoanPaymentId, String mDatePaid, double mAmountPaid, String mDateCreated) {
        this.mLoanPaymentId = mLoanPaymentId;
        this.mDatePaid = mDatePaid;
        this.mAmountPaid = mAmountPaid;
        this.mDateCreated = mDateCreated;
        mTotalAmountPaid += mAmountPaid;
    }

    protected LoanPayment(Parcel in) {
        mLoanPaymentId = in.readInt();
        mDatePaid = in.readString();
        mAmountPaid = in.readDouble();
        mDateCreated = in.readString();
        mTotalAmountPaid = in.readDouble();
    }

    public static final Creator<LoanPayment> CREATOR = new Creator<LoanPayment>() {
        @Override
        public LoanPayment createFromParcel(Parcel in) {
            return new LoanPayment(in);
        }

        @Override
        public LoanPayment[] newArray(int size) {
            return new LoanPayment[size];
        }
    };

    public void setmLoan(Loan mLoan) {
        this.mLoan = mLoan;
    }


    public int getmLoanPaymentId() {
        return mLoanPaymentId;
    }

    public String getmDatePaid() {
        return mDatePaid;
    }

    public double getmAmountPaid() {
        return mAmountPaid;
    }

    public String getmDateCreated() {
        return mDateCreated;
    }

    public Loan getmLoan() {
        return mLoan;
    }

    public double getmTotalAmountPaid() {
        return mTotalAmountPaid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLoanPaymentId);
        dest.writeString(mDatePaid);
        dest.writeDouble(mAmountPaid);
        dest.writeString(mDateCreated);
        dest.writeDouble(mTotalAmountPaid);
    }
}
