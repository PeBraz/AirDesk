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
        values.put(MySQLiteHelper.FILE_PATH, file.getPath());
        values.put(MySQLiteHelper.FILE_WORKSPACE, file.getWorkspace());
        database.insert(MySQLiteHelper.TABLE_FILES, null, values);

    }
    @Override
    public File get(final String filekey) {
        final File file;
        Cursor cursor = database.rawQuery("select * from "+MySQLiteHelper.TABLE_FILES+" where "+ MySQLiteHelper.FILE_NAME+" = ? ",
                new String[] {filekey});
        cursor.moveToFirst();
        file = cursorToFile(cursor);
        cursor.close();
        return file;

    }
    @Override
    public List<File> getAll() {

        List<File> files = new ArrayList<File>();
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

    private File cursorToFile(Cursor cursor) {
        File file = new File();
        file.setName(cursor.getString(0));
        file.setPath(cursor.getString(1));
        file.setWorkspace(cursor.getString(2));
        return file;
    }
}
