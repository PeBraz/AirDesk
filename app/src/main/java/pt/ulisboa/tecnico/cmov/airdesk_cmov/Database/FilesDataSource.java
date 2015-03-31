package pt.ulisboa.tecnico.cmov.airdesk_cmov.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.File;

public class FilesDataSource extends DataSource<File>{

    private SQLiteDatabase database;
    private String[] allColumns = {
            MySQLiteHelper.FILE_NAME,
            MySQLiteHelper.FILE_PATH,
            MySQLiteHelper.FILE_USER,
            MySQLiteHelper.FILE_WORKSPACE};

    public FilesDataSource(Context context) {
        super(context);
        database = DataSource.getDatabase();
    }

    public FilesDataSource() {
        database = DataSource.getDatabase();
    }

    @Override
    public void create(File file) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.FILE_NAME, file.getName());
        values.put(MySQLiteHelper.FILE_WORKSPACE, file.getWorkspace());
        values.put(MySQLiteHelper.FILE_USER, file.getUser());
        database.insert(MySQLiteHelper.TABLE_FILES, null, values);

    }
    @Override
    public void save(File file) {
        final String query = String.format("UPDATE %1$s SET %2$s= ?, %3$s= ? WHERE %4$s= ?",
                MySQLiteHelper.TABLE_FILES, MySQLiteHelper.FILE_PATH, MySQLiteHelper.FILE_WORKSPACE,
                MySQLiteHelper.FILE_NAME, MySQLiteHelper.FILE_USER);
        database.rawQuery(query, new String[]{ file.getPath(), file.getWorkspace(),
                                    file.getName(), file.getUser()});
    }
    /*
    @Override
    public File get(final String fileKey) {
        final File file;
        Cursor cursor = database.rawQuery("select * from "+MySQLiteHelper.TABLE_FILES+" where "+ MySQLiteHelper.FILE_NAME+" = ? ",
                new String[] {fileKey});
        cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;
        file = cursorToFile(cursor);
        cursor.close();
        return file;

    }*/

    public File get(final String filename, final String wsname, final String userEmail) {
        final File file;
        final String whereQuery =   MySQLiteHelper.FILE_NAME+"=? AND "+
                MySQLiteHelper.FILE_WORKSPACE+"=? AND "+
                MySQLiteHelper.FILE_USER+"=? ";
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FILES, null, whereQuery,
                new String[] {filename, wsname, userEmail}, null, null, null);
        cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;
        file = cursorToFile(cursor);
        cursor.close();
        return file;
    }

    @Override
    public List<File> getAll() {

        List<File> files = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FILES, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            File file = cursorToFile(cursor);
            files.add(file);
            cursor.moveToNext();
        }
        cursor.close();
        return files;
    }

    public final void delete(final String filename, final String wsname, final String userEmail) {
        final String whereQuery =   MySQLiteHelper.FILE_NAME+"=? AND "+
                MySQLiteHelper.FILE_WORKSPACE+"=? AND "+
                MySQLiteHelper.FILE_USER+"=? ";
        database.delete(MySQLiteHelper.TABLE_FILES , whereQuery,
                new String[] {filename, wsname, userEmail});
    }

    private File cursorToFile(Cursor cursor) {
        File file = new File();
        file.setName(cursor.getString(0));
        file.setPath(cursor.getString(1));
        file.setWorkspace(cursor.getString(3));
        file.setUser(cursor.getString(2));
        return file;
    }
}
