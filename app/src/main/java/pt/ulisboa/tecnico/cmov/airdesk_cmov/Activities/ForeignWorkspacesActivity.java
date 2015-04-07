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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.WorkspaceDto;

public class ForeignWorkspacesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_workspaces);
        listWorkspaces();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_foreign_workspaces, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ForeignWorkspacesActivity.this, UserSettingsActivity.class);
            startActivity(intent);

        }else if (id == R.id.subscribe) {
            this.subscribeDialog();

        }else if(id == R.id.my) {

            startActivity(new Intent(getApplicationContext(), MyWorkSpacesActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void listWorkspaces() {

        List<WorkspaceDto> foreignWorkspaces =
                new ArrayList<>(Application.getOwner().getForeignWorkspacesAsDto());
  //      List<String> foreignws = new ArrayList<>();

        final ListView listview = (ListView) findViewById(R.id.foreign_list);

//        for(WorkspaceDto w : foreignWorkspaces) foreignws.add(w.getWSName() + " - " + w.getUserEmail());

        final ArrayAdapter<WorkspaceDto> adapter = new ArrayAdapter(this,
                                android.R.layout.simple_list_item_1, android.R.id.text1, foreignWorkspaces);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WorkspaceDto itemValue = (WorkspaceDto) listview.getItemAtPosition(position);
                Intent intent = new Intent(ForeignWorkspacesActivity.this, FilesActivity.class);
                intent.putExtra("WSNAME", itemValue.getWSName());
                intent.putExtra("WSUSEREMAIL", itemValue.getUserEmail());
                startActivity(intent);

            }

        });
    }
    private void subscribeDialog() {


        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.subscribe_title);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_subscribe, null);
        dialog.setContentView(dialogView);

        dialog.show();

        Button ok = (Button) dialogView.findViewById(R.id.subscribe_confirm_button);
        Button search = (Button) dialogView.findViewById(R.id.subscribe_search_button);
        Button cancel = (Button) dialogView.findViewById(R.id.subscribe_cancel_button);

        //Store the contents searched, for when finalizing the action
        final Set<Workspace> availableWS = new HashSet<>();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Application.subscribe(availableWS);
                listWorkspaces();
                dialog.dismiss();

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText text = (EditText) dialog.findViewById(R.id.subscribe_query);

                availableWS.clear();
                availableWS.addAll(Application.networkSearch(text.getText().toString()));


                TextView list = (TextView) dialog.findViewById(R.id.subscribe_list);
                if (availableWS.isEmpty()) {
                    list.setText("No Workspaces found.");
                } else {
                    list.setText("");
                    for (Workspace ws : availableWS) {
                        list.setText(list.getText() + "\n" + ws.getName());
                    }
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
