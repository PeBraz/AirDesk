package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;


import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WorkspaceAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;

public class MyWorkSpacesActivity extends ActionBarActivity {

    private List<String> ws = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work_spaces);
        this.setTitle(R.string.app_name);

        listWorkspaces();

    }

    /**
     * Creates and updates the adapter to list all the workspaces
     */

    private void listWorkspaces() {

        List<Workspace> myWorkspaces = Application.getOwner().getMyWorkspaces();
        final ListView listview = (ListView) findViewById(R.id.listView);

        ws.clear();
        for(Workspace w : myWorkspaces) ws.add(w.getName());

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, ws);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemValue = (String) listview.getItemAtPosition(position);
                Intent intent = new Intent(MyWorkSpacesActivity.this, FilesActivity.class);
                intent.putExtra("WSUSEREMAIL", Application.getOwner().getEmail());
                intent.putExtra("WSNAME", itemValue);
                startActivity(intent);

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_my_work_spaces, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MyWorkSpacesActivity.this, UserSettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.new_workspace) {
            this.newWorkspaceDialog(Application.getDeviceStorageSpace());

        }else if(id == R.id.foreign) {
            startActivity(new Intent(getApplicationContext(), ForeignWorkspacesActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a dialog on top of the workspaces activity view that allows the user to create a new
     * workspace.
     *
     * The dialog accepts a workspace name and the quota number (given through a seek bar)
     *
     * Error messages will be displayed on the dialog.
     *
     * @param maxquota max value for the seek bar (will probably be Application.MAX_APPLICATION_QUOTA)
     *
     */

    private void newWorkspaceDialog(int maxquota) {


        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.new_workspace_title);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_new_workspace, null);
        dialog.setContentView(dialogView);

        final SeekBar bar = (SeekBar) dialogView.findViewById(R.id.new_workspace_seekbar);
        final TextView quotaView = (TextView) dialogView.findViewById(R.id.new_workspace_quota_tag);

        //Set quota initial value at half the size of the slider
        bar.setProgress(maxquota/2);
        quotaView.setText("Quota: "+ (maxquota/2));
        bar.setMax(maxquota);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView view = (TextView) dialogView.findViewById(R.id.new_workspace_quota_tag);
                view.setText("Quota: "+progress);

            }
        });

        dialog.show();

        Button ok = (Button) dialogView.findViewById(R.id.new_workspace_confirm_button);
        Button cancel = (Button) dialogView.findViewById(R.id.new_workspace_cancel_button);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView text = (TextView) dialog.findViewById(R.id.new_workspace_username);
                SeekBar bar = (SeekBar) dialog.findViewById(R.id.new_workspace_seekbar);

                TextView error = (TextView) dialog.findViewById(R.id.new_workspace_error);
                if (text.getText().toString().isEmpty()) {
                    error.setText("No workspace name chosen.");
                    return;
                }
                try {
                    Application.getOwner().createWorkspace(text.getText().toString().trim(), bar.getProgress());
                    listWorkspaces();
                    dialog.dismiss();
                }catch (InvalidQuotaException e) {
                    //should not happen because the seek bar is limited
                }catch (WorkspaceAlreadyExistsException e) {
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

}




