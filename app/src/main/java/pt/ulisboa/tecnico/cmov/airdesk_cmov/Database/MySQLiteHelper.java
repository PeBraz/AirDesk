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
    public static final String USER_FOREIGN = "subscribed";

    private static final String DATABASE_NAME = "airdeskdb";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_WORKSPACES = "workspaces";
    public static final String WS_NAME = "name";
    public static final String WS_QUOTA = "quota";
    public static final String WS_PRIVACY = "private";
    public static final String WS_TAGS = "tags";
    public static final String WS_USER = "owner";
    public static final String WS_ACCESS ="access";
    public static final String WS_STORAGE = "storage";

    public static final String TABLE_FILES = "files";
    public static final String FILE_WORKSPACE = "workspace";
    public static final String FILE_NAME = "filename";
    public static final String FILE_PATH = "path";
    public static final String FILE_USER = "owner";


    private static final String USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    USER_EMAIL + " TEXT PRIMARY KEY, "+
                    USER_USERNAME + " TEXT, " +
                    USER_FOREIGN + " BLOB);";


    private static final String WORKSPACES_CREATE =
            "CREATE TABLE " + TABLE_WORKSPACES + " ( "+
                WS_NAME + " TEXT, " +
                WS_QUOTA + " INTEGER, " +
                WS_PRIVACY + " INTEGER, " +
                WS_TAGS + " TEXT, " +
                WS_ACCESS + " BLOB, " +
                WS_USER + " TEXT, " +
                WS_STORAGE + " INTEGER, " +
                " FOREIGN KEY ("+WS_USER+") REFERENCES "+TABLE_USERS+" (" + USER_EMAIL+ ")," +
                " PRIMARY KEY ("+WS_NAME+", "+WS_USER+"));";

    private static final String FILES_CREATE =
            "CREATE TABLE " + TABLE_FILES + " (" +
                    FILE_NAME + " TEXT, " +
                    FILE_PATH + " TEXT, " +
                    FILE_USER + " TEXT, " +
                    FILE_WORKSPACE + " TEXT, " +
                    " FOREIGN KEY ("+FILE_WORKSPACE+") REFERENCES "+TABLE_WORKSPACES+" (" + WS_NAME + "), "+
                    " FOREIGN KEY ("+FILE_USER+") REFERENCES "+TABLE_USERS+" ("+ USER_EMAIL+"), " +
                    " PRIMARY KEY (" +FILE_NAME+", "+FILE_WORKSPACE+", "+FILE_USER +" ));";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(USERS_CREATE);
        database.execSQL(WORKSPACES_CREATE);
        database.execSQL(FILES_CREATE);
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
