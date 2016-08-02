package glivion.y2k.ui.constants;

/**
 * Created by saeedissah on 5/16/16.
 */
public final class Constants {

    private Constants() {

    }

    public static final String LOAN = "loan";

    public static final String DATABASE_NAME = "y2k";
    public static final int DATABASE_VERSION = 4;
    public static final String LOAN_TABLE = "loans";
    public static final String LOAN_PAYMENT = "loan_payment";
    public static final String INCOME_TABLE = "income";
    public static final String EXPENDITURE_TABLE = "expenditure";


    public static final String DATE_CREATED = "date_created";
    public static final String LOAN_COLOR = "loan_color";

    public static final String CAT = "category";
    public static final String CAT_ID = "category_id";
    public static final String CAT_NAME = "category_name";
    public static final String CAT_COLOR = "category_color";

    //Loan id,title, details, amount(principal), interest rate, borrowed?, due date, created date.
    public static final String LOAN_ID = "loan_id";
    public static final String LOAN_TITLE = "loan_title";
    public static final String LOAN_DETAILS = "loan_detail";
    public static final String LOAN_AMOUNT = "loan_amount";
    public static final String LOAN_INTEREST = "loan_interest";
    public static final String LOAN_BORROWED = "loan_borrowed";
    public static final String LOAN_DUE_DATE = "loan_due_date";

    public static final String CREATE_LOAN_TABLE = "CREATE TABLE IF NOT EXISTS " + LOAN_TABLE + "(" + LOAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOAN_TITLE
            + " TEXT NOT NULL, " + LOAN_DETAILS + " TEXT," + LOAN_AMOUNT + " REAL NOT NULL,"
            + LOAN_INTEREST + " REAL NOT NULL,"
            + LOAN_BORROWED + " INTEGER NOT NULL," + LOAN_DUE_DATE + " TEXT NOT NULL," + DATE_CREATED + " TEXT NOT NULL);";

    //LOAN PAYMENT payment id, loan id, amount paid and date paid
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_LOAN_ID = "loan_id";
    public static final String PAYMENT_AMOUNT = "payment_amount";
    public static final String DATE_PAID = "date_paid";

    public static final String CREATE_PAYMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + LOAN_PAYMENT + "(" + PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PAYMENT_LOAN_ID
            + " INTEGER NOT NULL, " + PAYMENT_AMOUNT + " REAL NOT NULL," +
            DATE_PAID + " TEXT NOT NULL, FOREIGN KEY (" + PAYMENT_LOAN_ID + ") REFERENCES " + LOAN_TABLE + "(" + LOAN_ID + "));";

    public static final String DROP_TABLE = "DROP TABLE ";

    //CAT
    public static final String CREATE_CAT_TABLE = "CREATE TABLE IF NOT EXISTS " + CAT + "(" + CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAT_NAME + " TEXT NOT NULL, " + CAT_COLOR + " INTEGER NOT NULL);";


}
