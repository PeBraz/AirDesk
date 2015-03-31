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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;

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
        showList();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String fileTitle = (String) listview.getItemAtPosition(position);
            fileOptionsDialog(fileTitle);

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
            this.newFileDialog();
            return true;
        }else if (id == R.id.ws_settings) {
            this.changeSettings();
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

        final Button createFile = (Button) dialogView.findViewById(R.id.button);

        createFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView fileTitle = (TextView) dialog.findViewById(R.id.editText6);

                if (fileTitle.getText().toString().isEmpty()) {
                    Toast.makeText(ListFilesActivity.this, "A value is missing.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String title = fileTitle.getText().toString() + ".txt";
                Workspace.createFile(title, wsName, Application.getOwner().getEmail());

                dialog.dismiss();
                showList();

            }
        });
    }

    private void fileOptionsDialog(final String fileName){

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(fileName);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_options_files, null);
        dialog.setContentView(dialogView);

        dialog.show();

        Button cancel = (Button) dialog.findViewById(R.id.button9);
        Button readFile = (Button) dialog.findViewById(R.id.button10);
        Button writeFile = (Button) dialog.findViewById(R.id.button);
        Button deleteFile = (Button) dialog.findViewById(R.id.button8);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        readFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFileDialog(fileName);
                dialog.dismiss();
            }
        });

        writeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFileDialog(fileName);
                dialog.dismiss();
            }
        });


        deleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFileDialog(fileName);
                dialog.dismiss();
            }
        });

    }

    private void readFileDialog(String filename){

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(filename);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_read_file, null);
        dialog.setContentView(dialogView);

        dialog.show();

        TextView text = (TextView) dialog.findViewById(R.id.textView16);
        String fileText = read(filename);

        if (fileText == null)
            Toast.makeText(this, "File is still empty.", Toast.LENGTH_SHORT).show();

        else text.setText(fileText);

        Button cancel = (Button) dialog.findViewById(R.id.button11);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void writeFileDialog(final String filename){

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(filename);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_write_file, null);
        dialog.setContentView(dialogView);

        dialog.show();

        Button save = (Button) dialog.findViewById(R.id.button12);
        final EditText text = (EditText) dialog.findViewById(R.id.editText7);

        String actualText = read(filename);

        if (actualText!=null) text.setText(actualText);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write(filename, text.getText().toString());
                dialog.dismiss();
            }
        });


        Button cancel = (Button) dialog.findViewById(R.id.button13);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void deleteFileDialog(final String filename){

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(filename);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_delete_file, null);
        dialog.setContentView(dialogView);

        dialog.show();

        Button cancel = (Button) dialog.findViewById(R.id.button15);
        Button delete = (Button) dialog.findViewById(R.id.button14);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workspace.deleteFile(filename, wsName, Application.getOwner().getEmail());
                deleteFileStorage(filename);
                dialog.dismiss();
                showList();
            }
        });
    }

    private void write(String title, String text) {

        if (FileUtil.isExternalStorageWritable()) {
            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getPackageName());
            File file = new File(dir, title + "|" + Application.getOwner().getEmail());
            FileUtil.writeStringAsFile(text, file);
            Toast.makeText(this, "File written", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "External storage not writable", Toast.LENGTH_SHORT).show();
        }
    }

    private String read(String fileName) {

        if (FileUtil.isExternalStorageReadable()) {
            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getPackageName());
            File file = new File(dir, fileName + "|" + Application.getOwner().getEmail());

            if (file.exists() && file.canRead())
                return FileUtil.readFileAsString(file);
        }
        else Toast.makeText(this, "External storage not readable", Toast.LENGTH_SHORT).show();

        return null;
    }

    private void deleteFileStorage(String name){

        if (FileUtil.isExternalStorageReadable()) {
            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getPackageName());
            File file = new File(dir, name);

            if (file.exists()) file.delete();

            else Toast.makeText(this, "File does not exist.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showList(){

        final ListView listview = (ListView) findViewById(R.id.listView2);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, Workspace.getAllFiles(wsName));
        listview.setAdapter(adapter);
    }

    private void changeSettings() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.settings);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_ws_settings, null);
        dialog.setContentView(dialogView);

        dialog.show();
        final Workspace ws = Application.getOwner().getWorkspace(wsName);

        final int currentQuota = ws.getQuota();
        int deviceSpace = Application.getDeviceStorageSpace();


        final SeekBar quota = (SeekBar) dialog.findViewById(R.id.ws_settings_quota);
        quota.setMax(deviceSpace - currentQuota);

        final CheckBox isPrivate = (CheckBox) dialog.findViewById(R.id.private_checkbox);
        isPrivate.setChecked(ws.getPrivacy());

        final EditText tags = (EditText) dialog.findViewById(R.id.ws_settings_tags);
        tags.setText(ws.getTags());

        final TextView quotaTag = (TextView) dialog.findViewById(R.id.ws_settings_quota_tag);
        quotaTag.setText(Integer.toString(currentQuota));

        Button confirm = (Button) dialogView.findViewById(R.id.ws_settings_confirm);
        Button cancel = (Button) dialogView.findViewById(R.id.ws_settings_cancel);

        quota.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quotaTag.setText(Integer.toString(currentQuota + progress));
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            ws.setPrivacy(isPrivate.isChecked());
            ws.setQuota(quota.getProgress() + currentQuota);
            System.out.println("TAGS: "+ tags.getText().toString());
            ws.setTags(tags.getText().toString());
            ws.save();
            dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
