package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;

public class UsersDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.USER_USERNAME, MySQLiteHelper.USER_PASSWORD, MySQLiteHelper.USER_EMAIL};

    public UsersDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void createUser(User user) {

        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.USER_EMAIL, user.getUsername());
        values.put(MySQLiteHelper.USER_PASSWORD, user.getPassword());
        values.put(MySQLiteHelper.USER_EMAIL, user.getEmail());

        database.insert(MySQLiteHelper.TABLE_USERS, null, values);

    }

    public User getUser(final String userKey) {
        final User user;
        Cursor cursor = database.rawQuery("select * from ? where ? = ? ",
                    new String[] {MySQLiteHelper.TABLE_USERS, MySQLiteHelper.USER_USERNAME, userKey});
        cursor.moveToFirst();
        user = cursorToUser(cursor);
        cursor.close();
        return user;

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
        user.setEmail(cursor.getString(2));
        return user;
    }

    public void createWorkspace(User user) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.USER_EMAIL, user.getUsername());
        values.put(MySQLiteHelper.USER_PASSWORD, user.getPassword());
        values.put(MySQLiteHelper.USER_EMAIL, user.getEmail());
        database.insert(MySQLiteHelper.TABLE_USERS, null, values);

    }


}
