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
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_USER };

    public UsersDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User createUser(User user) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USER, user.getUsername());
        long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public void deleteUser(User user) {
        long id = User.getId();
        System.out.println("User deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
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
