package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;

public class UsersDataSource extends DataSource<User>{

    private SQLiteDatabase database;

    private String[] allColumns = { MySQLiteHelper.USER_EMAIL,
                                    MySQLiteHelper.USER_USERNAME};

    public UsersDataSource(Context context) {
        super(context);
        database = DataSource.getDatabase();
    }

    @Override
    public void create(User user) {

        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.USER_USERNAME, user.getUsername());
        values.put(MySQLiteHelper.USER_EMAIL, user.getEmail());

        database.insert(MySQLiteHelper.TABLE_USERS, null, values);
    }
    @Override
    public void save(User user) {
        final String query = String.format("UPDATE %1$s SET %2$s= ? WHERE %3$s= ?",
               MySQLiteHelper.TABLE_USERS, MySQLiteHelper.USER_USERNAME, MySQLiteHelper.USER_EMAIL);
        database.rawQuery(query, new String[]{ user.getUsername(), user.getEmail()});
    }
    @Override
    public User get(final String userKey) {
        final User user;
        Cursor cursor = database.rawQuery("select * from "+MySQLiteHelper.TABLE_USERS+" where "+ MySQLiteHelper.USER_EMAIL+" = ? ",
        new String[] {userKey});
        cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;
        user = cursorToUser(cursor);
        return user;
    }

    @Override
    public List<User> getAll() {

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
        user.setUsername(cursor.getString(1));
        user.setEmail(cursor.getString(0));
        return user;
    }
}
