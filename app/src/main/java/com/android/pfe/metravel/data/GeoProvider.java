package com.android.pfe.metravel.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.FacebookSdk;

/**
 * Created by hpthai7 on 03/02/16.
 */
public class GeoProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.android.pfe.metravel/locations");

    private static final UriMatcher sUriMatcher;

    private static final int ALL_ROWS = 0;
    private static final int SINGLE_ROW = 1;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI("com.android.pfe.metravel", "locations", ALL_ROWS);
        sUriMatcher.addURI("com.android.pfe.metravel", "locations/#", SINGLE_ROW);
    }

    private DatabaseHelper dbHelper;

    // The index column name for use in where clauses
    public static final String KEY_ID = "_id";
    // The name and column index of each column in database
    public static final String GEO_NAME = "name";
    public static final String GEO_FBID = "fbid";
    public static final String GEO_ADDRESS = "address";
    public static final String GEO_CATEGORY = "category";
    public static final String GEO_COMMENT = "comment";
    public static final String GEO_LATITUDE = "latitude";
    public static final String GEO_LONGTITUDE = "longtitude";

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Open a read-only database.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Replace these with valid SQL statements if necessary.
        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(dbHelper.DATABASE_TABLE);

        // If this is a row query, limit the result set to the passed in row.
        switch (sUriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowID);
            default:
                break;
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, groupBy, having, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Return \the MIME type for a Content Provider URI
        switch (sUriMatcher.match(uri)) {
            case ALL_ROWS:
                return "vnd.android.cursor.dir/vnd.metravel.locations";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.metravel.locations";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = null;
        // Open a read / write database to support the transaction.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // To add empty rows to your database by passing in an empty Content Values object
        // you must use the null column hack parameter to specify the name of the column
        // that can be set to null.
        String nullColumnHack = null;

        // Insert the values into the table
        long id = db.insert(DatabaseHelper.DATABASE_TABLE,
                nullColumnHack, values);

        // Construct and return the URI of the newly inserted row.
        if (id > -1)
            resultUri = ContentUris.withAppendedId(CONTENT_URI, id);
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Open a read / write database to support the transaction.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // If this is a row URI, limit the deletion to the specified row.
        switch (sUriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            case ALL_ROWS:
                // To return the number of deleted items you must specify a where
                // clause. To delete all rows and return a value pass in "1".
                selection = "1";
                break;
            default:
                break;
        }
        int count = db.delete(DatabaseHelper.DATABASE_TABLE, selection, selectionArgs);
        // Notify any observers of the change in the data set
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Open a read / write database to support the transaction.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // If this is a row URI, limit the deletion to the specified row.
        switch (sUriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
            default:
                break;
        }

        int count = db.update(DatabaseHelper.DATABASE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    ////////////////////////////////////////////////////////////////////
    // DabataseHelper that handle creation and management of database //
    ////////////////////////////////////////////////////////////////////

    class DatabaseHelper extends SQLiteOpenHelper {

        private String sUserAccount;
        private static final String DATABASE_NAME = "geoDatabase.db";
        private static final String DATABASE_TABLE = "Locations";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" +
                GeoProvider.KEY_ID + " integer primary key autoincrement, " +
                GeoProvider.GEO_FBID + " text not null, " +
                GeoProvider.GEO_NAME + " text not null, " +
                GeoProvider.GEO_ADDRESS + " text not null, " +
                GeoProvider.GEO_CATEGORY + " text, " +
                GeoProvider.GEO_COMMENT + " text, " +
                GeoProvider.GEO_LATITUDE + " double not null, " +
                GeoProvider.GEO_LONGTITUDE + " double not null);";

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
