package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;


import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        Button save = (Button) dialogView.findViewById(R.id.button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView fileTitle = (TextView) dialog.findViewById(R.id.editText6);
                TextView fileText = (TextView) dialog.findViewById(R.id.editText5);

                if (fileText.getText().toString().isEmpty() || fileTitle.getText().toString().isEmpty()) {
                    Toast.makeText(ListFilesActivity.this, "A value is missing.", Toast.LENGTH_SHORT).show();
                    return;
                }

                write(fileTitle.getText().toString(),fileText.getText().toString());
                new Workspace().createFile(fileTitle.getText().toString(),wsName);
                dialog.dismiss();

            }
        });

    }

    private void write(String title, String text) {
        if (FileUtil.isExternalStorageWritable()) {
            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getPackageName());
            File file = new File(dir, title + ".txt");
            FileUtil.writeStringAsFile(text, file);
            Toast.makeText(this, "File written", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "External storage not writable", Toast.LENGTH_SHORT).show();
        }
    }


}
