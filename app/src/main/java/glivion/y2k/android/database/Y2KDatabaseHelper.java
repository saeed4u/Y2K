package glivion.y2k.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static glivion.y2k.android.constants.Constants.BUDGET;
import static glivion.y2k.android.constants.Constants.BUDGET_ITEM;
import static glivion.y2k.android.constants.Constants.CAT;
import static glivion.y2k.android.constants.Constants.CREATE_BUDGET_ITEM;
import static glivion.y2k.android.constants.Constants.CREATE_BUDGET_TABLE;
import static glivion.y2k.android.constants.Constants.CREATE_CAT_TABLE;
import static glivion.y2k.android.constants.Constants.CREATE_FIRST_CAT;
import static glivion.y2k.android.constants.Constants.CREATE_IN_EX_TABLE;
import static glivion.y2k.android.constants.Constants.CREATE_LOAN_TABLE;
import static glivion.y2k.android.constants.Constants.CREATE_PAYMENT_TABLE;
import static glivion.y2k.android.constants.Constants.DATABASE_NAME;
import static glivion.y2k.android.constants.Constants.DATABASE_VERSION;
import static glivion.y2k.android.constants.Constants.DROP_TABLE;
import static glivion.y2k.android.constants.Constants.IN_EX_TABLE;
import static glivion.y2k.android.constants.Constants.LOAN_PAYMENT;
import static glivion.y2k.android.constants.Constants.LOAN_TABLE;

/**
 * Created by saeedissah on 5/16/16.
 */
public class Y2KDatabaseHelper extends SQLiteOpenHelper {


    public Y2KDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOAN_TABLE);
        db.execSQL(CREATE_PAYMENT_TABLE);
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_IN_EX_TABLE);
        db.execSQL(CREATE_BUDGET_TABLE);
        db.execSQL(CREATE_FIRST_CAT);
        db.execSQL(CREATE_BUDGET_ITEM);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE + LOAN_PAYMENT);
        db.execSQL(DROP_TABLE + LOAN_TABLE);
        db.execSQL(DROP_TABLE + CAT);
        db.execSQL(DROP_TABLE + IN_EX_TABLE);
        db.execSQL(DROP_TABLE + BUDGET);
        db.execSQL(DROP_TABLE + BUDGET_ITEM);
        onCreate(db);
    }
}
