package glivion.y2k.ui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static glivion.y2k.ui.constants.Constants.*;

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE + LOAN_PAYMENT);
        db.execSQL(DROP_TABLE + LOAN_TABLE);
        onCreate(db);
    }
}
