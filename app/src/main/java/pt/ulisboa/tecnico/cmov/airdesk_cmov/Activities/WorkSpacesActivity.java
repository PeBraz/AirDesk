package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;


public class WorkSpacesActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_spaces);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
