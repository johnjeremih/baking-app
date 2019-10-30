package com.johnny.john.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;

import android.util.Log;

import com.johnny.john.bakingapp.data.Contractor.IngredientsEntry;

public class RecipeContentProvider extends ContentProvider {



    public static final int CODE_LIST_WIDGET = 100;
    public static final int CODE_LIST_WIDGET_ITEM = 101;
    private IngredientsDbHelper helper;
    private String LOG_TAG= RecipeContentProvider.class.getSimpleName();

    private static final UriMatcher matcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        helper = new IngredientsDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        int match = matcher.match(uri);
        SQLiteDatabase db = helper.getReadableDatabase();

        switch (match) {
            case CODE_LIST_WIDGET:


                cursor = db.query(IngredientsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_LIST_WIDGET_ITEM:


                String[] args = {uri.getLastPathSegment()};
                cursor = db.query(IngredientsEntry.TABLE_NAME,
                        projection,
                        IngredientsEntry.COLUMN_RECIPE_ID + "=?",
                        args,
                        null,
                        null,
                        sortOrder
                );

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = matcher.match(uri);

        switch (match) {
            case CODE_LIST_WIDGET:
                return Contractor.IngredientsEntry.CONTENT_LIST_TYPE;
            case CODE_LIST_WIDGET_ITEM:
                return Contractor.IngredientsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Uri " + uri + "has no match with " + match);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (values == null || values.size() == 0) {

            return null;

        }

        long newRowId;
        int recipeId = 1;
        String title = null;


        if (values.containsKey(IngredientsEntry.COLUMN_RECIPE_ID)) {

            recipeId = values.getAsInteger(IngredientsEntry.COLUMN_RECIPE_ID);

        }

        if (values.containsKey(IngredientsEntry.COLUMN_RECIPE_NAME)) {

            title = values.getAsString(IngredientsEntry.COLUMN_RECIPE_NAME);

        }

        if (recipeId <= 0 || title == null) {

            throw new IllegalArgumentException("A recipe needs a valid recipe id and a title");

        }

        int match =matcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();

        switch (match) {
            case CODE_LIST_WIDGET:


                newRowId = db.insert(IngredientsEntry.TABLE_NAME, null, values);

                if (newRowId == -1) {

                    Log.e(LOG_TAG, "Movie insert failed for URI: " + uri);

                } else {

                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(uri, recipeId);

                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;

        int match = matcher.match(uri);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        switch (match) {
            case CODE_LIST_WIDGET:

                numberOfRowsDeleted = sqLiteDatabase.delete(IngredientsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_LIST_WIDGET_ITEM:

                String[] args = {uri.getLastPathSegment()};
                numberOfRowsDeleted = sqLiteDatabase.delete(IngredientsEntry.TABLE_NAME, IngredientsEntry.COLUMN_RECIPE_ID + "=?", args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {

            return 0;

        }

        int numberOfRowsUpdated;
        int movieId = 0;
        String title = null;


        if (values.containsKey(IngredientsEntry.COLUMN_RECIPE_ID)) {

            movieId = values.getAsInteger(IngredientsEntry.COLUMN_RECIPE_ID);

        }

        if (values.containsKey(IngredientsEntry.COLUMN_RECIPE_NAME)) {

            title = values.getAsString(IngredientsEntry.COLUMN_RECIPE_NAME);

        }

        if (movieId <= 0 || title == null) {

            throw new IllegalArgumentException("A movie needs a valid movie id and a title");

        }

        int match = matcher.match(uri);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        switch (match) {
            case CODE_LIST_WIDGET:


                numberOfRowsUpdated = sqLiteDatabase.update(IngredientsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_LIST_WIDGET_ITEM:

                String[] args = new String[]{uri.getLastPathSegment()};
                numberOfRowsUpdated = sqLiteDatabase.update(
                        IngredientsEntry.TABLE_NAME,
                        values,
                        IngredientsEntry.COLUMN_RECIPE_ID + "=?",
                        args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    public static UriMatcher buildUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contractor.CONTENT_AUTHORITY, Contractor.PATH_WIDGET, CODE_LIST_WIDGET);
        uriMatcher.addURI(Contractor.CONTENT_AUTHORITY, Contractor.PATH_WIDGET + "/#", CODE_LIST_WIDGET_ITEM);
        return uriMatcher;
    }

}
