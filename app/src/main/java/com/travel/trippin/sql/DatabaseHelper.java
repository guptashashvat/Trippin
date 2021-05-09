package com.travel.trippin.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.travel.trippin.data.model.Tripper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TripperManager.db";

    // Tripper table name
    private static final String TABLE_TRIPPER = "tripper";

    // Tripper Table Columns names
    private static final String COLUMN_TRIPPER_ID = "_id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_TRIPPER_NAME = "tripper_name";
    private static final String COLUMN_TRIPPER_EMAIL = "tripper_email";
    private static final String COLUMN_TRIPPER_PASSWORD = "tripper_password";

    // create table sql query
    private String CREATE_TRIPPER_TABLE = "CREATE TABLE " + TABLE_TRIPPER + "("
            + COLUMN_TRIPPER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FIRST_NAME
            + " TEXT," + COLUMN_LAST_NAME + " TEXT," + COLUMN_TRIPPER_NAME + " TEXT NOT NULL UNIQUE,"
            + COLUMN_TRIPPER_EMAIL + " TEXT NOT NULL UNIQUE," + COLUMN_TRIPPER_PASSWORD + " TEXT" + ")";

    // drop table sql query
    private String DROP_TRIPPER_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRIPPER;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRIPPER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop Tripper Table if exist
        db.execSQL(DROP_TRIPPER_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create tripper record
     *
     * @param tripper
     */
    public void addTripper(Tripper tripper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, tripper.getFirstName());
        values.put(COLUMN_LAST_NAME, tripper.getLastName());
        values.put(COLUMN_TRIPPER_NAME, tripper.getTripperName());
        values.put(COLUMN_TRIPPER_EMAIL, tripper.getEmail());
        values.put(COLUMN_TRIPPER_PASSWORD, tripper.getPassword());

        // Inserting Row
        db.insert(TABLE_TRIPPER, null, values);
        db.close();
    }

    /**
     * This method is to fetch all tripper and return the list of tripper records
     *
     * @return list
     */
    public List<Tripper> getAllTripper() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_TRIPPER_ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_TRIPPER_EMAIL,
                COLUMN_TRIPPER_NAME,
                COLUMN_TRIPPER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_FIRST_NAME + " ASC, " + COLUMN_LAST_NAME + " ASC";
        List<Tripper> tripperList = new ArrayList<Tripper>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the tripper table
        /**
         * Here query function is used to fetch records from tripper table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT tripper_id, first_name, last_name, tripper_name,tripper_email,tripper_password FROM tripper ORDER BY first_name, last_name;
         */
        Cursor cursor = db.query(TABLE_TRIPPER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Tripper tripper = new Tripper(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TRIPPER_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TRIPPER_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TRIPPER_PASSWORD)));
                tripper.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TRIPPER_ID))));

                // Adding tripper record to list
                tripperList.add(tripper);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return tripper list
        return tripperList;
    }

    /**
     * This method to update tripper record
     *
     * @param tripper
     */
    public void updateTripper(Tripper tripper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, tripper.getFirstName());
        values.put(COLUMN_LAST_NAME, tripper.getLastName());
        values.put(COLUMN_TRIPPER_NAME, tripper.getTripperName());
        values.put(COLUMN_TRIPPER_EMAIL, tripper.getEmail());
        values.put(COLUMN_TRIPPER_PASSWORD, tripper.getPassword());

        // updating row
        db.update(TABLE_TRIPPER, values, COLUMN_TRIPPER_ID + " = ?",
                new String[]{String.valueOf(tripper.getId())});
        db.close();
    }

    /**
     * This method is to delete tripper record by Id
     *
     * @param id
     */
    public void deleteTripperById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete tripper record by id
        db.delete(TABLE_TRIPPER, COLUMN_TRIPPER_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * This method is to delete tripper record by tripperName or email
     *
     * @param emailOrtripperName
     */
    public void deleteTripperByTripperNameOrEmail(String emailOrtripperName) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete tripper record by tripperName
        db.delete(TABLE_TRIPPER, COLUMN_TRIPPER_NAME + " = ? OR " + COLUMN_TRIPPER_EMAIL + " = ?",
                new String[]{emailOrtripperName, emailOrtripperName});
        db.close();
    }

    /**
     * This method to check tripper exist or not
     *
     * @param emailOrTripperName
     * @return true/false
     */
    public boolean checkTripper(String emailOrTripperName) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_TRIPPER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_TRIPPER_NAME + " = ? OR "+ COLUMN_TRIPPER_EMAIL + " = ?";
        String[] selectionArgs = {emailOrTripperName, emailOrTripperName};

        // query tripper table with condition
        /**
         * Here query function is used to fetch records from tripper table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT tripper_id FROM tripper WHERE tripper_email = 'shashvat@tripper.com';
         */
        Cursor cursor = db.query(TABLE_TRIPPER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to get existing tripper
     *
     * @param emailOrTripperName
     * @param password
     * @return tripper object
     */
    public Tripper getTripper(String emailOrTripperName, String password) {

        Tripper tripper = null;
        // array of columns to fetch
        String[] columns = {
                COLUMN_TRIPPER_ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_TRIPPER_EMAIL,
                COLUMN_TRIPPER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "(" + COLUMN_TRIPPER_NAME + " = ? OR " + COLUMN_TRIPPER_EMAIL
                + "= ? )" + " AND " + COLUMN_TRIPPER_PASSWORD + " = ?";;
        String[] selectionArgs = {emailOrTripperName, emailOrTripperName, password};

        // query tripper table with conditions
        /**
         * Here query function is used to fetch records from tripper table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT tripper_id FROM tripper WHERE tripper_email = 'shashvat@tripper.com' AND tripper_password = 'trippinLove';
         */
        Cursor cursor = db.query(TABLE_TRIPPER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        if (cursor.moveToFirst()) {
            tripper = new Tripper(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TRIPPER_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TRIPPER_EMAIL)),
                    password);
            tripper.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TRIPPER_ID))));
        }

        cursor.close();
        db.close();

        return tripper;
    }
}
