package glivion.y2k.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import glivion.y2k.ui.model.Loan;
import glivion.y2k.ui.model.LoanPayment;

import static glivion.y2k.ui.constants.Constants.*;

/**
 * Created by saeedissah on 5/16/16.
 */
public class Y2KDatabase {

    private SQLiteDatabase mDatabase;
    private ContentValues mContentValues;

    public Y2KDatabase(Context context) {
        Y2KDatabaseHelper database = new Y2KDatabaseHelper(context);
        mDatabase = database.getWritableDatabase();

    }

    //Loan id,title, details, amount(principal), interest rate, borrowed?, due date, created date.
    public boolean addLoan(String title, String description, double amount, float interest, boolean borrowed, String dueDate, String createdDate) {

        mContentValues = new ContentValues();
        mContentValues.put(LOAN_TITLE, title);
        mContentValues.put(LOAN_DETAILS, description);
        mContentValues.put(LOAN_AMOUNT, amount);
        mContentValues.put(LOAN_INTEREST, interest);
        mContentValues.put(LOAN_BORROWED, borrowed ? 0 : 1);
        mContentValues.put(LOAN_DUE_DATE, dueDate);
        mContentValues.put(DATE_CREATED, createdDate);
        return mDatabase.insert(LOAN_TABLE, null, mContentValues) > 0;

    }

    public boolean addPayment(int loanId, double amount, String datePaid) {
        mContentValues = new ContentValues();
        mContentValues.put(PAYMENT_LOAN_ID, loanId);
        mContentValues.put(PAYMENT_AMOUNT, amount);
        mContentValues.put(DATE_PAID, datePaid);
        return mDatabase.insert(LOAN_PAYMENT, null, mContentValues) > 0;
    }

    public ArrayList<Loan> getLoans(int type) {

        String[] selectionArgs = {String.valueOf(type)};

        Cursor cursor = mDatabase.query(LOAN_TABLE, null, type > -1 ? LOAN_BORROWED + "=?" : null, type > -1 ? selectionArgs : null, null, null, null);
        ArrayList<Loan> loans = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String details = cursor.getString(2);
                double amount = cursor.getDouble(3);
                float interest = cursor.getFloat(4);
                int borrowed = cursor.getInt(5);
                String dueDate = cursor.getString(6);
                String dateCreated = cursor.getString(7);
                Loan loan = new Loan(id, title, details, amount, interest, dateCreated, dueDate, borrowed);
                loan.setmLoanPayments(getLoanPayment(loan));
                loans.add(loan);

            }
            cursor.close();
        }
        return loans;
    }

    public ArrayList<LoanPayment> getLoanPayment(Loan loan) {
        String[] selectionArgs = {String.valueOf(loan.getmLoanId())};
        Cursor cursor = mDatabase.query(LOAN_PAYMENT, null, PAYMENT_LOAN_ID + "=?", selectionArgs, null, null, null);
        ArrayList<LoanPayment> loanPayments = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                double amountPaid = cursor.getDouble(2);
                String datePaid = cursor.getString(3);

                LoanPayment loanPayment = new LoanPayment(id, datePaid, amountPaid, datePaid);
                loanPayment.setmLoan(loan);
                loanPayments.add(loanPayment);
            }
            cursor.close();
        }
        return loanPayments;
    }

}
