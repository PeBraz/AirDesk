package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//The DATA ACCESS OBJECT(DAO) is responsible for handling the database connection and for accessing and modifying the data.
//AND convert the database objects into real Java Objects so that our user interface code does not have to deal with the persistence layer.


public class MySQLiteHelper extends SQLiteOpenHelper{ //CLASSE RESPONSAVEL POR CRIAR A BASE DE DADOS

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER = "USER";

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_CREATE = "create table "
            + TABLE_USERS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_USER
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
