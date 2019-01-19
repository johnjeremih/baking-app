package com.johnny.john.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.johnny.john.bakingapp.data.Contractor.IngredientsEntry;

public class IngredientsDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "contractor.db";
    public static final int DATABASE_VERSION = 1;
    private static final String MODIFY_TABLE_COMMAND = "";




    public IngredientsDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + IngredientsEntry.TABLE_NAME + " (" +
                IngredientsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientsEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL," +
                IngredientsEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL," +
                IngredientsEntry.COLUMN_INGREDIENT_INGREDIENT+ " TEXT, " +
                IngredientsEntry.COLUMN_INGREDIENT_MEASURE + " TEXT, " +
                IngredientsEntry.COLUMN_INGREDIENT_QUANTITY + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {

            sqLiteDatabase.execSQL(MODIFY_TABLE_COMMAND);

        }
    }
}
