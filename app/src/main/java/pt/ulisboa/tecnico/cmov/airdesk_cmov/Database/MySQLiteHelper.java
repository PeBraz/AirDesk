package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{

    public static final String TABLE_USERS = "users";
   // public static final String USER_ID = "id";
    public static final String USER_USERNAME = "USER";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_PASSWORD = "PASSWORD";

    private static final String DATABASE_NAME = "airdeskdb";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_WORKSPACES = "workspaces";
    private static final String WS_KEY = "name";
    private static final String WS_QUOTA = "quota";

    private static final String TABLE_FILES = "files";
    private static final String FILE_WORKSPACE = "owner";
    private static final String FILE_NAME = "filename";
    private static final String FILE_PATH = "path";


    private static final String USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    USER_USERNAME + " TEXT, " +
                    USER_EMAIL + " TEXT PRIMARY KEY, " +
                    USER_PASSWORD + " TEXT);";

    private static final String WORKSPACES_CREATE =
            "CREATE TABLE" + TABLE_WORKSPACES + " ( "+
                WS_KEY + " TEXT PRIMARY KEY, " +
                WS_QUOTA + " INT, " +
                " FOREIGN KEY ("+USER_EMAIL+") REFERENCES "+TABLE_USERS+" (" + USER_EMAIL+ "));";

    private static final String FILES_CREATE =
            "CREATE TABLE " + TABLE_FILES + " (" +
                    FILE_NAME + " TEXT PRIMARY KEY, " +
                    FILE_PATH + " TEXT, " +
                    " FOREIGN KEY ("+FILE_WORKSPACE+") REFERENCES "+TABLE_WORKSPACES+" (" + WS_KEY+ "));";

    // TABLE  for workspace clients



    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_USERS);
        database.execSQL(TABLE_WORKSPACES);
        database.execSQL(TABLE_FILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKSPACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
