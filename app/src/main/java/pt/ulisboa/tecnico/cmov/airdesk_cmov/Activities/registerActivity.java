package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;



public class registerActivity extends ActionBarActivity {

    private UsersDataSource datasource;

    private EditText username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.editText2);
        Button button = (Button) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(registerActivity.this, WorkSpacesActivity.class);
                intent.putExtra("location", "You are logged in as " + username.getText().toString());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
