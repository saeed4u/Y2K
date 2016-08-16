package glivion.y2k.android.constants;

import android.graphics.Color;

/**
 * Created by saeedissah on 5/16/16.
 */
public final class Constants {

    private Constants() {

    }


    public static final int INCOME_CAT = 0;
    public static final int EXPENDITURE_CAT = 1;

    public static final int IS_INCOME = 0;
    public static final int IS_EXPENDITURE = 1;

    public static final int NUMBER_OF_DEFAULT = 0;

    public static final String LOAN = "loan";

    public static final String DATABASE_NAME = "y2k";
    public static final int DATABASE_VERSION = 9;
    public static final String LOAN_TABLE = "loans";
    public static final String LOAN_PAYMENT = "loan_payment";
    public static final String IN_EX_TABLE = "in_ex_table";


    public static final String DATE_CREATED = "date_created";
    public static final String LOAN_COLOR = "loan_color";

    public static final String CAT = "category";
    public static final String CAT_ID = "category_id";
    public static final String CAT_NAME = "category_name";
    public static final String CAT_COLOR = "category_color";
    public static final String CAT_TYPE = "cat_type";

    //Loan id,title, details, amount(principal), interest rate, borrowed?, due date, created date.
    public static final String LOAN_ID = "loan_id";
    public static final String LOAN_TITLE = "loan_title";
    public static final String LOAN_DETAILS = "loan_detail";
    public static final String LOAN_AMOUNT = "loan_amount";
    public static final String LOAN_INTEREST = "loan_interest";
    public static final String LOAN_BORROWED = "loan_borrowed";
    public static final String LOAN_DUE_DATE = "loan_due_date";
    public static final String LOAN_COMPLETED = "loan_completed";
    public static final String IS_NOTIFICATION = "is_notified";


    public static final String IN_EX_ID = "in_ex_id";
    public static final String IN_EX_TITLE = "in_ex_title";
    public static final String IN_EX_DETAILS = "in_ex_detail";
    public static final String IN_EX_AMOUNT = "in_ex_amount";
    public static final String IN_EX_IS_INCOME = "in_ex_is_income";
    public static final String IN_EX_CREATED_AT = "in_ex_created_at";
    public static final String IN_EX_PAY_DATE = "in_ex_pay_date";
    public static final String IN_EX_WEEK = "in_ex_week";
    public static final String IN_EX_MONTH = "in_ex_month";
    public static final String IN_EX_YEAR = "in_ex_year";

    //Budget
    public static final String BUDGET = "budget";
    public static final String BUDGET_ID = "budget_id";
    public static final String BUDGET_TOTAL = "budget_total";
    public static final String BUDGET_TITLE = "budget_title";
    public static final String BUDGET_IN_EX = "budget_in_ex";
    public static final String BUDGET_CREATED_AT = "budget_created_at";
    public static final String BUDGET_DUE_DATE = "budget_due_date";
    public static final String BUDGET_COMPLETED = "budget_completed";
    public static final String BUDGET_COLOR = "budget_color";

    //Budget Item
    public static final String BUDGET_ITEM = "budget_item";
    public static final String BUDGET_ITEM_ID = "budget_item_id";
    public static final String BUDGET_ITEM_NAME = "budget_item_name";
    public static final String BUDGET_ITEM_AMOUNT = "budget_item_amount";

    // loan
    public static final String CREATE_LOAN_TABLE = "CREATE TABLE IF NOT EXISTS " + LOAN_TABLE + "(" + LOAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOAN_TITLE
            + " TEXT NOT NULL, " + LOAN_DETAILS + " TEXT," + LOAN_AMOUNT + " REAL NOT NULL,"
            + LOAN_INTEREST + " REAL NOT NULL,"
            + LOAN_BORROWED + " INTEGER NOT NULL," + LOAN_DUE_DATE + " TEXT NOT NULL," + LOAN_COLOR + " INTEGER NOT NULL," + DATE_CREATED + " TEXT NOT NULL," + IS_NOTIFICATION + " INTEGER);";

    //LOAN PAYMENT payment id, loan id, amount paid and date paid
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_LOAN_ID = "loan_id";
    public static final String PAYMENT_AMOUNT = "payment_amount";
    public static final String DATE_PAID = "date_paid";

    public static final String CREATE_PAYMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + LOAN_PAYMENT + "(" + PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PAYMENT_LOAN_ID
            + " INTEGER NOT NULL, " + PAYMENT_AMOUNT + " REAL NOT NULL," +
            DATE_PAID + " TEXT NOT NULL, FOREIGN KEY (" + PAYMENT_LOAN_ID + ") REFERENCES " + LOAN_TABLE + "(" + LOAN_ID + "));";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    //CAT
    public static final String CREATE_CAT_TABLE = "CREATE TABLE IF NOT EXISTS " + CAT + "(" + CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAT_NAME + " TEXT NOT NULL, " + CAT_TYPE + " INTEGER NOT NULL, " + CAT_COLOR + " INTEGER NOT NULL);";

    //INCOME & EXPENDITURE
    public static final String CREATE_IN_EX_TABLE = "CREATE TABLE IF NOT EXISTS " + IN_EX_TABLE + "(" + IN_EX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IN_EX_TITLE + " TEXT NOT NULL, " + IN_EX_WEEK + " INTEGER NOT NULL, " + IN_EX_MONTH + " INTEGER NOT NULL," + IN_EX_YEAR + " INTEGER NOT NULL, " + IN_EX_DETAILS + " TEXT, " + IN_EX_AMOUNT + " REAL NOT NULL," + CAT_ID + " INTEGER NOT NULL, " + IN_EX_IS_INCOME + " INTEGER NOT NULL," + IN_EX_CREATED_AT + " TEXT NOT NULL,"
            + IN_EX_PAY_DATE + " TEXT NOT NULL, FOREIGN KEY (" + CAT_ID + ") REFERENCES " + CAT + "(" + CAT_ID + "))";

    //BUDGET
    public static final String CREATE_BUDGET_TABLE = "CREATE TABLE IF NOT EXISTS " + BUDGET + "(" + BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BUDGET_TITLE + " TEXT NOT NULL," + BUDGET_TOTAL + " REAL NOT NULL," + BUDGET_COLOR + " INTEGER NOT NULL," + BUDGET_IN_EX + " INTEGER NOT NULL," + BUDGET_DUE_DATE + " TEXT NOT NULL," + BUDGET_CREATED_AT + " TEXT NOT NULL,"
            + BUDGET_COMPLETED + " INTEGER NOT NULL);";

    public static String CREATE_FIRST_CAT = "INSERT INTO " + CAT + " VALUES(" + "1,'default',-1," + Color.parseColor("#FF00BCD4") + ");";

    public static String CREATE_BUDGET_ITEM = "CREATE TABLE IF NOT EXISTS " + BUDGET_ITEM + "(" + BUDGET_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + BUDGET_ITEM_NAME + " TEXT NOT NULL," + BUDGET_ITEM_AMOUNT + " REAL NOT NULL," + BUDGET_ID + " INTEGER NOT NULL);";


}
