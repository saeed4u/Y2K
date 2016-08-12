package glivion.y2k.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.apache.commons.lang.WordUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import glivion.y2k.R;
import glivion.y2k.android.model.Budget;
import glivion.y2k.android.model.BudgetItem;
import glivion.y2k.android.model.Category;
import glivion.y2k.android.model.CategoryType;
import glivion.y2k.android.model.IncomeExpenditure;
import glivion.y2k.android.model.Loan;
import glivion.y2k.android.model.LoanPayment;

import static glivion.y2k.android.constants.Constants.BUDGET;
import static glivion.y2k.android.constants.Constants.BUDGET_COLOR;
import static glivion.y2k.android.constants.Constants.BUDGET_COMPLETED;
import static glivion.y2k.android.constants.Constants.BUDGET_CREATED_AT;
import static glivion.y2k.android.constants.Constants.BUDGET_DUE_DATE;
import static glivion.y2k.android.constants.Constants.BUDGET_ID;
import static glivion.y2k.android.constants.Constants.BUDGET_IN_EX;
import static glivion.y2k.android.constants.Constants.BUDGET_ITEM;
import static glivion.y2k.android.constants.Constants.BUDGET_ITEM_AMOUNT;
import static glivion.y2k.android.constants.Constants.BUDGET_ITEM_NAME;
import static glivion.y2k.android.constants.Constants.BUDGET_TITLE;
import static glivion.y2k.android.constants.Constants.BUDGET_TOTAL;
import static glivion.y2k.android.constants.Constants.CAT;
import static glivion.y2k.android.constants.Constants.CAT_COLOR;
import static glivion.y2k.android.constants.Constants.CAT_ID;
import static glivion.y2k.android.constants.Constants.CAT_NAME;
import static glivion.y2k.android.constants.Constants.CAT_TYPE;
import static glivion.y2k.android.constants.Constants.DATE_CREATED;
import static glivion.y2k.android.constants.Constants.DATE_PAID;
import static glivion.y2k.android.constants.Constants.EXPENDITURE_CAT;
import static glivion.y2k.android.constants.Constants.INCOME_CAT;
import static glivion.y2k.android.constants.Constants.IN_EX_AMOUNT;
import static glivion.y2k.android.constants.Constants.IN_EX_CREATED_AT;
import static glivion.y2k.android.constants.Constants.IN_EX_DETAILS;
import static glivion.y2k.android.constants.Constants.IN_EX_ID;
import static glivion.y2k.android.constants.Constants.IN_EX_IS_INCOME;
import static glivion.y2k.android.constants.Constants.IN_EX_PAY_DATE;
import static glivion.y2k.android.constants.Constants.IN_EX_TABLE;
import static glivion.y2k.android.constants.Constants.IN_EX_TITLE;
import static glivion.y2k.android.constants.Constants.IN_EX_WEEK;
import static glivion.y2k.android.constants.Constants.IS_EXPENDITURE;
import static glivion.y2k.android.constants.Constants.IS_INCOME;
import static glivion.y2k.android.constants.Constants.LOAN_AMOUNT;
import static glivion.y2k.android.constants.Constants.LOAN_BORROWED;
import static glivion.y2k.android.constants.Constants.LOAN_COLOR;
import static glivion.y2k.android.constants.Constants.LOAN_DETAILS;
import static glivion.y2k.android.constants.Constants.LOAN_DUE_DATE;
import static glivion.y2k.android.constants.Constants.LOAN_INTEREST;
import static glivion.y2k.android.constants.Constants.LOAN_PAYMENT;
import static glivion.y2k.android.constants.Constants.LOAN_TABLE;
import static glivion.y2k.android.constants.Constants.LOAN_TITLE;
import static glivion.y2k.android.constants.Constants.PAYMENT_AMOUNT;
import static glivion.y2k.android.constants.Constants.PAYMENT_LOAN_ID;

/**
 * Created by saeedissah on 5/16/16.
 */
public class Y2KDatabase {

    private SQLiteDatabase mDatabase;
    private ContentValues mContentValues;
    private Context mContext;
    private DecimalFormat decimalFormat;

