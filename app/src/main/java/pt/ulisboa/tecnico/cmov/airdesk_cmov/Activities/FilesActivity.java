package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;


import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.BuildConfig;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.StorageOverLimitException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserAlreadyAddedException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserIsMyselfException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserNotFoundException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Files.FileUtil;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.Peer;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.WorkspaceDto;


public class FilesActivity extends ActionBarActivity {


    private String wsName = null;
    private String wsEmail = null;
    private Workspace ws = null;
    private Peer peer;
    private boolean isMyWs;
    private Handler filesHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        Bundle info = getIntent().getExtras();
        if (info != null){
            wsName = info.getString("WSNAME");
            wsEmail = info.getString("WSUSEREMAIL");
            isMyWs = Application.getOwner().getEmail().equals(wsEmail);

            if (isMyWs) {
                ws = Application.getOwner().getWorkspace(wsName);
            }else
            {

                this.peer = Application.getPeer(wsEmail);
                this.syncGetFiles();
            }

        }
        setTitle("Workspace - " + wsName);

        final ListView listview = (ListView) findViewById(R.id.listView2);





        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String fileTitle = (String) listview.getItemAtPosition(position);
            fileOptionsDialog(fileTitle);
            }
        });
    }

    public void syncGetFiles(){
        (new Thread() {
            @Override
            public void run(){
                peer.getRemoteFiles(wsName);
                try {
                    while (true) {
                        Thread.sleep(50);
                        if (peer.filesChanged()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showList();
                                }
                            });
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMyWs) {
            getMenuInflater().inflate(R.menu.menu_files_owned, menu);
            MenuItem delete = menu.findItem(R.id.ws_remove);
            delete.setTitle("Remove - " + wsName);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_files_foreign, menu);
        }
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
        }else if (id == R.id.invite_user) {
            this.inviteDialog();
        }else if (id == R.id.ws_remove){
            this.removeWorkspaceDialog();
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
                    Toast.makeText(FilesActivity.this, "A value is missing.", Toast.LENGTH_SHORT).show();
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
        String fileText = readFileStorage(filename);

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

        final String actualText = readFileStorage(filename);
        //checks space being used, so that id doesn't get counted again
        final int oldSize = (actualText!=null)? actualText.getBytes().length: 0;

        if (actualText!=null)
            text.setText(actualText);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = text.getText().toString();
                int newSize = newText.getBytes().length;

                try {

                    ws.changeStorageUsed(newSize - oldSize);
                    boolean success = writeFileStorage(filename, newText);
                    if (!success)
                       ws.changeStorageUsed(-(newSize - oldSize));
                    dialog.dismiss();
                }catch(StorageOverLimitException e) {
                     // not possible in exception
                    if (BuildConfig.DEBUG && (newSize - oldSize) < 0)
                        throw new AssertionError("size had negative value, not possible");

                    Toast.makeText(getApplicationContext(),
                            "Can't save file, quota over limit.", Toast.LENGTH_SHORT).show();
                }
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

    private void deleteFileDialog(final String filename){ //.txt

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
                int weightLost = deleteFileStorage(filename);
                try {
                    ws.changeStorageUsed(-weightLost);
                }catch(StorageOverLimitException e){
                    System.out.println(e.getMessage());
                }
                dialog.dismiss();
                showList();
            }
        });
    }

    private boolean writeFileStorage(String title, String text) {

        if (FileUtil.isExternalStorageWritable()) {
            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getFilesPath());
            File file = new File(dir, title);
            FileUtil.writeStringAsFile(text, file);
            Toast.makeText(this, "File written", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "External storage not writable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private String readFileStorage(String fileName) {

        if (FileUtil.isExternalStorageReadable()) {
            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getFilesPath());
            File file = new File(dir, fileName);

            if (file.exists() && file.canRead())
                return FileUtil.readFileAsString(file);
        }
        else Toast.makeText(this, "External storage not readable", Toast.LENGTH_SHORT).show();

        return null;
    }

    /**
     *  Deletes a file from the application storage.
     *
     * @return number of bytes released
     */
    private int deleteFileStorage(String name){

        if (FileUtil.isExternalStorageReadable()) {

            File dir = FileUtil.getExternalFilesDirAllApiLevels(this.getFilesPath());
            File file = new File(dir, name);

            if (file.exists()) {

                String filetext = readFileStorage(name);
                if (file.delete()) {
                    return filetext.getBytes().length;
                }
                else {
                    Toast.makeText(this, "Unable to delete file.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return 0;
    }

    private void showList(){

        List<String> files = (isMyWs ? ws.getFiles() : peer.getLocalFiles(wsName));

        final ListView listview = (ListView) findViewById(R.id.listView2);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1,files);
        listview.setAdapter(adapter);
    }

    private void changeSettings() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.settings);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_ws_settings, null);
        dialog.setContentView(dialogView);

        dialog.show();
        final Workspace ws = this.ws;

        final int usedWSSpace = ws.getStorage();

        int deviceSpace = Application.getDeviceStorageSpace() + ws.getQuota();


        final SeekBar quota = (SeekBar) dialog.findViewById(R.id.ws_settings_quota);
        quota.setMax(deviceSpace - usedWSSpace);
        quota.setProgress(ws.getQuota() - usedWSSpace);


        final CheckBox isPrivate = (CheckBox) dialog.findViewById(R.id.private_checkbox);
        isPrivate.setChecked(ws.getPrivacy());

        final EditText tags = (EditText) dialog.findViewById(R.id.ws_settings_tags);
        tags.setText(ws.getTags());

        final TextView quotaTag = (TextView) dialog.findViewById(R.id.ws_settings_quota_tag);
        quotaTag.setText(Integer.toString(ws.getQuota()));

        Button confirm = (Button) dialogView.findViewById(R.id.ws_settings_confirm);
        Button cancel = (Button) dialogView.findViewById(R.id.ws_settings_cancel);

        quota.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quotaTag.setText(Integer.toString(usedWSSpace + progress));
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            ws.setPrivacy(isPrivate.isChecked());
            ws.setQuota(quota.getProgress() + usedWSSpace);
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

    /**
     * Displays a Dialog that allows the workspace owner to invite a different user into the
     * workspace
     *
     */
    public void inviteDialog () {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.title_invite_dialog);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_invite , null);
        dialog.setContentView(dialogView);

        dialog.show();
        final Workspace ws = this.ws;

        final TextView error = (TextView)dialog.findViewById(R.id.invite_error);

        Button confirm = (Button) dialog.findViewById(R.id.dialog_invite_confirm);
        Button cancel = (Button) dialog.findViewById(R.id.dialog_invite_cancel);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) dialog.findViewById(R.id.invite_user_email);
                String email = text.getText().toString();
                if (email.isEmpty()) {
                    error.setText("No email given");
                    return;
                }
                try {
                    ws.invite(Application.getNetworkUser(email));
                    dialog.dismiss();
                }catch(UserIsMyselfException | UserNotFoundException | UserAlreadyAddedException e) {
                    error.setText(e.getMessage());
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Removes the current workspace.
     * Can be used in the foreign or owned context, but the deletion is done in different ways.
     * Removing a owned workspace will affect all users that are subscribed to the workspace.
     * Removing a foreign workspace will only affect the real owner of the workspace.
     */
    public void removeWorkspaceDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.last_warning);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_ws_remove , null);
        dialog.setContentView(dialogView);

        dialog.show();
        final Workspace ws = this.ws;


        Button confirm = (Button) dialog.findViewById(R.id.dialog_ws_confirm);
        Button cancel = (Button) dialog.findViewById(R.id.dialog_ws_cancel);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isMyWs  = Application.getOwner().getEmail()
                                    .equals(ws.populateUser().getOwner().getEmail());
                Application.getOwner().remove(ws);

                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(),
                        (isMyWs)? MyWorkSpacesActivity.class : ForeignWorkspacesActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    public String getFilesPath() {
        return this.getPackageName() + "/" + wsName + "." + wsEmail;
    }
}
