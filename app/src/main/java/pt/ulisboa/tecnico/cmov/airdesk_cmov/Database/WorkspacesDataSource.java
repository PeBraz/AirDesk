package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoDatabaseException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;

public class WorkspacesDataSource extends DataSource<Workspace>{

    private SQLiteDatabase database;
    private String[] allColumns = { MySQLiteHelper.WS_NAME,
            MySQLiteHelper.USER_USERNAME,
            MySQLiteHelper.USER_PASSWORD};

    public WorkspacesDataSource(Context context) {
        super(context);
        database = DataSource.getDatabase();
    }
    public WorkspacesDataSource() throws NoDatabaseException {
        database = DataSource.getDatabase();
    }

    @Override
    public void create(Workspace ws) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.WS_NAME, ws.getName());
        values.put(MySQLiteHelper.WS_QUOTA, ws.getMaxQuota());
        values.put(MySQLiteHelper.WS_USER, ws.getOwner().getUsername());
        database.insert(MySQLiteHelper.TABLE_WORKSPACES, null, values);

    }
    @Override
    public Workspace get(final String wsKey) {
        final Workspace ws;
        Cursor cursor = database.rawQuery("select * from "+MySQLiteHelper.TABLE_WORKSPACES+" where "+MySQLiteHelper.WS_NAME+" = ? ",
                new String[] {wsKey});
        cursor.moveToFirst();
        ws = cursorToWorkspace(cursor);
        cursor.close();
        return ws;

    }
    @Override
    public List<Workspace> getAll() {

        List<Workspace> wss = new ArrayList<Workspace>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Workspace ws = cursorToWorkspace(cursor);
            wss.add(ws);
            cursor.moveToNext();
        }
        cursor.close();
        return wss;
    }

    private Workspace cursorToWorkspace(Cursor cursor) {
        Workspace ws = new Workspace();
        ws.setName(cursor.getString(0));
        ws.setMaxQuota(cursor.getInt(1));
        //  LOAD user to get name??
        return ws;
    }
}
