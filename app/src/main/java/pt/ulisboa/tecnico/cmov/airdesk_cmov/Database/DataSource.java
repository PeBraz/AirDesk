package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoDatabaseException;


public abstract class DataSource <T>{


    private static SQLiteDatabase database = null;
    private static MySQLiteHelper dbHelper = null;
    //private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_USERNAME, MySQLiteHelper.COLUMN_PASSWORD, MySQLiteHelper.COLUMN_EMAIL};

    public DataSource(Context context) throws SQLException{
        if (DataSource.database != null) return;
        DataSource.dbHelper = new MySQLiteHelper(context);
        DataSource.database = dbHelper.getWritableDatabase();
    }
    public DataSource () {
    }

    public static SQLiteDatabase getDatabase () throws NoDatabaseException{
        if (DataSource.database == null) throw new NoDatabaseException();
        return DataSource.database;
    }

    public abstract void create(T object);

    public abstract T get(final String key);

    public abstract List<T> getAll();

}
