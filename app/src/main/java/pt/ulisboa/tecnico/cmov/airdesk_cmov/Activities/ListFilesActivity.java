package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;


import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Files.FileUtil;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;


public class ListFilesActivity extends ActionBarActivity {

    private String wsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);

        Bundle info = getIntent().getExtras();
        if (info != null) wsName = info.getString("WSNAME");

        final ListView listview = (ListView) findViewById(R.id.listView2);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, new Workspace().getAllFiles(wsName));
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String itemValue = (String) listview.getItemAtPosition(position);
            String text = read(itemValue);
            fileOptionsDialog(itemValue, text);

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_list_files, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.create_file) {
            newFileDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newFileDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.new_file_title);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_new_file, null);
        dialog.setContentView(dialogView);

        dialog.show();

        Button createFile = (Button) dialogView.findViewById(R.id.button);

        createFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView fileTitle = (TextView) dialog.findViewById(R.id.editText6);
                TextView fileText = (TextView) dialog.findViewById(R.id.editText5);

                if (fileText.getText().toString().isEmpty() || fileTitle.getText().toString().isEmpty()) {
                    Toast.makeText(ListFilesActivity.this, "A value is missing.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String title = fileTitle.getText().toString() + ".txt";

                write(title, fileText.getText().toString());
                new Workspace().createFile(title, wsName);
                dialog.dismiss();

            }
        });
    }

    private void fileOptionsDialog(final String fileName, String textfile){

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(fileName);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_options_files, null);
        dialog.setContentView(dialogView);

        dialog.show();

        final EditText text = (EditText) dialog.findViewById(R.id.editText5);
        text.setText(textfile,TextView.BufferType.EDITABLE);

        Button cancel = (Button) dialog.findViewById(R.id.button9);
        Button saveFile = (Button) dialog.findViewById(R.id.button);
        Button deleteFile = (Button) dialog.findViewById(R.id.button8);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(fileName,text.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void write(String title, String text) {

        if (FileUtil.isExternalStorageWritable()) {
            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getPackageName());
            File file = new File(dir, title);
            FileUtil.writeStringAsFile(text, file);
            Toast.makeText(this, "File written", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "External storage not writable", Toast.LENGTH_SHORT).show();
    }

    private String read(String fileName) {

        if (FileUtil.isExternalStorageReadable()) {
            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getPackageName());
            File file = new File(dir, fileName);

            if (file.exists() && file.canRead())
                return FileUtil.readFileAsString(file);

            else Toast.makeText(this, "Unable to read file: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "External storage not readable", Toast.LENGTH_SHORT).show();

        return null;
    }


}
