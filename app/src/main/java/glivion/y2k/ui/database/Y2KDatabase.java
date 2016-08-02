package glivion.y2k.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;

import glivion.y2k.ui.model.Category;
import glivion.y2k.ui.model.Loan;
import glivion.y2k.ui.model.LoanPayment;

import static glivion.y2k.ui.constants.Constants.CAT;
import static glivion.y2k.ui.constants.Constants.CAT_COLOR;
import static glivion.y2k.ui.constants.Constants.CAT_ID;
import static glivion.y2k.ui.constants.Constants.CAT_NAME;
import static glivion.y2k.ui.constants.Constants.DATE_CREATED;
import static glivion.y2k.ui.constants.Constants.DATE_PAID;
import static glivion.y2k.ui.constants.Constants.LOAN_AMOUNT;
import static glivion.y2k.ui.constants.Constants.LOAN_BORROWED;
import static glivion.y2k.ui.constants.Constants.LOAN_COLOR;
import static glivion.y2k.ui.constants.Constants.LOAN_DETAILS;
import static glivion.y2k.ui.constants.Constants.LOAN_DUE_DATE;
import static glivion.y2k.ui.constants.Constants.LOAN_INTEREST;
import static glivion.y2k.ui.constants.Constants.LOAN_PAYMENT;
import static glivion.y2k.ui.constants.Constants.LOAN_TABLE;
import static glivion.y2k.ui.constants.Constants.LOAN_TITLE;
import static glivion.y2k.ui.constants.Constants.PAYMENT_AMOUNT;
import static glivion.y2k.ui.constants.Constants.PAYMENT_LOAN_ID;

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
    public boolean addLoan(String title, String description, double amount, float interest, boolean borrowed, String dueDate, String createdDate, int color) {

        mContentValues = new ContentValues();
        mContentValues.put(LOAN_TITLE, WordUtils.capitalize(title));
        mContentValues.put(LOAN_DETAILS, WordUtils.capitalize(description));
        mContentValues.put(LOAN_AMOUNT, amount);
        mContentValues.put(LOAN_INTEREST, interest);
        mContentValues.put(LOAN_BORROWED, borrowed ? 0 : 1);
        mContentValues.put(LOAN_DUE_DATE, dueDate);
        mContentValues.put(DATE_CREATED, createdDate);
        mContentValues.put(LOAN_COLOR, color);
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
                int color = cursor.getInt(7);
                String dateCreated = cursor.getString(8);
                Loan loan = new Loan(id, title, details, amount, interest, dateCreated, dueDate, borrowed);
                loan.setmLoanPayments(getLoanPayment(loan));
                loan.setmLoanColor(color);
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

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = mDatabase.query(CAT, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(CAT_ID));
                String name = cursor.getString(cursor.getColumnIndex(CAT_NAME));
                int color = cursor.getInt(cursor.getColumnIndex(CAT_COLOR));
                categories.add(new Category(id, name, color));
            }
            cursor.close();
        }
        return categories;
    }

    private Category findCategory(int id) {
        String selectionArgs[] = {String.valueOf(id)};
        Cursor cursor = mDatabase.query(CAT, null, CAT_ID + " =? ", selectionArgs, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(CAT_NAME));
                int color = cursor.getInt(cursor.getColumnIndex(CAT_COLOR));
                return new Category(id, name, color);
            }
        }
        return null;
    }

}