    public Y2KDatabase(Context context) {
        Y2KDatabaseHelper database = new Y2KDatabaseHelper(context);
        mDatabase = database.getWritableDatabase();
        mContext = context;
        decimalFormat = new DecimalFormat("0.00");
    }

    //Loan id,title, details, amount(principal), interest rate, borrowed?, due date, created date.
    public boolean addLoan(String title, String description, double amount, float interest, boolean borrowed, String dueDate, String createdDate, int color) {

        mContentValues = new ContentValues();
        mContentValues.put(LOAN_TITLE, WordUtils.capitalize(title));
        mContentValues.put(LOAN_DETAILS, WordUtils.capitalize(description));
        mContentValues.put(LOAN_AMOUNT, Double.parseDouble(decimalFormat.format(amount)));
        mContentValues.put(LOAN_INTEREST, Double.parseDouble(decimalFormat.format(interest)));
        mContentValues.put(LOAN_BORROWED, borrowed ? 0 : 1);
        mContentValues.put(LOAN_DUE_DATE, dueDate);
        mContentValues.put(DATE_CREATED, createdDate);
        mContentValues.put(LOAN_COLOR, color);
        return mDatabase.insert(LOAN_TABLE, null, mContentValues) > 0;

    }

    public boolean addPayment(int loanId, double amount, String datePaid) {
        mContentValues = new ContentValues();
        mContentValues.put(PAYMENT_LOAN_ID, loanId);
        mContentValues.put(PAYMENT_AMOUNT, Double.parseDouble(decimalFormat.format(amount)));
        mContentValues.put(DATE_PAID, datePaid);
        boolean successful = mDatabase.insert(LOAN_PAYMENT, null, mContentValues) > 0;
        mDatabase.close();
        return successful;
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
        mDatabase.close();
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
        mDatabase.close();
        return loanPayments;
    }

