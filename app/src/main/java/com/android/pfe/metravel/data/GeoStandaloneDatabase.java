package com.android.pfe.metravel.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Interaction with database without using Content Provider
 */
public class GeoStandaloneDatabase {

    // The index column name for use in where clauses
    public static final String KEY_ID = "_id";
    // The name and column index of each column in database
    public static final String GEO_NAME = "name";
    public static final String GEO_ADDRESS = "address";
    public static final String GEO_CATEGORY = "category";
    public static final String GEO_COMMENT = "comment";

    private DatabaseHelper dbHelper;

    public GeoStandaloneDatabase(Context context) {
        dbHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
    }

    public void close() {
        dbHelper.close();
    }

    public void add(String name, String address, String category, String comment) {
        ContentValues values = new ContentValues();
        values.put(GEO_NAME, name);
        values.put(GEO_ADDRESS, address);
        values.put(GEO_CATEGORY, category);
        values.put(GEO_COMMENT, comment);
        String whereArgs[] = null;

        // Update the row with the specified index with the new values.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DatabaseHelper.DATABASE_TABLE, null, values);
    }

    public void update(int geoId, String name, String address, String category, String comment) {
        ContentValues updatedValues = new ContentValues();
        if (name != null) {
            updatedValues.put(GEO_NAME, name);
        }
        if (address != null) {
            updatedValues.put(GEO_ADDRESS, address);
        }
        if (category != null) {
            updatedValues.put(GEO_CATEGORY, category);
        }
        if (comment != null) {
            updatedValues.put(GEO_COMMENT, comment);
        }
        String where = KEY_ID + " = " + geoId;
        String whereArgs[] = null;

        // Update the row with the specified index with the new values.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(DatabaseHelper.DATABASE_TABLE, updatedValues, where, whereArgs);
    }

    public void delete(int geoId) {
        String where = KEY_ID + " = " + geoId;
        String whereArgs[] = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.DATABASE_TABLE, where, whereArgs);
    }

    ////////////////////////////////////////////////////////////////////
    // DabataseHelper that handle creation and management of database //
    ////////////////////////////////////////////////////////////////////

    class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "geoDatabase.db";
        private static final String DATABASE_TABLE = "GeoLink";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" +
                GeoStandaloneDatabase.KEY_ID + " integer primary key autoincrement, " +
                GeoStandaloneDatabase.GEO_NAME + " text not null, " +
                GeoStandaloneDatabase.GEO_ADDRESS + " text not null, " +
                GeoStandaloneDatabase.GEO_CATEGORY + " text, " +
                GeoStandaloneDatabase.GEO_COMMENT + " text);";

        private final String TAG = DatabaseHelper.class.getSimpleName();

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
                              DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrade from " + oldVersion + " to " + newVersion + ", which will destroy all data");
            db.execSQL("drop table if it exists " + DATABASE_TABLE);
            onCreate(db);
        }
    }
}
