package kz.informatics.labirintoiyn.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "labirint.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_STUDENTS = "students";

    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_LEVEL = "level";


    Context context;

    public StoreDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + "(" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LEVEL + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);

        onCreate(db);
    }

}