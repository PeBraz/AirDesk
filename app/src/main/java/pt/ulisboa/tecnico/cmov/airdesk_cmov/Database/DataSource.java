package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;


public class DataSource {


    private static SQLiteDatabase database;
    private static MySQLiteHelper dbHelper;
    //private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_USERNAME, MySQLiteHelper.COLUMN_PASSWORD, MySQLiteHelper.COLUMN_EMAIL};

    public DataSource(Context context) {
        DataSource.dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public DataSource() {

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
                new String[] {MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_USERNAME, userKey});
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
        user.setPassword(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        return user;
    }
}
