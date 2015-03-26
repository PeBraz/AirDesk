package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WorkspaceAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;

public class WorkSpacesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_spaces);

        Button button2 = (Button)findViewById(R.id.my_workspaces);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyWorkSpacesActivity.class));
            }
        });

        Button button3 = (Button)findViewById(R.id.button2);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForeignWorkspacesActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_work_spaces, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(WorkSpacesActivity.this, UserSettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.new_workspace) {
            this.newWorkspaceDialog(Application.MAX_APPLICATION_QUOTA);
         //   Intent intent = new Intent(getApplicationContext(), NewWorkspaceActivity.class);
         //   startActivity(intent);
        }else if (id == R.id.subscribe) {
            //TODO
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
