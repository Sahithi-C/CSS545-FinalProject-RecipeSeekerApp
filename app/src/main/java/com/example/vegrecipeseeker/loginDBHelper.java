package com.example.vegrecipeseeker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

public class loginDBHelper extends SQLiteOpenHelper {  // Changed class name to follow Java naming conventions and extend SQLiteOpenHelper
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public loginDBHelper(Context context) {  // Constructor name matches class name
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to add new user
    public boolean addUser(String username, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            // Encode username and password in base64
            String encodedUsername = Base64.encodeToString(username.getBytes(), Base64.DEFAULT);
            String encodedPassword = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);

            values.put(COLUMN_USERNAME, encodedUsername);
            values.put(COLUMN_PASSWORD, encodedPassword);

            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        } catch (Exception e) {
            return false;
        }
    }

    public int checkUser(String username, String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            // Encode credentials for comparison
            String encodedUsername = Base64.encodeToString(username.getBytes(), Base64.DEFAULT);
            String encodedPassword = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);

            // First, check if user exists
            String[] columns = {COLUMN_ID};
            String selection = COLUMN_USERNAME + " = ?";
            String[] selectionArgs = {encodedUsername};

            Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

            if (cursor.getCount() == 0) {
                // User does not exist
                cursor.close();
                return 0; // User not found
            }

            // User exists, now check password
            selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
            selectionArgs = new String[]{encodedUsername, encodedPassword};

            cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
            boolean passwordCorrect = cursor.getCount() > 0;
            cursor.close();

            return passwordCorrect ? 1 : -1; // 1 for successful login, -1 for wrong password
        } catch (Exception e) {
            return -2; // Error case
        }
    }

    // Method to check if username exists
    public boolean isUsernameTaken(String username) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String encodedUsername = Base64.encodeToString(username.getBytes(), Base64.DEFAULT);

            String selection = COLUMN_USERNAME + " = ?";
            String[] selectionArgs = {encodedUsername};

            Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        } catch (Exception e) {
            return false;
        }
    }
}