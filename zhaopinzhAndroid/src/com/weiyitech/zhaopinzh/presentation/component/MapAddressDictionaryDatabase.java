package com.weiyitech.zhaopinzh.presentation.component;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-24
 * Time: 下午8:30
 * To change this template use File | Settings | File Templates.
 */
public class MapAddressDictionaryDatabase{

    private static final String TAG = "MapAddressDictionaryDatabase";

    //The columns we'll include in the dictionary table

    private static final String DATABASE_NAME = "MAPADDRESSDICTIONARY";
    private static final String MAP_ADDRESS_SEARCHHISTORY_VIRTUAL_TABLE = "MAPADDRESSSEARCHHISTORY";
    private static final int DATABASE_VERSION = 1;
    public static final String INPUTTEXT = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String ADDTIME = SearchManager.SUGGEST_COLUMN_TEXT_2;

    private final DatabaseOpenHelper mDatabaseOpenHelper;
    private static final HashMap<String, String> mColumnMap = buildColumnMap();

    public MapAddressDictionaryDatabase(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
        mDatabaseOpenHelper.getReadableDatabase();
    }

    /**
     * Builds a map for all columns that may be requested, which will be given to the
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */
    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ADDTIME, ADDTIME);
        map.put(INPUTTEXT, INPUTTEXT);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }

    public Cursor getWordMatches(String query, String[] columns) {
        String selection = INPUTTEXT + " MATCH ?";
        String[] selectionArgs = new String[]{query + "*"};

        return querySearchHistoryTable(selection, selectionArgs, columns);
    }


    public Cursor getRecentWords(String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MAP_ADDRESS_SEARCHHISTORY_VIRTUAL_TABLE);
        builder.setProjectionMap(mColumnMap);
        Cursor cursor = null;
        try {
            cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                    columns, null, null, null, null, ADDTIME + " DESC");
        } catch (Exception e) {
            Log.e("cursor exception", e.getMessage());
        }
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor getWord(String rowId, String[] columns) {
        String selection = "rowid = ?";
        String[] selectionArgs = new String[]{rowId};

        return querySearchHistoryTable(selection, selectionArgs, columns);

        /* This builds a querySearchHistoryTable that looks like:
         *     SELECT <columns> FROM <table> WHERE rowid = <rowId>
         */
    }

    private Cursor querySearchHistoryTable(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MAP_ADDRESS_SEARCHHISTORY_VIRTUAL_TABLE);
        builder.setProjectionMap(mColumnMap);
        Cursor cursor = null;
        try {
            cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                    columns, selection, selectionArgs, null, null, null);
        } catch (Exception e) {
            Log.e("cursor exception", e.getMessage());
        }
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public void insertSearchHistoryTable(ContentValues contentValues) {
        mDatabaseOpenHelper.insertSearchHistoryTable(contentValues);
    }

    public int updateSearchHistoryTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
        return mDatabaseOpenHelper.updateSearchHistoryTable(initialValues, whereClause, whereArgs);
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String SEARCHHISTORY_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + MAP_ADDRESS_SEARCHHISTORY_VIRTUAL_TABLE +
                        " USING fts3(" +
                        ADDTIME + "," +
                        INPUTTEXT + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(SEARCHHISTORY_TABLE_CREATE);
            //loadDictionary();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + MAP_ADDRESS_SEARCHHISTORY_VIRTUAL_TABLE);
            onCreate(db);
        }

        public int updateSearchHistoryTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            try {
                return getWritableDatabase().update(MAP_ADDRESS_SEARCHHISTORY_VIRTUAL_TABLE, initialValues, whereClause, whereArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        public long insertSearchHistoryTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            long ret = db.insert(MAP_ADDRESS_SEARCHHISTORY_VIRTUAL_TABLE, null, initialValues);
            db.close();
            return ret;
        }
    }
}
