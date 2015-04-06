package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoDatabaseException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;

public class WorkspacesDataSource extends DataSource<Workspace>{

    private SQLiteDatabase database;
    private String[] allColumns = { MySQLiteHelper.WS_NAME,
                                    MySQLiteHelper.WS_QUOTA,
                                    MySQLiteHelper.WS_PRIVACY,
                                    MySQLiteHelper.WS_TAGS,
                                    MySQLiteHelper.WS_ACCESS,
                                    MySQLiteHelper.WS_USER,
                                    MySQLiteHelper.WS_STORAGE};

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
        values.put(MySQLiteHelper.WS_QUOTA, ws.getQuota());
        values.put(MySQLiteHelper.WS_PRIVACY, ws.getPrivacy()?1:0);
        values.put(MySQLiteHelper.WS_TAGS, ws.getTags());
        values.put(MySQLiteHelper.WS_ACCESS, ws.getAccessListSerialized());
        values.put(MySQLiteHelper.WS_USER, ws.populateUser().getOwner().getUsername());
        values.put(MySQLiteHelper.WS_STORAGE, ws.getStorage());
        database.insert(MySQLiteHelper.TABLE_WORKSPACES, null, values);

    }
    @Override
    public void save(Workspace ws) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.WS_NAME, ws.getName());
        values.put(MySQLiteHelper.WS_QUOTA, ws.getQuota());
        values.put(MySQLiteHelper.WS_PRIVACY, ws.getPrivacy() ? 1 : 0);
        values.put(MySQLiteHelper.WS_TAGS, ws.getTags());
        values.put(MySQLiteHelper.WS_ACCESS, ws.getAccessListSerialized());
        values.put(MySQLiteHelper.WS_USER, ws.getOwner().getEmail());
        values.put(MySQLiteHelper.WS_STORAGE, ws.getStorage());
        database.update(MySQLiteHelper.TABLE_WORKSPACES, values,
                String.format(" %1$s=? ", MySQLiteHelper.WS_NAME), new String[]{ws.getName()});
    }


   public Workspace get(final String wsname, final String userEmail) {
        final Workspace ws;
        final String whereQuery =   MySQLiteHelper.WS_NAME+"=? AND "+
                MySQLiteHelper.WS_USER+"=? ";
        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKSPACES, null, whereQuery,
                new String[] {wsname, userEmail}, null, null, null);
        cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;
        ws = cursorToWorkspace(cursor);
        cursor.close();
        return ws;
    }

    @Override
    public List<Workspace> getAll() {

        List<Workspace> wss = new ArrayList<Workspace>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKSPACES, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Workspace ws = cursorToWorkspace(cursor);
            wss.add(ws);
            cursor.moveToNext();
        }
        cursor.close();
        return wss;
    }

    public final void remove(final String wsname, final String userEmail) {
        final String whereQuery =   MySQLiteHelper.WS_NAME+"=? AND "+
                MySQLiteHelper.WS_USER+"=? ";
        database.delete(MySQLiteHelper.TABLE_WORKSPACES, whereQuery,
                                new String[] {wsname, userEmail});
    }

    private Workspace cursorToWorkspace(Cursor cursor) {
        Workspace ws = new Workspace();
        ws.setName(cursor.getString(0));
        ws.setQuota(cursor.getInt(1));
        ws.setPrivacy(cursor.getInt(2) == 1);
        ws.setTags(cursor.getString(3));
        ws.setAccessList(cursor.getBlob(4));
        ws.setOwnerEmail(cursor.getString(5));
        ws.setStorage(cursor.getInt(6));
        return ws;
    }
}