    public boolean addCategory(String categoryName, int catType, int catColor) {
        mContentValues = new ContentValues();
        mContentValues.put(CAT_NAME, categoryName);
        mContentValues.put(CAT_COLOR, catColor);
        mContentValues.put(CAT_TYPE, catType);
        boolean successful = mDatabase.insert(CAT, null, mContentValues) > 0;
        mDatabase.close();
        return successful;
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(-1, "Add Income Category", INCOME_CAT, 0).setmCategoryType(new CategoryType(INCOME_CAT, mContext.getString(R.string.income_categories))));
        categories.add(new Category(0, "Add Expenditure Category", EXPENDITURE_CAT, 0).setmCategoryType(new CategoryType(EXPENDITURE_CAT, mContext.getString(R.string.expenditure_categories))));
        Cursor cursor = mDatabase.query(CAT, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(CAT_ID));
                int catType = cursor.getInt(cursor.getColumnIndex(CAT_TYPE));
                String name = cursor.getString(cursor.getColumnIndex(CAT_NAME));
                int color = cursor.getInt(cursor.getColumnIndex(CAT_COLOR));
                CategoryType categoryType;
                if (catType == 0) {
                    categoryType = new CategoryType(catType, "Income Categories");
                } else {
                    categoryType = new CategoryType(catType, "Expenditure Categories");
                }
                categories.add(new Category(id, name, catType, color).setmCategoryType(categoryType));
            }
            cursor.close();
        }
        mDatabase.close();
        return categories;
    }

    public ArrayList<Category> getCategory(int type) {
        ArrayList<Category> categories = new ArrayList<>();
        String selectionArgs[] = {String.valueOf(type)};
        Cursor cursor = mDatabase.query(CAT, null, CAT_TYPE + "=?", selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(CAT_ID));
                int catType = cursor.getInt(cursor.getColumnIndex(CAT_TYPE));
                String name = cursor.getString(cursor.getColumnIndex(CAT_NAME));
                int color = cursor.getInt(cursor.getColumnIndex(CAT_COLOR));
                CategoryType categoryType;
                if (catType == 0) {
                    categoryType = new CategoryType(catType, "Income Categories");
                } else {
                    categoryType = new CategoryType(catType, "Expenditure Categories");
                }
                categories.add(new Category(id, name, catType, color).setmCategoryType(categoryType));
            }
            cursor.close();
        }
        mDatabase.close();
        return categories;
    }

    private Category findCategory(int id) {
        String selectionArgs[] = {String.valueOf(id)};
        Cursor cursor = mDatabase.query(CAT, null, CAT_ID + " =? ", selectionArgs, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(CAT_NAME));
                int catType = cursor.getInt(cursor.getColumnIndex(CAT_TYPE));
                int color = cursor.getInt(cursor.getColumnIndex(CAT_COLOR));
                CategoryType categoryType;
                if (catType == 0) {
                    categoryType = new CategoryType(catType, "Income");
                } else {
                    categoryType = new CategoryType(catType, "Expenditure");
                }
                cursor.close();
                return new Category(id, name, catType, color).setmCategoryType(categoryType);
            }
        }
        mDatabase.close();
        return null;
    }

    public boolean addIncomeOrExpenditure(String title, String detail, boolean isIncome, double amount, String createdAt, String payDate, int catId, int weekNo) {
        mContentValues = new ContentValues();
        mContentValues.put(IN_EX_TITLE, title);
        mContentValues.put(IN_EX_DETAILS, detail);
        mContentValues.put(IN_EX_IS_INCOME, isIncome ? IS_INCOME : IS_EXPENDITURE);
        mContentValues.put(IN_EX_AMOUNT, Double.parseDouble(decimalFormat.format(amount)));
        mContentValues.put(IN_EX_CREATED_AT, createdAt);
        mContentValues.put(IN_EX_PAY_DATE, payDate);
        mContentValues.put(CAT_ID, catId);
        mContentValues.put(IN_EX_WEEK, weekNo);
        boolean successful = mDatabase.insert(IN_EX_TABLE, null, mContentValues) > 0;
        mDatabase.close();
        return successful;
    }

    public ArrayList<IncomeExpenditure> getIncomeOrExpenditure(int type, int week) {
        ArrayList<IncomeExpenditure> incomeExpenditures = new ArrayList<>();
        String[] selectionArgs = {String.valueOf(type)};
        Cursor cursor = mDatabase.query(IN_EX_TABLE, null, IN_EX_IS_INCOME + "=?", selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(IN_EX_ID));
                String title = cursor.getString(cursor.getColumnIndex(IN_EX_TITLE));
                String details = cursor.getString(cursor.getColumnIndex(IN_EX_DETAILS));
                double amount = cursor.getDouble(cursor.getColumnIndex(IN_EX_AMOUNT));
                String createdAt = cursor.getString(cursor.getColumnIndex(IN_EX_CREATED_AT));
                String payDate = cursor.getString(cursor.getColumnIndex(IN_EX_PAY_DATE));
                int isIncome = cursor.getInt(cursor.getColumnIndex(IN_EX_IS_INCOME));
                int catId = cursor.getInt(cursor.getColumnIndex(CAT_ID));
                int weekNumber = cursor.getInt(cursor.getColumnIndex(IN_EX_WEEK));
                Category category = findCategory(catId);
                IncomeExpenditure incomeExpenditure = new IncomeExpenditure(isIncome == IS_INCOME, amount, createdAt, details, id, payDate, title, weekNumber);
                incomeExpenditure.setmCategory(category);
                incomeExpenditures.add(incomeExpenditure);
            }
            cursor.close();
        }
        mDatabase.close();
        return incomeExpenditures;
    }

    public long addBudget(String title, double amount, boolean isIncome, String dueDate, boolean isCompleted, int color) {
        mContentValues = new ContentValues();
        mContentValues.put(BUDGET_TITLE, title);
        mContentValues.put(BUDGET_TOTAL, Double.parseDouble(decimalFormat.format(amount)));
        mContentValues.put(BUDGET_IN_EX, isIncome ? IS_INCOME : IS_EXPENDITURE);
        mContentValues.put(BUDGET_DUE_DATE, dueDate);
        mContentValues.put(BUDGET_COMPLETED, isCompleted ? 0 : 1);
        mContentValues.put(BUDGET_CREATED_AT, dueDate);
        mContentValues.put(BUDGET_COLOR, color);
        long successful = mDatabase.insert(BUDGET, null, mContentValues);
        mDatabase.close();
        return successful;
    }

    public ArrayList<Budget> getBudgets() {
        ArrayList<Budget> budgets = new ArrayList<>();
        Cursor cursor = mDatabase.query(BUDGET, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int budgetId = cursor.getInt(cursor.getColumnIndex(BUDGET_ID));
                String budgetTitle = cursor.getString(cursor.getColumnIndex(BUDGET_TITLE));
                boolean isIncome = cursor.getInt(cursor.getColumnIndex(BUDGET_IN_EX)) == IS_INCOME;
                double budgetTotal = cursor.getDouble(cursor.getColumnIndex(BUDGET_TOTAL));
                String dueDate = cursor.getString(cursor.getColumnIndex(BUDGET_DUE_DATE));
                boolean isCompleted = cursor.getInt(cursor.getColumnIndex(BUDGET_COMPLETED)) == 1;
                int color = cursor.getInt(cursor.getColumnIndex(BUDGET_COLOR));
                budgets.add(new Budget(isCompleted, isIncome, budgetId, budgetTitle, budgetTotal, dueDate, dueDate, color).setmBudgetItems(getBudgetItems(budgetId)));
            }
            cursor.close();
        }
        mDatabase.close();
        return budgets;
    }

    public ArrayList<Budget> getBudgets(int type) {
        ArrayList<Budget> budgets = new ArrayList<>();
        String selectionArgs[] = {String.valueOf(type)};
        Cursor cursor = mDatabase.query(BUDGET, null, BUDGET_IN_EX + "=?", selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int budgetId = cursor.getInt(cursor.getColumnIndex(BUDGET_ID));
                String budgetTitle = cursor.getString(cursor.getColumnIndex(BUDGET_TITLE));
                boolean isIncome = cursor.getInt(cursor.getColumnIndex(BUDGET_IN_EX)) == IS_INCOME;
                double budgetTotal = cursor.getDouble(cursor.getColumnIndex(BUDGET_TOTAL));
                String dueDate = cursor.getString(cursor.getColumnIndex(BUDGET_DUE_DATE));
                boolean isCompleted = cursor.getInt(cursor.getColumnIndex(BUDGET_COMPLETED)) == 1;
                int color = cursor.getInt(cursor.getColumnIndex(BUDGET_COLOR));
                budgets.add(new Budget(isCompleted, isIncome, budgetId, budgetTitle, budgetTotal, dueDate, dueDate, color).setmBudgetItems(getBudgetItems(budgetId)));
            }
            cursor.close();
        }
        mDatabase.close();
        return budgets;
    }

    public boolean addBudgetItem(String name, double amount, int budgetId) {
        mContentValues = new ContentValues();
        mContentValues.put(BUDGET_ITEM_NAME, name);
        mContentValues.put(BUDGET_ITEM_AMOUNT, Double.parseDouble(decimalFormat.format(amount)));
        mContentValues.put(BUDGET_ID, budgetId);
        boolean successful = mDatabase.insert(BUDGET_ITEM, null, mContentValues) > 0;
        mDatabase.close();
        return successful;
    }

    public ArrayList<BudgetItem> getBudgetItems(int budgetId) {
        ArrayList<BudgetItem> budgetItems = new ArrayList<>();
        String[] selectionArgs = {String.valueOf(budgetId)};
        Cursor cursor = mDatabase.query(BUDGET_ITEM, null, BUDGET_ID + "=?", selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int budgetItemId = cursor.getInt(cursor.getColumnIndex(BUDGET_ID));
                String name = cursor.getString(cursor.getColumnIndex(BUDGET_ITEM_NAME));
                double amount = cursor.getDouble(cursor.getColumnIndex(BUDGET_ITEM_AMOUNT));
                budgetItems.add(new BudgetItem(budgetItemId, name, amount));
            }
            cursor.close();
        }
        mDatabase.close();
        return budgetItems;
    }

    public boolean deleteItem(String tableName, String primaryKeyName, int rowId) {
        return mDatabase.delete(tableName, primaryKeyName + "=?", new String[]{String.valueOf(rowId)}) > 0;
    }

}
