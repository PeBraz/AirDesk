package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import android.content.Context;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoDatabaseException;


public abstract class DataSource <T>{


    private static SQLiteDatabase database = null;
    private static MySQLiteHelper dbHelper = null;

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

    /**
     *  Saves an already existing object onto the database, the fields will be updated if required.
     *  Only the attribute that serves as key will be used to find the existing entry.
     *
     * @param object the type of object that needs to be saved in the database
     *
     */
    public abstract void save(T object);

    public abstract T get(final String key);

    public abstract List<T> getAll();

}
