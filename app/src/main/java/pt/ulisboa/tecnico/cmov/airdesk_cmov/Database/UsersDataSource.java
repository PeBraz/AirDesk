package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;

public class UsersDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_USERNAME, MySQLiteHelper.COLUMN_PASSWORD, MySQLiteHelper.COLUMN_EMAIL};

    public UsersDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public User createUser(User user) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USERNAME, user.getUsername());
        values.put(MySQLiteHelper.COLUMN_PASSWORD, user.getPassword());
        values.put(MySQLiteHelper.COLUMN_EMAIL, user.getEmail());
        long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public List<String> getAllUsers() {

        List<String> users = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user.getUsername());
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setUsername(cursor.getString(1));
        return user;
    }


}
